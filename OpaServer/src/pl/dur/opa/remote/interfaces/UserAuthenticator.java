package pl.dur.opa.remote.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interface to manipulate files located on server. Client calls methods of this
 * class to make some operations of files located on server.
 *
 * @author Dur
 */
public interface UserAuthenticator extends Remote
{
	/**
	 * Trys to log in user by checking it on passed username and password.
	 * If credentials maches any user registered on the server then new FileManipulator object 
	 * is created in this object.
	 * On this object client can do any operations related with his files.
	 * @param username - user name.
	 * @param password - password.
	 * @return boolaen if users was successfully loged or not
	 */
	UsersInterface loginUser(String username, String password, Notificator notificator )  throws RemoteException;
}
