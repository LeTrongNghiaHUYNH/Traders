package server.rpc;

import org.apache.xmlrpc.server.PropertyHandlerMapping;
import org.apache.xmlrpc.server.XmlRpcServer;
import org.apache.xmlrpc.webserver.WebServer;
import server.log.LogType;
import server.log.Logger;

/**
 * Created by elfaus on 02/03/2016.
 */
public class TraderXmlRpcServer extends Thread {

    int port;

    public TraderXmlRpcServer(int port)
    {
        this.port = port;
    }

    @Override
    public void run()
    {
        try {
            WebServer webServer = new WebServer(port);

            XmlRpcServer xmlRpcServer = webServer.getXmlRpcServer();
            PropertyHandlerMapping phm = new PropertyHandlerMapping();

            phm.addHandler( "Prices", XRPCDefinition.class);
            xmlRpcServer.setHandlerMapping(phm);

            webServer.start();
            Logger.write(LogType.notice, "Multithreaded RPC starts on Port " + port);

        } catch (Exception exception) {
            Logger.write(LogType.error, "RPC Server error: " + exception);
        }
    }

}
