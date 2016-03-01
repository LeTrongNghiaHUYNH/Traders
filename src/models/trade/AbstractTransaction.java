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

    protected AbstractTransaction(String item, int quantity, double price)
    {
        this._id = ++AbstractTransaction.ID;
        this._datetime = new Date();

        this._owner = new User(null);
        this._item = item;
        this._quantity = quantity;
        this._price = price;
    }

    /**
     * Get the transaction ID
     * @return the current transaction ID
     */
    public long getId()
    {
        return this._id;
    }

    /**
     * Get the transaction quantity
     * @return the current transaction item quantity buy or sell
     */
    public int getQuantity()
    {
        return this._quantity;
    }

    /**
     * Get the transaction price
     * @return the current transaction price per item
     */
    public double getPrice()
    {
        return this._price;
    }

    /**
     * Get the transaction creation time
     * @return the time when the current transaction has been created
     */
    public Date getDateTime()
    {
        return this._datetime;
    }

    /**
     * Provide a way to compare transaction between them based on their price
     * @param transaction The transaction to compare with the current one
     * @return 1: The current transaction is less important | -1 : The transaction is more important | 0 : Both transactions are same
     */
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
    public String toString()
    {
        return String.format("(%s) > %s %d %.2f",
                this._owner.getName(),
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
    
    /**
     * Decrease the current quantity of nb
     * @param nb
     */
    public void decreaseQuantity(int nb) {
    	this._quantity -= nb;
    }
}
