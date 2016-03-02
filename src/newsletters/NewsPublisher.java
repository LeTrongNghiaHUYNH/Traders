package newsletters;

import models.trade.Stock;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQTopic;

import javax.jms.*;
import java.util.Random;

/**
 * Created by elfaus on 02/03/2016.
 */
public class NewsPublisher {

    private static int ID;

    private static String user;
    private static String password;
    private static String host;
    private static int port;
    private static String destination;

    public static void main(String args[]) throws JMSException
    {
        user = env("ACTIVEMQ_USER", "admin");
        password = env("ACTIVEMQ_PASSWORD", "password");
        host = env("ACTIVEMQ_HOST", "localhost");
        port = Integer.parseInt(env("ACTIVEMQ_PORT", "61616"));
        destination = arg(args, 0, "event");

        while (true) {
            Random r = new Random();
            int kindOfNews = r.nextInt(2);
            int kindOfStock = r.nextInt(4);

            sendNews(kindOfNews, kindOfStock);
            System.out.println("message sent");
        }
    }

    private static void sendNews(int kindOfNews, int kindOfStock)  throws JMSException
    {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory("tcp://" + host + ":" + port);

        Connection connection = factory.createConnection(user, password);
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination dest = new ActiveMQTopic(destination);
        MessageProducer producer = session.createProducer(dest);
        producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
        TextMessage msg = session.createTextMessage(prepareNews(kindOfNews, kindOfStock));
        msg.setIntProperty("id", ++ID);
        msg.setStringProperty("stock", getStockName(kindOfStock));
        producer.send(msg);
        producer.send(session.createTextMessage("SHUTDOWN"));
        connection.close();
    }

    private static String prepareNews(int kindOfNews, int kindOfStock)
    {
        if (kindOfNews == 1) {
            return "Bad news about " + getStockName(kindOfStock);
        }

        return "Good news about " + getStockName(kindOfStock);
    }

    private static String getStockName(int stockId)
    {
        switch (stockId) {
            case 0:
                return Stock.AAPL.toString();
            case 1:
                return Stock.ORCL.toString();
            case 2:
                return Stock.MSFT.toString();
            case 3:
                return Stock.IBM.toString();
            default:
                return "";
        }
    }

    private static String env(String key, String defaultValue) {
        String rc = System.getenv(key);
        if( rc== null )
            return defaultValue;
        return rc;
    }

    private static String arg(String []args, int index, String defaultValue) {
        if( index < args.length )
            return args[index];
        else
            return defaultValue;
    }

}
