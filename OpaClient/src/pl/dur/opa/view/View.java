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
	private JMenuItem refreshItem = new JMenuItem( "refresh" );
	private JMenuItem refreshRemoteItem = new JMenuItem( "refresh" );
	private JMenuItem deleteItem = new JMenuItem( "delete" );
	private JProgressBar localProgressBar = new JProgressBar( 0, 100);
	private JProgressBar remoteProgressBar = new JProgressBar( 0, 100);

	public View( ClientController controller )
	{
		super( new BorderLayout() );
		this.controller = controller;
	}

	private void initComponents() throws RemoteException
	{
		localProgressBar.setStringPainted( true );
		localProgressBar.setString( "Ready");
		remoteProgressBar.setStringPainted( true );
		remoteProgressBar.setString( "Ready");
		File[] roots = File.listRoots();
		ArrayList<File> newRoots = new ArrayList<>();
		for( File root : roots )
		{
			if( root.isDirectory() )
			{
				newRoots.add( root );
			}
		}
		deleteItem.addActionListener( this );
		refreshItem.addActionListener( this );
		refreshRemoteItem.addActionListener( this );
		JPopupMenu localPopup = new JPopupMenu( "localPopup" );
		JPopupMenu remotePopup = new JPopupMenu( "remotePopup" );
		localPopup.add( refreshItem );
		remotePopup.add( deleteItem );
		remotePopup.add( refreshRemoteItem );

		roots = new File[ newRoots.size() ];
		roots = (File[]) newRoots.toArray( roots );
		localFiles = new JFileChooser( new ExtendedFileSystemView( roots, roots[0], controller ) );
		localFiles.setMultiSelectionEnabled( true );
		localFiles.setDragEnabled( true );
		localFiles.setControlButtonsAreShown( false );
		localFiles.setFileSelectionMode( JFileChooser.FILES_ONLY );
		localFiles.setFileView( new ExtendedFileView() );
		localFiles.setComponentPopupMenu( localPopup );


		remoteFiles = new JFileChooser( new RemoteFileBrowser( controller.
				getManipulator().getFileSystemView() ) );
		remoteFiles.setMultiSelectionEnabled( true );
		remoteFiles.setDragEnabled( true );
		remoteFiles.setControlButtonsAreShown( false );
		remoteFiles.setFileSelectionMode( JFileChooser.FILES_ONLY );
		remoteFiles.setComponentPopupMenu( remotePopup );

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

		JScrollPane leftLowerScrollPanel = new javax.swing.JScrollPane();
		leftLowerScrollPanel.setBorder( BorderFactory.createEmptyBorder( 5, 5, 5, 5 ) );
		leftListModel = new DefaultListModel();
		leftDropZone = new JList( leftListModel );
		leftDropZone.setCellRenderer( new ExtendedFileCellRenderer() );
		leftDropZone.setTransferHandler( new ListTransferHandler( leftDropZone ) );
		leftDropZone.setDragEnabled( true );
		leftDropZone.setDropMode( javax.swing.DropMode.INSERT );
		leftDropZone.setBorder( new TitledBorder( "Files to send" ) );
		leftLowerScrollPanel.setViewportView( new JScrollPane( leftDropZone ) );

		JPanel leftProgressPanel = new JPanel( new BorderLayout() );
		leftProgressPanel.setBorder( BorderFactory.createEmptyBorder( 5, 5, 5, 5 ) );
		leftProgressPanel.add( localProgressBar, BorderLayout.CENTER );

		JPanel rightProgressPanel = new JPanel( new BorderLayout() );
		rightProgressPanel.setBorder( BorderFactory.createEmptyBorder( 5, 5, 5, 5 ) );
		rightProgressPanel.add(remoteProgressBar , BorderLayout.CENTER );

		JPanel leftLowerPanel = new JPanel( new BorderLayout() );
		leftLowerPanel.setBorder( BorderFactory.createEmptyBorder( 5, 5, 5, 5 ) );
		leftLowerPanel.add( leftLowerScrollPanel, BorderLayout.CENTER );
		leftLowerPanel.add( leftProgressPanel, BorderLayout.PAGE_END );

		JScrollPane rightLoverScrollPanel = new javax.swing.JScrollPane();
		rightLoverScrollPanel.setBorder( BorderFactory.createEmptyBorder( 5, 5, 5, 5 ) );
		rightListModel = new DefaultListModel();
		rightDropZone = new JList( rightListModel );
		rightDropZone.setCellRenderer( new ExtendedFileCellRenderer() );
		rightDropZone.setTransferHandler( new ListTransferHandler( rightDropZone ) );
		rightDropZone.setDragEnabled( true );
		rightDropZone.setDropMode( javax.swing.DropMode.INSERT );
		rightDropZone.setBorder( new TitledBorder( "Files to download" ) );
		rightLoverScrollPanel.setViewportView( new JScrollPane( rightDropZone ) );

		JPanel rightLowerPanel = new JPanel( new BorderLayout() );
		rightLowerPanel.setBorder( BorderFactory.createEmptyBorder( 5, 5, 5, 5 ) );
		rightLowerPanel.add( rightLoverScrollPanel, BorderLayout.CENTER );
		rightLowerPanel.add( rightProgressPanel, BorderLayout.PAGE_END );

		localNavi = new JSplitPane( JSplitPane.VERTICAL_SPLIT,
				leftPanel, leftLowerPanel );
		localNavi.setDividerLocation( 300 );
		localNavi.setPreferredSize( new Dimension( 450, 500 ) );

		serverNavi = new JSplitPane( JSplitPane.VERTICAL_SPLIT,
				rightPanel, rightLowerPanel );
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
		System.out.println( "Receive Action" );
		List<File> filesToReceive = ((ListTransferHandler) rightDropZone.
				getTransferHandler()).getFiles();
		File fileToReceive = filesToReceive.get( 0 );
		System.out.println( fileToReceive.length() );
		File directory = localFiles.getCurrentDirectory();
		controller.receiveFile( directory, fileToReceive );
		((ListTransferHandler) rightDropZone.getTransferHandler()).clear();
	}

	public void sendAction()
	{
		System.out.println( "send action" );
		List<File> filesToSend = ((ListTransferHandler) leftDropZone.
				getTransferHandler()).getFiles();
		File fileToSend = filesToSend.get( 0 );
		File directory = remoteFiles.getCurrentDirectory();
		controller.sendFile( directory, fileToSend );
		((ListTransferHandler) leftDropZone.getTransferHandler()).clear();
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
			sendAction();
		}
		if( e.getSource() == receive )
		{
			receiveAction();
		}
		if( e.getSource() == refreshItem )
		{
		}
		if( e.getSource() == deleteItem )
		{

			File file = remoteFiles.getSelectedFile();
			if( file != null )
			{
				System.out.println( "selected file is " + file.getName() );
				controller.deleteFile( file );
			}

		}
		if( e.getSource() == refreshRemoteItem )
		{
			remoteFiles.rescanCurrentDirectory();
		}
	}
	
	public JProgressBar getLocalProgressBar()
	{
		return localProgressBar;
	}
}
