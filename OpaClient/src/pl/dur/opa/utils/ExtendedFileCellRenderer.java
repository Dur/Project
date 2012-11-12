package pl.dur.opa.utils;

import java.awt.Component;
import java.io.File;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.filechooser.FileSystemView;

/**
 *
 * @author Dur
 */
public class ExtendedFileCellRenderer extends DefaultListCellRenderer
{
	public Component getListCellRendererComponent( JList list,
			Object value,
			int index,
			boolean isSelected,
			boolean cellHasFocus )
	{

		Component c = super.getListCellRendererComponent(
				list, value, index, isSelected, cellHasFocus );

		if( c instanceof JLabel && value instanceof File )
		{
			JLabel l = (JLabel) c;
			File f = (File) value;
			l.setIcon( FileSystemView.getFileSystemView().getSystemIcon( f ));
			ImageIcon icon = new ImageIcon( "icons//fileOk.jpg" );
			if( f.isFile() && f instanceof ExtendedFile )
			{
				ExtendedFile file = (ExtendedFile)f;
				if( file.isIsStored() )
				{
					l.setIcon( icon );
				}
			}
			l.setText( f.getName() );
			l.setToolTipText( f.getAbsolutePath() );
		}
		return c;
	}
}
