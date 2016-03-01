package models.trade;

import models.user.User;
import java.text.SimpleDateFormat;
import java.util.Hashtable;

/**
 * Created by warlof on 29/02/2016.
 */
public class Ask extends AbstractTransaction
{

    private static Hashtable<String, Double> LOWESTOFFER = new Hashtable<String, Double>();

    public Ask(User user, String item, int quantity, double price)
    {
        super(item, quantity, price);
        this._owner = user;

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

    public static double getLowestOffer(String item)
    {
        if (Ask.LOWESTOFFER.containsKey(item)) {
            return Ask.LOWESTOFFER.get(item);
        }

        return 0.0;
    }

}
