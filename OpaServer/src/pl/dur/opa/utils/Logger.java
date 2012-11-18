/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.dur.opa.utils;

import pl.dur.opa.server.controller.Controller;

/**
 *
 * @author Dur
 */
public class Logger
{
	static Controller controller;
	
	public static void setController( Controller cont )
	{
		controller = cont;
	}
	
	public static void log( String text )
	{
		controller.sendToConsole( text );
	}
}
