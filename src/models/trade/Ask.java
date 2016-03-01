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
        super();
        this._owner = user;
        this._item = item;
        this._quantity = quantity;
        this._price = price;

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
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd [hh:mm:ss]");
        return String.format("%s | [%s] (%s) > %s %d %.2f",
                df.format(this._datetime),
                "SELL",
                this._owner.getName(),
                this._item,
                this._quantity,
                this._price);
    }

    public static double getLowestOffer(String item)
    {
        if (Ask.LOWESTOFFER.containsKey(item)) {
            return Ask.LOWESTOFFER.get(item);
        }

        return 0.0;
    }

}
