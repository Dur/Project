/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.dur.opa.file.browser;

import java.io.File;
import javax.swing.JFileChooser;

/**
 *
 * @author Dur
 */
public class ExtendedFileChooser extends JFileChooser
{
	private RemoteFileBrowser remoteFileBrowser;

	public ExtendedFileChooser( RemoteFileBrowser fsv )
	{
		super( fsv );
		this.remoteFileBrowser = fsv;
	}

	@Override
	public void setCurrentDirectory( File dir )
	{
		File oldValue = super.getCurrentDirectory();

		if( dir != null )
		{
			dir = super.getCurrentDirectory();
		}
		if( dir == null )
		{
			dir = getFileSystemView().getDefaultDirectory();
		}
		if( super.getCurrentDirectory() != null )
		{
			/*
			 * Verify the toString of object
			 */
			if( super.getCurrentDirectory().equals( dir ) )
			{
				return;
			}
		}

		File prev = null;
		while( !isTraversable( dir ) && prev != dir )
		{
			prev = dir;
			dir = getFileSystemView().getParentDirectory( dir );
		}
		super.setCurrentDirectory( dir );

		firePropertyChange( DIRECTORY_CHANGED_PROPERTY, oldValue, super.getCurrentDirectory() );
	}
}
