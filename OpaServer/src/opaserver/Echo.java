package opaserver;


import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Echo extends Remote {
    public String echo(String input) throws RemoteException;
}
