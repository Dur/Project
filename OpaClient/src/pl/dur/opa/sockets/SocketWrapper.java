package pl.dur.opa.sockets;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import pl.dur.opa.file.browser.LocalFileAdministrator;
import pl.dur.opa.utils.Fraction;
import pl.dur.opa.utils.StateObserver;

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
		fileAdmin = new LocalFileAdministrator( directory, name );
		File file = fileAdmin.getFile();
		FileOutputStream inFile = null;
		try
		{
			inFile = new FileOutputStream( file );
		}
		catch( FileNotFoundException ex )
		{
			StateObserver.log("Problem with receiving file");
			return;
		}
		try
		{
			socket = new Socket( host, port );
			byte[] buffer = new byte[ PACKAGE_SIZE ];
			int len = 0;
			InputStream is = socket.getInputStream();
			OutputStream outputSocket = socket.getOutputStream();
			outputSocket.write( key.getBytes() );
			BufferedInputStream bufferedInput = new BufferedInputStream( is, PACKAGE_SIZE );
			while( (len = bufferedInput.read( buffer, 0, PACKAGE_SIZE )) != NO_DATA )
			{
				inFile.write( buffer, 0, len );
			}
			socket.close();
			inFile.close();
			fileAdmin.getFile().setLastModified( lastModified );

		}
		catch( IOException ex )
		{
			try
			{
				inFile.close();
				socket.close();
				StateObserver.log("Server terminated connection for unknown reason");
				file.delete();
				StateObserver.logOutUser();
			}
			catch( IOException ex1 )
			{
				StateObserver.log("Cannt close file");
			}
			
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
		FileInputStream in = null;
		try
		{
			in = new FileInputStream( fileToSend );
		}
		catch( FileNotFoundException ex )
		{
			StateObserver.log("File not found");
		}
		try
		{
			Fraction fraction;
			long bytesSend = 0;
			Long fileSize = fileToSend.length();
			socket = new Socket( host, port );
			byte[] buffer = new byte[ PACKAGE_SIZE ];
			OutputStream os = socket.getOutputStream();
			BufferedOutputStream out = new BufferedOutputStream( os, PACKAGE_SIZE );
			out.write( key.getBytes() );
			int len = 0;
			while( (len = in.read( buffer, 0, PACKAGE_SIZE )) != NO_DATA )
			{
				bytesSend += len;
				fraction = new Fraction( fileSize, bytesSend, "Sending: " + fileToSend.getName() );
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
			try
			{
				StateObserver.log("Server terminated connection for unknown reason");
				StateObserver.logOutUser();
				socket.close();
				in.close();
			}
			catch( IOException ex1 )
			{
				StateObserver.log("Problem with closing socket");
			}
		}
		catch( InterruptedException ie )
		{
			StateObserver.log("Interrupt exception");
		}
	}
}
