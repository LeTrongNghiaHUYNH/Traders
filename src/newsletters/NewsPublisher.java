package newsletters;

import models.trade.Stock;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQTopic;
import javax.jms.*;

import server.log.LogType;
import server.log.Logger;

import java.util.Random;

/**
 * Created by warlof on 02/03/2016.
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
            // determine random values in order to write random news
            Random r = new Random();
            int kindOfNews = r.nextInt(2);
            int kindOfStock = r.nextInt(4);
            // send the news to the MQ server and log the query
            sendNews(kindOfNews, kindOfStock);
            Logger.write(LogType.debug, String.format("A news has been wrote : %s",
                    prepareNews(kindOfNews, kindOfStock)));

            // send our journalist to the coffee machine
            // maybe he's looking for new kind of news too
            try {
                Thread.sleep(5000); // wait for 10 minutes
            } catch (InterruptedException e) {
                System.out.println("Our journalist is sick :( " + e);
            }
        }
    }

    /**
     * Able to send a news to an MQ daemon using "event" channel
     * @param kindOfNews Determine what kind of news we want to write.
     *                   1 means "Bad news"
     *                   2 means "Good news"
     * @param kindOfStock Determine what kind of stock we want our news reliable about
     * @throws JMSException
     */
    private static void sendNews(int kindOfNews, int kindOfStock)  throws JMSException
    {
        // open a socket to the MQ daemon
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory("tcp://" + host + ":" + port);
        Connection connection = factory.createConnection(user, password);
        connection.start();

        // register our-self to the MQ daemon
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        // determine the channel on which we want to send our news
        Destination dest = new ActiveMQTopic(destination);
        // sign in on the specified MQ channel
        MessageProducer producer = session.createProducer(dest);
        producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
        // prepare our news
        TextMessage msg = session.createTextMessage(prepareNews(kindOfNews, kindOfStock));
        // add two property in order to make our news sortable
        // the id is the news id which has been wrote (every wrote news increase this ID by one)
        msg.setIntProperty("id", ++ID);
        // the stock is the stock about which is our news related to
        msg.setStringProperty("stock", getStockName(kindOfStock));
        // the kind of news
        if (kindOfNews == 1) {
            msg.setStringProperty("kindOfNews", "bad");
        } else {
            msg.setStringProperty("kindOfNews", "good");
        }
        // send our news to the MQ daemon
        producer.send(msg);
        producer.send(session.createTextMessage("SHUTDOWN"));
        // disconnect our-self
        connection.close();
    }

    /**
     * Prepare a news string based on random integers
     * @param kindOfNews Determine what kind of news we want to write.
     *                   1 means "Bad news"
     *                   2 means "Good news"
     * @param kindOfStock Determine what kind of stock we want our news reliable about
     * @return A news string well formatted
     */
    private static String prepareNews(int kindOfNews, int kindOfStock)
    {
        if (kindOfNews == 1) {
            return "Bad news about " + getStockName(kindOfStock);
        }

        return "Good news about " + getStockName(kindOfStock);
    }

    /**
     * Able to find a specific Stock name based on a random id
     * @param stockId The enum value rank
     * @return The stock value if it was a suitable id, an empty string if the id doesn't match
     */
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
