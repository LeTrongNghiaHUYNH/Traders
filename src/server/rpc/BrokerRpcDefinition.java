package server.rpc;

import models.trade.Ask;
import models.trade.Bid;
import models.trade.Stock;
import models.user.User;
import server.Server;

/**
 * Created by elfaus on 02/03/2016.
 */
public class BrokerRpcDefinition {

    /**
     * Publish a method which enable us to send an ask to the Broker Service
     * @param username The user name
     * @param item The stock name
     * @param qty The quantity of the share we're selling
     * @param price The amount we're asking for each share
     * @return True if the ask has been took by the server
     */
    public boolean ask(String username, String item, int qty, double price)
    {
        if (Server.isStockExists(item)) {
            User user = new User(null, username);
            Server.addAsk(user, Stock.valueOf(item), qty, price);

            return true;
        }

        return false;
    }

    /**
     * Publish a method which enable us to send a bid to the Broker Service
     * @param username The user name
     * @param item The stock name
     * @param qty The quantity of the share we're buying
     * @param price The amount we're offering per share
     * @return
     */
    public boolean bid(String username, String item, int qty, double price)
    {
        if (Server.isStockExists(item)) {
            User user = new User(null, username);
            Server.addBid(user, Stock.valueOf(item), qty, price);

            return true;
        }

        return false;
    }

    /**
     * Publish a method which enable us to get prices stats for a specific Stock
     * @param stock The stock we're looking for
     * @return The highest bid and the lowest ask
     */
    public Double[] get(String stock) {
        double bid = 0.0;
        double ask = 0.0;

        if (Server.isStockExists(stock)) {

            Stock stockEnum = Stock.valueOf(stock);

            if (Bid.getHighestOffer(stockEnum) != null)
                bid = Bid.getHighestOffer(stockEnum).getPrice();
            if (Ask.getLowestOffer(stockEnum) != null)
                ask = Ask.getLowestOffer(stockEnum).getPrice();

        }

        return new Double[] {bid, ask};
    }

}
