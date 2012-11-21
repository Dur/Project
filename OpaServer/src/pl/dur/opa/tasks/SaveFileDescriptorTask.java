package pl.dur.opa.tasks;

import com.google.common.io.Files;
import java.io.IOException;
import java.rmi.RemoteException;
import pl.dur.opa.file.browser.LocalFileAdministrator;
import pl.dur.opa.remote.interfaces.Notificator;
import pl.dur.opa.utils.ExtendedFile;
import pl.dur.opa.utils.Logger;

/**
 * Task which writes to text file all informations about file which is passed to
 * it. Every line in this file contains exact path to file and its checksum.
 *
 * @author Dur
 */
public class SaveFileDescriptorTask extends TaskNotificator implements Task
{
	private ExtendedFile savedFile;
	private LocalFileAdministrator fileAdmin;

	/**
	 * Constructor.
	 *
	 * @param file - file which we want to describe in conf file.
	 * @param configurationFile - file which contains all informationns about
	 * all files assignned to specify user.
	 */
	public SaveFileDescriptorTask( ExtendedFile file, LocalFileAdministrator fileAdmin, Notificator notificator )
	{
		super(notificator);
		this.savedFile = file;
		this.fileAdmin = fileAdmin;
	}

	@Override
	public Object execute( Object params )
	{
		try
		{
			//super.getNotificator().serverComputingMessage( "Calculating CRC", true);
			Long CRC = Files.getChecksum( savedFile, new java.util.zip.CRC32() );
			String input = savedFile.getPath() + ";" + CRC + "\n";
			savedFile.setFileCheckSum( CRC );
			fileAdmin.addNewFileToServer( input, savedFile );
			//super.getNotificator().serverComputingMessage( "Ready", false);
		}
		catch( IOException ex )
		{
			ex.printStackTrace();
			try
			{
				super.getNotificator().sendMessageToClient( "Problem with CRC calculation. Try to send file once again");
			}
			catch( RemoteException ex1 )
			{
				Logger.log("Problem with comuniaction with client");
			}
		}
		return true;
	}
}
