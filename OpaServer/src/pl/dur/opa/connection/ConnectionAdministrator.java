/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.dur.opa.connection;

import java.io.File;
import java.util.Random;
import pl.dur.opa.file.browser.LocalFileAdministrator;
import pl.dur.opa.remote.interfaces.Notificator;
import pl.dur.opa.tasks.ReceiveFileTask;
import pl.dur.opa.tasks.SendFileTask;

/**
 *
 * @author Dur
 */
public class ConnectionAdministrator
{
	private final static int KEY_SIZE = 8;
	
	private Notificator notificator;

	public ConnectionAdministrator(Notificator notificator)
	{
		this.notificator = notificator;
	}

	
	public String getSocketNumForFileSending( File file )
	{
		String taskKey = generateRandomString();
		ConnectionAccepter.putTaskToMap( taskKey, new SendFileTask( file, notificator ) );
		return taskKey;
	}

	public String getSocketForFileReceiving( File directory, String name, LocalFileAdministrator fileAdmin, long lastModified )
	{
		String taskKey = generateRandomString();
		taskKey.getBytes();
		ConnectionAccepter.putTaskToMap( taskKey, new ReceiveFileTask( directory, name, fileAdmin, lastModified, notificator ) );
		return taskKey;
	}

	private String generateRandomString()
	{
		String key;
		Random generator = new Random();
		byte keyBytes[] = new byte[ KEY_SIZE ];
		generator.nextBytes( keyBytes );
		for( int i = 0; i < keyBytes.length; i++ )
		{
			if( keyBytes[i] < 0 )
			{
				keyBytes[i] = (byte) -keyBytes[i];
			}
		}
		key = new String( keyBytes );
		return key;
	}

	public int getSocketNumForFilesSending( File[] file )
	{
		int port = 1024;
		return port;
	}

	public int getSocketForFilesReceiving( File directory )
	{
		int port = 1024;
		return port;
	}
}
