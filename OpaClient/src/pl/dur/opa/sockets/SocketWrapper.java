package pl.dur.opa.sockets;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import pl.dur.opa.file.browser.LocalFileAdministrator;
import pl.dur.opa.utils.Fraction;

/**
 * Class to wrapp socket and make some operations on it. It can send files ad
 * make some other stuff.
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
	private BlockingQueue<Fraction> queue;

	/**
	 * Constructor.
	 *
	 * @param newPort - port for new socket.
	 * @param newHost - host to connect.
	 */
	public SocketWrapper( final int newPort, final String newHost, BlockingQueue<Fraction> queue )
	{
		this.port = newPort;
		this.host = newHost;
		this.queue = queue;
	}

	/**
	 * Method creates new socket and connects to server socket.
	 * LocalFileAdministrator class is used to administrate selected file. File
	 * will stored in client local file system.
	 *
	 * @param name - name of file.
	 * @param directory - directory where selected file should be stored.
	 */
	public void receiveFile( String key, String name, File directory, long lastModified )
	{
		try
		{
			socket = new Socket( host, port );
			fileAdmin = new LocalFileAdministrator( directory, name );
			byte[] buffer = new byte[ PACKAGE_SIZE ];
			int len = 0;
			FileOutputStream inFile = new FileOutputStream( fileAdmin.getFile() );
			InputStream is = socket.getInputStream();
			OutputStream outputSocket = socket.getOutputStream();
			outputSocket.write( key.getBytes() );
			BufferedInputStream bufferedInput = new BufferedInputStream( is, PACKAGE_SIZE );
			while( (len = bufferedInput.read( buffer, 0, PACKAGE_SIZE )) != NO_DATA )
			{
				inFile.write( buffer, 0, len );
			}
			socket.close();
			fileAdmin.getFile().setLastModified( lastModified );
			inFile.close();
			
		}
		catch( IOException ex )
		{
			ex.printStackTrace();
		}
	}

	/**
	 * Method opens new socket and connects to server to send selected file to
	 * server.
	 *
	 * @param file - selected file.
	 */
	public void sendFile( String key, File fileToSend, long lastModified )
	{
		try
		{
			Fraction fraction;
			long bytesSend = 0;
			Long fileSize = fileToSend.length();
			socket = new Socket( host, port );
			System.out.println( "After connecting to server" );
			byte[] buffer = new byte[ PACKAGE_SIZE ];
			OutputStream os = socket.getOutputStream();
			BufferedOutputStream out = new BufferedOutputStream( os, PACKAGE_SIZE );
			out.write( key.getBytes() );
			FileInputStream in = new FileInputStream( fileToSend );
			int len = 0;
			while( (len = in.read( buffer, 0, PACKAGE_SIZE )) != NO_DATA )
			{
				bytesSend +=len;
				fraction = new Fraction( fileSize, bytesSend );
				queue.put( fraction );
				out.write( buffer, 0, len );
				out.flush();
			}
			socket.shutdownOutput();
			socket.close();
			in.close();
			fileToSend.setLastModified( lastModified );
		}
		catch( IOException ex )
		{
			ex.printStackTrace();
		}
		catch(InterruptedException ie)
		{
			ie.printStackTrace();
		}
	}
}
