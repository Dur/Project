package pl.dur.opa.tasks;

import java.io.File;
import pl.dur.opa.connection.SocketWrapper;

/**
 * Receive file tak. Implemetns Task to be run in new thread by TaskExecutor.
 * This task creates new SocketWrapper and starting to receiving file on open socket.
 * File is save on local server file system.
 * @author Dur
 */
public class ReceiveFileTask implements Task, SocketTask
{
	private final File directory;
	private final String fileName;
	private SocketWrapper socketWrapper;

	/**
	 * Constructor.
	 * @param newDirectory - directory where file should be stored.
	 * @param newFileName - name of new file.
	 */
	public ReceiveFileTask( final File newDirectory, final String newFileName )
	{
		this.directory = newDirectory;
		this.fileName = newFileName;
	}

	@Override
	public final Object execute( final Object params )
	{
		socketWrapper.receiveFile( fileName, directory );
		return true;
	}

	@Override
	public void setSocket( SocketWrapper socket )
	{
		this.socketWrapper = socket;
	}
}
