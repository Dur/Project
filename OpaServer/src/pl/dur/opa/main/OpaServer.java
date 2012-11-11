/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.dur.opa.main;

import java.io.File;
import java.rmi.RMISecurityManager;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import pl.dur.opa.connection.ConnectionAccepter;
import pl.dur.opa.remote.impl.RemoteFileSystemViewImpl;
import pl.dur.opa.remote.impl.UserAuthenticatorImpl;
import pl.dur.opa.server.configuration.Configuration;
import pl.dur.opa.server.configuration.UsersConfiguration;

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
			
			File root = new File(conf.getParameter( "ROOT") );
			File[] roots = new File[1];
			roots[0]=root;
			UsersConfiguration users = new UsersConfiguration();
			RemoteFileSystemViewImpl fileView = new RemoteFileSystemViewImpl( roots, root );
			UserAuthenticatorImpl authenticator = new UserAuthenticatorImpl( users, root );
			Registry registry = LocateRegistry.createRegistry( 1099 );
			registry.rebind( "FILE_VIEW", fileView );
			registry.rebind( "AUTH", authenticator);
			
			Thread connectionListener = new Thread( new ConnectionAccepter( 80 ) );
			connectionListener.start();
			
			
		}
		catch( Exception x )
		{
			System.out.println( x.toString() );
		}
	}
}
