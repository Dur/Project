package pl.dur.opa.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.TitledBorder;
import pl.dur.opa.controllers.ClientController;
import pl.dur.opa.file.browser.ExtendedFileSystemView;
import pl.dur.opa.file.browser.ExtendedFileView;
import pl.dur.opa.file.browser.RemoteFileBrowser;
import pl.dur.opa.utils.ExtendedFileCellRenderer;
import pl.dur.opa.utils.ListTransferHandler;
import pl.dur.opa.utils.StateObserver;

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
	private JMenuItem locateOnSever = new JMenuItem( "locate on server" );
	private JMenuItem deleteItem = new JMenuItem( "delete" );
	private JProgressBar localProgressBar = new JProgressBar( 0, 100 );
	private JProgressBar remoteProgressBar = new JProgressBar( 0, 100 );
	private JFrame messagePopup;
	private JProgressBar popupProgress = new JProgressBar( 0, 100 );
	private JPanel messagePanel;
	private ExtendedFileSystemView extendedFileSystem;
	private JCheckBox withCRCCheckBox;
	private JFrame frame = new JFrame( "Opa Client" );
	public boolean wasInitalized = false;
	private RemoteFileBrowser browser;

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
		roots = new File[ newRoots.size() ];
		roots = (File[]) newRoots.toArray( roots );
		extendedFileSystem = new ExtendedFileSystemView( roots, roots[0], controller );

		messagePopup = new JFrame();
		messagePanel = new JPanel( new BorderLayout() );
		messagePanel.setBorder( BorderFactory.createEmptyBorder( 5, 5, 5, 5 ) );
		messagePanel.add( new JLabel(), BorderLayout.CENTER );
		messagePanel.add( popupProgress, BorderLayout.PAGE_END );
		popupProgress.setStringPainted( true );
		messagePopup.setDefaultCloseOperation( JFrame.HIDE_ON_CLOSE );
		messagePopup.add( messagePanel, BorderLayout.CENTER );
		messagePanel.setPreferredSize( new Dimension( 400, 100 ) );
		messagePopup.setLocationRelativeTo( null );
		messagePopup.pack();
		messagePopup.setAlwaysOnTop( true );

		localProgressBar.setStringPainted( true );
		localProgressBar.setString( "Ready" );
		localProgressBar.setVisible( false );
		remoteProgressBar.setStringPainted( true );
		remoteProgressBar.setString( "Ready" );
		remoteProgressBar.setIndeterminate( true );
		remoteProgressBar.setVisible( false );

		deleteItem.addActionListener( this );
		locateOnSever.addActionListener( this );
		refreshItem.addActionListener( this );
		refreshRemoteItem.addActionListener( this );
		JPopupMenu localPopup = new JPopupMenu( "localPopup" );
		JPopupMenu remotePopup = new JPopupMenu( "remotePopup" );
		localPopup.add( refreshItem );
		localPopup.add( locateOnSever );
		remotePopup.add( deleteItem );
		remotePopup.add( refreshRemoteItem );

		localFiles = new JFileChooser( extendedFileSystem );
		localFiles.setMultiSelectionEnabled( true );
		localFiles.setDragEnabled( true );
		localFiles.setControlButtonsAreShown( false );
		localFiles.setFileSelectionMode( JFileChooser.FILES_ONLY );
		localFiles.setFileView( new ExtendedFileView() );
		localFiles.setComponentPopupMenu( localPopup );

		browser = new RemoteFileBrowser( controller.getManipulator().getFileSystemView() );
		remoteFiles = new JFileChooser( browser );
		remoteFiles.setMultiSelectionEnabled( true );
		remoteFiles.setDragEnabled( true );
		remoteFiles.setControlButtonsAreShown( false );
		remoteFiles.setFileSelectionMode( JFileChooser.FILES_ONLY );
		remoteFiles.setComponentPopupMenu( remotePopup );

		JPanel localPanel = new JPanel( new BorderLayout() );
		localPanel.add( localFiles, BorderLayout.CENTER );
		JLabel clientSideLabel = new JLabel( "Client Side" );
		clientSideLabel.setFont( new Font( "sansserif", Font.BOLD, 32 ) );
		localPanel.add( clientSideLabel, BorderLayout.PAGE_START );
		withCRCCheckBox = new JCheckBox( "With CRC check" );
		withCRCCheckBox.addActionListener( this );
		localPanel.add( withCRCCheckBox, BorderLayout.PAGE_END );

		JPanel remotePanel = new JPanel( new BorderLayout() );
		remotePanel.add( remoteFiles, BorderLayout.CENTER );
		JLabel serverSideLabel = new JLabel( "Server Side" );
		serverSideLabel.setFont( new Font( "sansserif", Font.BOLD, 32 ) );
		remotePanel.add( serverSideLabel, BorderLayout.PAGE_START );

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
		rightProgressPanel.add( remoteProgressBar, BorderLayout.CENTER );

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
		localNavi.setDividerLocation( 400 );
		localNavi.setPreferredSize( new Dimension( 450, 600 ) );

		serverNavi = new JSplitPane( JSplitPane.VERTICAL_SPLIT,
				rightPanel, rightLowerPanel );
		serverNavi.setDividerLocation( 400 );
		serverNavi.setPreferredSize( new Dimension( 450, 600 ) );

		splitPane = new JSplitPane( JSplitPane.HORIZONTAL_SPLIT,
				localNavi, serverNavi );
		splitPane.setDividerLocation( 500 );
		splitPane.setPreferredSize( new Dimension( 1000, 600 ) );

		add( splitPane, BorderLayout.CENTER );
	}

	public void receiveAction()
	{
		List<File> filesToReceive = new ArrayList<File>( ((ListTransferHandler) rightDropZone.getTransferHandler()).
				getFiles() );
		File directory = localFiles.getCurrentDirectory();
		controller.receiveFile( directory, filesToReceive );
		((ListTransferHandler) rightDropZone.getTransferHandler()).clear();
	}

	public void sendAction()
	{
		List<File> filesToSend = new ArrayList<>( ((ListTransferHandler) leftDropZone.getTransferHandler()).getFiles() );
		File directory = remoteFiles.getCurrentDirectory();
		controller.sendFile( directory, filesToSend );
		((ListTransferHandler) leftDropZone.getTransferHandler()).clear();
	}

	/**
	 * Create the GUI and show it. For thread safety, this method should be invoked from the event-dispatching thread.
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
			UIManager.put( "ProgressBar.repaintInterval", new Integer( 100 ) );
			UIManager.put( "ProgressBar.cycleTime", new Integer( 2000 ) );
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}

		//Create and set up the window.
		frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
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
			return;
		}
		if( e.getSource() == receive )
		{
			receiveAction();
			return;
		}
		if( e.getSource() == refreshItem )
		{
			localFiles.rescanCurrentDirectory();
			frame.repaint();
		}
		if( e.getSource() == deleteItem )
		{
			File[] files = remoteFiles.getSelectedFiles();
			if( files != null )
			{
				controller.deleteFile( files );
			}
		}
		if( e.getSource() == refreshRemoteItem )
		{
			remoteFiles.rescanCurrentDirectory();
		}
		if( e.getSource() == withCRCCheckBox )
		{
			extendedFileSystem.setWithCRCCheck( withCRCCheckBox.isSelected() );
		}
		if( e.getSource() == locateOnSever )
		{
			if( localFiles.getSelectedFile() != null )
			{
				File file;
				if( ( file = controller.locateFileOnServer( localFiles.getSelectedFile() ) ) != null  )
				{
					remoteFiles.setCurrentDirectory( file );
				}
			}
		}
	}

	public JProgressBar getLocalProgressBar()
	{
		return localProgressBar;
	}

	public void notifyUser( String message )
	{
		JLabel label = (JLabel) messagePanel.getComponent( 0 );
		label.setVisible( true );
		label.setText( message );
		messagePopup.setVisible( true );
		popupProgress.setVisible( false );
	}

	public void setServerComputingState( String message, boolean isComputing )
	{
		remoteProgressBar.setString( message );
		remoteProgressBar.setVisible( isComputing );
		remoteProgressBar.setIndeterminate( true );
		send.setVisible( !isComputing );
	}

	public void serverProgressMessage( String message, int progress )
	{
		if( progress < 100 )
		{
			receive.setVisible( false );
			remoteProgressBar.setIndeterminate( false );
			remoteProgressBar.setVisible( true );
			remoteProgressBar.setValue( progress );
			remoteProgressBar.setString( message + " " + progress + "%" );
		}
		else
		{
			receive.setVisible( true );
			remoteProgressBar.setVisible( false );
		}
	}
	
	public void showProgressPopup(String message, int value, boolean indeterminateMode )
	{
		messagePopup.setVisible( true );
		this.popupProgress.setString( message );
		popupProgress.setIndeterminate( indeterminateMode );
		popupProgress.setValue( value );
		popupProgress.setVisible( true );
		JLabel label = (JLabel) messagePanel.getComponent( 0 );
		label.setText("Please wait...");
	}
	
	public void hideProgressPopup()
	{
		popupProgress.setVisible( false );
		messagePopup.setVisible( false );
	}
	
	public void hideView()
	{
		frame.setVisible( false );
	}
	
	public void setSendButtonVisible( boolean isVisible )
	{
		send.setVisible( isVisible );
	}
}
