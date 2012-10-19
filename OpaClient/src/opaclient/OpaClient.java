/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package opaclient;

import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.Remote;

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
			Remote remote = Naming.lookup( "ECHO-SERVER" );
			Echo server = null;
			if( remote instanceof Echo )
			{
				server = (Echo) remote;
			}
			String result = server.echo( "Hello server" );
			System.out.println( result );
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
