/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.dur.opa.tasks;

import com.google.common.io.Files;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Task which writes to text file all informations about file which is passed to
 * it. Every line in this file contains exact path to file and its checksum.
 *
 * @author Dur
 */
public class SaveFileDescriptorTask implements Task
{
	private File savedFile;
	private File config;

	/**
	 * Constructor.
	 *
	 * @param file - file which we want to describe in conf file.
	 * @param configurationFile - file which contains all informationns about
	 * all files assignned to specify user.
	 */
	public SaveFileDescriptorTask( File file, File configurationFile )
	{
		this.savedFile = file;
		this.config = configurationFile;
		if( !config.exists() )
		{
			try
			{
				config.createNewFile();
			}
			catch( IOException ex )
			{
				ex.printStackTrace();
			}
		}
	}

	@Override
	public Object execute( Object params )
	{
		try
		{
			Long CRC = Files.getChecksum( savedFile, new java.util.zip.CRC32() );
			String input = savedFile.getPath() + " " + CRC + "\n";
			ByteBuffer bbuf = ByteBuffer.wrap( input.getBytes() );
			FileChannel wChannel = new FileOutputStream( config, true ).
					getChannel();
			wChannel.write( bbuf );
			wChannel.close();
		}
		catch( IOException ex )
		{
			ex.printStackTrace();
		}
		return true;
	}
}
