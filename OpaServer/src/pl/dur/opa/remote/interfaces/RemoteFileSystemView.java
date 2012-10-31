package pl.dur.opa.remote.interfaces;

import java.io.File;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interface for FileSystemView for remote access from server. 
 * Each method will be used by class which will extend FileSystemView class. 
 * This will allow client to display server files in JFileChoose component.
 *
 * @author Dur
 */
public interface RemoteFileSystemView extends Remote
{
	/**
	 * Returns roots directories from remote server.
	 *
	 * @return array of roots directories.
	 * @throws RemoteException
	 */
	File[] getRemoteRoots() throws RemoteException;

	/**
	 * Returns home directory for user on remote server.
	 *
	 * @return home directory.
	 * @throws RemoteException
	 */
	File getRemoteHomeDirectory() throws RemoteException;

	/**
	 * Returns remote parent directory of selectd dir.
	 *
	 * @param dir selected directory for which we want parent.
	 * @return remote parent directory.
	 * @throws RemoteException
	 */
	File getRemoteParentDirectory( File dir ) throws RemoteException;

	/**
	 * If selected file is root directory.
	 *
	 * @param file passed file to check
	 * @return true if selected file is root, false otherwise.
	 * @throws RemoteException
	 */
	boolean isRoot( File file ) throws RemoteException;

	/**
	 * Get all files in selected remote directory.
	 *
	 * @param dir - selected directory.
	 * @param useFileHiding - are hidden files listed.
	 * @return Array of files in selected directory.
	 * @throws RemoteException
	 */
	File[] getRemoteFiles( File dir, boolean useFileHiding ) throws RemoteException;
	
	/**
	 * Return the user's default starting remote directory for the file chooser.
	 * 
	 * @return user default directory.
	 * @throws RemoteException
	 */
	File getDefaultRemoteDirectory() throws RemoteException;
	
	/**
	 * Gets file secified by name which is located in selected parent remote directory.
	 * @param parent parent of selected file
	 * @param fileName name of file
	 * @return file
	 * @throws RemoteException
	 */
	File getChild( File parent, String fileName ) throws RemoteException;
	
	/**
	 * Creates new directory in selected remote directory.
	 * @param containingDir remote directory.
	 * @return handler to created file.
	 * @throws RemoteException
	 */
	File createRemoteNewFolder( File containingDir ) throws RemoteException;
			
	/**
	 * Creates remote file in remote directory.
	 * @param path path to remote directory.
	 * @return handler to created file.
	 * @throws RemoteException
	 */
	File createRemoteFileObject( String path ) throws RemoteException;
	
	/**
	 * Creates new file in selected remote directory.
	 * @param dir remote directory.
	 * @param filename file name
	 * @return handler to file
	 * @throws RemoteException
	 */
	File createRemoteFileObject( File dir, String filename ) throws RemoteException;
}
