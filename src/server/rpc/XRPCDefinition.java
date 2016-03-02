package server.rpc;

import models.trade.Ask;
import models.trade.Bid;
import models.trade.Stock;
import models.user.User;
import server.Server;

/**
 * Created by elfaus on 02/03/2016.
 */
public class XRPCDefinition {

    public boolean ask(String username, String item, int qty, double price)
    {
        if (Server.isStockExists(item)) {
            User user = new User(null, username);
            Server.addAsk(user, Stock.valueOf(item), qty, price);

            return true;
        }

        return false;
    }

    public boolean bid(String username, String item, int qty, double price)
    {
        if (Server.isStockExists(item)) {
            User user = new User(null, username);
            Server.addBid(user, Stock.valueOf(item), qty, price);

            return true;
        }

        return false;
    }

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
