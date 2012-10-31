/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.dur.opa.file.browser;

import java.io.File;
import java.rmi.RemoteException;
import javax.swing.filechooser.FileSystemView;
import pl.dur.opa.remote.interfaces.RemoteFileSystemView;

/**
 * Class extends FileSystemView. Makes user availlable to see remote system file system.
 * Also can be passsed to JFileChooser.
 * @author Dur
 */
public class RemoteFileBrowser extends FileSystemView
{
	private RemoteFileSystemView remoteFileSystem;

	public RemoteFileBrowser( RemoteFileSystemView newRemoteFileSystem )
	{
		super();
		this.remoteFileSystem = newRemoteFileSystem;
	}

	@Override
	public final File createFileObject( final File dir, final String filename )
	{
		File file;
		try
		{
			file = remoteFileSystem.createRemoteFileObject( dir, filename );
		}
		catch( RemoteException ex )
		{
			file = (File) null;
		}
		return file;
	}

	@Override
	public final File createFileObject( final String path )
	{
		File file;
		try
		{
			file = remoteFileSystem.createRemoteFileObject( path );
		}
		catch( RemoteException ex )
		{
			file = (File) null;
		}
		return file;
	}

	@Override
	public final File createNewFolder( final File containingDir )
	{
		File file;
		try
		{
			file = remoteFileSystem.createRemoteNewFolder( containingDir );
		}
		catch( RemoteException ex )
		{
			file = (File) null;
		}
		return file;
	}

	@Override
	public final File getChild( final File parent, final String fileName )
	{
		File file;
		try
		{
			file = remoteFileSystem.getChild( parent, fileName );
		}
		catch( RemoteException ex )
		{
			file = (File) null;
		}
		return file;
	}

	@Override
	public final File getDefaultDirectory()
	{
		File file;
		try
		{
			file = remoteFileSystem.getDefaultRemoteDirectory();
		}
		catch( RemoteException ex )
		{
			file = (File) null;
		}
		return file;
	}

	@Override
	public final File[] getFiles( final File dir, final boolean useFileHiding )
	{
		File[] file;
		try
		{
			file = remoteFileSystem.getRemoteFiles( dir, useFileHiding );
		}
		catch( RemoteException ex )
		{
			file = (File[]) null;
		}
		return file;
	}

	@Override
	public final File getHomeDirectory()
	{
		File file;
		try
		{
			file = remoteFileSystem.getRemoteHomeDirectory();
		}
		catch( RemoteException ex )
		{
			file = (File) null;
		}
		return file;
	}

	@Override
	public final File getParentDirectory( final File dir )
	{
		File file;
		try
		{
			file = remoteFileSystem.getRemoteParentDirectory( dir );
		}
		catch( RemoteException ex )
		{
			file = (File) null;
		}
		return file;
	}

	@Override
	public final File[] getRoots()
	{
		File[] file;
		try
		{
			file = remoteFileSystem.getRemoteRoots();
		}
		catch( RemoteException ex )
		{
			file = (File[]) null;
		}
		return file;
	}

	@Override
	public final boolean isRoot( final File file )
	{
		boolean isRoot;
		try
		{
			isRoot = remoteFileSystem.isRoot( file );
		}
		catch( RemoteException ex )
		{
			isRoot = false;
		}
		return isRoot;
	}
}
