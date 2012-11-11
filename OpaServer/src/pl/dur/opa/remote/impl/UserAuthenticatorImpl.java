package pl.dur.opa.remote.impl;

import java.io.File;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import pl.dur.opa.file.browser.LocalFileAdministrator;
import pl.dur.opa.remote.interfaces.UserAuthenticator;
import pl.dur.opa.remote.interfaces.UsersInterface;
import pl.dur.opa.server.configuration.UsersConfiguration;

/**
 *
 * @author Dur
 */
public class UserAuthenticatorImpl extends UnicastRemoteObject implements UserAuthenticator
{
	private UsersConfiguration users;
	
	private UsersInterface manipulator;
	
	private File rootDirectory;

	public UserAuthenticatorImpl( UsersConfiguration users, File rootDirectory ) throws RemoteException
	{
		this.users = users;
		this.rootDirectory = rootDirectory;
	}

	@Override
	public UsersInterface loginUser( String username, String password ) throws RemoteException
	{
		if( users.checkCredentials( username, password ) == true )
		{
			File usersHome = new File( rootDirectory + File.separator + username );
			LocalFileAdministrator fileAdmin = new LocalFileAdministrator( usersHome, username, true );
			File[] roots = new File[1];
			roots[0] = fileAdmin.getHomeDir();
			RemoteFileSystemViewImpl filesView = new RemoteFileSystemViewImpl( roots, roots[0] );
			manipulator = new UsersInterfaceImpl(fileAdmin.getHomeDir(), fileAdmin, filesView );
			fileAdmin.listFiles();
			return manipulator;
		}
		else
		{
			return null;
		}
	}
}
