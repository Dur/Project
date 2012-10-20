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
public class OpaServer extends UnicastRemoteObject implements Echo
{
	private static final long serialVersionUID = 7L;

	@Override
	public JFileChooser echo( String input ) throws RemoteException
	{
		File file = new File("C:\\");
		JFileChooser jsf = new JFileChooser(file);
		return jsf;
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

	@Override
	public void onSelectFile( File file ) throws RemoteException
	{
		System.out.println ( "can read " + file.canRead() );
	}
}
