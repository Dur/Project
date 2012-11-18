/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.dur.opa.tasks;

import pl.dur.opa.remote.interfaces.Notificator;

/**
 *
 * @author Dur
 */
public class TaskNotificator
{
	private Notificator notificator;

	public TaskNotificator( Notificator notificator )
	{
		this.notificator = notificator;
	}

	public Notificator getNotificator()
	{
		return notificator;
	}

	public void setNotificator( Notificator notificator )
	{
		this.notificator = notificator;
	}
	
	
}