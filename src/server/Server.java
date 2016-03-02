package server;

import models.trade.Ask;
import models.trade.Bid;
import models.user.User;
import server.app.AppServer;
import server.app.AppService;
import server.log.LogType;
import server.log.Logger;
import server.rpc.XmlRpcServer;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by elfaus on 02/03/2016.
 */
public class Server {

    public static int rpcPort = 8080;
    public static int appPort = 9495;

    public static String stocks[] = {"AAPL", "IBM", "MSFT", "ORCL"};

    public static ArrayList<Ask> asks;
    public static ArrayList<Bid> bids;
    public static ArrayList<User> users;

    public static void main(String[] args) throws Exception {
        asks = new ArrayList();
        bids = new ArrayList();
        users = new ArrayList();

        new AppServer(Server.appPort).start();
        new XmlRpcServer(Server.rpcPort).start();

        while (true);
    }

}
