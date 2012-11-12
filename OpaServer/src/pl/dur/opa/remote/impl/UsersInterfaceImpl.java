/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.dur.opa.remote.impl;

import java.io.File;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import pl.dur.opa.connection.ConnectionAdministrator;
import pl.dur.opa.file.browser.LocalFileAdministrator;
import pl.dur.opa.remote.interfaces.RemoteFileSystemView;
import pl.dur.opa.remote.interfaces.UsersInterface;
import pl.dur.opa.utils.ExtendedFile;

/**
 * Class implements file manipulator interface. Methods in this class are
 * calling new threads to serve clients calls.
 *
 * @author Dur
 */
public class UsersInterfaceImpl extends UnicastRemoteObject implements UsersInterface
{
	private ConnectionAdministrator connectionAdmin;
	private LocalFileAdministrator filesAdministrator;
	private RemoteFileSystemViewImpl fileSystemView;
	private File userHomeDirectory;

	/**
	 * Constructor.
	 */
	public UsersInterfaceImpl( File usersHome, LocalFileAdministrator fileAdmin, RemoteFileSystemViewImpl fileSystemView ) throws RemoteException
	{
		connectionAdmin = new ConnectionAdministrator();
		this.fileSystemView = fileSystemView;
		this.userHomeDirectory = usersHome;
		if( !userHomeDirectory.exists() )
		{
			userHomeDirectory.mkdirs();
		}
		this.filesAdministrator = fileAdmin;
	}

	@Override
	public final String getFile( final File file ) throws RemoteException
	{
		return connectionAdmin.getSocketNumForFileSending( file );
	}

	@Override
	public final String getFiles( final File[] files ) throws RemoteException
	{
		throw new UnsupportedOperationException( "Not supported yet." );
	}

	@Override
	public final String saveFile( final File directory, String fileName ) throws RemoteException
	{
		return connectionAdmin.getSocketForFileReceiving( directory, fileName, filesAdministrator );
	}

	@Override
	public final String saveFiles( final File directory ) throws RemoteException
	{
		throw new UnsupportedOperationException( "Not supported yet." );
	}

	@Override
	public ExtendedFile[] checkFilesBackups( ExtendedFile[] filesToCheck ) throws RemoteException
	{
		for( ExtendedFile file : filesToCheck )
		{
			file.setIsStored( filesAdministrator.isFileStoredOnServer( file ) );
		}
		return filesToCheck;
	}

	@Override
	public void removeFileFromServer( ExtendedFile file ) throws RemoteException
	{
		filesAdministrator.removeFileFromServer( file );
	}
	
	@Override
	public RemoteFileSystemView getFileSystemView( ) throws RemoteException
	{
		return fileSystemView;
	}
}
