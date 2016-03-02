package server.app;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import models.trade.Ask;
import models.trade.Bid;
import models.user.User;
import server.Server;
import server.log.LogType;
import server.log.Logger;

public class AppServer extends Thread {

	int port;

	public AppServer(int port) throws Exception {
		this.port = port;
	}

	@Override
	public void run()
	{
		try {
			ServerSocket listenSocket = new ServerSocket(port);
			Logger.write(LogType.notice, "Multithreaded Server starts on Port " + port);

			while (true) {
				Socket client = listenSocket.accept();
				Logger.write(LogType.notice, "Connection from: " + client.getRemoteSocketAddress());
				User user = new User(client);
				Server.users.add(user);
				new AppService(user).start();
			}
		} catch (IOException e) {
			Logger.write(LogType.error, e.toString());
		}
	}
}
