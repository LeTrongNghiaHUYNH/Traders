package server;

import models.trade.Ask;
import models.trade.Bid;
import models.trade.Stock;
import models.user.User;

import server.app.AppServer;
import server.log.LogType;
import server.log.Logger;
import server.rpc.TraderXmlRpcServer;

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
        new TraderXmlRpcServer(Server.rpcPort).start();

        // infinite loop in order to avoid the server termination
        while (true);
    }
    
    public static void addAsk(User user, String item, int quantity, double price) {
    	Ask ask = new Ask(user, item, quantity, price);
    	asks.add(ask);
    	Logger.write(LogType.notice, ask.toString());
    }
    
    public static void addBid(User user, String item, int quantity, double price) {
    	Bid bid = new Bid(user, item, quantity, price);
    	bids.add(bid);
    	Logger.write(LogType.notice, bid.toString());
    }
    
    public static boolean isMatched(String item) {
    	Bid highestBid = null;
    	for (Bid bid : bids) {
    		if (bid.getItem().equals(item)) {
    			if (highestBid == null || bid.getPrice() > highestBid.getPrice()) {
    				highestBid = bid;
    			}
    		}
    	}
    	
    	Ask lowestAsk = null;
    	for (Ask ask : asks) {
    		if (ask.getItem().equals(item)) {
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
            Stock.valueOf(searchedStock);
        } catch (IllegalArgumentException e) {
            return false;
        }

        return true;
    }

}
