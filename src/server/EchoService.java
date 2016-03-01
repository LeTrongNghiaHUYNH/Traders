package server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

import models.trade.Ask;
import models.trade.Bid;
import models.user.User;
import server.log.LogType;
import server.log.Logger;

public class EchoService extends Thread {
	
	User user;
	//Socket client;
	
	public EchoService(User user) {
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
						System.out.println("pb data");
					} else {						
						if (data[0].equals("BUY") || data[0].equals("SELL")) {
							type = data[0];
							item = data[1];
							quantity = Integer.parseInt(data[2]);
							price = Double.parseDouble(data[3]);
							
							if (type.equals("SELL")) {
								Ask ask = new Ask(user, item, quantity, price);
								Server.asks.add(ask);
								Logger.write(LogType.notice, ask.toString());
								toClient.writeBytes("Received: " + ask.toString());
							} else { // BUY
								Bid bid = new Bid(user, item, quantity, price);
								Server.bids.add(bid);
								Logger.write(LogType.notice, bid.toString());
								toClient.writeBytes("Received: " + bid.toString());
							}
						} else {
							// error
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
