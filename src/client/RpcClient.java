package client;

import java.net.URL;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

public class RpcClient {

    private static final int port = 8080;
    private static final String ip = "127.0.0.1";

    public static void main(String[] args) throws Exception {

        // prepare XML-RPC configuration
        XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
        config.setServerURL(new URL("http://" + ip + ":" + port + "/xmlrpc"));
        // run our XML-RPC client with specified configuration
        XmlRpcClient client = new XmlRpcClient();
        client.setConfig(config);

        // prepare an ask request
        Object[] param2 = new Object[]{new String("LLEUILLIOT"), new String("IBM"), new Integer(3), new Double(1.0)};
        client.execute("Prices.ask", param2);

        // prepare a bid request
        Object[] param3 = new Object[]{new String("LLEUILLIOT"), new String("IBM"), new Integer(3), new Double(55.0)};
        client.execute("Prices.bid", param3);

        // display IBM prices (first is the highest bid, second is the lowest ask)
        Object[] param = new String[]{new String("IBM")};
        Object[] r = (Object[]) client.execute("Prices.get", param);

        // display result
        System.out.println("Stock = " + (Double) r[0] + " and " + (Double) r[1]);
    }
}

