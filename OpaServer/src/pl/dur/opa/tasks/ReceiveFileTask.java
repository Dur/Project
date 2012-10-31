package pl.dur.opa.tasks;

import java.io.File;
import pl.dur.opa.connection.SocketWrapper;

/**
 * Receive file tak. Implemetns Task to be run in new thread by TaskExecutor.
 * This task creates new SocketWrapper and starting to receiving file on open socket.
 * File is save on local server file system.
 * @author Dur
 */
public class ReceiveFileTask implements Task
{
	private final File directory;
	private final Integer port;
	private final String fileName;

	/**
	 * Constructor.
	 * @param newDirectory - directory where file should be stored.
	 * @param newSocketPort - port for socket to open.
	 * @param newFileName - name of new file.
	 */
	public ReceiveFileTask( final File newDirectory, final Integer newSocketPort, final String newFileName )
	{
		this.directory = newDirectory;
		this.port = newSocketPort;
		this.fileName = newFileName;
	}

	@Override
	public final Object execute( final Object params )
	{
		final SocketWrapper socket = new SocketWrapper( port );
		socket.receiveFile( fileName, directory );
		return true;
	}
}
