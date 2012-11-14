/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.dur.opa.tasks;

import java.util.concurrent.BlockingQueue;
import javax.swing.JProgressBar;
import pl.dur.opa.utils.Fraction;

/**
 *
 * @author Dur
 */
public class UpdateProgressTask implements Task
{
	private JProgressBar bar;
	private BlockingQueue<Fraction> queue;

	public UpdateProgressTask( BlockingQueue<Fraction> queue, JProgressBar bar )
	{
		this.bar = bar;
		this.queue = queue;
	}

	@Override
	public Object execute( Object params )
	{
		Fraction elem;
		Double value;
		while( !Thread.interrupted() )
		{
			try
			{
				elem = (Fraction) queue.take();
				value = new Double( elem.getCounter() );
				value = value / elem.getDenominator();
				value = value * 100;
				if( value.intValue() < 100 )
				{
					bar.setValue( value.intValue() );
					bar.setString( "Sending " + value.intValue() + "%" );

				}
				else
				{
					bar.setString( "Ready" );
					bar.setValue( 0 );
				}
			}
			catch( InterruptedException ex )
			{
				ex.printStackTrace();
			}
		}
		return null;
	}
}
