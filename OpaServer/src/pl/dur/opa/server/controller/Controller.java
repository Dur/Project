package pl.dur.opa.server.controller;

import java.io.File;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import pl.dur.opa.connection.ConnectionAccepter;
import pl.dur.opa.remote.impl.UserAuthenticatorImpl;
import pl.dur.opa.server.configuration.Configuration;
import pl.dur.opa.server.configuration.UsersConfiguration;
import pl.dur.opa.server.view.ServerView;
import pl.dur.opa.utils.Logger;

/**
 *
 * @author Dur
 */
public class Controller
{
	private ServerView view;

	public Controller( ServerView view )
	{
		Logger.setController( this );
		this.view = view;
		try
		{
			Configuration conf = new Configuration();
			UsersConfiguration users = new UsersConfiguration();

			File root = new File( conf.getParameter( "ROOT" ) );
			Integer rmiPort = new Integer(conf.getParameter( "RMI_PORT" ));
			Integer socketPort = new Integer(conf.getParameter( "SOCKET_PORT" ));
			
			UserAuthenticatorImpl authenticator = new UserAuthenticatorImpl( users, root );
			Registry registry = LocateRegistry.createRegistry( rmiPort );
			registry.rebind( "AUTH", authenticator );

			Thread connectionListener = new Thread( new ConnectionAccepter( socketPort ) );
			connectionListener.start();
		}
		catch(RemoteException ex )
		{
			ex.printStackTrace();
		}
	}
	
	public void sendToConsole(String text)
	{
		view.appendText( text );
	}
}
