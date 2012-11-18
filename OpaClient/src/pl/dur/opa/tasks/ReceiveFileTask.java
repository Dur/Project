package pl.dur.opa.tasks;

import java.io.File;
import java.rmi.RemoteException;
import java.util.List;
import pl.dur.opa.remote.interfaces.UsersInterface;
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
	private final String host;
	private final Integer port;
	private UsersInterface manipulator;
	private List<File> files;

	/**
	 * Constructor.
	 * @param newDirectory - directory where file should be stored.
	 * @param newKey - key which client must send to server.
	 * @param newFileName - name of new file.
	 * @param newHost - host to connect.
	 * @param port - server port to connect.
	 */
	public ReceiveFileTask( final File newDirectory,
							List<File> files,
							final String newHost,
							final Integer port,
							UsersInterface manipulator)
	{
		this.directory = newDirectory;
		this.host = newHost;
		this.port = port;
		this.manipulator = manipulator;
		this.files = files;
	}

	@Override
	public final Object execute( final Object params )
	{
		final SocketWrapper socket = new SocketWrapper( port, host, null );
		String key;
		for( File fileToReceive : files )
		{
			try
			{
				key = manipulator.getFile( fileToReceive );
				long lastModified = manipulator.getFileLastModify( fileToReceive );
				socket.receiveFile( key, fileToReceive.getName(), directory, lastModified );
			}
			catch( RemoteException ex )
			{
				ex.printStackTrace();
			}
		}
		return true;
	}
}
