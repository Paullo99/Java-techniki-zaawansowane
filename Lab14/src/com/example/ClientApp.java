package com.example;

import java.io.IOException;

import javax.management.InstanceNotFoundException;
import javax.management.JMX;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.Notification;
import javax.management.NotificationListener;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JList;
import java.awt.TextArea;
import java.awt.Color;

public class ClientApp extends JFrame implements NotificationListener {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private DefaultListModel<Case> defaultListModel;
	private QueueManagementMXBean proxy;
	private JTextField portTF;
	private int currentId = 4;
	private JList<Case> list;
	private Case[] cases;
	private TextArea notificationsTextArea;

	public static void main(String[] args) {

		ClientApp frame = new ClientApp();
		frame.setVisible(true);
	}

	public ClientApp() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 326, 561);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblNewLabel = new JLabel("Port:");
		lblNewLabel.setBounds(60, 15, 46, 14);
		contentPane.add(lblNewLabel);

		portTF = new JTextField();
		portTF.setText("8008");
		portTF.setBounds(103, 12, 54, 20);
		contentPane.add(portTF);
		portTF.setColumns(10);

		defaultListModel = new DefaultListModel<>();
		list = new JList<Case>(defaultListModel);
		list.setBounds(60, 55, 201, 166);
		contentPane.add(list);

		JButton addCaseButton = new JButton("Dodaj spraw\u0119");
		addCaseButton.setEnabled(false);
		addCaseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addCase();
			}
		});
		addCaseButton.setBounds(88, 243, 130, 23);
		contentPane.add(addCaseButton);

		JButton editCaseButton = new JButton("Edytuj spraw\u0119");
		editCaseButton.setEnabled(false);
		editCaseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				editCase();
			}
		});
		editCaseButton.setBounds(88, 277, 130, 23);
		contentPane.add(editCaseButton);

		JButton deleteCaseButton = new JButton("Usu\u0144 spraw\u0119");
		deleteCaseButton.setEnabled(false);
		deleteCaseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				deleteCase();
			}
		});
		deleteCaseButton.setBounds(88, 311, 130, 23);
		contentPane.add(deleteCaseButton);

		JButton connectButton = new JButton("Po\u0142\u0105cz");
		connectButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					connect();
					JOptionPane.showMessageDialog(null, "Uda³o siê poprawnie pod³¹czyæ!");
					connectButton.setEnabled(false);
					portTF.setEnabled(false);
					deleteCaseButton.setEnabled(true);
					editCaseButton.setEnabled(true);
					addCaseButton.setEnabled(true);
					cases = proxy.getAllCases();
					for (Case c : cases) {
						defaultListModel.addElement(c);
					}
				} catch (MalformedObjectNameException | IOException | InstanceNotFoundException e1) {
					JOptionPane.showMessageDialog(null, "B³¹d!");
				}
			}
		});
		connectButton.setBounds(172, 11, 89, 23);
		contentPane.add(connectButton);

		notificationsTextArea = new TextArea();
		notificationsTextArea.setBackground(Color.WHITE);
		notificationsTextArea.setEditable(false);
		notificationsTextArea.setBounds(10, 352, 290, 160);
		contentPane.add(notificationsTextArea);
	}

	public void connect() throws MalformedObjectNameException, IOException, InstanceNotFoundException {
		int jmxPort = 8008;
		JMXServiceURL target = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://localhost:" + jmxPort + "/jmxrmi");
		JMXConnector connector = JMXConnectorFactory.connect(target);
		MBeanServerConnection mbs = connector.getMBeanServerConnection();
		ObjectName name = new ObjectName("com.example:name=QueueManagementApp");
		proxy = JMX.newMXBeanProxy(mbs, name, QueueManagementMXBean.class);
		mbs.addNotificationListener(name, this, null, null);
	}

	private void addCase() {
		JTextField nameField = new JTextField();
		JTextField priorityField = new JTextField();
		JTextField symbolField = new JTextField();
		Object[] componentsObjects = { "Nazwa: ", nameField, "Priorytet: ", priorityField, "Symbol: ", symbolField };
		JOptionPane.showMessageDialog(null, componentsObjects, "Dodaj sprawê", JOptionPane.INFORMATION_MESSAGE);

		try {

			String name = nameField.getText();
			int priority = Integer.parseInt(priorityField.getText());
			char symbol = symbolField.getText().charAt(0);
			Case newCase = new Case(currentId, name, priority, symbol);
			currentId++;

			proxy.addCase(newCase);
			defaultListModel.addElement(newCase);
			cases = proxy.getAllCases();

		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null, "B³¹d wprowadzonych danych.");
		}
	}

	private void editCase() {
		if (list.getSelectedIndex() >= 0) {
			JTextField nameField = new JTextField(defaultListModel.get(list.getSelectedIndex()).getName());
			JTextField priorityField = new JTextField(
					String.valueOf(defaultListModel.get(list.getSelectedIndex()).getPriority()));
			JTextField symbolField = new JTextField(
					String.valueOf(defaultListModel.get(list.getSelectedIndex()).getSymbol()));
			Object[] componentsObjects = { "Nazwa: ", nameField, "Priorytet: ", priorityField, "Symbol: ",
					symbolField };
			JOptionPane.showMessageDialog(null, componentsObjects, "Edytuj sprawê", JOptionPane.INFORMATION_MESSAGE);

			try {
				int priority = Integer.parseInt(priorityField.getText());
				char symbol = symbolField.getText().charAt(0);
				Case newCase = new Case(list.getSelectedValue().getId(), nameField.getText(), priority, symbol);
				proxy.editCase(newCase);
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(null, "B³¹d wprowadzonych danych.");
			}

			cases = proxy.getAllCases();
			
			defaultListModel.removeAllElements();
			for (Case c : cases) 
				defaultListModel.addElement(c);
		}

	}

	private void deleteCase() {
		if (list.getSelectedIndex() >= 0) {
			proxy.removeCase(list.getSelectedValue());
			defaultListModel.remove(list.getSelectedIndex());
			cases = proxy.getAllCases();
		}
	}

	@Override
	public void handleNotification(Notification notification, Object handback) {

		notificationsTextArea.append(notification.getMessage() + "\n");
		cases = proxy.getAllCases();
		defaultListModel.removeAllElements();
		for (Case c : cases) {
			defaultListModel.addElement(c);
		}
	}
}
