package models.trade;

import models.user.User;

import java.util.Hashtable;

/**
 * Created by warlof on 29/02/2016.
 */
public class Ask extends AbstractTransaction
{
    /**
     * Store the lowest offer for a specific share codename
     */
    private static Hashtable<Stock, Ask> LOWESTOFFER = new Hashtable<Stock, Ask>();

    public Ask(User user, Stock stock, int quantity, double price)
    {
        super(user, stock, quantity, price);

        // control if the price is a top price
        // First, in order to avoid any kind of null exception, we check that the provided share codename
        // exists.
        // Then we compared both recorded price and provided price
        // Finally, we update the price if it's suitable
        if (Ask.LOWESTOFFER.containsKey(this._stock)) {
            if (this._price < Ask.LOWESTOFFER.get(this._stock)._price) {
                Ask.LOWESTOFFER.put(this._stock, this);
            }
        } else {
            Ask.LOWESTOFFER.put(this._stock, this);
        }
    }

    /**
     * Return the lowest record for the specified share codename
     * @param stock The stock about which the transaction is related
     * @return The lowest record
     */
    public static Ask getLowestOffer(Stock stock)
    {
        if (Ask.LOWESTOFFER.containsKey(stock)) {
            return Ask.LOWESTOFFER.get(stock);
        }

        return null;
    }

    public static String displayLowestOffers() {
    	String res = "[" + Ask.LOWESTOFFER.size() + "] ";
    	for (Stock stock : Ask.LOWESTOFFER.keySet()) {
    		res += stock + " [" + Ask.LOWESTOFFER.get(stock) + "]\n";
    	}
    	return res;
    }
}
