package pl.dur.opa.main;

import java.rmi.RMISecurityManager;
import pl.dur.opa.server.controller.Controller;
import pl.dur.opa.server.view.ServerView;

/**
 * Class which starts server and prepares it to serve clients. 
 * After calling main method of this class server is ready
 * for catching client connections.
 *
 * @author Dur
 */
public final class OpaServer
{
	private ServerView view;
	private Controller controller;

	/**
	 * Construtor.
	 */
	private OpaServer()
	{
		view = new ServerView();
		controller = new Controller( view );
	}

	public Controller getController()
	{
		return controller;
	}

	public void setController( Controller controller )
	{
		this.controller = controller;
	}

	public ServerView getView()
	{
		return view;
	}

	public void setView( ServerView view )
	{
		this.view = view;
	}

	/**
	 * Program starter.
	 *
	 * @param args - not used.
	 */
	public static void main( final String[] args )
	{
		final OpaServer server = new OpaServer();

		System.setSecurityManager( new RMISecurityManager() );
		java.awt.EventQueue.invokeLater( new Runnable()
		{
			public void run()
			{
				server.getView().setVisible( true );
			}
		} );
	}
}
