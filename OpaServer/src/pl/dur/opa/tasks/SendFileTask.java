package pl.dur.opa.tasks;

import java.io.File;
import pl.dur.opa.connection.SocketWrapper;
import pl.dur.opa.remote.interfaces.Notificator;

/**
 * Task class to implement sending file to user. 
 * This class should be fired by TaskExecutor class to be runed in other thread. 
 * It creates new SocketWrapper to open socket
 * and sends selected file by this socket.
 *
 * @author Dur
 */
public class SendFileTask extends TaskNotificator implements Task, SocketTask
{
	private final File file;
	private SocketWrapper socketWrapper;

	/**
	 * Constructor.
	 *
	 * @param newFileToSend - file to be send to client.
	 * @param newSocketPort - port for socket.
	 */
	public SendFileTask( final File newFileToSend, Notificator notificator )
	{
		super(notificator);
		this.file = newFileToSend;
	}

	@Override
	public final Object execute( final Object params )
	{
		socketWrapper.sendFile( file, super.getNotificator() );
		return true;
	}

	@Override
	public void setSocket( SocketWrapper socket )
	{
		this.socketWrapper = socket;
	}
}
