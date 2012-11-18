/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.dur.opa.file.browser;

import com.google.common.io.Files;
import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import javax.swing.filechooser.FileSystemView;
import pl.dur.opa.controllers.ClientController;
import pl.dur.opa.utils.ExtendedFile;

/**
 * Class implements RemoteFileSystemView and make it available for clients to view server side file system. It will
 * present server file system via RMI call insted of clients local file system.
 *
 * @author Dur
 */
public class ExtendedFileSystemView extends FileSystemView
{
	private File[] roots;
	private File homeDirectory;
	private ClientController controller;
	private boolean withCRCCheck = false;
	
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

			newFolder = createFileObject( containingDir, newFolderString );

			int i = 1;

			while( newFolder.exists() && (i < 100) )
			{
				newFolder = createFileObject( containingDir,
						MessageFormat.format( newFolderString, new Object[]
						{
							new Integer( i )
						} ) );
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
			return new ExtendedFile( newFolder.getPath() );
		}
	};

	public ExtendedFileSystemView( final File[] newRoots, final File newHomeDirectory,
			ClientController controller ) 
	{
		this.roots = newRoots;
		this.controller = controller;
		this.homeDirectory = newHomeDirectory;
	}

	@Override
	public final File[] getRoots()
	{
		ExtendedFile[] files = new ExtendedFile[ roots.length ];
		int i = 0;
		for( File file : roots )
		{
			files[i] = new ExtendedFile( file.getPath() );
			i++;
		}
		return files;
	}

	@Override
	public final File getHomeDirectory()
	{
		return new ExtendedFile( homeDirectory.getPath() );
	}

	@Override
	public final File getParentDirectory( final File dir )
	{
		for( File root : roots )
		{
			if( root.equals( dir ) )
			{
				return new ExtendedFile( dir.getPath() );
			}
		}
		return new ExtendedFile( localSystem.getParentDirectory( dir ).getPath() );
	}

	@Override
	public final boolean isRoot( final File file )
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
	public final File[] getFiles( final File dir, final boolean useFileHiding )
	{
		ExtendedFile[] files = new ExtendedFile[ localSystem.getFiles( dir, useFileHiding ).length ];
		int i = 0;
		ArrayList<ExtendedFile> filesToCheck = new ArrayList<>();
		for( File file : localSystem.getFiles( dir, useFileHiding ) )
		{
			files[i] = new ExtendedFile( file.getPath() );
			if( files[i].isFile() )
			{
				if( withCRCCheck )
				{
					controller.notifyViewOfProgress( "Calculating CRC for:   " + file.getName(), 0, true );
					try
					{
						if( files[i].isFile() )
						{
							files[i].setFileCheckSum( Files.getChecksum( files[i], new java.util.zip.CRC32() ) );
						}
					}
					catch( IOException ex )
					{
						ex.printStackTrace();
						controller.hideProgressPopup();
					}
				}
				filesToCheck.add( files[i] );
			}
			i++;
		}
		controller.hideProgressPopup();
		return controller.areFilesVersioned( files );
	}

	@Override
	public final File getDefaultDirectory()
	{
		return new ExtendedFile( homeDirectory.getPath() );
	}

	@Override
	public final File getChild( final File parent, final String fileName )
	{
		return new ExtendedFile( localSystem.getChild( parent, fileName ).
				getPath() );
	}

	@Override
	public final File createNewFolder( final File containingDir )
	{
		try
		{
			return new ExtendedFile( localSystem.createNewFolder( containingDir ).
					getPath() );
		}
		catch( IOException ex )
		{
			return null;
		}
	}

	@Override
	public final File createFileObject( final String path )
	{
		return new ExtendedFile( localSystem.createFileObject( path ).getPath() );
	}

	@Override
	public final File createFileObject( final File dir, final String filename )
	{
		return new ExtendedFile( localSystem.createFileObject( dir, filename ).
				getPath() );
	}

	public void setWithCRCCheck( boolean withCRC )
	{
		this.withCRCCheck = withCRC;
	}
}
