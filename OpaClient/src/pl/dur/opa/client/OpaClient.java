
package pl.dur.opa.client;

import java.io.File;
import java.rmi.RMISecurityManager;
import java.rmi.Remote;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import javax.swing.JFileChooser;
import pl.dur.opa.file.browser.RemoteFileBrowser;
import pl.dur.opa.interfaces.RemoteFileSystemView;

/**
 * Class represents client of program. It contains main class which invokes all other metods to start.
 * 
 * @author Dur
 */
public class OpaClient
{
	/** 
	 * Constructor.
	 */
	public OpaClient()
	{
		try
		{
			Registry registry = LocateRegistry.getRegistry( "192.168.1.10" );
			Remote remote = registry.lookup( "SERVER" );
			RemoteFileSystemView server = null;
			if( remote instanceof RemoteFileSystemView )
			{
				server = (RemoteFileSystemView) remote;
			}
			RemoteFileBrowser fileBrowser = new RemoteFileBrowser( server );
			JFileChooser chooser = new JFileChooser( fileBrowser );
			chooser.showOpenDialog( null );
			File file = chooser.getSelectedFile();
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
