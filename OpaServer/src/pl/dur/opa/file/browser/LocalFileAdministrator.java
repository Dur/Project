/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.dur.opa.file.browser;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

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
		if( !selectedFile.exists() )
		{
			try
			{
				selectedFile.createNewFile();
			}
			catch( IOException ex )
			{
				Logger.getLogger( LocalFileAdministrator.class.getName() ).
						log( Level.SEVERE, null, ex );
			}
		}
	}

	public LocalFileAdministrator( File directory, String newFileName )
	{
		selectedFile = null;
		try
		{
			selectedFile = new File( "C:" + File.separator + "copies" + File.separator + newFileName );
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

	public boolean writeToFile( byte[] data )
	{
		FileWriter fstream = null;
		try
		{
			fstream = new FileWriter( selectedFile );
			BufferedWriter out = new BufferedWriter( fstream );
			out.write( new String( data ) );
			out.close();
		}
		catch( FileNotFoundException ex )
		{
			ex.printStackTrace();
		}
		catch( IOException ex )
		{
			ex.printStackTrace();
		}
		return true;
	}

	public boolean appendToFile( byte[] data )
	{
		FileWriter fstream = null;
		try
		{
			fstream = new FileWriter( selectedFile, true );
			BufferedWriter out = new BufferedWriter( fstream );
			out.append( new String( data ) );
			out.close();
		}
		catch( IOException ex )
		{
			ex.printStackTrace();
		}
		return true;
	}

	public File getFile()
	{
		return selectedFile;
	}
}
