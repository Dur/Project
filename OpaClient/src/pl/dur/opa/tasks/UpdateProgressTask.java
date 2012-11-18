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
		int value;
		while( !Thread.interrupted() )
		{
			try
			{
				elem = (Fraction) queue.take();
				value = elem.getValue();
				if( value < 100 )
				{
					bar.setValue( value );
					bar.setVisible( true );
					bar.setString( elem.getCompleteMessage() );

				}
				else
				{
					bar.setString( "Ready" );
					bar.setVisible( false );
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
