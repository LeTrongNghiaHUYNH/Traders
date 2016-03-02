package server.rpc;

import server.Server;

/**
 * Created by elfaus on 02/03/2016.
 */
public class XRPCDefinition {

    public Integer add(int x, int y) {
        return new Integer(x+y);
    }

    public Double get(String stock) {
        if (Server.isStockExists(stock)) {
            return 33.4;
        } else {
            return 0.0;
        }
    }

}
