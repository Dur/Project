
package pl.dur.opa.main;

import java.io.File;
import java.rmi.RMISecurityManager;
import java.rmi.Remote;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import javax.swing.JFileChooser;
import pl.dur.opa.file.browser.RemoteFileBrowser;
import pl.dur.opa.remote.interfaces.FileManipulator;
import pl.dur.opa.remote.interfaces.RemoteFileSystemView;
import pl.dur.opa.tasks.ReceiveFileTask;
import pl.dur.opa.tasks.SendFileTask;
import pl.dur.opa.tasks.TaskExecutor;

/**
 * Class represents client of program. 
 * It contains main class which invokes all other metods to start.
 * 
 * @author Dur
 */
public final class OpaClient
{
	/** 
	 * Constructor.
	 */
	private OpaClient()
	{
		try
		{
			Registry registry = LocateRegistry.getRegistry( "localhost" );
			Remote remote = registry.lookup( "FILE_VIEW" );
			RemoteFileSystemView server = null;
			if( remote instanceof RemoteFileSystemView )
			{
				server = (RemoteFileSystemView) remote;
			}
			RemoteFileBrowser fileBrowser = new RemoteFileBrowser( server );
			
 			Remote remoteManipulator = registry.lookup( "MANIPULATOR" );
			FileManipulator mnipulator = null;
			if( remoteManipulator instanceof FileManipulator )
			{
				mnipulator = (FileManipulator) remoteManipulator;
			}
			
			JFileChooser chooser = new JFileChooser( fileBrowser );
			chooser.showOpenDialog( null );
			File file = chooser.getSelectedFile();
			int port = mnipulator.getFile( file );
			TaskExecutor executor = new TaskExecutor( new ReceiveFileTask( file, port, file.getName(), "localhost") );
			Thread thread = new Thread(executor);
			thread.start();
			
			chooser = new JFileChooser( );
			chooser.showOpenDialog( null );
			file = chooser.getSelectedFile();
			port = mnipulator.saveFile( file, file.getName() );
			executor = new TaskExecutor( new SendFileTask( file, port, "localhost") );
			thread = new Thread(executor);
			thread.start();
		}
		catch( Exception e )
		{
			System.out.println( e.toString() );
		}
	}

	/**
	 * Main class. starts program.
	 * @param args start args.
	 */
	public static void main( final String[] args )
	{
		System.setSecurityManager( new RMISecurityManager() );
		new OpaClient();
	}
}
