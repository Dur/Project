/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.dur.opa.tasks;

import com.google.common.io.Files;
import java.io.IOException;
import pl.dur.opa.file.browser.LocalFileAdministrator;
import pl.dur.opa.utils.ExtendedFile;

/**
 * Task which writes to text file all informations about file which is passed to
 * it. Every line in this file contains exact path to file and its checksum.
 *
 * @author Dur
 */
public class SaveFileDescriptorTask implements Task
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
	public SaveFileDescriptorTask( ExtendedFile file, LocalFileAdministrator fileAdmin )
	{
		this.savedFile = file;
		this.fileAdmin = fileAdmin;
	}

	@Override
	public Object execute( Object params )
	{
		try
		{
			System.out.println("Calculating crc");
			Long CRC = Files.getChecksum( savedFile, new java.util.zip.CRC32() );
			String input = savedFile.getPath() + ";" + CRC + "\n";
			fileAdmin.addNewFileToServer( input, savedFile );
		}
		catch( IOException ex )
		{
			ex.printStackTrace();
		}
		return true;
	}
}
