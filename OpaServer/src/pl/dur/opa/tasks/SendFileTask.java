package pl.dur.opa.tasks;

import java.io.File;
import pl.dur.opa.connection.SocketWrapper;

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
	private final File file;
	private final Integer port;

	/**
	 * Constructor.
	 *
	 * @param newFileToSend - file to be send to client.
	 * @param newSocketPort - port for socket.
	 */
	public SendFileTask( final File newFileToSend, final Integer newSocketPort )
	{
		this.file = newFileToSend;
		this.port = newSocketPort;
	}

	@Override
	public final Object execute( final Object params )
	{
		final SocketWrapper socket = new SocketWrapper( port );
		socket.sendFile( file );
		return true;
	}
}
