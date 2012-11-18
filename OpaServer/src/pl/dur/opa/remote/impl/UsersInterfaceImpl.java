/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.dur.opa.remote.impl;

import java.io.File;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import pl.dur.opa.connection.ConnectionAdministrator;
import pl.dur.opa.file.browser.LocalFileAdministrator;
import pl.dur.opa.remote.interfaces.Notificator;
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
	private Notificator notificator;

	/**
	 * Constructor.
	 */
	public UsersInterfaceImpl( File usersHome, LocalFileAdministrator fileAdmin, 
			RemoteFileSystemViewImpl fileSystemView, Notificator notificator) throws RemoteException
	{
		connectionAdmin = new ConnectionAdministrator(notificator);
		this.fileSystemView = fileSystemView;
		this.userHomeDirectory = usersHome;
		if( !userHomeDirectory.exists() )
		{
			userHomeDirectory.mkdirs();
		}
		this.filesAdministrator = fileAdmin;
		this.notificator = notificator;
	}

	@Override
	public final String getFile( final File file ) throws RemoteException
	{
		return connectionAdmin.getSocketNumForFileSending( file );
	}

	@Override
	public final String saveFile( final File directory, String fileName, long lastModified ) throws RemoteException
	{
		return connectionAdmin.getSocketForFileReceiving( directory, fileName, filesAdministrator, lastModified );
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

	@Override
	public long getFileLastModify( File file ) throws RemoteException
	{
		if( file != null && file.canRead() )
		{
			return file.lastModified();
		}
		else
		{
			return 0;
		}
	}

	@Override
	public File locateFileOnServer( ExtendedFile file ) throws RemoteException
	{
		return filesAdministrator.locateFile(file);
	}
}
