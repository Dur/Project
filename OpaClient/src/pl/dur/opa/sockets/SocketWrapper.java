package pl.dur.opa.sockets;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import pl.dur.opa.file.browser.LocalFileAdministrator;

/**
 * Class to wrapp socket and make some operations on it. It can send files ad make some other stuff.
 *
 * @author Dur
 */
public class SocketWrapper
{
	private static int NO_DATA = -1;
	private int port;
	private Socket socket;
	private String host;
	private LocalFileAdministrator fileAdmin;
	private final static int PACKAGE_SIZE = 1024;

	/**
	 * Constructor.
	 *
	 * @param newPort - port for new socket.
	 * @param newHost - host to connect.
	 */
	public SocketWrapper( final int newPort, final String newHost )
	{
		this.port = newPort;
		this.host = newHost;
	}

	public final static int getAvailablePort()
	{
		int port = -1;
		try
		{
			ServerSocket socket = new ServerSocket( 0 );
			port = socket.getLocalPort();
			socket.setReuseAddress( true );
			socket.close();
		}
		catch( IOException ex )
		{
			ex.printStackTrace();
		}
		return port;
	}

	/**
	 * Method creates new socket and connects to server socket. LocalFileAdministrator class is used to administrate selected file. File will stored in client local file
	 * system.
	 *
	 * @param name - name of file.
	 * @param directory - directory where selected file should be stored.
	 */
	public void receiveFile( String name, File directory )
	{
		try
		{
			socket = new Socket( host, port );
			fileAdmin = new LocalFileAdministrator( directory, name );
			byte[] buffer = new byte[ PACKAGE_SIZE ];
			int len = 0;
			int bytcount = PACKAGE_SIZE;
			FileOutputStream inFile = new FileOutputStream( fileAdmin.getFile() );
			InputStream is = socket.getInputStream();
			BufferedInputStream bufferedInput = new BufferedInputStream( is, PACKAGE_SIZE );
			while( (len = bufferedInput.read( buffer, 0, PACKAGE_SIZE )) != NO_DATA )
			{
				inFile.write( buffer, 0, len );
			}
			socket.close();
		}
		catch( IOException ex )
		{
			ex.printStackTrace();
		}
	}

	/**
	 * Method opens new socket and connects to server to send selected file to server.
	 *
	 * @param file - selected file.
	 */
	public void sendFile( File file )
	{
		try
		{
			socket = new Socket( host, port );
			System.out.println("After connecting to server");
			fileAdmin = new LocalFileAdministrator( file );
			File fileToSend = fileAdmin.getFile();
			byte[] buffer = new byte[ PACKAGE_SIZE ];
			OutputStream os = socket.getOutputStream();
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
			socket.shutdownOutput();
			socket.close();
		}
		catch( IOException ex )
		{
			ex.printStackTrace();
		}
	}
}