package pl.dur.opa.main;

import pl.dur.opa.controllers.ClientController;
import pl.dur.opa.view.LoggingPage;
import pl.dur.opa.view.View;

/**
 * Class represents client of program. 
 * It contains main class which invokes all other metods to start.
 * 
 * @author Dur
 */
public final class OpaClient
{
	private ClientController controller;
	private LoggingPage loggingPage;
	private View view;

	public ClientController getController()
	{
		return controller;
	}

	public void setController( ClientController controller )
	{
		this.controller = controller;
	}

	public LoggingPage getLoggingPage()
	{
		return loggingPage;
	}

	public void setLoggingPage( LoggingPage loggingPage )
	{
		this.loggingPage = loggingPage;
	}

	public View getView()
	{
		return view;
	}

	public void setView( View view )
	{
		this.view = view;
	}
	
	
	/**
	 * Main class. starts program.
	 * @param args start args.
	 */
	public static void main( final String[] args )
	{
		final OpaClient client = new OpaClient();
		client.setController( new ClientController() );
		client.setLoggingPage( new LoggingPage( client.getController() ));
		client.getController().setLoggingPage( client.getLoggingPage());
		javax.swing.SwingUtilities.invokeLater( new Runnable()
		{
			public void run()
			{
				client.getLoggingPage().setVisible( true );
			}
		} );
	}
}
