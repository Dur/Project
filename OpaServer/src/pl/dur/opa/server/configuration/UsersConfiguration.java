/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.dur.opa.server.configuration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.StringTokenizer;

/**
 * Class which reads configuration file and updates server dependencies.
 * Configuration file can be updated befor server start. To reconfigure the
 * server by this file server must be restarted.
 *
 * @author Dur
 */
public class UsersConfiguration
{
	private HashMap<String, String> users = new HashMap<>();

	public UsersConfiguration()
	{
		readRegisteredUsers();
	}

	public final void readRegisteredUsers()
	{
		File configurationFile = new File( "conf//users.conf" );
		if( !configurationFile.exists() )
		{
			try
			{
				configurationFile.createNewFile();
			}
			catch( IOException ex )
			{
				ex.printStackTrace();
			}
			return;
		}
		try
		{
			FileReader inFile = new FileReader( configurationFile );
			BufferedReader reader = new BufferedReader( inFile );
			String confLine;
			String key;
			String value;
			StringTokenizer tokenizer;
			while( (confLine = reader.readLine()) != null )
			{
				if( !confLine.startsWith( "#" ) )
				{
					tokenizer = new StringTokenizer( confLine, "=" );
					key = tokenizer.nextToken().trim();
					value = tokenizer.nextToken().trim();
					if( key.contains( " " ) || value.contains( " " ) )
					{
						System.out.println( "bad configuration file structure" );
						return;
					}
					users.put( key, value );
					System.out.println( key + " = " + value );
				}
			}
		}
		catch( FileNotFoundException ex )
		{
			return;
		}
		catch( IOException iex )
		{
			iex.printStackTrace();
			return;
		}
	}

	public boolean checkCredentials( String login, String password )
	{
		if( users.get( login ) != null && users.get( login ).equals( password ) )
		{
			return true;
		}
		else
		{
			return false;
		}
	}
}
