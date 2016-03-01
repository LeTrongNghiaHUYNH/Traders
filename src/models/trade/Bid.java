package models.trade;

import models.user.User;

import java.text.SimpleDateFormat;
import java.util.Hashtable;

/**
 * Created by warlof on 29/02/2016.
 */
public class Bid extends AbstractTransaction
{
    /**
     * Store the highest offer for a specific share codename
     */
    private static Hashtable<String, Double> HIGHESTOFFER = new Hashtable<String, Double>();

    public Bid(User user, String item, int quantity, double price)
    {
        super(item, quantity, price);
        this._owner = user;

        if (Bid.HIGHESTOFFER.containsKey(this._item)) {
            if (this._price > Bid.HIGHESTOFFER.get(this._item)) {
                Bid.HIGHESTOFFER.put(this._item, this._price);
            }
        } else {
            Bid.HIGHESTOFFER.put(this._item, this._price);
        }
    }

    /**
     * Provide a string formatted as following :
     * 2016/02/29 [18:35:07] | [BUY] (Warlof) > Apple 55 30.07
     * @return year/month/day [hours:minutes:seconds] | [BUY] (username) > item quantity price
     */
    @Override
    public String toString()
    {
        return "[BUY] " + super.toString();
    }

    /**
     * Return the highest recorded amount for a specified share codename
     * @param item The share codename
     * @return The last recorded amount
     */
    public static double getHighestOffer(String item)
    {
        if (Bid.HIGHESTOFFER.containsKey(item)) {
            return Bid.HIGHESTOFFER.get(item);
        }

        return 0.0;
    }
}
