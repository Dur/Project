/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package opaserver;

import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

/**
 *
 * @author Dur
 */
public class OpaServer extends UnicastRemoteObject implements Echo
{
	private static final long serialVersionUID = 7L;

	public String echo( String input ) throws RemoteException
	{
		System.out.println( "from client: [" + input + "]" );
		return "Echo: " + input;
	}

	public OpaServer() throws RemoteException
	{
		super();
	}

	public static void main( String args[] )
	{
		System.setSecurityManager( new RMISecurityManager() );
		try
		{
			OpaServer e = new OpaServer();
			LocateRegistry.createRegistry( 1099 );
			Naming.rebind( "ECHO-SERVER", e );
		}
		catch( Exception x )
		{
			System.out.println( x.toString() );
		}
	}
}
