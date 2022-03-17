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

import com.example.daoImpl.ChargeDao;
import com.example.daoImpl.InstallationDao;
import com.example.models.Charge;

public class ChargeManagementWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private DefaultTableModel defaultTableModel;
	private JTable table;
	private ChargeDao chargeDao;
	private InstallationDao installationDao;
	private JButton addChargeButton;

	/**
	 * Create the frame.
	 */
	public ChargeManagementWindow() {
		setTitle("Zarz¹dzanie Nale¿noœciami");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		setBounds(100, 100, 651, 461);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		chargeDao = new ChargeDao();
		installationDao = new InstallationDao();

		JScrollPane scrollPane = new JScrollPane();
		defaultTableModel = new DefaultTableModel();
		table = new JTable(defaultTableModel);
		scrollPane.setBounds(198, 11, 414, 397);
		scrollPane.setViewportView(table);

		defaultTableModel.setColumnIdentifiers(new Object[] { "ID", "Termin p³atnoœci", "Kwota", "Installation_id" });
		contentPane.setLayout(null);

		refreshTable();

		contentPane.add(scrollPane);

		addChargeButton = new JButton("Dodaj now¹ nale¿noœæ");
		addChargeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JTextField paymentDateField = new JTextField();
				JTextField paymentAmountField = new JTextField();
				JTextField installationIdField = new JTextField();
				Object[] fields = { "Termin p³atnoœci: ", paymentDateField, "Kwota: ", paymentAmountField,
						"Identyfikator instalacji: ", installationIdField };

				int result = JOptionPane.showConfirmDialog(null, fields, "Wprowadz dane nowej nale¿noœci",
						JOptionPane.OK_CANCEL_OPTION);
				if (result == JOptionPane.OK_OPTION) {
					if (paymentDateField.getText().equals("") || paymentAmountField.getText().equals("")
							|| installationIdField.getText().equals("")) {
						JOptionPane.showMessageDialog(null, "Nie podano wszystkich danych!");
						return;
					}
					chargeDao.add(new Charge(LocalDate.parse(paymentDateField.getText()),
							Float.parseFloat(paymentAmountField.getText()),
							installationDao.get(Integer.parseInt(installationIdField.getText()))));
					refreshTable();

				}

			}
		});
		addChargeButton.setBounds(10, 21, 170, 23);
		contentPane.add(addChargeButton);

		new Thread(new Runnable() {

			@Override
			public void run() {
				while (true) {
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					refreshTable();
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
		setVisible(true);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void refreshTable() {
		defaultTableModel.getDataVector().removeAllElements();
		List<Charge> charges = new ArrayList<>();
		charges = chargeDao.getAll();
		for (Charge c : charges) {
			defaultTableModel.addRow(new Vector(Arrays.asList(c.getChargeId(), c.getDeadline(), c.getAmount(),
					c.getInstallation().getInstallationId())));
		}
	}
}
