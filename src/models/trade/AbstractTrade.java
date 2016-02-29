package models.trade;

import models.user.User;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by warlof on 29/02/2016.
 */
public abstract class AbstractTrade
{
    /**
     * The ID is defined statically, so be able to keep a track of each transaction
     */
    private static long ID = 0;

    /**
     * The current transaction id
     */
    private long _id;
    /**
     * The current transaction time
     */
    private Date _datetime;
    /**
     * The current transaction owner
     */
    protected User _owner;
    /**
     * The item about which the transaction is related
     */
    protected String _item;
    /**
     * The amount of item which are concerned by the transaction
     */
    protected int _quantity;
    /**
     * The price asked per unit
     */
    protected double _price;

    protected AbstractTrade()
    {
        this._id = ++AbstractTrade.ID;
        this._datetime = new Date();

        this._owner = new User(null);
        this._item = "";
        this._quantity = 0;
        this._price = 0.0;
    }

    /**
     * Enable to know what kind of transaction the current transaction is
     * @return the type of transaction
     */
    private String getTransactionType()
    {
        if (this instanceof Ask) {
            return "Buy";
        }

        if (this instanceof Bid) {
            return "Sell";
        }

        return "";
    }

    /**
     * Get the transaction ID
     * @return the current transaction ID
     */
    public long getId()
    {
        return this._id;
    }

    @Override
    public String toString()
    {
        SimpleDateFormat df = new SimpleDateFormat("yyyy.MM.dd [hh:mm:ss]");
        return String.format("%s | (%s) %s > %s %d %.2f",
                df.format(this._datetime),
                this._owner.getName(),
                this.getTransactionType(),
                this._item,
                this._quantity,
                this._price);
    }

    /**
     * Get the last transaction ID
     * @return the last transaction ID which has been affected
     */
    public static long getLastId()
    {
        return AbstractTrade.ID;
    }

    /**
     * Get the next transaction ID
     * @return the next transaction ID which will be affected
     */
    public static long getNextId()
    {
        return (AbstractTrade.ID + 1);
    }
}
