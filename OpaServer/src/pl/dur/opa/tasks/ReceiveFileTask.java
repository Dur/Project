package pl.dur.opa.tasks;

import java.io.File;
import pl.dur.opa.connection.SocketWrapper;
import pl.dur.opa.file.browser.LocalFileAdministrator;
import pl.dur.opa.remote.interfaces.Notificator;

/**
 * Receive file tak. Implemetns Task to be run in new thread by TaskExecutor.
 * This task creates new SocketWrapper and starting to receiving file on open socket.
 * File is save on local server file system.
 * @author Dur
 */
public class ReceiveFileTask extends TaskNotificator implements Task, SocketTask
{
	private final File directory;
	private final String fileName;
	private SocketWrapper socketWrapper;
	private LocalFileAdministrator fileAdmin;
	private long lastModified;
	private Notificator notificator;

	/**
	 * Constructor.
	 * @param newDirectory - directory where file should be stored.
	 * @param newFileName - name of new file.
	 */
	public ReceiveFileTask( final File newDirectory, final String newFileName, 
							LocalFileAdministrator fileAdmin, long lastModified,
							Notificator notificator )
	{
		super(notificator);
		this.directory = newDirectory;
		this.fileName = newFileName;
		this.fileAdmin = fileAdmin;
		this.lastModified = lastModified;
		this.notificator = notificator;
	}

	@Override
	public final Object execute( final Object params )
	{
		socketWrapper.receiveFile( fileName, directory, fileAdmin, lastModified, super.getNotificator() );
		return true;
	}

	@Override
	public void setSocket( SocketWrapper socket )
	{
		this.socketWrapper = socket;
	}
}
