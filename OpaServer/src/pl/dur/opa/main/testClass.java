/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.dur.opa.main;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import pl.dur.opa.file.browser.LocalFileAdministrator;

/**
 *
 * @author Dur
 */
public class testClass
{
	public static void main( String[] args )
	{
//		LocalFileAdministrator fileAdmin = new LocalFileAdministrator( new File( "C:\\" ), "nowyPlik.txt" );
		LocalFileAdministrator fileAdmin = new LocalFileAdministrator( new File( "C:\\test.iso" ) );
//		fileAdmin.writeToFile( "chujmuju".getBytes() );
//		fileAdmin.appendToFile( "test".getBytes() );
//		fileAdmin.appendToFile( "test".getBytes() );
//		fileAdmin.appendToFile( "test".getBytes() );
//		fileAdmin.appendToFile( "test".getBytes() );
//		fileAdmin.appendToFile( "test".getBytes() );
//		fileAdmin.appendToFile( "test".getBytes() );
//		fileAdmin.appendToFile( "test".getBytes() );
		
		LocalFileAdministrator kopia = new LocalFileAdministrator( new File( "C:\\" ), "kopia.iso" );
		
		File fileToSend = fileAdmin.getFile();
		FileOutputStream stream = null;
		BufferedInputStream input = null;
		try
		{
			stream = new FileOutputStream(kopia.getFile());
			input = new BufferedInputStream( new FileInputStream( fileToSend ) );
			BufferedOutputStream outStream = new BufferedOutputStream( stream );
			byte buffer[] = new byte[ 1024 ];
			int read;
			while( (read = input.read( buffer )) != -1 )
			{
				outStream.write( buffer, 0, read );
				outStream.flush();
			}
			input.close();
			outStream.close();
		}
		catch( FileNotFoundException ex )
		{
			Logger.getLogger( testClass.class.getName() ).log( Level.SEVERE, null, ex );
		}
		catch(IOException ex )
		{
			ex.printStackTrace();
		}
			
	}
}
