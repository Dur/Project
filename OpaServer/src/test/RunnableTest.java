/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JProgressBar;

/**
 *
 * @author Dur
 */
public class RunnableTest implements java.lang.Runnable
{
	public JProgressBar bar;
	
	public RunnableTest(JProgressBar bar)
	{
		this.bar = bar;
	}
	
	@Override
	public void run()
	{
		for( int i = 1; i<100;i++)
		{
			bar.setValue( i );
			try
			{
				Thread.sleep( 1000 );
			}
			catch( InterruptedException ex )
			{
				Logger.getLogger( RunnableTest.class.getName() ).
						log( Level.SEVERE, null, ex );
			}
		}
	}
	
}
