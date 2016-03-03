package models.trade;

import models.user.User;
import java.text.SimpleDateFormat;
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

    public Ask(User user, Stock item, int quantity, double price)
    {
        super(user.getName(), item, quantity, price);
        this._owner = user;

        // control if the price is a top price
        // First, in order to avoid any kind of null exception, we check that the provided share codename
        // exists.
        // Then we compared both recorded price and provided price
        // Finally, we update the price if it's suitable
        if (Ask.LOWESTOFFER.containsKey(this._item)) {
            if (this._price < Ask.LOWESTOFFER.get(this._item)._price) {
                Ask.LOWESTOFFER.put(this._item, this);
            }
        } else {
            Ask.LOWESTOFFER.put(this._item, this);
        }
    }

    /**
     * Return the lowest record for the specified share codename
     * @param item The share codename
     * @return The lowest record
     */
    public static Ask getLowestOffer(Stock item)
    {
        if (Ask.LOWESTOFFER.containsKey(item)) {
            return Ask.LOWESTOFFER.get(item);
        }

        return null;
    }

}
