package client;

import java.awt.BorderLayout;

import javax.swing.*;

import models.trade.Stock;


public class ClientInterface extends JFrame {

	private static final long serialVersionUID = 1L;

	JPanel jp = new JPanel(new BorderLayout());
	JPanel jp1 = new JPanel();
	JPanel jp2 = new JPanel();
	JPanel jp12 = new JPanel();
	JPanel jp3 = new JPanel();
	
	JLabel jlb1 = new JLabel("Stock:");
	JLabel jlb2 = new JLabel("Quantité:");
	JLabel jlb3 = new JLabel("Prix:");
	JLabel jlb4 = new JLabel("Action:");
	
	JComboBox jcb = new JComboBox(Stock.values());
	JTextArea jta = new JTextArea(3,30);
	JTextField jtf1 = new JTextField(5);
	JTextField jtf2 = new JTextField(5);
	JRadioButton jrb1 = new JRadioButton("SELL");
	JRadioButton jrb2 = new JRadioButton("BUY");
	JButton jb = new JButton("GO!");
	ButtonGroup bg = new ButtonGroup();

	
	public static void main(String[] args)  {
		
		JFrame jf = new ClientInterface("Client Interface");
		
		jf.setResizable(false);
		jf.pack();
		jf.setLocationRelativeTo(null);
		jf.setVisible(true);
		jf.setFocusTraversalKeysEnabled(false);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public ClientInterface(String string) {
		super(string);
		
		jta.setEditable(false);
		
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
		/*jp.add(jp1, BorderLayout.WEST);
		jp.add(jp2, BorderLayout.EAST);*/
		jp.add(jp12, BorderLayout.NORTH);
		jp.add(jp3, BorderLayout.CENTER);
		jp.add(jta, BorderLayout.SOUTH);
		add(jp);
	}
}
