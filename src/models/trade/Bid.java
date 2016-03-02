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
    private static Hashtable<String, Bid> HIGHESTOFFER = new Hashtable<String, Bid>();

    public Bid(User user, String item, int quantity, double price)
    {
        super(item, quantity, price);
        this._owner = user;

        if (Bid.HIGHESTOFFER.containsKey(this._item)) {
            if (this._price > Bid.HIGHESTOFFER.get(this._item)._price) {
                Bid.HIGHESTOFFER.put(this._item, this);
            }
        } else {
            Bid.HIGHESTOFFER.put(this._item, this);
        }
    }

    /**
     * Return the highest record for a specified share codename
     * @param item The share codename
     * @return The highest record
     */
    public static Bid getHighestOffer(String item)
    {
        if (Bid.HIGHESTOFFER.containsKey(item)) {
            return Bid.HIGHESTOFFER.get(item);
        }

        return null;
    }
}
