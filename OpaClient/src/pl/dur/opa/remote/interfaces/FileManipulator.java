
package pl.dur.opa.remote.interfaces;

import java.io.File;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interface to manipulate files located on server. 
 * Client calls methods of this class to make some operations of files located on server.
 * @author Dur
 */
public interface FileManipulator extends Remote
{
	/**
	 * Returns port number to which client should connect to download file.
	 *
	 * @param file - file to download.\
	 * @return port number to connect.
	 * @throws RemoteException
	 */
	int getFile( File file ) throws RemoteException;

	/**
	 * Returns port number to which client should connect to download file.
	 *
	 * @param files - files to download.
	 * @return port number to connect.
	 * @throws RemoteException
	 */
	int getFiles( File[] file ) throws RemoteException;
	
	/**
	 * Returns port number for client to connect.
	 * On this port client should connect to save file.
	 * @return port number.
	 * @param directory - directory in which sended file should be saved.
	 * @param fileName - name of file to save.
	 * 
	 * @throws RemoteException
	 */
	int saveFile( File directory, String fileName ) throws RemoteException;
	
	/**
	 * Returns port number for client to connect.
	 * On this port client should connect to save files (more than one).
	 * Diference between saveFile() method is that after receiving first file, server will not close connection.
	 * Use this method if u want to save more than one file.
	 * All files will be save in the same directory.
	 * @return port number.
	 * @param directory - directory in which sended files should be saved.
	 * @throws RemoteException
	 */
	int saveFiles( File directory ) throws RemoteException;
	
	/**
	 * Checks if selected file is the same as one located on server
	 * @throws RemoteException
	 */
	//checkFileVersion //TODO
	
	
}
