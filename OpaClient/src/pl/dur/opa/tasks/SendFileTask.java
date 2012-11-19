package pl.dur.opa.tasks;

import java.io.File;
import java.rmi.RemoteException;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import pl.dur.opa.remote.interfaces.UsersInterface;
import pl.dur.opa.sockets.SocketWrapper;
import pl.dur.opa.utils.Fraction;
import pl.dur.opa.utils.StateObserver;

/**
 * Task class to implement sending file to user. 
 * This class should be fired by TaskExecutor class to be runed in other thread. 
 * It creates new SocketWrapper to open socket
 * and sends selected file by this socket.
 *
 * @author Dur
 */
public class SendFileTask implements Task
{
	private final List<File> files;
	private final int port;
	private final String host;
	private BlockingQueue<Fraction> queue;
	private UsersInterface manipulator;
	private File serverDirectory;

	/**
	 * Constructor.
	 *
	 * @param newFileToSend - file to be send to client.
	 * @param newSocketPort - port for socket.
	 * @param newHost - host to connnect.
	 */
	public SendFileTask( final List<File> newFilesToSend, final int newSocketPort, 
						final String newHost, BlockingQueue<Fraction> queue, 
						UsersInterface manipulator, File serverDirectory )
	{
		this.files = newFilesToSend;
		this.port = newSocketPort;
		this.host = newHost;
		this.queue = queue;
		this.manipulator = manipulator;
		this.serverDirectory = serverDirectory;
	}

	@Override
	public final Object execute( final Object params )
	{
		final SocketWrapper socket = new SocketWrapper( port, host, queue );
		String key;
		for( File file : files )
		{
			try
			{
				key = manipulator.saveFile( serverDirectory, file.getName(), file.lastModified() );
				socket.sendFile( key, file, file.lastModified() );
			}
			catch( RemoteException ex )
			{
				StateObserver.log( "Server disconnected for unknown reason");
				StateObserver.logOutUser();
			}
		}
		return true;
	}
}
