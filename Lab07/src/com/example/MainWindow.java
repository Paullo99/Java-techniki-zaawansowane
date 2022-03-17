package com.example;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import com.example.daoImpl.ChargeDao;
import com.example.daoImpl.InstallationDao;
import com.example.daoImpl.PaymentDao;
import com.example.guiViews.ChargeManagementWindow;
import com.example.guiViews.ClientsManagementWindow;
import com.example.guiViews.InstallationManagementWindow;
import com.example.guiViews.PaymentManagementWindow;
import com.example.guiViews.PriceListManagementWindow;
import com.example.models.Charge;
import com.example.models.Client;
import com.example.models.Installation;
import com.example.models.Payment;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.List;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.Font;

public class MainWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private LocalDate currentDate;
	private JTextArea chargesTextArea;
	private JTextField dateTextField;
	private ChargeDao chargeDao;
	private PaymentDao paymentDao;
	private InstallationDao installationDao;
	private int dayAmount = 0;
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow frame = new MainWindow();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainWindow() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setBounds(100, 100, 694, 521);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		currentDate = LocalDate.of(2021, 05, 14);
		chargeDao = new ChargeDao();
		paymentDao = new PaymentDao();
		installationDao = new InstallationDao();

		JButton clientManagementBtn = new JButton("Zarz\u0105dzaj klientami");
		clientManagementBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new ClientsManagementWindow();
			}
		});
		clientManagementBtn.setBounds(10, 11, 175, 23);
		contentPane.add(clientManagementBtn);

		JButton priceListManagementBtn = new JButton("Zarz\u0105dzaj cennikiem");
		priceListManagementBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new PriceListManagementWindow();
			}
		});
		priceListManagementBtn.setBounds(10, 48, 175, 23);
		contentPane.add(priceListManagementBtn);

		JButton installationManagementBtn = new JButton("Zarz\u0105dzaj instalacjami");
		installationManagementBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new InstallationManagementWindow();
			}
		});
		installationManagementBtn.setBounds(10, 90, 175, 23);
		contentPane.add(installationManagementBtn);

		JButton paymentManagementBtn = new JButton("Zarz\u0105dzaj p\u0142atno\u015Bciami");
		paymentManagementBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new PaymentManagementWindow();
			}
		});
		paymentManagementBtn.setBounds(10, 133, 175, 23);
		contentPane.add(paymentManagementBtn);

		JButton chargeManagementBtn = new JButton("Zarz\u0105dzaj nale\u017Cno\u015Bciami");
		chargeManagementBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new ChargeManagementWindow();
			}
		});
		chargeManagementBtn.setBounds(10, 177, 175, 23);
		contentPane.add(chargeManagementBtn);

		chargesTextArea = new JTextArea();
		chargesTextArea.setEditable(false);
		JScrollPane scroll = new JScrollPane (chargesTextArea, 
				   JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scroll.setBounds(209, 45, 442, 160);
		contentPane.add(scroll);

		JLabel lblNewLabel = new JLabel("Automatycznie naliczone nale\u017Cno\u015Bci: ");
		lblNewLabel.setBounds(209, 20, 226, 14);
		contentPane.add(lblNewLabel);

		dateTextField = new JTextField();
		dateTextField.setEditable(false);
		dateTextField.setFont(new Font("Tahoma", Font.PLAIN, 16));
		dateTextField.setBounds(526, 445, 125, 26);
		contentPane.add(dateTextField);
		dateTextField.setColumns(10);
		
		JTextArea debtorsTextArea = new JTextArea();
		debtorsTextArea.setEditable(false);
		JScrollPane scroll2 = new JScrollPane (debtorsTextArea, 
				   JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scroll2.setBounds(25, 252, 626, 160);
		contentPane.add(scroll2);
		
		JLabel lblOsobyZaduone = new JLabel("Osoby zad\u0142u\u017Cone:");
		lblOsobyZaduone.setBounds(25, 227, 226, 14);
		contentPane.add(lblOsobyZaduone);
		
		JButton btnStart = new JButton("START");
		JButton btnPause = new JButton("PAUZA");
		btnPause.setEnabled(false);
		btnPause.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(dayAmount == 1) {
					dayAmount = 0;
					btnStart.setEnabled(true);
					btnPause.setEnabled(false);
				}
			}
		});
		btnPause.setBounds(420, 449, 84, 23);
		contentPane.add(btnPause);
		
		
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(dayAmount == 0) {
					dayAmount = 1;
					btnPause.setEnabled(true);
					btnStart.setEnabled(false);
				}
			}
		});
		btnStart.setBounds(329, 449, 81, 23);
		contentPane.add(btnStart);

		new Thread(new Runnable() {

			@Override
			public void run() {
				while (true) {
					
					List<Charge> charges = chargeDao.getAll();
					List<Payment> payments = paymentDao.getAll();
			
					for(Charge c : charges) {
						
						float paymentSum = 0;
						float chargeSum = 0;

						if(c.getDeadline().equals(currentDate) && dayAmount == 1) {
							
							Installation currInstallation = installationDao.get(c.getInstallation().getInstallationId());
							Client currentClient = currInstallation.getClient();
							
							for(Payment payment : payments) {
								if(payment.getInstallation().getInstallationId() == c.getInstallation().getInstallationId()) {
									paymentSum += payment.getPaymentAmount();
								}
							}
							
							for(Charge ch : charges) {
								if(c.getInstallation().getInstallationId() == ch.getInstallation().getInstallationId()) {
									chargeSum += ch.getAmount();
								}
							}
							
							if(chargeSum > paymentSum) {
								debtorsTextArea.append(currentClient.getFirstName() 
							+ " "+ currentClient.getSurname() + " (" +currInstallation.getCity() 
							+ ", " + currInstallation.getAddress() + ")" + " ma d³ug w wysokoœci: " + (chargeSum - paymentSum) + "\n");
							}
														
							Charge nextCharge = new Charge(
									currentDate.plusMonths(1), 
									currInstallation.getPriceList().getPrice(), 
									c.getInstallation());
							
							chargeDao.add(nextCharge);
							
							chargesTextArea.append("Naliczono nale¿noœæ dla: " + currentClient.getFirstName() 
							+ " "+ currentClient.getSurname() + " --> "+ nextCharge.getAmount() +"\n");
						}
					}
					
					currentDate = currentDate.plusDays(dayAmount);
					dateTextField.setText(currentDate.toString());

					try {
						Thread.sleep(1200);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

			}
		}).start();

	}
}
