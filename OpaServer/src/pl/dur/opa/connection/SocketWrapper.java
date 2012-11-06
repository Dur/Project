package pl.dur.opa.connection;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import pl.dur.opa.file.browser.LocalFileAdministrator;

/**
 * Class to wrapp socket and make some operations on it. It can send files ad
 * make some other stuff.
 *
 * @author Dur
 */
public class SocketWrapper
{
	private Socket client;
	private LocalFileAdministrator fileAdmin;
	private final static int PACKAGE_SIZE = 1024;
	private final static int NO_DATA = -1;

	/**
	 * Constructor.
	 *
	 * @param newPort - port for new socket.
	 */
	public SocketWrapper( final Socket socket )
	{
			client = socket;
	}

	/**
	 * Method opens new socket and after clients connection send selected file.
	 * LocalFileAdministrator class is used to administrate selected file.
	 *
	 * @param name - name of file.
	 * @param directory - directory where selected file should be stored.
	 */
	public void receiveFile( String name, File directory )
	{
		try
		{
			fileAdmin = new LocalFileAdministrator( directory, name );
			byte[] buffer = new byte[ PACKAGE_SIZE ];
			int len = 0;
			FileOutputStream inFile = new FileOutputStream( fileAdmin.getFile() );
			InputStream is = client.getInputStream();
			BufferedInputStream bufferedInput = new BufferedInputStream( is, PACKAGE_SIZE );
			while( (len = bufferedInput.read( buffer, 0, PACKAGE_SIZE )) != NO_DATA )
			{
				inFile.write( buffer, 0, len );
			}
			client.close();
			inFile.close();
		}
		catch( IOException ex )
		{
			ex.printStackTrace();
		}
	}

	public void sendFile( File file )
	{
		try
		{
			fileAdmin = new LocalFileAdministrator( file );
			File fileToSend = fileAdmin.getFile();
			byte[] buffer = new byte[ PACKAGE_SIZE ];
			OutputStream os = client.getOutputStream();
			BufferedOutputStream out = new BufferedOutputStream( os, PACKAGE_SIZE );
			FileInputStream in = new FileInputStream( fileToSend );
			int len = 0;
			int bytecount = PACKAGE_SIZE;
			while( (len = in.read( buffer, 0, PACKAGE_SIZE )) != NO_DATA )
			{
				bytecount = bytecount + PACKAGE_SIZE;
				out.write( buffer, 0, len );
				out.flush();
			}
			client.shutdownOutput();
		}
		catch( IOException ex )
		{
			ex.printStackTrace();
		}
	}
}
