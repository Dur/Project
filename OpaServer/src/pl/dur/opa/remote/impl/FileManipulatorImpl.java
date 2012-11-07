/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.dur.opa.remote.impl;

import java.io.File;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import pl.dur.opa.connection.ConnectionAdministrator;
import pl.dur.opa.remote.interfaces.FileManipulator;

/**
 * Class implements file manipulator interface. Methods in this class are
 * calling new threads to serve clients calls.
 *
 * @author Dur
 */
public class FileManipulatorImpl extends UnicastRemoteObject implements FileManipulator
{
	private ConnectionAdministrator connectionAdmin;

	/**
	 * Constructor.
	 */
	public FileManipulatorImpl() throws RemoteException
	{
		connectionAdmin = new ConnectionAdministrator();
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
		return connectionAdmin.getSocketForFileReceiving( directory, fileName );
	}

	@Override
	public final String saveFiles( final File directory ) throws RemoteException
	{
		throw new UnsupportedOperationException( "Not supported yet." );
	}
}
