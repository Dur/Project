package pl.dur.opa.controllers;

import java.io.File;
import java.rmi.RMISecurityManager;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.StringTokenizer;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import pl.dur.opa.remote.impl.NotificatorImpl;
import pl.dur.opa.remote.interfaces.UserAuthenticator;
import pl.dur.opa.remote.interfaces.UsersInterface;
import pl.dur.opa.tasks.ReceiveFileTask;
import pl.dur.opa.tasks.SendFileTask;
import pl.dur.opa.tasks.TaskExecutor;
import pl.dur.opa.tasks.UpdateProgressTask;
import pl.dur.opa.utils.ExtendedFile;
import pl.dur.opa.utils.Fraction;
import pl.dur.opa.utils.StateObserver;
import pl.dur.opa.view.LoggingPage;
import pl.dur.opa.view.View;

/**
 *
 * @author Dur
 */
public class ClientController
{
	private LoggingPage loggingPage;
	private View view;
	private UsersInterface manipulator;
	private String serverAddress = "";
	private BlockingQueue<Fraction> progress;
	private NotificatorImpl notificator;
	private static Integer DEFAULT_SERVER_PORT = 1099;
	private Thread progressThread = null;

	public ClientController()
	{
		System.setSecurityManager( new RMISecurityManager() );
		StateObserver.setController( this );
	}

	public boolean connectToServer( String login, String password, String serverAddress )
	{
		view = new View( this );
		if( progressThread != null && progressThread.isAlive() )
		{
			progressThread.interrupt();
		}
		StringTokenizer tokenizer = new StringTokenizer( serverAddress, ":" );
		String host = tokenizer.nextToken();
		Integer port = DEFAULT_SERVER_PORT;
		if( tokenizer.hasMoreTokens() )
		{
			port = new Integer( tokenizer.nextToken() );
		}
		this.serverAddress = serverAddress;
		try
		{
			notificator = new NotificatorImpl( view );
			Registry registry = LocateRegistry.getRegistry( host, port );
			Remote remote = registry.lookup( "AUTH" );
			UserAuthenticator auth = null;
			if( remote instanceof UserAuthenticator )
			{
				auth = (UserAuthenticator) remote;
			}
			manipulator = auth.loginUser( login, password, notificator );
		}
		catch( Exception e )
		{
			return false;
		}
		javax.swing.SwingUtilities.invokeLater( new Runnable()
		{
			public void run()
			{
				view.createAndShowGUI();
			}
		} );
		progress = new ArrayBlockingQueue<Fraction>( 100 );
		TaskExecutor exec = new TaskExecutor( new UpdateProgressTask( progress, view.getLocalProgressBar() ) );
		progressThread = new Thread( exec );
		progressThread.start();
		return true;
	}

	public void sendFile( File serverDirectory, List<File> files )
	{
		TaskExecutor executor = new TaskExecutor( new SendFileTask( files, 80, serverAddress,
				progress, manipulator, serverDirectory ) );
		Thread thread = new Thread( executor );
		thread.start();
	}

	public void receiveFile( File localDirectory, List<File> remoteFiles )
	{
		TaskExecutor executor = new TaskExecutor( new ReceiveFileTask( localDirectory, remoteFiles,
				serverAddress, 80, manipulator ) );
		Thread thread = new Thread( executor );
		thread.start();
	}

	public ExtendedFile[] areFilesVersioned( ExtendedFile[] files )
	{
		try
		{
			files = manipulator.checkFilesBackups( files );
		}
		catch( RemoteException ex )
		{
			StateObserver.log( "Server disconnected for unknown reason" );
			StateObserver.logOutUser();
		}
		return files;
	}

	public void deleteFile( File[] files )
	{
		try
		{
			for( File file : files )
			{
				manipulator.removeFileFromServer( new ExtendedFile( file.getPath() ) );
			}
		}
		catch( RemoteException ex )
		{
			StateObserver.log( "Server disconnected for unknown reason" );
			StateObserver.logOutUser();
		}
	}

	public LoggingPage getLoggingPage()
	{
		return loggingPage;
	}

	public void setLoggingPage( LoggingPage loggingPage )
	{
		this.loggingPage = loggingPage;
	}

	public View getView()
	{
		return view;
	}

	public void setView( View view )
	{
		this.view = view;
	}

	public UsersInterface getManipulator()
	{
		return manipulator;
	}

	public void setManipulator( UsersInterface manipulator )
	{
		this.manipulator = manipulator;
	}

	public void showMessageToUser( String text )
	{
		view.notifyUser( text );
	}

	public File locateFileOnServer( File file )
	{
		try
		{
			return manipulator.locateFileOnServer( new ExtendedFile( file.getPath() ) );
		}
		catch( RemoteException ex )
		{
			StateObserver.log( "Server disconnected for unknown reason" );
			StateObserver.logOutUser();
			return null;
		}
	}

	public void notifyViewOfProgress( String info, int value, boolean indeterminateMode )
	{
		view.showProgressPopup( info, value, indeterminateMode );
	}

	public void hideProgressPopup()
	{
		view.hideProgressPopup();
	}

	public void logOutUser()
	{
		view.hideView();
		loggingPage.setVisible( true );
	}
}
