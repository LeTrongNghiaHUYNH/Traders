package server.app;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

import models.trade.Ask;
import models.trade.Bid;
import models.trade.Stock;
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
		Stock item;
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
				if (line == null || (line != null && line.equals("."))) {
					connected = false;
                    Logger.write(LogType.debug, String.format("Session from %s is over", user));
				} else {
                    if (line.startsWith("hello")) {
                        data = line.split(" ");
                        if (data.length >= 2) {
                            this.user.setName(data[1]);
                            Logger.write(LogType.debug, String.format("New authentification from %s",
                                    this.user));
                            toClient.writeBytes("hello brocker\n");
                        } else {
                            Logger.write(LogType.warning, String.format("Authentication failure from %s. Unknown user.",
                                    this.user));
                            toClient.writeBytes(String.format("unknown user, you're %s\n",
                                    this.user.getName()));
                        }
                    } else {
                        data = line.split(" ");
                        if (data.length != 4) {
                            if (data.length == 1 && data[0].equals("ls")) {
                                Server.listing();
                                toClient.writeBytes("End of the listing\n");
                            } else {
                                Logger.write(LogType.error, "There is no enough argument.");
                                toClient.writeBytes("There is no enough argument.\n");
                            }
                        } else {
                            type = data[0].toUpperCase();
                            if (type.equals("BUY") || type.equals("SELL")) {

                                if (Server.isStockExists(data[1])) {
                                    item = Stock.valueOf(data[1].toUpperCase());
                                    quantity = Integer.parseInt(data[2]);
                                    price = Double.parseDouble(data[3]);

                                    if (type.equals("SELL")) {
                                        Server.addAsk(user, item, quantity, price, toClient);
                                    } else { // BUY
                                        Server.addBid(user, item, quantity, price, toClient);
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
			}
			
			fromClient.close();
		} catch (IOException e) {
			Logger.write(LogType.error, e.toString());
		}
	}
}
