package pl.dur.opa.tasks;

import pl.dur.opa.connection.SocketWrapper;

/**
 * Interface for tasks which need access to SocketWrapper to be executed properly. 
 * @author Dur
 */
public interface SocketTask extends Task
{
	/**
	 * Method sets to object socketWrapper object to allow task to use this socket.
	 * @param socket socket wrapper object.
	 */
	public void setSocket(SocketWrapper socket);
}
