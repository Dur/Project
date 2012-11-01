/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.dur.opa.main;

import java.io.File;
import java.rmi.RMISecurityManager;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import pl.dur.opa.remote.impl.FileManipulatorImpl;
import pl.dur.opa.remote.impl.RemoteFileSystemViewImpl;
import pl.dur.opa.remote.interfaces.FileManipulator;
import pl.dur.opa.server.configuration.Configuration;

/**
 * Class which starts server and prepares it to serve clients.
 * After calling main method of this class server is ready for catching client connections. 
 * @author Dur
 */
public final class OpaServer
{
	/**
	 * Construtor.
	 */
	private OpaServer()
	{
		
	}
	/**
	 * Program starter.
	 * @param args - not used.
	 */
	public static void main( final String[] args )
	{
		System.setSecurityManager( new RMISecurityManager() );
		try
		{
			Configuration conf = new Configuration();
			File root = new File("C:\\");
			File[] roots = new File[1];
			roots[0]=root;
			RemoteFileSystemViewImpl fileView = new RemoteFileSystemViewImpl( roots, root );
			FileManipulatorImpl manipulator = new FileManipulatorImpl();
			Registry registry = LocateRegistry.createRegistry( 1099 );
			registry.rebind( "FILE_VIEW", fileView );
			registry.rebind( "MANIPULATOR", manipulator);
		}
		catch( Exception x )
		{
			System.out.println( x.toString() );
		}
	}
}
