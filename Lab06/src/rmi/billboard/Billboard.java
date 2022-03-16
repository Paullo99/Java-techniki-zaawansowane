package rmi.billboard;

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
import java.util.LinkedList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import bilboards.IBillboard;
import bilboards.IManager;
import bilboards.Order;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import java.awt.Color;

public class Billboard extends JFrame implements IBillboard {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private boolean setupReady = false;

	private IBillboard billboardInt;
	private IManager managerInt;
	private int billboardId;
	private int registryPort = 1000;
	private String registryHost = "127.0.0.1";
	private int billboardPort = 4000;
	private String managerRMIName = "RMI-Manager";
	private int capacity = 10;
	private Duration displayInterval = Duration.ofSeconds(3);
	private int currentId;
	private boolean threadRunning = true;

	private Thread displayingThread;

	private LinkedList<Integer> orderIdQueue;
	private HashMap<Integer, Order> adHashMap;

	private JTextArea textArea;

	public static void main(String[] args) {
		new Billboard();
		System.setProperty("java.security.policy", "./java.policy");
		System.setSecurityManager(new SecurityManager());
	}

	public Billboard() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 174);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		orderIdQueue = new LinkedList<>();
		adHashMap = new HashMap<>();

		textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setWrapStyleWord(true);
		textArea.setForeground(new Color(0, 0, 139));
		textArea.setBackground(new Color(211, 211, 211));
		textArea.setFont(new Font("Monospaced", Font.BOLD, 25));
		textArea.setText("Czekam na reklamę...");
		textArea.setBounds(0, 0, 434, 135);
		contentPane.add(textArea);

		setupBillboardRMI();

		while (!setupReady) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		register();

	}

	@Override
	public boolean addAdvertisement(String advertText, Duration displayPeriod, int orderId) throws RemoteException {
		if (adHashMap.size() < capacity) {
			Order order = new Order();
			order.advertText = advertText;
			order.displayPeriod = displayPeriod;
			orderIdQueue.add(orderId);
			adHashMap.put(orderId, order);
			System.out.println("Add adv: " + advertText);
			return true;
		}
		return false;
	}

	@Override
	public boolean removeAdvertisement(int orderId) throws RemoteException {
		if (!adHashMap.containsKey(orderId))
			return false;
		Order order = adHashMap.remove(orderId);
		if (order != null) {
			int help = orderIdQueue.indexOf(orderId);
			orderIdQueue.remove(help);
		}
		return true;
	}

	@Override
	public int[] getCapacity() throws RemoteException {
		return new int[] { capacity, capacity - adHashMap.size() };
	}

	@Override
	public void setDisplayInterval(Duration displayInterval) throws RemoteException {
		this.displayInterval = displayInterval;

	}

	@Override
	public boolean start() throws RemoteException {

		displayingThread = new Thread(new Runnable() {
			Order order;

			@Override
			public void run() {
				while (threadRunning) {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e1) {
					}

					if (orderIdQueue.size() > 0) {
						currentId = orderIdQueue.poll();
						order = adHashMap.get(currentId);
						if (order.displayPeriod.getSeconds() > 0) {
							order.displayPeriod = order.displayPeriod.minus(displayInterval);
							textArea.setText(order.advertText);

							if (order.displayPeriod.getSeconds() > 0) {
								orderIdQueue.add(currentId);
							} else {
								adHashMap.remove(currentId);
							}

							try {
								Thread.sleep(displayInterval.toMillis());
							} catch (InterruptedException e) {
							}

						}
					} else {
						textArea.setText("Czekam na reklamę...");
					}
				}

			}
		});
		displayingThread.start();

		return false;
	}

	@Override
	public boolean stop() throws RemoteException {
		threadRunning = false;
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		for (int i = 3; i > 0; i--) {
			textArea.setText("Billboard zamyka się za: " + String.valueOf(i));
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		managerInt.unbindBillboard(billboardId);
		UnicastRemoteObject.unexportObject(this, true);
		dispose();
		System.exit(0);
		return true;
	}

	public void register() {
		try {
			Registry registry = LocateRegistry.getRegistry(registryHost, registryPort);
			billboardInt = (IBillboard) UnicastRemoteObject.exportObject(this, billboardPort);
			managerInt = (IManager) registry.lookup(managerRMIName);
			billboardId = managerInt.bindBillboard(billboardInt);
		} catch (RemoteException | NotBoundException e) {
			this.dispose();
			JOptionPane.showMessageDialog(null, "Wystąpił problem z RMI", "ERROR", JOptionPane.ERROR_MESSAGE);
		}
	}

	public void setupBillboardRMI() {
		JFrame setupBillboardFrame = new JFrame();
		setupBillboardFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setupBillboardFrame.setTitle("Billboard-🔑");
		setupBillboardFrame.setBounds(100, 100, 294, 239);
		setupBillboardFrame.setLocationRelativeTo(null);
		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setupBillboardFrame.setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel rmiHostLabel = new JLabel("Host RMI:");
		rmiHostLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		rmiHostLabel.setFont(new Font("Tahoma", Font.PLAIN, 13));
		rmiHostLabel.setBounds(20, 13, 114, 14);
		contentPane.add(rmiHostLabel);

		JLabel lblPortRMI = new JLabel("Port RMI:");
		lblPortRMI.setHorizontalAlignment(SwingConstants.RIGHT);
		lblPortRMI.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblPortRMI.setBounds(20, 35, 114, 20);
		contentPane.add(lblPortRMI);

		JLabel managerNameLabel = new JLabel("Nazwa Managera:");
		managerNameLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		managerNameLabel.setFont(new Font("Tahoma", Font.PLAIN, 13));
		managerNameLabel.setBounds(20, 60, 114, 20);
		contentPane.add(managerNameLabel);

		JLabel lblPortBillboard = new JLabel("Port Billboardu:");
		lblPortBillboard.setHorizontalAlignment(SwingConstants.RIGHT);
		lblPortBillboard.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblPortBillboard.setBounds(20, 85, 114, 20);
		contentPane.add(lblPortBillboard);

		JTextField rmiHostTF = new JTextField();
		rmiHostTF.setText(registryHost);
		rmiHostTF.setBounds(144, 11, 121, 20);
		contentPane.add(rmiHostTF);
		rmiHostTF.setColumns(10);

		JTextField rmiPortTF = new JTextField();
		rmiPortTF.setText(String.valueOf(registryPort));
		rmiPortTF.setColumns(10);
		rmiPortTF.setBounds(144, 36, 121, 20);
		contentPane.add(rmiPortTF);

		JTextField managerNameTF = new JTextField();
		managerNameTF.setText(managerRMIName);
		managerNameTF.setColumns(10);
		managerNameTF.setBounds(144, 61, 121, 20);
		contentPane.add(managerNameTF);

		JTextField billboardPortTF = new JTextField();
		billboardPortTF.setText(String.valueOf(billboardPort));
		billboardPortTF.setEditable(false);
		billboardPortTF.setColumns(10);
		billboardPortTF.setBounds(144, 86, 121, 20);
		contentPane.add(billboardPortTF);

		JLabel displayIntervalLabel = new JLabel("Interwał czasowy[s]:");
		displayIntervalLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		displayIntervalLabel.setFont(new Font("Tahoma", Font.PLAIN, 13));
		displayIntervalLabel.setBounds(10, 109, 124, 20);
		contentPane.add(displayIntervalLabel);

		JTextField displayIntervalTF = new JTextField();
		displayIntervalTF.setText(String.valueOf(displayInterval.getSeconds()));
		displayIntervalTF.setColumns(10);
		displayIntervalTF.setBounds(144, 111, 121, 19);
		contentPane.add(displayIntervalTF);

		JTextField capacityTF = new JTextField();
		capacityTF.setText(String.valueOf(capacity));
		capacityTF.setEditable(false);
		capacityTF.setColumns(10);
		capacityTF.setBounds(144, 136, 121, 19);
		contentPane.add(capacityTF);

		JLabel capacityLabel = new JLabel("Pojemność:");
		capacityLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		capacityLabel.setFont(new Font("Tahoma", Font.PLAIN, 13));
		capacityLabel.setBounds(20, 134, 114, 20);
		contentPane.add(capacityLabel);

		billboardPortTF.setEditable(false);
		rmiHostTF.setEditable(false);
		managerNameTF.setEditable(false);
		rmiPortTF.setEditable(false);
		displayIntervalTF.setEditable(false);

		JButton editBtn = new JButton("Edytuj");
		editBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				rmiHostTF.setEditable(true);
				managerNameTF.setEditable(true);
				rmiPortTF.setEditable(true);
				billboardPortTF.setEditable(true);
				capacityTF.setEditable(true);
				displayIntervalTF.setEditable(true);
			}
		});
		editBtn.setFont(new Font("Tahoma", Font.PLAIN, 13));
		editBtn.setBounds(127, 162, 69, 23);
		contentPane.add(editBtn);

		JButton btnOk = new JButton("OK");
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				registryPort = Integer.parseInt(rmiPortTF.getText());
				billboardPort = Integer.parseInt(billboardPortTF.getText());
				managerRMIName = managerNameTF.getText();
				registryHost = rmiHostTF.getText();
				capacity = Integer.parseInt(capacityTF.getText());
				displayInterval = Duration.ofSeconds(Integer.parseInt(displayIntervalTF.getText()));
				setupBillboardFrame.dispose();
				setupReady = true;
				setVisible(true);
			}
		});
		btnOk.setFont(new Font("Tahoma", Font.PLAIN, 13));
		btnOk.setBounds(206, 162, 59, 23);
		contentPane.add(btnOk);
		setupBillboardFrame.setVisible(true);
	}
}
