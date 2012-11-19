package pl.dur.opa.utils;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.TransferHandler;

/**
 *
 * @author Dur
 */
public class ListTransferHandler extends TransferHandler
{
	private JList list;
	private List<File> files = new ArrayList<>();

	public ListTransferHandler( JList list )
	{
		this.list = list;
	}

	@Override
	public boolean canImport( TransferHandler.TransferSupport info )
	{
		// we only import ExtendedFileList
		if( !info.isDataFlavorSupported( DataFlavor.javaFileListFlavor ) )
		{
			return false;
		}
		return true;
	}

	@Override
	public boolean importData( TransferHandler.TransferSupport info )
	{
		if( !info.isDrop() )
		{
			return false;
		}

		// Check for ExtendedFileList flavor
		if( !info.isDataFlavorSupported( DataFlavor.javaFileListFlavor ) )
		{
			return false;
		}

		// Get the fileList that is being dropped.
		Transferable t = info.getTransferable();
		List<File> data;
		try
		{
			data = (List<File>) t.getTransferData( DataFlavor.javaFileListFlavor );
			List<File> cantTransfer = new ArrayList<>();
			for( File file : data )
			{
				if( file.isDirectory() )
				{
					cantTransfer.add( file );
				}
			}
			data.removeAll( cantTransfer );
		}
		catch( Exception e )
		{
			e.printStackTrace();
			return false;
		}
		DefaultListModel model = (DefaultListModel) list.getModel();
		for( Object file : data )
		{
			model.addElement( (File) file );
			files.add((File) file);
		}
		return true;
	}

	public List<File> getFiles()
	{
		return files;
	}
	
	public void clear()
	{
		DefaultListModel model = (DefaultListModel) list.getModel();
		model.clear();
		files.clear();
	}
}
