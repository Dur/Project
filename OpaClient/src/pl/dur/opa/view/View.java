package pl.dur.opa.view;

import java.awt.datatransfer.*;
import java.awt.event.*;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.io.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileSystemView;
import java.util.List;
import pl.dur.opa.file.browser.ExtendedFileSystemView;
import pl.dur.opa.utils.ExtendedFile;

public class View extends JPanel implements ActionListener
{
	private static final long serialVersionUID = -4487732343062917781L;
	JFileChooser fc;
	JButton clear;
	JList dropZone;
	DefaultListModel listModel;
	JSplitPane splitPane;
	PrintStream ps;

	public View()
	{
		super( new BorderLayout() );
		File[] roots = new File[ 1 ];
		roots[0] = new ExtendedFile( "C:\\");
		fc = new JFileChooser( new ExtendedFileSystemView( roots, new File("C:\\") ) );
		fc.setMultiSelectionEnabled( true );
		fc.setDragEnabled( true );
		fc.setControlButtonsAreShown( false );
		fc.setFileSelectionMode( JFileChooser.FILES_ONLY );

		JPanel fcPanel = new JPanel( new BorderLayout() );
		fcPanel.add( fc, BorderLayout.CENTER );

		clear = new JButton( "Clear" );
		clear.addActionListener( this );
		JPanel buttonPanel = new JPanel( new BorderLayout() );
		buttonPanel.setBorder( BorderFactory.createEmptyBorder( 5, 5, 5, 5 ) );
		buttonPanel.add( clear, BorderLayout.LINE_END );

		JPanel leftUpperPanel = new JPanel( new BorderLayout() );
		leftUpperPanel.setBorder( BorderFactory.createEmptyBorder( 5, 5, 5, 5 ) );
		leftUpperPanel.add( fcPanel, BorderLayout.CENTER );
		leftUpperPanel.add( buttonPanel, BorderLayout.PAGE_END );

		JScrollPane leftLowerPanel = new javax.swing.JScrollPane();
		leftLowerPanel.setBorder( BorderFactory.createEmptyBorder( 5, 5, 5, 5 ) );

		listModel = new DefaultListModel();
		dropZone = new JList( listModel );
		dropZone.setCellRenderer( new ExtendedFileCellRenderer() );
		dropZone.setTransferHandler( new ListTransferHandler( dropZone ) );
		dropZone.setDragEnabled( true );
		dropZone.setDropMode( javax.swing.DropMode.INSERT );
		dropZone.setBorder( new TitledBorder( "ExtendedFiles to send" ) );
		leftLowerPanel.setViewportView( new JScrollPane( dropZone ) );

		splitPane = new JSplitPane( JSplitPane.VERTICAL_SPLIT,
				leftUpperPanel, leftLowerPanel );
		splitPane.setDividerLocation( 400 );
		splitPane.setPreferredSize( new Dimension( 480, 650 ) );

		add( splitPane, BorderLayout.CENTER );

	}

//	public void setDefaultButton()
//	{
//		getRootPane().setDefaultButton( clear );
//	}
	public void actionPerformed( ActionEvent e )
	{
		if( e.getSource() == clear )
		{
			listModel.clear();
		}
	}

	/**
	 * Create the GUI and show it. For thread safety, this method should be
	 * invoked from the event-dispatching thread.
	 */
	private static void createAndShowGUI()
	{
		//Make sure we have nice window decorations.
		JFrame.setDefaultLookAndFeelDecorated( true );
		try
		{
			for( LookAndFeelInfo info : UIManager.getInstalledLookAndFeels() )
			{
				if( "Nimbus".equals( info.getName() ) )
				{
					UIManager.setLookAndFeel( info.getClassName() );
					break;
				}
			}
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}

		//Create and set up the window.
		JFrame frame = new JFrame( "Consolidator!" );
		frame.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );

		//Create and set up the menu bar and content pane.
		View demo = new View();
		demo.setOpaque( true ); //content panes must be opaque
		frame.setContentPane( demo );

		//Display the window.
		frame.pack();
		frame.setVisible( true );
		//demo.setDefaultButton();
	}

	public static void main( String[] args )
	{
		//Schedule a job for the event-dispatching thread:
		//creating and showing this application's GUI.
		javax.swing.SwingUtilities.invokeLater( new Runnable()
		{
			public void run()
			{
				createAndShowGUI();
			}
		} );
	}
}

class ExtendedFileCellRenderer extends DefaultListCellRenderer
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
			ImageIcon icon = new ImageIcon( "icons//fileOk.jpg" ) {};
			if( f.isFile() )
			{
				l.setIcon( icon );
			}
			else
			{
				l.setIcon( FileSystemView.getFileSystemView().getSystemIcon( f ) );
			}
			l.setText( f.getName() );
			l.setToolTipText( f.getAbsolutePath() );
		}
		return c;
	}
}

class ListTransferHandler extends TransferHandler
{
	private JList list;

	ListTransferHandler( JList list )
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
			displayDropLocation( "List doesn't accept a drop of this type." );
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
		}
		return true;
	}

	private void displayDropLocation( String string )
	{
		System.out.println( string );
	}
}