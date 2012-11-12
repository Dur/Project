package pl.dur.opa.file.browser;

import java.io.File;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.filechooser.FileView;
import pl.dur.opa.utils.ExtendedFile;


public class ExtendedFileView extends FileView
{
	private ImageIcon versioned = new ImageIcon( "icons//fileOk.jpg" );

	@Override
	public Icon getIcon( File f )
	{
		Icon icon = super.getIcon( f );
		if( f.isFile() && f instanceof ExtendedFile )
		{
			ExtendedFile file = (ExtendedFile)f;
			if( file.isIsStored() )
			{
				icon = versioned;
			}
		}
		return icon;
	}
}