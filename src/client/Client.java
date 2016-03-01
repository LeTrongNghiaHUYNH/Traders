package client;

import java.io.*;
import java.net.*;

import models.user.User;

public class Client {
	
	protected User user;
	
	private static String line;
	private static Socket socket;
	private static BufferedReader fromServer;
	private static DataOutputStream toServer;
	private static BufferedReader stdIn;
	
	public Client() throws UnknownHostException, IOException {
		user = new User(new Socket("localhost", 9999));
	}	
	
	public Client(String name) throws UnknownHostException, IOException {
		user = new User(new Socket("localhost", 9999), name);
	}
	
	public Client(String url, int port) throws UnknownHostException, IOException {
		user = new User(new Socket(url, port));
	}
	
	public Client(String url, int port, String name) throws UnknownHostException, IOException {
		user = new User(new Socket(url, port), name);
	}

	public void init() throws UnknownHostException, IOException {
	    toServer = new DataOutputStream( user.getSocket().getOutputStream() );
	    fromServer = new BufferedReader( new InputStreamReader(user.getSocket().getInputStream()) );
	    stdIn = new BufferedReader(new InputStreamReader(System.in));
	}
	
	public void start() throws IOException {
		while (sendRequest()) {              // Send requests while connected
			receiveResponse();                 // Process server's answer
		}
	}
	
	public void stop() throws IOException {
	    user.getSocket().close();
	    toServer.close();
	    fromServer.close();
	}
	
	private static boolean sendRequest() throws IOException {
		boolean holdTheLine = true;          // Connection exists
		System.out.print("Enter message for the Server, or end the session with . : ");
		line = stdIn.readLine();
		toServer.writeBytes( line + '\n' );
		if (line.equals(".")) {              // Does the user want to end the session?
			holdTheLine = false;
		}
		return holdTheLine;
	}
	
	private static void receiveResponse() throws IOException {
		System.out.println("Server answers: " + new String(fromServer.readLine()) );
	}
	
	/** getters **/
	public User getUser() {
		return user;
	}
	
	/** setters **/
	public void setUser(User usr) {
		user = usr;
	}
}
