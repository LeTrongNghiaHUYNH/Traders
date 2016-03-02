package server.rpc;

import server.log.LogType;
import server.log.Logger;

/**
 * Created by elfaus on 02/03/2016.
 */
public class XmlRpcServer extends Thread {

    int port;

    public XmlRpcServer(int port)
    {
        this.port = port;
    }

    @Override
    public void run()
    {
        Logger.write(LogType.notice, "Multithreaded RPC starts on Port " + port);
    }

}
