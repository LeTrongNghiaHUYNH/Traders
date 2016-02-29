package models.user;

/**
 * Created by warlof on 29/02/2016.
 */
public class User {
    /**
     * The ID is defined statically, so be able to keep a track of each user
     */
    private static long ID = 0;

    /**
     * The current user id
     */
    private long _id;
    /**
     * The current user name
     */
    private String _name;

    public User()
    {
        this._id = ++User.ID;
        this._name = ("Anonymous " + this._id);
    }

    public User(String name){
        this._name = name;
    }

    /**
     * Get the user ID
     * @return
     */
    public long getID()
    {
        return this._id;
    }

    /**
     * Get the last user ID
     * @return the current user ID
     */
    public static long getLastId()
    {
        return User.ID;
    }

    /**
     * Get the next user ID
     * @return the next user ID which will be affected
     */
    public static long getNextId()
    {
        return (User.ID + 1);
    }
}
