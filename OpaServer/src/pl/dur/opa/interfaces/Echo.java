package pl.dur.opa.interfaces;

import java.io.File;
import java.rmi.Remote;
import java.rmi.RemoteException;
import javax.swing.JFileChooser;

public interface Echo extends Remote
{
	public JFileChooser echo( String input ) throws RemoteException;
	
	public void onSelectFile( File file ) throws RemoteException;
}
