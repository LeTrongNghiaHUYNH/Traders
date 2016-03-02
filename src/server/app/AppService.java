package server.app;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

import models.trade.Ask;
import models.trade.Bid;
import models.user.User;
import server.Server;
import server.log.LogType;
import server.log.Logger;

public class AppService extends Thread {
	
	User user;
	//Socket client;
	
	public AppService(User user) {
		this.user = user;
	}
	
	@Override
	public void run() {
		String line;
		String[] data;
		String type;
		String item;
		int quantity;
		double price;
		
		boolean connected = true;
		
		BufferedReader fromClient;
		DataOutputStream toClient;

        Logger.write(LogType.notice, "Thread started: " + this);
		
		try {
			fromClient = new BufferedReader(new InputStreamReader(user.getSocket().getInputStream()));
			toClient = new DataOutputStream(user.getSocket().getOutputStream());

			while (connected) {
				line = fromClient.readLine();
				if (line.equals(".")) {
					connected = false;
				} else {
					data = line.split(" ");
					if (data.length != 4) {
						if (data.length == 1 && data[0].equals("ls")) {
							if (Server.users.size() > 0) {
								String users = "Users:\n";
								for (User user : Server.users) {
									users += user.toString() + "\n";
								}
								Logger.write(LogType.notice, users);
							}
							
							if (Server.asks.size() > 0) {
								String asks = "Asks:\n";
								for (Ask ask : Server.asks) {
									asks += ask.toString() + "\n";
								}
								Logger.write(LogType.notice, asks);
							}
							
							if (Server.bids.size() > 0) {
								String bids = "Bids:\n";							
								for (Bid bid : Server.bids) {
									bids += bid.toString() + "\n";
								}
								Logger.write(LogType.notice, bids);
							}
							
							toClient.writeBytes("End of the listing\n");
						} else {
							Logger.write(LogType.error, "There is no enough argument.");
							toClient.writeBytes("There is no enough argument.\n");
						}
					} else {
						type = data[0].toUpperCase();
						if (type.equals("BUY") || type.equals("SELL")) {
							item = data[1];
							
							if (Arrays.asList(Server.stocks).contains(item)) {
								quantity = Integer.parseInt(data[2]);
								price = Double.parseDouble(data[3]);
								
								if (type.equals("SELL")) {
									Ask ask = new Ask(user, item, quantity, price);
									Server.asks.add(ask);
									Logger.write(LogType.notice, ask.toString());
									toClient.writeBytes("Received: " + ask.toString() + '\n');
								} else { // BUY
									Bid bid = new Bid(user, item, quantity, price);
									Server.bids.add(bid);
									Logger.write(LogType.notice, bid.toString());
									toClient.writeBytes("Received: " + bid.toString() + '\n');
								}
								
								Server.isMatched(item);
							} else {
								Logger.write(LogType.error, "This item is not saleable.");
								toClient.writeBytes("This item is not saleable.\n");
							}
						} else {
							Logger.write(LogType.error, "It's not a BUY or SELL command.");
							toClient.writeBytes("It's not a BUY or SELL command.\n");
						}
					}
				}
			}
			
			fromClient.close();
		} catch (Exception e) {
			Logger.write(LogType.error, e.toString());
		}
	}
}
