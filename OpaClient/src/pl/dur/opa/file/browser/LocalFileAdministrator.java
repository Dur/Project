package pl.dur.opa.file.browser;

import java.io.File;
import java.io.IOException;

/**
 *
 * @author Dur
 */
public class LocalFileAdministrator
{
	private File selectedFile;

	public LocalFileAdministrator( File file )
	{
		selectedFile = file;
	}

	public LocalFileAdministrator( File directory, String newFileName )
	{
		selectedFile = null;
		try
		{
			selectedFile = new File( directory.getAbsolutePath() + File.separator + newFileName );
			if( !selectedFile.exists() )
			{
				selectedFile.createNewFile();
			}
		}
		catch( IOException ex )
		{
			ex.printStackTrace();
		}
	}

	public LocalFileAdministrator( String path )
	{
		selectedFile = new File( path );
	}

	public File getFile()
	{
		return selectedFile;
	}
}
