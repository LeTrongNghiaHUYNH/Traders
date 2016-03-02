package server;

import models.trade.Ask;
import models.trade.Bid;
import models.trade.Stock;
import models.user.User;

import server.app.AppServer;
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
