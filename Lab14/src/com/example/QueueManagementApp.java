package com.example;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import javax.swing.JLabel;
import java.awt.Color;
import javax.swing.JTextField;
import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanException;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.Notification;
import javax.management.NotificationBroadcasterSupport;
import javax.management.ObjectName;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import java.awt.Font;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.JList;
import javax.swing.JOptionPane;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/*
-Dcom.sun.management.jmxremote
-Dcom.sun.management.jmxremote.port=8008
-Dcom.sun.management.jmxremote.authenticate=false
-Dcom.sun.management.jmxremote.ssl=false
 */
public class QueueManagementApp extends NotificationBroadcasterSupport implements QueueManagementMXBean {

	private JPanel contentPane;
	private JTextField line;
	private JTextField currentNumberTF;
	private DefaultListModel<Case> defaultListModel;
	private ArrayList<Case> caseArrayList = new ArrayList<>();
	private ArrayList<Ticket> ticketsArrayList = new ArrayList<>();
	private ArrayList<Character> symbolsAndProbabilityArrayList = new ArrayList<>();
	private int currentId = 4;
	private JList<Case> casesList;
	private JScrollPane jScrollPane;
	private AtomicInteger sequence = new AtomicInteger();
	private JTextField textField;

	public static void main(String[] args) {
		new QueueManagementApp();
	}

