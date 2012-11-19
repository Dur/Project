package pl.dur.opa.connection;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import pl.dur.opa.tasks.SocketTask;
import pl.dur.opa.tasks.Task;
import pl.dur.opa.tasks.TaskExecutor;
import pl.dur.opa.utils.Logger;

/** 
 * Class which contains server socket to allow clients to connect to serwer.
 * After client connection new thread is created. This thread can execute task which 
 * will be get from tasksMap.
 * If tasks map does not contains task with corresponding key then connection is closed.
 * @author Dur
 */
public class ConnectionAccepter implements Runnable
{
	private final static int KEY_SIZE = 8;
	private final static int NO_DATA = -1;
	private ServerSocket socket;
	private byte[] taskKey = new byte[ KEY_SIZE ];
	private final static HashMap<String, Task> tasksMap = new HashMap<>();

	/**
	 * Constructor.
	 * @param port - port to listen.
	 * @param newTasksMap hash map which contains pairs <codeName, task> 
	 */
	public ConnectionAccepter( int port )
	{
		try
		{
			socket = new ServerSocket( port );
		}
		catch( IOException ex )
		{
			Logger.log("Cant initalize connection");
		}
	}

	@Override
	public void run()
	{
		Socket clientSocket;
		InputStream is;
		TaskExecutor taskExecutor;
		String parsedKey;
		SocketTask task;
		while( !Thread.interrupted() )
		{
			try
			{
				clientSocket = socket.accept();
				Logger.log("User " + clientSocket.getInetAddress().getHostAddress() + " connected");
				is = clientSocket.getInputStream();
				if( is.read( taskKey, 0, KEY_SIZE ) != NO_DATA )
				{
					parsedKey = new String( taskKey );
					if( ( task = (SocketTask) tasksMap.get( parsedKey ) ) != null && task instanceof SocketTask )
					{
						task.setSocket( new SocketWrapper( clientSocket ) );
						taskExecutor = new TaskExecutor( task );
						Thread thread = new Thread(taskExecutor);
						thread.start();
						tasksMap.remove( parsedKey );
					}
					else
					{
						clientSocket.close();
					}
				}
				else
				{
					clientSocket.close();
				}
			}
			catch( IOException ex )
			{
				Logger.log("Problem with accept connection");
			}
		}
	}
	
	public final static boolean putTaskToMap(String key, Task task )
	{
		if( tasksMap.put( key, task ) == null )
		{
			return true;
		}
		return false;
	}
}
