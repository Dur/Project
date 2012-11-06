package pl.dur.opa.tasks;

import java.io.File;
import pl.dur.opa.sockets.SocketWrapper;

/**
 * Receive file tak. Implemetns Task to be run in new thread by TaskExecutor.
 * This task creates new SocketWrapper and starting to receiving file on open socket.
 * File is save on local server file system.
 * @author Dur
 */
public class ReceiveFileTask implements Task
{
	private final File directory;
	private final String key;
	private final String fileName;
	private final String host;
	private final Integer port;

	/**
	 * Constructor.
	 * @param newDirectory - directory where file should be stored.
	 * @param newKey - key which client must send to server.
	 * @param newFileName - name of new file.
	 * @param newHost - host to connect.
	 * @param port - server port to connect.
	 */
	public ReceiveFileTask( final File newDirectory, final String newKey, 
							final String newFileName, final String newHost,
							final Integer port )
	{
		this.directory = newDirectory;
		this.key = newKey;
		this.fileName = newFileName;
		this.host = newHost;
		this.port = port;
	}

	@Override
	public final Object execute( final Object params )
	{
		final SocketWrapper socket = new SocketWrapper( port, host );
		socket.receiveFile( key, fileName, directory );
		return true;
	}
}
