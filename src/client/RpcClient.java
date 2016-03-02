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

        Object[] params = new Object[]{new Integer(33), new Integer(9)};
        System.out.println("About to get results...(params[0] = " + params[0]
                + ", params[1] = " + params[1] + ")." );

        int result;
        result = (Integer) client.execute("Prices.add", params);
        System.out.println("Mul Result = " + result );

        Object[] param = new String[]{new String()};
        double r = (Double) client.execute("Prices.get", param);
        System.out.println("Stock = " + r);
    }
}

