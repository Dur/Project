package pl.dur.opa.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.PrintStream;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.TitledBorder;
import pl.dur.opa.controllers.ClientController;
import pl.dur.opa.file.browser.ExtendedFileSystemView;
import pl.dur.opa.file.browser.RemoteFileBrowser;
import pl.dur.opa.utils.ExtendedFile;
import pl.dur.opa.utils.ExtendedFileCellRenderer;
import pl.dur.opa.utils.ListTransferHandler;

public class View extends JPanel implements ActionListener
{
	private static final long serialVersionUID = -4487732343062917781L;
	private JFileChooser fc;
	private JButton clear;
	private JList dropZone;
	private DefaultListModel listModel;
	private JSplitPane splitPane;
	private PrintStream ps;
	private ClientController controller;

	public View( ClientController controller )
	{
		super( new BorderLayout() );
		this.controller = controller;
	}
	
	private void initComponents() throws RemoteException
	{
		File[] roots = new File[ 2 ];
		roots[0] = new ExtendedFile( "C:\\");
		roots[1] = new ExtendedFile("D:\\");
		//fc = new JFileChooser( new ExtendedFileSystemView( roots, roots[0] ) );
		fc = new JFileChooser( new RemoteFileBrowser( controller.getManipulator().getFileSystemView() ) );
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
		dropZone.setBorder( new TitledBorder( "Files to send" ) );
		leftLowerPanel.setViewportView( new JScrollPane( dropZone ) );

		splitPane = new JSplitPane( JSplitPane.VERTICAL_SPLIT,
				leftUpperPanel, leftLowerPanel );
		splitPane.setDividerLocation( 400 );
		splitPane.setPreferredSize( new Dimension( 480, 650 ) );

		add( splitPane, BorderLayout.CENTER );
	}

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
	public void createAndShowGUI()
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
		JFrame frame = new JFrame( "Opa Client" );
		frame.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
		try
		{
			//Create and set up the menu bar and content pane.
			initComponents();
		}
		catch( RemoteException ex )
		{
			ex.printStackTrace();
		}
		this.setOpaque( true ); //content panes must be opaque
		frame.setContentPane( this );

		//Display the window.
		frame.pack();
		frame.setVisible( true );
	}
}