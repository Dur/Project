/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.dur.opa.remote.impl;

import java.io.File;
import java.io.IOException;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.text.MessageFormat;
import javax.swing.filechooser.FileSystemView;
import pl.dur.opa.remote.interfaces.RemoteFileSystemView;
import pl.dur.opa.remote.interfaces.RemoteFileSystemView;

/**
 * Class implements RemoteFileSystemView and make it available for clients to view server side file system. 
 * It will present server file system via RMI call insted of
 * clients local file system.
 *
 * @author Dur
 */
public class RemoteFileSystemViewImpl extends UnicastRemoteObject implements RemoteFileSystemView
{
	private File[] roots;
	private File homeDirectory;
	
	FileSystemView localSystem = new FileSystemView()
	{
		@Override
		public File createNewFolder( File containingDir ) throws IOException
		{
			String newFolderString = "Nowy";
			if( containingDir == null )
			{
				throw new IOException( "Containing directory is null:" );
			}
			File newFolder = null;

			// Unix - using OpenWindows' default folder name. Can't find one for Motif/CDE.

			newFolder = createFileObject( containingDir, newFolderString );

			int i = 1;

			while( newFolder.exists() && (i < 100) )
			{
				newFolder = createFileObject( containingDir, 
						MessageFormat.format( newFolderString, new Object[]
						{
							new Integer( i )
						} ) 
						);
				i++;
			}
			if( newFolder.exists() )
			{
				throw new IOException( "Directory already exists:" + newFolder.getAbsolutePath() );
			}
			else
			{
				newFolder.mkdirs();
			}
			return newFolder;
		}
	};

	public RemoteFileSystemViewImpl( final File[] newRoots, final File newHomeDirectory ) throws RemoteException
	{
		this.roots = newRoots;
		this.homeDirectory = newHomeDirectory;
	}

	@Override
	public final File[] getRemoteRoots() throws RemoteException
	{
		return roots;
	}

	@Override
	public final File getRemoteHomeDirectory() throws RemoteException
	{
		return homeDirectory;
	}

	@Override
	public final File getRemoteParentDirectory( final File dir ) throws RemoteException
	{
		for( File root : roots )
		{
			if( root.equals( dir ) )
			{
				return dir;
			}
		}
		return localSystem.getParentDirectory( dir );
	}

	@Override
	public final boolean isRoot( final File file ) throws RemoteException
	{
		for( File root : roots )
		{
			if( root.equals( file ) )
			{
				return true;
			}
		}
		return false;
	}

	@Override
	public final File[] getRemoteFiles( final File dir, final boolean useFileHiding ) throws RemoteException
	{
		return localSystem.getFiles( dir, useFileHiding );
	}

	@Override
	public final File getDefaultRemoteDirectory() throws RemoteException
	{
		return homeDirectory;
	}

	@Override
	public final File getChild( final File parent, final String fileName ) throws RemoteException
	{
		return localSystem.getChild( parent, fileName );
	}

	@Override
	public final File createRemoteNewFolder( final File containingDir ) throws RemoteException
	{
		try
		{
			return localSystem.createNewFolder( containingDir );
		}
		catch( IOException ex )
		{
			return null;
		}
	}

	@Override
	public final File createRemoteFileObject( final String path ) throws RemoteException
	{
		return localSystem.createFileObject( path );
	}

	@Override
	public final File createRemoteFileObject( final File dir, final String filename ) throws RemoteException
	{
		return localSystem.createFileObject( dir, filename );
	}
}
