/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.dur.opa.remote.impl;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.BlockingQueue;
import pl.dur.opa.remote.interfaces.Notificator;
import pl.dur.opa.view.View;

/**
 *
 * @author Dur
 */
public class NotificatorImpl extends UnicastRemoteObject implements Notificator
{

	private BlockingQueue<Object> notify;
	
	private View view;
	
	public NotificatorImpl( View view ) throws RemoteException
	{
		this.view = view;
	}

	@Override
	public void serverComputingMessage( String message, boolean isComputing ) throws RemoteException
	{
		view.setServerComputingState( message, isComputing );
	}
	
	@Override
	public void serverProgressMessage( String message, int progress ) throws RemoteException
	{
		view.serverProgressMessage( message, progress );
	}

	@Override
	public void sendMessageToClient( String message ) throws RemoteException
	{
		view.notifyUser(message);
	}


	
}
