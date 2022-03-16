package rmi.client;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.time.Duration;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import bilboards.IClient;
import bilboards.IManager;
import bilboards.Order;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;


public class Client extends JFrame implements IClient{

	private static final long serialVersionUID = 1L;
	private boolean setupReady = false;
	
	private JPanel contentPane;
	
	private IManager managerInt;
	private IClient clientInt = this;
	private int registryPort = 1000;
	private String registryHost = "127.0.0.1";
	private int clientPort = 3000;
	private String managerRMIName = "RMI-Manager";
	private HashMap<Integer, Order> ordersHashMap;
	private int orderId = 0;
	
	private JTable table;
	private DefaultTableModel ordersTableModel;
	private JTextArea textArea;
	private JLabel text;
	private JLabel durationTimeLbl;
	private JTextField durationTimeTF;
	private JButton placeOrderBtn;
	private JButton withdrawOrderBtn;

	public static void main(String[] args) {
		System.setProperty("java.security.policy", "./java.policy");
		System.setSecurityManager(new SecurityManager());
		new Client();
	}


	public Client() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 778, 377);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		ordersHashMap = new HashMap<>();
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(418, 12, 329, 274);
		contentPane.add(scrollPane);
		ordersTableModel = new DefaultTableModel();
		table = new JTable(ordersTableModel);
		
		scrollPane.setViewportView(table);
		
		textArea = new JTextArea();
		textArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
		textArea.setBounds(10, 39, 380, 160);
		contentPane.add(textArea);
		
		text = new JLabel("Tekst ogłoszenia:");
		text.setFont(new Font("Tahoma", Font.PLAIN, 13));
		text.setBounds(10, 12, 117, 23);
		contentPane.add(text);
		
		durationTimeLbl = new JLabel("Czas trwania[s]:");
		durationTimeLbl.setFont(new Font("Tahoma", Font.PLAIN, 13));
		durationTimeLbl.setBounds(10, 219, 100, 23);
		contentPane.add(durationTimeLbl);
		
		durationTimeTF = new JTextField();
		durationTimeTF.setBounds(113, 221, 86, 20);
		contentPane.add(durationTimeTF);
		durationTimeTF.setColumns(10);
		placeOrderBtn = new JButton("Złóż zamówienie");
		withdrawOrderBtn = new JButton("Usuń zamówienie");
		withdrawOrderBtn.setBounds(630, 297, 117, 23);
		contentPane.add(withdrawOrderBtn);
		placeOrderBtn.setBounds(273, 265, 117, 23);
		contentPane.add(placeOrderBtn);
				
		ordersTableModel.setColumnIdentifiers(new Object[] {"OrderID", "Czas trwania", "Tekst"});
		
		setupClientRMI();
		
		while(!setupReady) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		register();		
		
		placeOrderBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Order order = new Order();
				order.advertText = textArea.getText();
				order.client = clientInt;
				order.displayPeriod = Duration.ofSeconds(Integer.parseInt(durationTimeTF.getText()));	
				
				try {
					boolean check =  managerInt.placeOrder(order);
					if(check) {
						ordersHashMap.put(orderId, order);
						ordersTableModel.addRow(new Object[] {orderId, durationTimeTF.getText(), order.advertText});
					}else {
						JOptionPane.showMessageDialog(null, "Nie udało się umieścić zamówienia.", "ERROR", JOptionPane.ERROR_MESSAGE);
					}
				} catch (RemoteException e1) {
					JOptionPane.showMessageDialog(null, "Nie udało się umieścić zamówienia.", "ERROR", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		
		
		
		withdrawOrderBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(table.getSelectedRow()>=0) {
					try {
						if(managerInt.withdrawOrder((Integer)table.getValueAt(table.getSelectedRow(), 0))){
							ordersHashMap.remove((Integer)table.getValueAt(table.getSelectedRow(), 0));
							ordersTableModel.removeRow(table.getSelectedRow());
						}
						else {
							JOptionPane.showMessageDialog(null, "Nie udało się usunąć zamówienia.", "ERROR", JOptionPane.ERROR_MESSAGE);
						}
					} catch (RemoteException e1) {
						JOptionPane.showMessageDialog(null, "Nie udało się usunąć zamówienia.", "ERROR", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		
	}

	@Override
	public void setOrderId(int orderId) throws RemoteException {
		this.orderId = orderId;
	}
	
	public void register() {
		try {
            Registry registry = LocateRegistry.getRegistry(registryHost, registryPort);
            clientInt = (IClient) UnicastRemoteObject.exportObject(this, clientPort);
            managerInt = (IManager) registry.lookup(managerRMIName);
        } catch (RemoteException | NotBoundException e) {
        	this.dispose();
			 JOptionPane.showMessageDialog(null,"Wystąpił problem z RMI", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
	}
	
	public void setupClientRMI() {
		JFrame setupClientFrame = new JFrame(); 
		setupClientFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setupClientFrame.setTitle("Client-🔑");
		setupClientFrame.setBounds(100, 100, 281, 181);
		setupClientFrame.setLocationRelativeTo(null);
		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setupClientFrame.setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblHostRMI = new JLabel("Host RMI:");
		lblHostRMI.setHorizontalAlignment(SwingConstants.RIGHT);
		lblHostRMI.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblHostRMI.setBounds(10, 11, 114, 14);
		contentPane.add(lblHostRMI);
		
		JLabel rmiPortLabel = new JLabel("Port RMI:");
		rmiPortLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		rmiPortLabel.setFont(new Font("Tahoma", Font.PLAIN, 13));
		rmiPortLabel.setBounds(10, 33, 114, 20);
		contentPane.add(rmiPortLabel);
		
		JLabel managerNameLabel = new JLabel("Nazwa Managera:");
		managerNameLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		managerNameLabel.setFont(new Font("Tahoma", Font.PLAIN, 13));
		managerNameLabel.setBounds(10, 58, 114, 20);
		contentPane.add(managerNameLabel);
		
		JLabel clientPortLabel = new JLabel("Port Klienta:");
		clientPortLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		clientPortLabel.setFont(new Font("Tahoma", Font.PLAIN, 13));
		clientPortLabel.setBounds(10, 83, 114, 20);
		contentPane.add(clientPortLabel);
		
		JTextField rmiHostTF = new JTextField();
		rmiHostTF.setBounds(134, 9, 121, 20);
		rmiHostTF.setText(registryHost);
		contentPane.add(rmiHostTF);
		rmiHostTF.setColumns(10);
		
		JTextField rmiPortTF = new JTextField();
		rmiPortTF.setColumns(10);
		rmiPortTF.setText(String.valueOf(registryPort));
		rmiPortTF.setBounds(134, 34, 121, 20);
		contentPane.add(rmiPortTF);
		
		JTextField managerNameTF = new JTextField();
		managerNameTF.setText(managerRMIName);
		managerNameTF.setColumns(10);
		managerNameTF.setBounds(134, 59, 121, 20);
		contentPane.add(managerNameTF);
		
		JTextField clientPortTF = new JTextField();
		clientPortTF.setText(String.valueOf(clientPort));
		clientPortTF.setColumns(10);
		clientPortTF.setBounds(134, 84, 121, 20);
		contentPane.add(clientPortTF);
		
		clientPortTF.setEditable(false);
		rmiHostTF.setEditable(false);
		managerNameTF.setEditable(false);
		rmiPortTF.setEditable(false);
		
		JButton editBtn = new JButton("Edytuj");
		editBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				rmiHostTF.setEditable(true);
				managerNameTF.setEditable(true);
				rmiPortTF.setEditable(true);
				clientPortTF.setEditable(true);
			}
		});
		editBtn.setFont(new Font("Tahoma", Font.PLAIN, 13));
		editBtn.setBounds(116, 114, 69, 23);
		contentPane.add(editBtn);
		
		JButton btnOk = new JButton("OK");
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				registryPort = Integer.parseInt(rmiPortTF.getText());
				clientPort =Integer.parseInt(clientPortTF.getText()); 
				managerRMIName = managerNameTF.getText();
				registryHost = rmiHostTF.getText();
				setupClientFrame.dispose();
				setupReady = true;
				setVisible(true);
			}
		});
		btnOk.setFont(new Font("Tahoma", Font.PLAIN, 13));
		btnOk.setBounds(196, 114, 59, 23);
		contentPane.add(btnOk);	
		
		setupClientFrame.setVisible(true);
	}

}
