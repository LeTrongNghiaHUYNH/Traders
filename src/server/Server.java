package server;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import models.trade.Ask;
import models.trade.Bid;
import models.user.User;
import server.log.LogType;
import server.log.Logger;

public class Server {
	
	static ArrayList<Ask> asks;
	static ArrayList<Bid> bids;
	static ArrayList<User> users;
	
	public static void main(String[] args) throws Exception {
		users = new ArrayList<User>();
		int port = 9495;
		ServerSocket listenSocket = new ServerSocket(port);
		Logger.write(LogType.notice, "Multithreaded Server starts on Port " + port);

		while (true) {
			Socket client = listenSocket.accept();
            Logger.write(LogType.notice, "Connection from: " + client.getRemoteSocketAddress());
			User user = new User(client);
			users.add(user);
			new EchoService(user).start();
		}
	}	
}
