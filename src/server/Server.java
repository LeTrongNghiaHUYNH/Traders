package server;

import models.trade.Ask;
import models.trade.Bid;
import models.trade.Stock;
import models.user.User;

import server.app.AppServer;
import server.log.LogType;
import server.log.Logger;
import server.rpc.BrokerXmlRpcServer;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by elfaus on 02/03/2016.
 */
public class Server {

    public static int rpcPort = 8080;
    public static int appPort = 9495;

    public static ArrayList<Ask> asks;
    public static ArrayList<Bid> bids;
    public static ArrayList<User> users;

    public static void main(String[] args) throws Exception {
        asks = new ArrayList();
        bids = new ArrayList();
        users = new ArrayList();

        // run both cmdLine and RPC server in separate threads
        new AppServer(Server.appPort).start();
        new BrokerXmlRpcServer(Server.rpcPort).start();

        // infinite loop in order to avoid the server termination
        while (true);
    }

    /**
	 * Add an ask to the server list
	 * @param user The user name
	 * @param stock The stock about which the transaction is related
	 * @param quantity The share quantity which are sold
	 * @param price The amount of each share
     * @param client The client stream if available
     */
    public static void addAsk(User user, Stock stock, int quantity, double price, DataOutputStream client) {
    	Ask ask = new Ask(user, stock, quantity, price);
    	asks.add(ask);
    	Logger.write(LogType.notice, ask.toString());

        if (client != null) {
            try {
                client.writeBytes("Received: " + ask.toString() + '\n');
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
	 * Add a bid to the server list
	 * @param user The user name
	 * @param stock The stock about which the transaction is related
	 * @param quantity The share quantity which are bought
	 * @param price The amount of each share
     * @param client The client stream if available
     */
    public static void addBid(User user, Stock stock, int quantity, double price, DataOutputStream client) {
    	Bid bid = new Bid(user, stock, quantity, price);
    	bids.add(bid);
    	Logger.write(LogType.notice, bid.toString());

        if (client != null) {
            try {
                client.writeBytes("Received: " + bid.toString() + '\n');
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void listing()
    {
        if (Server.users.size() > 0) {
            String users = "Users:\n";
            for (User user : Server.users) {
                users += user.toString() + "\n";
            }
            Logger.write(LogType.debug, users);
        }

        if (Server.asks.size() > 0) {
            String asks = "Asks:\n";
            for (Ask ask : Server.asks) {
                asks += ask.toString() + "\n";
            }
            Logger.write(LogType.debug, asks);
        }

        if (Server.bids.size() > 0) {
            String bids = "Bids:\n";
            for (Bid bid : Server.bids) {
                bids += bid.toString() + "\n";
            }
            Logger.write(LogType.debug, bids);
        }
    }

    /**
	 * Check if we have some matched bid/ask
	 * @param stock The stock about which we want make the check
	 * @return True if there is a match
     */
    public static boolean isMatched(Stock stock) {
    	Bid highestBid = null;
    	for (Bid bid : bids) {
    		if (bid.getStock() == stock) {
    			if (highestBid == null || bid.getPrice() > highestBid.getPrice()) {
    				highestBid = bid;
    			}
    		}
    	}
    	
    	Ask lowestAsk = null;
    	for (Ask ask : asks) {
    		if (ask.getStock() == stock) {
    			if (lowestAsk == null || ask.getPrice() < lowestAsk.getPrice()) {
    				lowestAsk = ask;
    			}
    		}
    	}
		
		if (highestBid != null && lowestAsk != null && highestBid.getPrice() >= lowestAsk.getPrice()) {
			int minQuantity = Math.min(highestBid.getQuantity(), lowestAsk.getQuantity());
			
			highestBid.decreaseQuantity(minQuantity);
			lowestAsk.decreaseQuantity(minQuantity);
			
			if (highestBid.getQuantity() <= 0) {
				Server.bids.remove(highestBid);
				Logger.write(LogType.notice, "Removed: " + highestBid.toString());
			}
			
			if (lowestAsk.getQuantity() <= 0) {
				Server.asks.remove(lowestAsk);
				Logger.write(LogType.notice, "Removed: " + lowestAsk.toString());
			}
			return true;
		}
		return false;
    }

    /**
     * Enable to check if the specified string is a valid stock
     * @param searchedStock The stock we want check the existence
     * @return True if the stock exists
     */
    public static boolean isStockExists(String searchedStock)
    {
        try {
            Stock.valueOf(searchedStock.toUpperCase());
        } catch (IllegalArgumentException e) {
            return false;
        }

        return true;
    }

}
