/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.dur.opa.main;

import com.google.common.io.Files;
import java.io.File;
import java.io.IOException;
import pl.dur.opa.file.browser.LocalFileAdministrator;

/**
 *
 * @author Dur
 */
public class testClass
{
	public static void main( String[] args ) throws IOException
	{
//		LocalFileAdministrator fileAdmin = new LocalFileAdministrator( new File( "C:\\" ), "nowyPlik.txt" );
		//LocalFileAdministrator fileAdmin = new LocalFileAdministrator( new File( "C:\\test.iso" ) );
//		fileAdmin.writeToFile( "chujmuju".getBytes() );
//		fileAdmin.appendToFile( "test".getBytes() );
//		fileAdmin.appendToFile( "test".getBytes() );
//		fileAdmin.appendToFile( "test".getBytes() );
//		fileAdmin.appendToFile( "test".getBytes() );
//		fileAdmin.appendToFile( "test".getBytes() );
//		fileAdmin.appendToFile( "test".getBytes() );
//		fileAdmin.appendToFile( "test".getBytes() );
		
		//LocalFileAdministrator kopia = new LocalFileAdministrator( new File( "C:\\" ), "kopia.iso" );
		File file = new File("D:\\Gry\\ac.iso");
		System.out.println(Files.getChecksum( file, new java.util.zip.CRC32() ));
//		File fileToSend = fileAdmin.getFile();
//		FileOutputStream stream = null;
//		BufferedInputStream input = null;
//		try
//		{
//			stream = new FileOutputStream(kopia.getFile());
//			input = new BufferedInputStream( new FileInputStream( fileToSend ) );
//			BufferedOutputStream outStream = new BufferedOutputStream( stream );
//			byte buffer[] = new byte[ 1024 ];
//			int read;
//			while( (read = input.read( buffer )) != -1 )
//			{
//				outStream.write( buffer, 0, read );
//				outStream.flush();
//			}
//			input.close();
//			outStream.close();
//		}
//		catch( FileNotFoundException ex )
//		{
//			Logger.getLogger( testClass.class.getName() ).log( Level.SEVERE, null, ex );
//		}
//		catch(IOException ex )
//		{
//			ex.printStackTrace();
//		}
			
	}
}