	public QueueManagementApp() {
		JFrame frame = new JFrame();
		frame.setTitle("QueueManagementApp");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBounds(100, 100, 536, 273);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		frame.setContentPane(contentPane);
		contentPane.setLayout(null);

		line = new JTextField();
		line.setEditable(false);
		line.setBackground(Color.DARK_GRAY);
		line.setBounds(210, -7, 4, 268);
		contentPane.add(line);
		line.setColumns(10);

		JLabel lblNewLabel = new JLabel("Wybierz kategori\u0119 sprawy:");
		lblNewLabel.setBounds(10, 11, 178, 14);
		contentPane.add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("Aktualny numerek:");
		lblNewLabel_1.setBounds(224, 30, 107, 14);
		contentPane.add(lblNewLabel_1);

		currentNumberTF = new JTextField();
		currentNumberTF.setEditable(false);
		currentNumberTF.setFont(new Font("Tahoma", Font.BOLD, 16));
		currentNumberTF.setBounds(337, 18, 50, 35);
		contentPane.add(currentNumberTF);
		currentNumberTF.setColumns(10);

		JTextArea queueTextArea = new JTextArea();
		queueTextArea.setWrapStyleWord(true);
		queueTextArea.setLineWrap(true);
		queueTextArea.setEditable(false);
		queueTextArea.setBounds(244, 98, 250, 130);
		contentPane.add(queueTextArea);

		JButton getTicketButton = new JButton("Pobierz bilet");
		getTicketButton.addActionListener(new ActionListener() {
			public synchronized void actionPerformed(ActionEvent e) {

				if (casesList.getSelectedIndex() >= 0) {
					Case currentCase = casesList.getSelectedValue();
					Ticket newTicket = new Ticket(currentCase);
					ticketsArrayList.add(newTicket);

				}
			}
		});
		getTicketButton.setBounds(43, 138, 119, 42);
		contentPane.add(getTicketButton);

		JLabel lblNewLabel_1_1 = new JLabel("Numerki oczekuj\u0105ce w kolejce:");
		lblNewLabel_1_1.setBounds(224, 77, 190, 14);
		contentPane.add(lblNewLabel_1_1);

		JButton addCaseButton = new JButton("+");
		addCaseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addCase();
			}
		});
		addCaseButton.setBounds(10, 205, 41, 23);
		contentPane.add(addCaseButton);

		defaultListModel = new DefaultListModel<>();
		casesList = new JList<Case>(defaultListModel);
		jScrollPane = new JScrollPane(casesList);
		jScrollPane.setBounds(10, 30, 190, 97);
		jScrollPane.setViewportView(casesList);
		contentPane.add(jScrollPane);

		JButton editCaseButton = new JButton("Edytuj");
		editCaseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				editCase();
			}
		});
		editCaseButton.setBounds(61, 205, 81, 23);
		contentPane.add(editCaseButton);

		textField = new JTextField();
		textField.setEditable(false);
		textField.setColumns(10);
		textField.setBackground(Color.DARK_GRAY);
		textField.setBounds(0, 191, 214, 6);
		contentPane.add(textField);

		JButton deleteCaseButton = new JButton("-");
		deleteCaseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				removeCase();
			}
		});
		deleteCaseButton.setBounds(152, 205, 41, 23);
		contentPane.add(deleteCaseButton);

		initializeCaseList();
		register();
		frame.setVisible(true);

		// w¹tek odœwie¿aj¹cy bilety na ekranie
		new Thread(new Runnable() {

			@Override
			public synchronized void run() {

				while (true) {
					queueTextArea.setText("");

					try {
						for (Ticket ticket : ticketsArrayList) {
							queueTextArea
									.append(String.valueOf(ticket.getCase().getSymbol()) + ticket.getNumber() + " ");
						}
						Thread.sleep(200);
					} catch (InterruptedException | ConcurrentModificationException e) {

					}
				}
			}
		}).start();

		// w¹tek odpowiedzialny za pobieranie numerków zgodnie z priorytetami
		new Thread(new Runnable() {

			@Override
			public synchronized void run() {

				while (true) {

					int randomIndex = new Random().nextInt(symbolsAndProbabilityArrayList.size());

					if (ticketsArrayList.size() > 0) {

						for (int i = 0; i < ticketsArrayList.size(); i++) {
							Ticket currentTicket = ticketsArrayList.get(i);
							if (currentTicket.getCase().getSymbol() == symbolsAndProbabilityArrayList
									.get(randomIndex)) {
								ticketsArrayList.remove(i);
								currentNumberTF.setText(String.valueOf(currentTicket.getCase().getSymbol())
										+ currentTicket.getNumber());

								try {
									Thread.sleep(1500);
								} catch (InterruptedException e) {
								}
								break;
							}
						}
					}

					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
					}
					currentNumberTF.setText("");
				}
			}
		}).start();

		// generowanie numerków na poczekaniu
		new Thread(new Runnable() {

			@Override
			public synchronized void run() {
				while (true) {
					int randCase = new Random().nextInt(caseArrayList.size());

					Ticket newTicket = new Ticket(caseArrayList.get(randCase));

					ticketsArrayList.add(newTicket);

					try {
						Thread.sleep(1500);
					} catch (InterruptedException e) {
					}
				}
			}
		}).start();

	}

	@Override
	public synchronized void addCase(Case c) {
		c.setId(currentId);
		defaultListModel.addElement(c);
		caseArrayList.add(c);
		currentId++;

		symbolsAndProbabilityArrayList = new ArrayList<>();
		for (Case cas : caseArrayList) {
			for (int i = 0; i < cas.getPriority(); i++)
				symbolsAndProbabilityArrayList.add(cas.getSymbol());
		}
	}

	@Override
	public synchronized void removeCase(Case t) {

		for (int i = 0; i < defaultListModel.getSize(); ++i) {
			if (defaultListModel.get(i).getId() == t.getId()) {
				defaultListModel.remove(i);
			}
		}
		Case caseToRemove = null;
		for (Case c : caseArrayList) {
			if (t.getId() == c.getId()) {
				caseToRemove = c;
			}
		}
		caseArrayList.remove(caseToRemove);

		symbolsAndProbabilityArrayList = new ArrayList<>();
		for (Case cas : caseArrayList) {
			for (int i = 0; i < cas.getPriority(); i++)
				symbolsAndProbabilityArrayList.add(cas.getSymbol());
		}

		ArrayList<Ticket> temp = new ArrayList<>();

		for (Ticket ticket : ticketsArrayList) {
			if (ticket.getCase().getId() != t.getId())
				temp.add(ticket);
		}

		ticketsArrayList = temp;
	}

	@Override
	public synchronized void editCase(Case t) {
		updateArrayLists(t);
	}

	@Override
	public synchronized Case[] getAllCases() {
		Case[] cases = new Case[caseArrayList.size()];
		for (int i = 0; i < cases.length; i++) {
			cases[i] = caseArrayList.get(i);
		}
		return cases;
	}

	private void register() {
		try {
			ObjectName objName = new ObjectName("com.example:name=QueueManagementApp");
			ManagementFactory.getPlatformMBeanServer().registerMBean(this, objName);
		} catch (InstanceAlreadyExistsException | NotCompliantMBeanException | MalformedObjectNameException
				| MBeanException | NumberFormatException e) {
			e.printStackTrace();
		}
	}

	private void initializeCaseList() {
		caseArrayList.add(new Case(0, "Dowód rejestracyjny", 1, 'A'));
		defaultListModel.addElement(new Case(0, "Dowód rejestracyjny", 1, 'A'));

		caseArrayList.add(new Case(1, "Dowód osobisty", 5, 'B'));
		defaultListModel.addElement(new Case(1, "Dowód osobisty", 5, 'B'));

		caseArrayList.add(new Case(2, "Paszport", 1, 'C'));
		defaultListModel.addElement(new Case(2, "Paszport", 1, 'C'));

		caseArrayList.add(new Case(3, "Prawo jazdy", 2, 'D'));
		defaultListModel.addElement(new Case(3, "Prawo jazdy", 2, 'D'));

		for (Case c : caseArrayList) {
			for (int i = 0; i < c.getPriority(); i++)
				symbolsAndProbabilityArrayList.add(c.getSymbol());
		}
	}

	private void addCase() {
		JTextField nameField = new JTextField();
		JTextField priorityField = new JTextField();
		JTextField symbolField = new JTextField();
		Object[] componentsObjects = { "Nazwa: ", nameField, "Priorytet: ", priorityField, "Symbol: ", symbolField };
		JOptionPane.showMessageDialog(null, componentsObjects, "Dodaj sprawê", JOptionPane.INFORMATION_MESSAGE);

		String name = nameField.getText();
		int priority = Integer.parseInt(priorityField.getText());
		char symbol = symbolField.getText().charAt(0);
		Case newCase = new Case(currentId, name, priority, symbol);
		currentId++;

		defaultListModel.addElement(newCase);
		caseArrayList.add(newCase);

		symbolsAndProbabilityArrayList = new ArrayList<>();
		for (Case cas : caseArrayList) {
			for (int i = 0; i < cas.getPriority(); i++)
				symbolsAndProbabilityArrayList.add(cas.getSymbol());
		}

		Notification notification = new Notification("Notification!", QueueManagementMXBean.class,
				sequence.incrementAndGet(),
				"Dodano now¹ kategoriê spraw: [" + newCase.getSymbol() + "] " + newCase.getName());
		sendNotification(notification);
	}

	private void editCase() {
		if (casesList.getSelectedIndex() >= 0) {
			JTextField nameField = new JTextField(defaultListModel.get(casesList.getSelectedIndex()).getName());
			JTextField priorityField = new JTextField(
					String.valueOf(defaultListModel.get(casesList.getSelectedIndex()).getPriority()));
			JTextField symbolField = new JTextField(
					String.valueOf(defaultListModel.get(casesList.getSelectedIndex()).getSymbol()));
			Object[] componentsObjects = { "Nazwa: ", nameField, "Priorytet: ", priorityField, "Symbol: ",
					symbolField };
			JOptionPane.showMessageDialog(null, componentsObjects, "Edytuj sprawê", JOptionPane.INFORMATION_MESSAGE);

			String name = nameField.getText();
			int priority = Integer.parseInt(priorityField.getText());
			char symbol = symbolField.getText().charAt(0);
			Case newCase = new Case(casesList.getSelectedValue().getId(), name, priority, symbol);

			updateArrayLists(newCase);

			Notification notification = new Notification("Notification!", QueueManagementMXBean.class,
					sequence.incrementAndGet(),
					"Zedytowano kategoriê: [" + newCase.getSymbol() + "] " + newCase.getName());
			sendNotification(notification);
		}

	}

	private void removeCase() {
		if (casesList.getSelectedIndex() >= 0) {
			Case caseToRemove = casesList.getSelectedValue();
			defaultListModel.remove(casesList.getSelectedIndex());

			int idToRemove = -1;

			for (int i = 0; i < caseArrayList.size(); i++) {
				if (caseToRemove.getId() == caseArrayList.get(i).getId())
					idToRemove = i;
			}
			if (idToRemove >= 0)
				caseArrayList.remove(idToRemove);

			symbolsAndProbabilityArrayList = new ArrayList<>();
			for (Case cas : caseArrayList) {
				for (int i = 0; i < cas.getPriority(); i++)
					symbolsAndProbabilityArrayList.add(cas.getSymbol());
			}

			ArrayList<Ticket> temp = new ArrayList<>();

			for (Ticket ticket : ticketsArrayList) {
				if (ticket.getCase().getId() != caseToRemove.getId())
					temp.add(ticket);
			}

			ticketsArrayList = temp;

			Notification notification = new Notification("Notification!", QueueManagementMXBean.class,
					sequence.incrementAndGet(),
					"Usuniêto kategoriê: [" + caseToRemove.getSymbol() + "] " + caseToRemove.getName());
			sendNotification(notification);
		}
	}

	private void updateArrayLists(Case cas) {

		defaultListModel.removeAllElements();
		for (Case c : caseArrayList) {
			if (c.getId() == cas.getId()) {
				c.setName(cas.getName());
				c.setPriority(cas.getPriority());
				c.setSymbol(cas.getSymbol());
			}
			defaultListModel.addElement(c);
		}

		for (Ticket ticket : ticketsArrayList) {
			if (ticket.getCase().getId() == cas.getId())
				ticket.setCase(cas);
		}

		casesList.revalidate();
		casesList.repaint();
		jScrollPane.revalidate();
		jScrollPane.repaint();

		symbolsAndProbabilityArrayList = new ArrayList<>();
		for (Case c : caseArrayList) {
			for (int i = 0; i < c.getPriority(); i++)
				symbolsAndProbabilityArrayList.add(c.getSymbol());
		}
	}

}
