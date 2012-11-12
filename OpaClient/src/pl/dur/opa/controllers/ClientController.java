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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
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
	private String serverAddress = "";

	public ClientController()
	{
		System.setSecurityManager( new RMISecurityManager() );
	}

	public boolean connectToServer( String login, String password, String serverAddress )
	{
		this.serverAddress = serverAddress;
		try
		{
			Registry registry = LocateRegistry.getRegistry( serverAddress, 1099 );
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

	public void sendFile( File serverDirectory, File file )
	{
		String key;
		try
		{
			key = manipulator.saveFile( serverDirectory, file.getName() );
			TaskExecutor executor = new TaskExecutor( new SendFileTask( file, 80, serverAddress, key ) );
			Thread thread = new Thread( executor );
			thread.start();
		}
		catch( RemoteException ex )
		{
			ex.printStackTrace();
		}

	}

	public void receiveFile( File localDirectory, File remoteFile )
	{
		try
		{
			String key = manipulator.getFile( remoteFile );
			TaskExecutor executor = new TaskExecutor( new ReceiveFileTask( localDirectory, key, remoteFile.
					getName(),
					serverAddress, 80 ) );
			Thread thread = new Thread( executor );
			thread.start();
		}
		catch( RemoteException ex )
		{
			ex.printStackTrace();
		}
	}

	public ExtendedFile[] areFilesVersioned( ExtendedFile[] files )
	{
		try
		{
			files = manipulator.checkFilesBackups( files );
		}
		catch( RemoteException ex )
		{
			ex.printStackTrace();
		}
		return files;
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
