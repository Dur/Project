package pl.dur.opa.connection;

import java.io.*;
import java.net.Socket;
import pl.dur.opa.file.browser.LocalFileAdministrator;
import pl.dur.opa.remote.interfaces.Notificator;
import pl.dur.opa.tasks.SaveFileDescriptorTask;
import pl.dur.opa.tasks.TaskExecutor;
import pl.dur.opa.utils.ExtendedFile;
import pl.dur.opa.utils.Logger;

/**
 * Class to wrapp socket and make some operations on it. It can send files ad
 * make some other stuff.
 *
 * @author Dur
 */
public class SocketWrapper
{
	private Socket client;
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
	public void receiveFile( String name, File directory, LocalFileAdministrator fileAdmin, long lastModified, Notificator notificator )
	{
		String clientAddress = client.getInetAddress().getHostAddress();
		ExtendedFile file = new ExtendedFile( directory.getPath() + File.separator + name );
		FileOutputStream inFile=null;
		try
		{
			inFile = new FileOutputStream( file );
		}
		catch( FileNotFoundException ex )
		{
			Logger.log("Problem with file "+file.getPath());
		}
		try
		{
			byte[] buffer = new byte[ PACKAGE_SIZE ];
			int len = 0;
			InputStream is = client.getInputStream();
			BufferedInputStream bufferedInput = new BufferedInputStream( is, PACKAGE_SIZE );
			while( (len = bufferedInput.read( buffer, 0, PACKAGE_SIZE )) != NO_DATA )
			{
				inFile.write( buffer, 0, len );
			}
			client.close();
			inFile.close();
			file.setLastModified( lastModified );
			file.length();
			TaskExecutor executor = new TaskExecutor( new SaveFileDescriptorTask( file, fileAdmin, notificator ) );
			Thread thread = new Thread(executor);
			thread.start();
		}
		catch( IOException ex )
		{
			Logger.log( "Comunication with client " + clientAddress + " terminated for unknows reason" );
			try
			{
				inFile.close();
			}
			catch( IOException ex1 )
			{
				Logger.log("Problem with file " + file.getPath() );
			}
			file.delete();
		}
	}

	public void sendFile( File file, Notificator notificator )
	{
		Double size = new Double(file.length());
		String clientAddress = client.getInetAddress().getHostAddress();
		ExtendedFile fileToSend = new ExtendedFile( file.getPath() );
		FileInputStream in = null;
		try
		{
			in = new FileInputStream( fileToSend );
		}
		catch( FileNotFoundException ex )
		{
			Logger.log("Problem with file to send");
			return;
		}
		try
		{
			byte[] buffer = new byte[ PACKAGE_SIZE ];
			OutputStream os = client.getOutputStream();
			BufferedOutputStream out = new BufferedOutputStream( os, PACKAGE_SIZE );
			int len = 0;
			int bytecount = 0;
			while( (len = in.read( buffer, 0, PACKAGE_SIZE )) != NO_DATA )
			{
				bytecount += len;
				out.write( buffer, 0, len );
				out.flush();
				notificator.serverProgressMessage( "Sending: " + file.getName() + "  ", new Double((bytecount/size)*100).intValue());
			}
			client.shutdownOutput();
			client.close();
			out.close();
		}
		catch( IOException ex )
		{
			try
			{
				client.close();
				in.close();
				Logger.log( "Comunication with client " + clientAddress + " terminated for unknows reason" );
			}
			catch( IOException ex1 )
			{
				Logger.log("Cannot close socket");
			}
		}
	}
}
