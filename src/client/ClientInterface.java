package client;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.UnknownHostException;

import javax.swing.*;

import models.trade.Stock;


public class ClientInterface extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;

	private static Client client;

	JPanel jp = new JPanel(new BorderLayout());
	JPanel jp1 = new JPanel();
	JPanel jp2 = new JPanel();
	JPanel jp12 = new JPanel();
	JPanel jp3 = new JPanel();

	JLabel jlb1 = new JLabel("Stock:");
	JLabel jlb2 = new JLabel("Quantity:");
	JLabel jlb3 = new JLabel("Price:");
	JLabel jlb4 = new JLabel("Action:");

	static JTextArea jta = new JTextArea(5,40);
	JTextField jtf1 = new JTextField(5);
	JTextField jtf2 = new JTextField(5);
	JRadioButton jrb1 = new JRadioButton("SELL");
	JRadioButton jrb2 = new JRadioButton("BUY");
	JButton jb = new JButton("GO!");
	
	JComboBox jcb = new JComboBox(Stock.values());
	JScrollPane jsp = new JScrollPane( jta );
	ButtonGroup bg = new ButtonGroup();


	public static void main(String[] args) throws UnknownHostException, IOException  {

		JFrame jf = new ClientInterface("Client Interface");
		String name = JOptionPane.showInputDialog(jf, "Username? (empty for anonymous)", "Client connection", JOptionPane.QUESTION_MESSAGE);

		if(name.equals("")) {
			client = new Client(name);
			jta.setText("Connecting to server in anonymous mode ...\n");
		} else {
			client = new Client(name);
			jta.setText("Connecting to server with username: " + name + "...\n");
		}	
		
		jf.setResizable(false);
		jf.pack();
		jf.setLocationRelativeTo(null);
		jf.setVisible(true);
		jf.setFocusTraversalKeysEnabled(false);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		jf.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(WindowEvent winEvt) {
				try {
					jta.setText(jta.getText() + "Aplication is closing... \n");
					jta.update(jta.getGraphics());

					client.stop();

					Thread.sleep(500);
					System.exit(0);
				} catch (IOException | InterruptedException e) {
					e.printStackTrace();
				}
			}
		});

		client.init();
	}

	public ClientInterface(String string) {
		super(string);

		jta.setEditable(false);
		jb.addActionListener(this);
		jrb1.setSelected(true);

		//set button group
		bg.add(jrb1);
		bg.add(jrb2);

		//add first panel
		jp1.add(jlb1);
		jp1.add(jcb);


		//add second panel
		jp2.add(jlb4);
		jp2.add(jrb1);
		jp2.add(jrb2);

		//add third panel
		jp3.add(jlb2);
		jp3.add(jtf1);
		jp3.add(jlb3);
		jp3.add(jtf2);
		jp3.add(jb);

		//add main panel
		jp12.add(jp1);
		jp12.add(jp2);
		jp.add(jp12, BorderLayout.NORTH);
		jp.add(jp3, BorderLayout.CENTER);
		jp.add(jsp, BorderLayout.SOUTH);
		add(jp);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == jb) {
			String request = "";
			
			if(jrb1.isSelected()) {
				request = "SELL ";
			} else {
				request = "BUY ";
			}

			//add parameters
			request += jcb.getSelectedItem() + " ";
			request += jtf1.getText() + " ";
			request += jtf2.getText();

			try {
				String response = Client.contactServer(request);
				jta.setText(jta.getText() + response);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			System.out.println(request);
		}
	}
}
