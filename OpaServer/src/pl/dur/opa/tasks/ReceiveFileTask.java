package pl.dur.opa.tasks;

import java.io.File;
import pl.dur.opa.connection.SocketWrapper;
import pl.dur.opa.file.browser.LocalFileAdministrator;

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
	private LocalFileAdministrator fileAdmin;

	/**
	 * Constructor.
	 * @param newDirectory - directory where file should be stored.
	 * @param newFileName - name of new file.
	 */
	public ReceiveFileTask( final File newDirectory, final String newFileName, LocalFileAdministrator fileAdmin )
	{
		this.directory = newDirectory;
		this.fileName = newFileName;
		this.fileAdmin = fileAdmin;
	}

	@Override
	public final Object execute( final Object params )
	{
		socketWrapper.receiveFile( fileName, directory, fileAdmin );
		return true;
	}

	@Override
	public void setSocket( SocketWrapper socket )
	{
		this.socketWrapper = socket;
	}
}
