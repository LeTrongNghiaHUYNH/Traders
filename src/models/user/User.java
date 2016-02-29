package models.user;

import java.net.Socket;

/**
 * Created by warlof on 29/02/2016.
 */
public class User
{
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

    /**
     * The current user socket
     */
    private Socket _socket;

    public User(Socket socket)
    {
        this._id = ++User.ID;
        this._name = ("Anonymous " + this._id);
        this._socket = socket;
    }

    public User(Socket socket, String name)
    {
        this._name = name;
        this._socket = socket;
    }

    /**
     * Get the user ID
     * @return the user ID
     */
    public long getID()
    {
        return this._id;
    }

    /**
     * Return the current user name
     * @return the user name
     */
    public String getName()
    {
        return this._name;
    }

    /**
     * Return the current user socket
     * @return the user socket
     */
    public Socket getSocket()
    {
        return this._socket;
    }

    /**
     * Enable to modify the current user socket
     * @param socket the new user socket
     */
    public void setSocket(Socket socket)
    {
        this._socket = socket;
    }

    /**
     * Provide a string formatted as following :
     * [127.0.0.1:22503] Warlof 666
     * @return [userAddress:userPort] username userid
     */
    @Override
    public String toString()
    {
        return String.format("[%s:%d] %s (%d)",
                this._socket.getInetAddress(),
                this._socket.getPort(),
                this._name,
                this._id);
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
