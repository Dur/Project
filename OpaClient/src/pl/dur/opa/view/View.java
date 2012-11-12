package pl.dur.opa.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.TitledBorder;
import pl.dur.opa.controllers.ClientController;
import pl.dur.opa.file.browser.ExtendedFileSystemView;
import pl.dur.opa.file.browser.ExtendedFileView;
import pl.dur.opa.file.browser.RemoteFileBrowser;
import pl.dur.opa.utils.ExtendedFileCellRenderer;
import pl.dur.opa.utils.ListTransferHandler;

public class View extends JPanel implements ActionListener
{
	private static final long serialVersionUID = -4487732343062917781L;
	private JFileChooser remoteFiles;
	private JFileChooser localFiles;
	
	private JButton send;
	private JButton receive;
	private JList rightDropZone;
	private JList leftDropZone;
	private DefaultListModel rightListModel;
	private DefaultListModel leftListModel;
	private JSplitPane splitPane;
	private JSplitPane serverNavi;
	private JSplitPane localNavi;
	private ClientController controller;

	public View( ClientController controller )
	{
		super( new BorderLayout() );
		this.controller = controller;
	}
	
	private void initComponents() throws RemoteException
	{
		File[] roots = File.listRoots();
		ArrayList<File> newRoots = new ArrayList<>();
		for( File root : roots )
		{
			if( root.isDirectory() )
			{
				newRoots.add( root );
			}
		}
		roots = new File[newRoots.size()];
		roots = (File[]) newRoots.toArray( roots );
		localFiles = new JFileChooser( new ExtendedFileSystemView( roots, roots[0], controller ) );
		localFiles.setMultiSelectionEnabled( true );
		localFiles.setDragEnabled( true );
		localFiles.setControlButtonsAreShown( false );
		localFiles.setFileSelectionMode( JFileChooser.FILES_ONLY );
		localFiles.setFileView( new ExtendedFileView() );
		
		remoteFiles = new JFileChooser( new RemoteFileBrowser( controller.getManipulator().getFileSystemView() ) );
		remoteFiles.setMultiSelectionEnabled( true );
		remoteFiles.setDragEnabled( true );
		remoteFiles.setControlButtonsAreShown( false );
		remoteFiles.setFileSelectionMode( JFileChooser.FILES_ONLY );

		JPanel localPanel = new JPanel( new BorderLayout() );
		localPanel.add( localFiles, BorderLayout.CENTER );
		
		JPanel remotePanel = new JPanel( new BorderLayout() );
		remotePanel.add( remoteFiles, BorderLayout.CENTER );

		send = new JButton( "Send" );
		send.addActionListener( this );
		JPanel leftButtonPanel = new JPanel( new BorderLayout() );
		leftButtonPanel.setBorder( BorderFactory.createEmptyBorder( 5, 5, 5, 5 ) );
		leftButtonPanel.add( send, BorderLayout.LINE_END );

		JPanel leftPanel = new JPanel( new BorderLayout() );
		leftPanel.setBorder( BorderFactory.createEmptyBorder( 5, 5, 5, 5 ) );
		leftPanel.add( localPanel, BorderLayout.CENTER );
		leftPanel.add( leftButtonPanel, BorderLayout.PAGE_END );
		
		receive = new JButton( "Receive" );
		receive.addActionListener( this );
		JPanel rightButtonPanel = new JPanel( new BorderLayout() );
		rightButtonPanel.setBorder( BorderFactory.createEmptyBorder( 5, 5, 5, 5 ) );
		rightButtonPanel.add( receive, BorderLayout.LINE_END );
		
		JPanel rightPanel = new JPanel( new BorderLayout() );
		rightPanel.setBorder( BorderFactory.createEmptyBorder( 5, 5, 5, 5 ) );
		rightPanel.add( remotePanel, BorderLayout.CENTER );
		rightPanel.add( rightButtonPanel, BorderLayout.PAGE_END );

		JScrollPane leftLowerPanel = new javax.swing.JScrollPane();
		leftLowerPanel.setBorder( BorderFactory.createEmptyBorder( 5, 5, 5, 5 ) );
		leftListModel = new DefaultListModel();
		leftDropZone = new JList( leftListModel );
		leftDropZone.setCellRenderer( new ExtendedFileCellRenderer() );
		leftDropZone.setTransferHandler( new ListTransferHandler( leftDropZone ) );
		leftDropZone.setDragEnabled( true );
		leftDropZone.setDropMode( javax.swing.DropMode.INSERT );
		leftDropZone.setBorder( new TitledBorder( "Files to send" ) );
		leftLowerPanel.setViewportView( new JScrollPane( leftDropZone ) );
		
		JScrollPane rightLoverPanel = new javax.swing.JScrollPane();
		rightLoverPanel.setBorder( BorderFactory.createEmptyBorder( 5, 5, 5, 5 ) );
		rightListModel = new DefaultListModel();
		rightDropZone = new JList( rightListModel );
		rightDropZone.setCellRenderer( new ExtendedFileCellRenderer() );
		rightDropZone.setTransferHandler( new ListTransferHandler( rightDropZone ) );
		rightDropZone.setDragEnabled( true );
		rightDropZone.setDropMode( javax.swing.DropMode.INSERT );
		rightDropZone.setBorder( new TitledBorder( "Files to download" ) );
		rightLoverPanel.setViewportView( new JScrollPane( rightDropZone ) );

		localNavi = new JSplitPane( JSplitPane.VERTICAL_SPLIT,
				leftPanel, leftLowerPanel );
		localNavi.setDividerLocation( 300 );
		localNavi.setPreferredSize( new Dimension( 450, 500 ) );
		
		serverNavi = new JSplitPane( JSplitPane.VERTICAL_SPLIT,
				rightPanel, rightLoverPanel );
		serverNavi.setDividerLocation( 300 );
		serverNavi.setPreferredSize( new Dimension( 450, 500 ) );
		
		splitPane = new JSplitPane( JSplitPane.HORIZONTAL_SPLIT,
				localNavi, serverNavi );
		splitPane.setDividerLocation( 500 );
		splitPane.setPreferredSize( new Dimension( 1000, 500 ) );

		add( splitPane, BorderLayout.CENTER );
	}

	public void receiveAction()
	{
		System.out.println("Receive Action");
		List<File> filesToReceive = ((ListTransferHandler)rightDropZone.getTransferHandler()).getFiles();
		File fileToReceive = filesToReceive.get( 0 );
		File directory = localFiles.getCurrentDirectory();
		controller.receiveFile( directory, fileToReceive );
		((ListTransferHandler)rightDropZone.getTransferHandler()).clear();
	}
	
	public void sendAction( )
	{
		System.out.println("send action");
		List<File> filesToSend = ((ListTransferHandler)leftDropZone.getTransferHandler()).getFiles();
		File fileToSend = filesToSend.get( 0 );
		File directory = remoteFiles.getCurrentDirectory();
		controller.sendFile( directory, fileToSend );
		((ListTransferHandler)leftDropZone.getTransferHandler()).clear();
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

	@Override
	public void actionPerformed( ActionEvent e )
	{
		if( e.getSource() == send )
		{
			sendAction( );
		}
		if( e.getSource() == receive )
		{
			receiveAction();
		}
	}
}