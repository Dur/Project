package pl.dur.opa.remote.interfaces;

import java.io.File;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import pl.dur.opa.remote.impl.RemoteFileSystemViewImpl;
import pl.dur.opa.utils.ExtendedFile;

/**
 * Interface to manipulate files located on server. Client calls methods of this
 * class to make some operations of files located on server.
 *
 * @author Dur
 */
public interface UsersInterface extends Remote
{
	/**
	 * Returns String which is a key to send back by socket. This key determine
	 * which operation server will do for client.
	 *
	 * @param file - file to download.
	 * @return String key for task search.
	 * @throws RemoteException
	 */
	String getFile( File file ) throws RemoteException;

	/**
	 * Returns String which is a key to send back by socket. This key determine
	 * which operation server will do for client.
	 *
	 * @param files - files to download.
	 * @return String key for task search.
	 * @throws RemoteException
	 */
	String getFiles( File[] file ) throws RemoteException;

	/**
	 * Returns String which is a key to send back by socket. This key determine
	 * which operation server will do for client.
	 *
	 * @return String key for task search.
	 * @param directory - directory in which sended file should be saved.
	 * @param fileName - name of file to save.
	 *
	 * @throws RemoteException
	 */
	String saveFile( File directory, String fileName ) throws RemoteException;

	/**
	 * Returns String which is a key to send back by socket. This key determine
	 * which operation server will do for client. Diference between saveFile()
	 * method is that after receiving first file, server will not close
	 * connection. Use this method if u want to save more than one file. All
	 * files will be save in the same directory.
	 *
	 * @return port number.
	 * @param directory - directory in which sended files should be saved.
	 * @throws RemoteException
	 */
	String saveFiles( File directory ) throws RemoteException;

	/**
	 * As a parameters it gets list of files to check if server contains files
	 * backuped. It returns the same list of files with added information if
	 * file is backuped.
	 *
	 * @param filesToCheck - list of files to chcek if they are backuped on
	 * server.
	 * @return same list with info.
	 * @throws RemoteException
	 */
	List<ExtendedFile> checkFilesBackups( List<ExtendedFile> filesToCheck ) throws RemoteException;

	void removeFileFromServer( ExtendedFile file ) throws RemoteException;

	RemoteFileSystemView getFileSystemView() throws RemoteException;
}
