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
    private static Hashtable<String, Double> LOWESTOFFER = new Hashtable<String, Double>();

    public Ask(User user, String item, int quantity, double price)
    {
        super(item, quantity, price);
        this._owner = user;

        // control if the price is a top price
        // First, in order to avoid any kind of null exception, we check that the provided share codename
        // exists.
        // Then we compared both recorded price and provided price
        // Finally, we update the price if it's suitable
        if (Ask.LOWESTOFFER.containsKey(this._item)) {
            if (this._price < Ask.LOWESTOFFER.get(this._item)) {
                Ask.LOWESTOFFER.put(this._item, this._price);
            }
        } else {
            Ask.LOWESTOFFER.put(this._item, this._price);
        }
    }

    /**
     * Provide a string formatted as following :
     * 2016/02/29 [18:35:07] | [SELL] (Warlof) > Apple 55 30.07
     * @return year/month/day [hours:minutes:seconds] | [SELL] (username) > item quantity price
     */
    @Override
    public String toString()
    {
        return "[SELL] " + super.toString();
    }

    /**
     * Return the lowest amount recorded for the specified share codename
     * @param item The share codename
     * @return The last recorded amount
     */
    public static double getLowestOffer(String item)
    {
        if (Ask.LOWESTOFFER.containsKey(item)) {
            return Ask.LOWESTOFFER.get(item);
        }

        return 0.0;
    }

}
