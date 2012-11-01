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
 * Class which reads configuration file and updates server dependencies. Configuration file can be updated befor server start. To reconfigure the server by this file
 * server must be restarted.
 *
 * @author Dur
 */
public class Configuration
{
	private boolean isConfFile;
	private HashMap<String, String> params = new HashMap<>();

	public Configuration()
	{
		reconfigureServer();
	}

	public final void reconfigureServer()
	{
		System.out.println( "configure" );
		File configurationFile = new File( "conf//server.conf" );
		if( !configurationFile.canRead() )
		{
			System.out.println( "no file" );
			isConfFile = false;
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
					params.put( key, value );
					System.out.println( key + " = " + value );
				}
			}
		}
		catch( FileNotFoundException ex )
		{
			isConfFile = false;
			return;
		}
		catch( IOException iex )
		{
			iex.printStackTrace();
			isConfFile = false;
			return;
		}
	}
}
