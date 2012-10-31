/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.dur.opa.tasks;

import java.util.List;

/**
 * Creates new thread to execute passed task. After end of computing thread is killed.
 * @author Dur
 */
public class TaskExecutor implements Runnable
{
	private Task task;
	
	public TaskExecutor( Task task )
	{
		this.task = task;
	}
	
	@Override
	public void run()
	{
		task.execute( task );
	}
	
	
}
