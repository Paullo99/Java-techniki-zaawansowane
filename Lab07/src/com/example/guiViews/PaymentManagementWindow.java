package com.example.guiViews;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import com.example.daoImpl.InstallationDao;
import com.example.daoImpl.PaymentDao;
import com.example.models.Payment;

public class PaymentManagementWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private DefaultTableModel defaultTableModel;
	private JTable table;
	private PaymentDao paymentDao;
	private InstallationDao installationDao;
	private JButton addPaymentButton;
	private JButton updatePaymentButton;
	private JButton removePaymentButton;

	/**
	 * Create the frame.
	 */
	public PaymentManagementWindow() {
		setTitle("Zarz¹dzanie P³atnoœciami");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		setBounds(100, 100, 651, 461);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		paymentDao= new PaymentDao();
		installationDao = new InstallationDao();

		
		JScrollPane scrollPane = new JScrollPane();
		defaultTableModel = new DefaultTableModel();
		table = new JTable(defaultTableModel);
		scrollPane.setBounds(198, 11, 414, 397);
		scrollPane.setViewportView(table);
		
		defaultTableModel.setColumnIdentifiers(new Object [] {"ID", "Data p³atnoœci", "Kwota", "Installation_id"});
		contentPane.setLayout(null);
				
		refreshTable();
		
		
		contentPane.add(scrollPane);
		
		addPaymentButton = new JButton("Dodaj now¹ p³atnoœæ");
		addPaymentButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JTextField paymentDateField = new JTextField();
				JTextField paymentAmountField = new JTextField();
				JTextField installationIdField = new JTextField();
				Object[] fields = {
						"Data p³atnoœci: ", paymentDateField,
						"Kwota: ", paymentAmountField,
						"Identyfikator instalacji: ", installationIdField
				};
				
				int result = JOptionPane.showConfirmDialog(null, fields, "Wprowadz dane nowej p³atnoœci", JOptionPane.OK_CANCEL_OPTION);
				if(result == JOptionPane.OK_OPTION) {
					if(paymentDateField.getText().equals("") || paymentAmountField.getText().equals("")
							|| installationIdField.getText().equals("")) {
						JOptionPane.showMessageDialog(null, "Nie podano wszystkich danych!");
						return;
					}
					paymentDao.add(new Payment(LocalDate.parse(paymentDateField.getText()), Float.parseFloat(paymentAmountField.getText()),
							installationDao.get(Integer.parseInt(installationIdField.getText()))));
					refreshTable();
						
				}
				
			}
		});
		addPaymentButton.setBounds(10, 21, 170, 23);
		contentPane.add(addPaymentButton);
		
		updatePaymentButton = new JButton("Zaktualizuj p³atnoœæ");
		updatePaymentButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(table.getSelectedRow() >=0) {
					JTextField paymentDateField = new JTextField();
					JTextField paymentAmountField = new JTextField();
					JTextField installationIdField = new JTextField();
					paymentDateField.setText(table.getValueAt(table.getSelectedRow(), 1).toString());
					paymentAmountField.setText(table.getValueAt(table.getSelectedRow(), 2).toString());
					installationIdField.setText(table.getValueAt(table.getSelectedRow(), 3).toString());
					Object[] fields = {
							"Data p³atnoœci: ", paymentDateField,
							"Kwota: ", paymentAmountField,
							"Identyfikator instalacji: ", installationIdField
					};

				
					int result = JOptionPane.showConfirmDialog(null, fields, "Edycja danych p³atnoœci", JOptionPane.OK_CANCEL_OPTION);
					if(result == JOptionPane.OK_OPTION) {
						if(paymentDateField.getText().equals("") || paymentAmountField.getText().equals("")
								|| installationIdField.getText().equals("")) {
							JOptionPane.showMessageDialog(null, "Nie podano wszystkich danych!");
							return;
						}
						paymentDao.update(new Payment((Long)table.getValueAt(table.getSelectedRow(), 0), LocalDate.parse(paymentDateField.getText()), Float.parseFloat(paymentAmountField.getText()),
								installationDao.get(Integer.parseInt(installationIdField.getText()))));
						refreshTable();
							
					}
			
				}
			}
		});
		updatePaymentButton.setBounds(10, 65, 170, 23);
		contentPane.add(updatePaymentButton);
		
		removePaymentButton = new JButton("Usu\u0144 p³atnoœæ");
		removePaymentButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(table.getSelectedRow() >=0) {
					
						paymentDao.delete(new Payment(
								(Long)table.getValueAt(table.getSelectedRow(), 0),
								LocalDate.parse(table.getValueAt(table.getSelectedRow(), 1).toString()),
								Float.parseFloat(table.getValueAt(table.getSelectedRow(), 2).toString()),
								installationDao.get(Integer.parseInt((table.getValueAt(table.getSelectedRow(), 3).toString())))
								));
						refreshTable();
							
					}
				}
		});
		removePaymentButton.setBounds(10, 109, 170, 23);
		contentPane.add(removePaymentButton);
		setVisible(true);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void refreshTable() {
		defaultTableModel.getDataVector().removeAllElements();
		List<Payment> payments = new ArrayList<>();
		payments = paymentDao.getAll();
		for(Payment p : payments) {
			defaultTableModel.addRow(new Vector(Arrays.asList(p.getPaymentId(), p.getPaymentDate(), 
					p.getPaymentAmount(), p.getInstallation().getInstallationId())));
		}
	}
}