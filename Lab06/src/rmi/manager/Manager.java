package rmi.manager;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import bilboards.IBillboard;
import bilboards.IManager;
import bilboards.Order;
import javax.swing.JTable;

public class Manager extends JFrame implements IManager{

	private static final long serialVersionUID = 1L;
	private boolean setupReady = false;
	
	private int registryPort = 1000;
	private int managerPort = 2000;
	private String managerRMIName = "RMI-Manager";
	private IManager managerInt;
	
	private int billboardsId = -1;
	private int orderId = -1;
	private HashMap<Integer, IBillboard> billboardHashMap;
	private HashMap<Integer, Order> orderHashMap;
	
	
	private JPanel contentPane;
	private JTable billboardTable;
	private DefaultTableModel billboardsTableModel;

	public static void main(String[] args) {
		System.setProperty("java.security.policy", "./java.policy");
		System.setSecurityManager(new SecurityManager());
		new Manager();
	}

	public Manager() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 399, 411);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		orderHashMap = new HashMap<>();
		
		JScrollPane scrollPane = new JScrollPane();
		billboardsTableModel = new DefaultTableModel();
		billboardTable = new JTable(billboardsTableModel);
		scrollPane.setBounds(10, 11, 363, 294);
		scrollPane.setViewportView(billboardTable);
		
		billboardsTableModel.setColumnIdentifiers(new Object [] {"Billboard", "Pojemność", "Wolne miejsca"});
		
		JButton unbindBillboardButton = new JButton("Odłącz billboard");
		unbindBillboardButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(billboardTable.getSelectedRow()>=0) {
					int id = (Integer) billboardsTableModel.getValueAt(billboardTable.getSelectedRow(), 0);
					billboardsTableModel.removeRow(billboardTable.getSelectedRow());
					IBillboard b = billboardHashMap.remove(id);
					try {
						b.stop();
					} catch (RemoteException e1) {
					}
				}
			}
		});
		unbindBillboardButton.setBounds(255, 324, 118, 23);
		contentPane.add(unbindBillboardButton);
		contentPane.add(scrollPane);	
	
		setupManagerRMI();
		billboardHashMap = new HashMap<>();
		
		while(!setupReady) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		register();
				
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				while(true) {
					for(int i =0; i<billboardsTableModel.getRowCount(); i++) {
						int id = (Integer) billboardsTableModel.getValueAt(i, 0);
							try {
								billboardsTableModel.setValueAt(Integer.valueOf(billboardHashMap.get(id).getCapacity()[1]), i, 2);
							} catch (RemoteException e) {

							}
					}
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
				}
				
			}
		}).start();
	}

	@Override
	public int bindBillboard(IBillboard billboard) throws RemoteException {
		billboardsId++;
		billboardHashMap.put(billboardsId, billboard);
		billboardsTableModel.addRow(new Object [] {billboardsId, billboard.getCapacity()[0], billboard.getCapacity()[1]});
		billboard.start();
		return billboardsId;
	}

	@Override
	public boolean unbindBillboard(int billboardId) throws RemoteException {
		if(billboardHashMap.containsKey(billboardId)) {
			billboardHashMap.remove(billboardId);
		}
		return true;
	}

	@Override
	public boolean placeOrder(Order order) throws RemoteException {
		orderId++;
		int counterSucceed = 0;
		order.client.setOrderId(orderId);
		orderHashMap.put(orderId, order);
		IBillboard billboard;
		for(int i=0; i<billboardsTableModel.getRowCount(); i++) {
			billboard=billboardHashMap.get(billboardsTableModel.getValueAt(i, 0));
			boolean check = billboard.addAdvertisement(order.advertText, order.displayPeriod, orderId);
			if(check)
				counterSucceed++;
		}
		if(counterSucceed == 0)
			return false;
		return true;
	}

	@Override
	public boolean withdrawOrder(int orderId) throws RemoteException {
		if(orderHashMap.get(orderId) == null)
			return false;
		IBillboard billboard;
		for(int i=0; i<billboardsTableModel.getRowCount(); i++) {
			billboard=billboardHashMap.get(billboardsTableModel.getValueAt(i, 0));
			boolean check = billboard.removeAdvertisement(orderId);
			if(!check)
				return false;
		}

		return true;
	}
	
	private void register() {
		try {
			Registry registry = LocateRegistry.createRegistry(registryPort);
			managerInt = (IManager) UnicastRemoteObject.exportObject(this, managerPort);
			registry.bind(managerRMIName, managerInt);
		} catch (RemoteException | AlreadyBoundException e) {
			 this.dispose();
			 JOptionPane.showMessageDialog(null,"Wystąpił problem z RMI", "ERROR", JOptionPane.ERROR_MESSAGE);
			
		}
	}
	
	private void setupManagerRMI() {
		JFrame setupManagerFrame = new JFrame();
		setupManagerFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setupManagerFrame.setTitle("Manager-🔑");
		setupManagerFrame.setBounds(100, 100, 281, 157);
		setupManagerFrame.setLocationRelativeTo(null);
		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setupManagerFrame.setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel rmiPortLabel = new JLabel("Port RMI:");
		rmiPortLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		rmiPortLabel.setFont(new Font("Tahoma", Font.PLAIN, 13));
		rmiPortLabel.setBounds(10, 11, 114, 14);
		contentPane.add(rmiPortLabel);
		
		JLabel managerPortLabel = new JLabel("Port Managera:");
		managerPortLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		managerPortLabel.setFont(new Font("Tahoma", Font.PLAIN, 13));
		managerPortLabel.setBounds(10, 33, 114, 20);
		contentPane.add(managerPortLabel);
		
		JLabel managerNameLabel = new JLabel("Nazwa Managera:");
		managerNameLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		managerNameLabel.setFont(new Font("Tahoma", Font.PLAIN, 13));
		managerNameLabel.setBounds(10, 58, 114, 20);
		contentPane.add(managerNameLabel);
		
		JTextField rmiPortTF = new JTextField();
		rmiPortTF.setText(String.valueOf(this.registryPort));
		rmiPortTF.setBounds(134, 9, 121, 20);
		contentPane.add(rmiPortTF);
		rmiPortTF.setColumns(10);
		
		JTextField managerPortTF = new JTextField();
		managerPortTF.setText(String.valueOf(managerPort));
		managerPortTF.setColumns(10);
		managerPortTF.setBounds(134, 34, 121, 20);
		contentPane.add(managerPortTF);
		
		JTextField managerNameTF = new JTextField();
		managerNameTF.setText(String.valueOf(managerRMIName));
		managerNameTF.setColumns(10);
		managerNameTF.setBounds(134, 59, 121, 20);
		contentPane.add(managerNameTF);
		
		
		rmiPortTF.setEditable(false);
		managerNameTF.setEditable(false);
		managerPortTF.setEditable(false);
		
		JButton editBtn = new JButton("Edytuj");
		editBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				rmiPortTF.setEditable(true);
				managerNameTF.setEditable(true);
				managerPortTF.setEditable(true);
			}
		});
		editBtn.setFont(new Font("Tahoma", Font.PLAIN, 13));
		editBtn.setBounds(117, 90, 69, 23);
		contentPane.add(editBtn);
		JButton btnOk = new JButton("OK");
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				registryPort = Integer.parseInt(rmiPortTF.getText());
				managerPort =Integer.parseInt(managerPortTF.getText()); 
				managerRMIName = managerNameTF.getText();
				setupManagerFrame.dispose();
				setupReady = true;
				setVisible(true);
			}
		});
		btnOk.setFont(new Font("Tahoma", Font.PLAIN, 13));
		btnOk.setBounds(196, 90, 59, 23);
		contentPane.add(btnOk);
		
		setupManagerFrame.setVisible(true);
	}
}
