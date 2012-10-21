package pl.dur.opa.server;

import java.io.File;
import pl.dur.opa.interfaces.Echo;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import javax.swing.JFileChooser;

/**
 *
 * @author Dur
 */
public class OpaServer extends UnicastRemoteObject
{
	private static final long serialVersionUID = 7L;

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
//			OpaServer stub =
//                (OpaServer) UnicastRemoteObject.exportObject(e, 0);
			Registry registry = LocateRegistry.createRegistry( 1099 );
			registry.rebind( "ECHO-SERVER", e );

		}
		catch( Exception x )
		{
			System.out.println( x.toString() );
		}
	}
}
