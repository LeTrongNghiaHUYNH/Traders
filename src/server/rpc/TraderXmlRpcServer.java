package server.rpc;

import org.apache.xmlrpc.server.PropertyHandlerMapping;
import org.apache.xmlrpc.server.XmlRpcServer;
import org.apache.xmlrpc.webserver.WebServer;
import server.log.LogType;
import server.log.Logger;

/**
 * Created by warlof on 02/03/2016.
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
            // create a new XML-RPC server on specified port
            WebServer webServer = new WebServer(port);
            // map our interfaces to the XML-RPC service
            XmlRpcServer xmlRpcServer = webServer.getXmlRpcServer();
            PropertyHandlerMapping phm = new PropertyHandlerMapping();
            // we support BrokerRpcDefinition with Prices command
            phm.addHandler( "Prices", BrokerRpcDefinition.class);
            xmlRpcServer.setHandlerMapping(phm);
            // start our XML-RPC service
            webServer.start();
            // log the event
            Logger.write(LogType.notice, "Multithreaded RPC starts on Port " + port);
        } catch (Exception exception) {
            Logger.write(LogType.error, "RPC Server error: " + exception);
        }
    }

}
