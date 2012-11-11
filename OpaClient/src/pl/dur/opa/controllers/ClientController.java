/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.dur.opa.controllers;

import java.io.File;
import java.rmi.RMISecurityManager;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import pl.dur.opa.client.actions.Action;
import pl.dur.opa.file.browser.RemoteFileBrowser;
import pl.dur.opa.remote.interfaces.UserAuthenticator;
import pl.dur.opa.remote.interfaces.UsersInterface;
import pl.dur.opa.tasks.ReceiveFileTask;
import pl.dur.opa.tasks.SendFileTask;
import pl.dur.opa.tasks.TaskExecutor;
import pl.dur.opa.utils.ExtendedFile;
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

	public ClientController()
	{
		System.setSecurityManager( new RMISecurityManager() );
	}

	public boolean connectToServer( String login, String password, String serverAddress )
	{
		try
		{
			Registry registry = LocateRegistry.getRegistry( serverAddress );
			Remote remote = registry.lookup( "AUTH" );
			UserAuthenticator auth = null;
			if( remote instanceof UserAuthenticator )
			{
				auth = (UserAuthenticator) remote;
			}
			manipulator = auth.loginUser( login, password );
		}
		catch( Exception e )
		{
			System.out.println( e.toString() );
			return false;
		}
		javax.swing.SwingUtilities.invokeLater( new Runnable()
		{
			public void run()
			{
				view.createAndShowGUI();
			}
		} );
		return true;
	}

	public RemoteFileBrowser getRemoteFileBrowser()
	{
		RemoteFileBrowser fileBrowser = null;
		try
		{
			fileBrowser = new RemoteFileBrowser( manipulator.getFileSystemView() );
			JFileChooser chooser = new JFileChooser( fileBrowser );
			chooser.showOpenDialog( null );
			File file = chooser.getSelectedFile();
			String key = manipulator.getFile( file );
			File inputDir = new File( file.getParent() );
			TaskExecutor executor = new TaskExecutor( new ReceiveFileTask( inputDir, key, file.
					getName(),
					"localhost", 80 ) );
			Thread thread = new Thread( executor );
			thread.start();
			manipulator.removeFileFromServer( new ExtendedFile( file.getPath() ) );

			chooser = new JFileChooser();
			chooser.showOpenDialog( null );
			file = chooser.getSelectedFile();
			key = manipulator.saveFile( inputDir, file.getName() );
			executor = new TaskExecutor( new SendFileTask( file, 80, "localhost", key ) );
			thread = new Thread( executor );
			thread.start();
		}
		catch( RemoteException ex )
		{
			ex.printStackTrace();
		}
		return fileBrowser;
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
	
	
}
