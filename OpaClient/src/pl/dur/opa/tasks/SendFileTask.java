package pl.dur.opa.tasks;

import java.io.File;
import pl.dur.opa.sockets.SocketWrapper;

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
	private final String host;

	/**
	 * Constructor.
	 *
	 * @param newFileToSend - file to be send to client.
	 * @param newSocketPort - port for socket.
	 * @param newHost - host to connnect.
	 */
	public SendFileTask( final File newFileToSend, final Integer newSocketPort, final String newHost )
	{
		this.file = newFileToSend;
		this.port = newSocketPort;
		this.host = newHost;
	}

	@Override
	public final Object execute( final Object params )
	{
		final SocketWrapper socket = new SocketWrapper( port, host );
		System.out.println("befor calling send method");
		socket.sendFile( file );
		return true;
	}
}
