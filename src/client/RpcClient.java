package client;

import java.net.URL;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;


public class RpcClient {
    public static void main(String[] args) throws Exception {

        XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();

        config.setServerURL(new URL("http://127.0.0.1:8080/xmlrpc"));
        XmlRpcClient client = new XmlRpcClient();
        client.setConfig(config);

        Object[] param2 = new Object[]{new String("LLEUILLIOT"), new String("IBM"), new Integer(3), new Double(1.0)};
        client.execute("Prices.ask", param2);

        Object[] param3 = new Object[]{new String("LLEUILLIOT"), new String("IBM"), new Integer(3), new Double(55.0)};
        client.execute("Prices.bid", param3);

        Object[] param = new String[]{new String("IBM")};
        Object[] r = (Object[]) client.execute("Prices.get", param);
        System.out.println("Stock = " + (Double) r[0] + " and " + (Double) r[1]);
    }
}

