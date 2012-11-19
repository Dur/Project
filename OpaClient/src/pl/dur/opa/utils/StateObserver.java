package pl.dur.opa.utils;

import pl.dur.opa.controllers.ClientController;

/**
 *
 * @author Dur
 */
public class StateObserver
{
	static ClientController controller;
	
	public static void setController( ClientController cont )
	{
		controller = cont;
	}
	
	public static void log( String text )
	{
		controller.showMessageToUser( text );
	}
	
	public static void logOutUser()
	{
		controller.logOutUser();
	}
}
