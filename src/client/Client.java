package client;

import java.io.*;
import java.net.*;
import java.util.Locale;
import java.util.Random;

import models.trade.Stock;
import models.user.User;

public class Client {
	
	protected User user;
	
	private static String line;
	private static BufferedReader fromServer;
	private static DataOutputStream toServer;
	private static BufferedReader stdIn;
	
	public Client() throws UnknownHostException, IOException {
		user = new User(new Socket("localhost", 9495));
	}	
	
	public Client(String name) throws UnknownHostException, IOException {
		user = new User(new Socket("localhost", 9495), name);
	}
	
	public Client(String url, int port) throws UnknownHostException, IOException {
		user = new User(new Socket(url, port));
	}
	
	public Client(String url, int port, String name) throws UnknownHostException, IOException {
		user = new User(new Socket(url, port), name);
	}

	 public static void main(String[] args) throws Exception {
		 Client client = new Client();
		 client.init();
		 client.start();
		 client.stop();
	 }
	
	
	public void init() throws UnknownHostException, IOException {
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
	
	private static void receiveResponse() throws IOException {
		System.out.println("Server answers: " + new String(fromServer.readLine()) );
	}

    public static void bot() throws IOException
    {
        Random random = new Random();
        int action = random.nextInt(2);
        int stock = random.nextInt(4);
        int quantity = random.nextInt(99999);
        double price = random.nextDouble() * random.nextInt(999);
        String sAction;
        String sStock = "";

        if (action == 1) {
            sAction = "BUY";
        } else {
            sAction = "SELL";
        }

        switch (stock){
            case 0:
                sStock = Stock.AAPL.toString();
                break;
            case 1:
                sStock = Stock.IBM.toString();
                break;
            case 2:
                sStock = Stock.MSFT.toString();
                break;
            case 3:
                sStock = Stock.ORCL.toString();
                break;
        }

        line = String.format(Locale.ENGLISH, "%s %s %d %.2f",
                sAction,
                sStock,
                quantity,
                price);

        toServer.writeBytes( line + '\n' );
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            System.out.println("The bot is becoming crazy ! " + e);
        }
    }

	/** getters **/
	public User getUser() {
		return user;
	}
	
	/** setters **/
	public void setUser(User usr) {
		user = usr;
	}
}
