/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.dur.opa.client;

import java.io.File;
import java.rmi.RMISecurityManager;
import java.rmi.Remote;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import javax.swing.JFileChooser;
import net.sf.vfsjfilechooser.VFSJFileChooser;
import pl.dur.opa.interfaces.Echo;

/**
 *
 * @author Dur
 */
public class OpaClient
{
	OpaClient()
	{
		try
		{
			Registry registry = LocateRegistry.getRegistry("192.168.1.18");
			Remote remote = registry.lookup( "ECHO-SERVER" );
			Echo server = null;
			if( remote instanceof Echo )
			{
				server = (Echo) remote;
			}
			JFileChooser result = server.echo( "Hello server" );
			result.showOpenDialog(null);
			File file = result.getSelectedFile();
			server.onSelectFile( file );
		}
		catch( Exception e )
		{
			System.out.println( e.toString() );
		}
	}

	public static void main( String args[] )
	{
		System.setSecurityManager( new RMISecurityManager() );
		new OpaClient();
	}
}
