package models.trade;

import models.user.User;

import java.text.SimpleDateFormat;

/**
 * Created by warlof on 29/02/2016.
 */
public class Bid extends AbstractTransaction
{

    private static double HIGHESTOFFER = 0.0;

    public Bid(User user, String item, int quantity, double price)
    {
        super();
        this._owner = user;
        this._item = item;
        this._quantity = quantity;
        this._price = price;

        if (this._price > Bid.HIGHESTOFFER) {
            Bid.HIGHESTOFFER = this._price;
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
        SimpleDateFormat df = new SimpleDateFormat("yyyy.MM.dd [hh:mm:ss]");
        return String.format("%s | [%s] (%s) > %s %d %.2f",
                df.format(this._datetime),
                this._owner.getName(),
                "BUY",
                this._item,
                this._quantity,
                this._price);
    }

    public static double getHighestOffer()
    {
        return Bid.HIGHESTOFFER;
    }
}
