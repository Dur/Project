package pl.dur.opa.file.browser;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;
import org.apache.commons.io.FileUtils;
import pl.dur.opa.utils.ExtendedFile;
import pl.dur.opa.utils.Logger;

/**
 *
 * @author Dur
 */
public class LocalFileAdministrator
{
	private File filesList;
	private File homeDirectory;
	private HashMap<String, List<ExtendedFile>> filesTree = new HashMap<>();

	public LocalFileAdministrator( File homeDirectory, String userLogin, boolean flag )
	{
		this.homeDirectory = new File( homeDirectory.getPath() + File.separator + userLogin );
		if( !this.homeDirectory.exists() )
		{
			homeDirectory.mkdirs();
		}
		filesList = new File( homeDirectory + File.separator + userLogin + ".flf" );
		if( !filesList.exists() )
		{
			try
			{
				filesList.createNewFile();
			}
			catch( IOException ex )
			{
				ex.printStackTrace();
			}
		}
		else
		{
			constructTreeOfFiles();
		}
	}

	private void constructTreeOfFiles()
	{
		ExtendedFile currentFile;
		try
		{
			FileReader inFile = new FileReader( filesList );
			BufferedReader reader = new BufferedReader( inFile );
			String line;
			String filePath;
			String CRC;
			StringTokenizer tokenizer;

			ExtendedFile temp = new ExtendedFile( homeDirectory + File.separator + "temp.tmp" );

			PrintStream stream = new PrintStream( temp );
			while( (line = reader.readLine()) != null )
			{
				tokenizer = new StringTokenizer( line, ";" );
				filePath = tokenizer.nextToken().trim();
				CRC = tokenizer.nextToken().trim();
				currentFile = new ExtendedFile( filePath, Long.parseLong( CRC ) );
				if( currentFile.exists() )
				{
					putFileIntoFilesMap( currentFile );
					stream.println( line );
				}
			}
			reader.close();
			stream.close();
			FileUtils.copyFile( temp, filesList );
			temp.delete();
		}
		catch( FileNotFoundException ex )
		{
			ex.printStackTrace();
		}
		catch( IOException iex )
		{
			iex.printStackTrace();
		}
		catch( Exception ex )
		{
			ex.printStackTrace();
		}
	}

	private void putFileIntoFilesMap( ExtendedFile fileToPut )
	{
		List<ExtendedFile> files;
		if( (files = filesTree.get( fileToPut.getName() )) != null )
		{
			files.add( fileToPut );
		}
		else
		{
			files = new ArrayList<>();
			files.add( fileToPut );
			filesTree.put( fileToPut.getName(), files );
		}
	}

	public void listFiles()
	{
		for( List<ExtendedFile> files : filesTree.values() )
		{
			for( ExtendedFile file : files )
			{
				Logger.log( file.getPath() + " " + file.getFileCheckSum() );
			}
		}
	}

	public void removeFileFromServer( ExtendedFile file )
	{
		List<ExtendedFile> list = filesTree.get( file.getName() );
		ExtendedFile fileToDelete = null;
		if( list != null && list.size() > 0 )
		{
			for( ExtendedFile tempFile : list )
			{
				if( file.getPath().equals( tempFile.getPath() ) )
				{
					fileToDelete = tempFile;
					removeFileFromFile( file.getPath() );
					file.delete();
					break;
				}
			}
			if( fileToDelete != null )
			{
				list.remove( fileToDelete );
				if( list.isEmpty() )
				{
					filesTree.remove( file.getName() );
				}
			}
		}
	}

	private void removeFileFromFile( String path )
	{
		try
		{
			FileReader inFile = new FileReader( filesList );
			BufferedReader reader = new BufferedReader( inFile );
			String line;
			String filePath;
			StringTokenizer tokenizer;
			ExtendedFile temp = new ExtendedFile( homeDirectory + File.separator + "temp.tmp" );
			PrintStream stream = new PrintStream( temp );
			while( (line = reader.readLine()) != null )
			{
				tokenizer = new StringTokenizer( line, ";" );
				filePath = tokenizer.nextToken().trim();
				if( !filePath.equals( path ) )
				{
					stream.println( line );
				}
			}
			reader.close();
			stream.close();
			FileUtils.copyFile( temp, filesList );
			temp.delete();
		}
		catch( FileNotFoundException ex )
		{
			ex.printStackTrace();
		}
		catch( IOException iex )
		{
			iex.printStackTrace();
		}
		catch( Exception ex )
		{
			ex.printStackTrace();
		}
	}

	public boolean isFileStoredOnServer( ExtendedFile file )
	{
		List<ExtendedFile> fromMap;
		if( (fromMap = filesTree.get( file.getName() )) == null )
		{
			return false;
		}
		if( file.getFileCheckSum() != 0 )
		{
			return findByCRC( fromMap, file ) != null;
		}
		else
		{
			return findWithoutCRC( fromMap, file ) != null;
		}
	}

	private ExtendedFile findByCRC( List<ExtendedFile> list, ExtendedFile file )
	{
		for( ExtendedFile temp : list )
		{
			if( temp.getFileCheckSum() == file.getFileCheckSum() )
			{
				return temp;
			}
		}
		return null;
	}

	private ExtendedFile findWithoutCRC( List<ExtendedFile> list, ExtendedFile file )
	{
		for( ExtendedFile temp : list )
		{
			if( temp.lastModified() == file.lastModified() && temp.length() == file.length() )
			{
				return temp;
			}
		}
		return null;
	}

	public void addNewFileToServer( String input, ExtendedFile file )
	{
		try
		{
			ByteBuffer bbuf = ByteBuffer.wrap( input.getBytes() );
			FileChannel wChannel = new FileOutputStream( filesList, true ).getChannel();
			wChannel.write( bbuf );
			wChannel.close();
			putFileIntoFilesMap( file );
		}
		catch( FileNotFoundException fnfex )
		{
			fnfex.printStackTrace();
		}
		catch( IOException ex )
		{
			ex.printStackTrace();
		}
	}

	public File getHomeDir()
	{
		return homeDirectory;
	}

	public File locateFile( ExtendedFile file )
	{
		List<ExtendedFile> fromMap;
		if( (fromMap = filesTree.get( file.getName() )) != null )
		{
			if( file.getFileCheckSum() != 0 )
			{
				return (findByCRC( fromMap, file )).getParentFile();
			}
			else
			{
				return (findWithoutCRC( fromMap, file )).getParentFile();
			}
		}
		return null;
	}
}
