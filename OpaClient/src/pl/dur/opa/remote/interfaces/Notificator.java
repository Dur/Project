package pl.dur.opa.remote.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author Dur
 */
public interface Notificator extends Remote
{
	public void serverComputingMessage(String message, boolean isComputing) throws RemoteException;
	
	public void sendMessageToClient(String message) throws RemoteException;
	
	public void serverProgressMessage( String message, int progress ) throws RemoteException;
}
