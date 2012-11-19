/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.dur.opa.tasks;

import java.util.concurrent.BlockingQueue;
import javax.swing.JProgressBar;
import pl.dur.opa.controllers.ClientController;
import pl.dur.opa.utils.Fraction;

/**
 *
 * @author Dur
 */
public class UpdateProgressTask implements Task
{
	private ClientController controller;
	private BlockingQueue<Fraction> queue;

	public UpdateProgressTask( BlockingQueue<Fraction> queue, ClientController controller )
	{
		this.controller = controller;
		this.queue = queue;
	}

	@Override
	public Object execute( Object params )
	{
		Fraction elem;
		int value;
		while( !Thread.interrupted() )
		{
			try
			{
				elem = (Fraction) queue.take();
				value = elem.getValue();
				if( value < 100 )
				{
					controller.updateProgressBar( elem.getCompleteMessage(), value, true, false );
				}
				else
				{
					controller.updateProgressBar( "Ready", 0, false, false );
				}
			}
			catch( InterruptedException ex )
			{
				return null;
			}
		}
		return null;
	}
}
