/**
 * 
 */
package com.socket;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * @author Kawsar
 *
 */
public class SocketData {

	public Socket socket;
	public ObjectOutputStream objectOutputStream;
	public ObjectInputStream objectInputStream;
	/**
	 * @param socket
	 * @param objectOutputStream
	 * @param objectInputStream
	 */
	public SocketData(Socket socket, ObjectOutputStream objectOutputStream,
			ObjectInputStream objectInputStream) {
		this.socket = socket;
		this.objectOutputStream = objectOutputStream;
		this.objectInputStream = objectInputStream;
	}
	
	
}
