package client;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Locale;
import java.util.Random;

import javax.jms.*;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQTopic;

import models.trade.Stock;
import models.user.User;

public class AcyclicClient {
	
	protected User user;
	
	private static String line;
	private static BufferedReader fromServer;
	private static DataOutputStream toServer;
	private static BufferedReader stdIn;
	
	// IP and port of the server
	private static String serverIPAddress = "localhost";
	private static int serverPort = 9495;
	
	// IP and port of the publisher
	private static String activeMQAddress = "141.100.45.102";
	private static String activeMQPort = "61616";
	
	public AcyclicClient() throws UnknownHostException, IOException {
		user = new User(new Socket(serverIPAddress, serverPort));
	}	
	
	public AcyclicClient(String name) throws UnknownHostException, IOException {
		user = new User(new Socket(serverIPAddress, serverPort), name);
	}
	
	public AcyclicClient(String url, int port) throws UnknownHostException, IOException {
		user = new User(new Socket(url, port));
	}
	
	public AcyclicClient(String url, int port, String name) throws UnknownHostException, IOException {
		user = new User(new Socket(url, port), name);
	}

	public static void main(String[] args) throws Exception {
		Thread t = new Thread() {
			public void run() {
				try {
					String user = env("ACTIVEMQ_USER", "admin");
			        String password = env("ACTIVEMQ_PASSWORD", "password");
			        String host = env("ACTIVEMQ_HOST", activeMQAddress);
			        int port = Integer.parseInt(env("ACTIVEMQ_PORT", activeMQPort));
			        String destination = "event";
			        
			        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory("tcp://" + host + ":" + port);

			        Connection connection = factory.createConnection(user, password);
			        connection.start();
			        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			        Destination dest = new ActiveMQTopic(destination);
			        MessageConsumer consumer = session.createConsumer(dest);
			        
			        while (true) {
			        	Message msg = consumer.receive();
			        	if (msg instanceof TextMessage) {
			        		TextMessage textmsg = (TextMessage)msg;
			        		
			        		if (!textmsg.getText().equals("SHUTDOWN")) {
			            		String body = textmsg.getText();
			            		String kindOfNews = textmsg.getStringProperty("kindOfNews");
			            		String stock = textmsg.getStringProperty("stock");
			            		
			            		System.out.println("\nFROM NewsPublisher: " + body);
			            		
			            		if ("good".equals(kindOfNews)) {
			            			line = "SELL " + stock + " 500 -1";
			            	        toServer.writeBytes(line + '\n');
			            	        receiveResponse();
			            		} else if ("bad".equals(kindOfNews)) {
			            			line = "BUY " + stock + " 500 -1";
			            			toServer.writeBytes(line + '\n');
			            	        receiveResponse();
			            		}
			            		
				        		System.out.print("Enter message for the Server, or end the session with . : ");
			        		}

			        		
			        	}
			        }
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		
		t.start();
		
		AcyclicClient client;
		if (args.length > 0) {
			try {
				String host = args[0];
				int port = Integer.parseInt(args[1]);
				client = new AcyclicClient(host, port);
			} catch (Exception e) {
				client = new AcyclicClient();
			}
		} else {
			client = new AcyclicClient();
		}

		client.init();
		client.start();
		client.stop();
	}
	
	public void init() throws UnknownHostException, IOException {
        // prepare data stream/buffer which will be use by the socket
	    toServer = new DataOutputStream( user.getSocket().getOutputStream() );
	    fromServer = new BufferedReader( new InputStreamReader(user.getSocket().getInputStream()) );
	    stdIn = new BufferedReader(new InputStreamReader(System.in));
	}
	
	public void start() throws IOException {
		while (sendRequest()) {              // Send requests while connected
			receiveResponse();               // Process server's answer
		}
	}
	
	public void stop() throws IOException {
	    user.getSocket().close();
	    toServer.close();
	    fromServer.close();
	}
	
	private static boolean sendRequest() throws IOException {
		boolean holdTheLine = true;          // Connection exists
		System.out.print("Enter message for the Server, or end the session with . : ");
		line = stdIn.readLine();
		if (line.equals(".")) {              // Does the user want to end the session?
			holdTheLine = false;
			System.out.println("Session closed.");
		} else if (line.equals("bot")) {
            System.out.println("The client is becoming a nolife trader");
            while(true) {
                bot();
            }
        } else {
            toServer.writeBytes( line + '\n' );
        }
		return holdTheLine;
	}
	
	public static String contactServer(String request) throws IOException {         // Connection exists
		String contact = "Send to server: '" + request + "'...\n";
        toServer.writeBytes( request + '\n' );
        contact += responseServer() + '\n';
		return contact;
	}

	private static void receiveResponse() throws IOException {
		System.out.println("Server answers: " + new String(fromServer.readLine()) );
	}

	private static String responseServer() throws IOException {
		return "Recieve from server: " + new String(fromServer.readLine());
	}

    /**
     * Manage the client as a bot which send random ask/bid for a random quantity/amount on random stock
     * @throws IOException
     */
    public static void bot() throws IOException
    {
        Random random = new Random();
        int action = random.nextInt(2); // we can have to kind of action : BUY or SELL
        int stock = random.nextInt(4); // we can have 4 kind of stock
        int quantity = random.nextInt(99998) + 1; // generate a random quantity between 1 and 99999
        double price = random.nextDouble() * (random.nextInt(998) + 1); // generate a random amount between 1.00 and 999.99

        // prepare the transaction which will be made by the bot
        // force the number used format to be ENGLISH decimal instead user locale format
        line = String.format(Locale.ENGLISH, "%s %s %d %.2f",
                getAction(action),
                getStock(stock),
                quantity,
                price);

        // send the transaction the the server
        toServer.writeBytes( line + '\n' );
        try {
            // our trader is tired, wait 10 seconds
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            System.out.println("The bot is becoming crazy ! " + e);
        }
    }

    /**
     * Able to find a specific Stock name based on a random id
     * @param stockId The enum value rank
     * @return The stock value if it was a suitable id, an empty string if the id doesn't match
     */
    private static String getStock(int stockId)
    {
        switch (stockId){
            case 0:
                return Stock.AAPL.toString();
            case 1:
                return Stock.IBM.toString();
            case 2:
                return Stock.MSFT.toString();
            case 3:
                return Stock.ORCL.toString();
            default:
                return "";
        }
    }

    /**
     * Return a string based on the action ID
     * @param action The action id for which we want the String value
     *               1 mean BUY
     *               other mean SELL
     * @return BUY or SELL
     */
    private static String getAction(int action)
    {
        if (action == 1)
            return "BUY";

        return "SELL";
    }

	/** getters **/
	public User getUser() {
		return user;
	}
	
	/** setters **/
	public void setUser(User usr) {
		user = usr;
	}
	
    private static String env(String key, String defaultValue) {
        String rc = System.getenv(key);
        if( rc== null )
            return defaultValue;
        return rc;
    }
}
