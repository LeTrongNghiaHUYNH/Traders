package models.trade;

import models.user.User;

/**
 * Created by warlof on 29/02/2016.
 */
public class Bid extends AbstractTrade {

    public Bid(User user, String item, int quantity, double price){
        super();
        this._owner = user;
        this._item = item;
        this._quantity = quantity;
        this._price = price;
    }

}
