package models.trade;

import models.user.User;
import java.util.Date;

/**
 * Created by warlof on 29/02/2016.
 */
public abstract class AbstractTransaction implements Comparable
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
    protected Date _datetime;
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

    protected AbstractTransaction()
    {
        this._id = ++AbstractTransaction.ID;
        this._datetime = new Date();

        this._owner = new User(null);
        this._item = "";
        this._quantity = 0;
        this._price = 0.0;
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
    public int compareTo(Object transaction) {
        if (this._price > ((AbstractTransaction) transaction)._price) {
            return 1;
        }else if (this._price < ((AbstractTransaction) transaction)._price) {
            return -1;
        } else {
            return 0;
        }
    }

    @Override
    public abstract String toString();

    /**
     * Get the last transaction ID
     * @return the last transaction ID which has been affected
     */
    public static long getLastId()
    {
        return AbstractTransaction.ID;
    }

    /**
     * Get the next transaction ID
     * @return the next transaction ID which will be affected
     */
    public static long getNextId()
    {
        return (AbstractTransaction.ID + 1);
    }
}
