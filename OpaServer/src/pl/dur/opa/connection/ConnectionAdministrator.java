/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.dur.opa.connection;

import java.io.File;
import java.net.Socket;
import java.util.HashMap;
import pl.dur.opa.tasks.ReceiveFileTask;
import pl.dur.opa.tasks.SendFileTask;
import pl.dur.opa.tasks.TaskExecutor;

/**
 *
 * @author Dur
 */
public class ConnectionAdministrator
{
	private HashMap<Integer, Socket> connectionsMap;

	private TaskExecutor executor;
	
	public int getSocketNumForFileSending( File file )
	{
		int port = SocketWrapper.getAvailablePort();
		executor = new TaskExecutor( new SendFileTask( file, port ) );
		Thread thread = new Thread(executor);
		thread.start();
		return port;
	}

	public int getSocketForFileReceiving( File directory, String name )
	{
		int port = SocketWrapper.getAvailablePort();
		executor = new TaskExecutor( new ReceiveFileTask( directory, port, name ) );
		Thread thread = new Thread(executor);
		thread.start();
		return port;
	}

	public int getSocketNumForFilesSending( File[] file )
	{
		int port = 1024;
		return port;
	}

	public int getSocketForFilesReceiving( File directory )
	{
		int port = 1024;
		return port;
	}
}
