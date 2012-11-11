package pl.dur.opa.file.browser;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;
import pl.dur.opa.utils.ExtendedFile;

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
		this.homeDirectory = homeDirectory;
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
				tokenizer = new StringTokenizer( line, " " );
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
		//	FileUtils.copyFile( temp, filesList);
		//	temp.delete();
		}
		catch( FileNotFoundException ex )
		{
			ex.printStackTrace();
		}
		catch( IOException iex )
		{
			iex.printStackTrace();
		}
		catch (Exception ex )
		{
			ex.printStackTrace();
		}
	}

	private void putFileIntoFilesMap( ExtendedFile fileToPut )
	{
		List<ExtendedFile> files;
		if(  ( files = filesTree.get( fileToPut.getName() ) ) != null )
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
				System.out.println(file.getPath() + " " + file.getFileCheckSum() );
			}
		}
	}
}
