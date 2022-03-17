package com.example.guiViews;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import com.example.daoImpl.ClientDao;
import com.example.models.Client;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ClientsManagementWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private DefaultTableModel defaultTableModel;
	private JTable table;
	private ClientDao clientDao;
	private JButton addClientButton;
	private JButton updateClientButton;
	private JButton removeClientButton;

	/**
	 * Create the frame.
	 */
	public ClientsManagementWindow() {
		setTitle("Zarz¹dzanie Klientami");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		setBounds(100, 100, 654, 461);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		clientDao = new ClientDao();
		
		JScrollPane scrollPane = new JScrollPane();
		defaultTableModel = new DefaultTableModel();
		table = new JTable(defaultTableModel);
		scrollPane.setBounds(198, 11, 430, 397);
		scrollPane.setViewportView(table);
		
		defaultTableModel.setColumnIdentifiers(new Object [] {"ID", "Imiê", "Nazwisko"});
		contentPane.setLayout(null);
				
		refreshTable();
		
		
		contentPane.add(scrollPane);
		
		addClientButton = new JButton("Dodaj nowego klienta");
		addClientButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JTextField firstNameField = new JTextField();
				JTextField surnameField = new JTextField();
				Object[] fields = {
						"Imiê: ", firstNameField,
						"Nazwisko: ", surnameField
				};
				
				int result = JOptionPane.showConfirmDialog(null, fields, "Wprowadz dane nowego klienta", JOptionPane.OK_CANCEL_OPTION);
				if(result == JOptionPane.OK_OPTION) {
					if(firstNameField.getText().equals("") || surnameField.getText().equals("")) {
						JOptionPane.showMessageDialog(null, "Nie podano wszystkich danych!");
						return;
					}
					clientDao.add(new Client(firstNameField.getText(), surnameField.getText()));
					refreshTable();
						
				}
				
			}
		});
		addClientButton.setBounds(10, 21, 170, 23);
		contentPane.add(addClientButton);
		
		updateClientButton = new JButton("Zaktualizuj dane klienta");
		updateClientButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(table.getSelectedRow() >=0) {
					JTextField firstNameField = new JTextField();
					JTextField surnameField = new JTextField();
					firstNameField.setText(table.getValueAt(table.getSelectedRow(), 1).toString());
					surnameField.setText(table.getValueAt(table.getSelectedRow(), 2).toString());
					Object[] fields = {
							"Imiê: ", firstNameField,
							"Nazwisko: ", surnameField
					};
					
					int result = JOptionPane.showConfirmDialog(null, fields, "Edycja danych klienta", JOptionPane.OK_CANCEL_OPTION);
					if(result == JOptionPane.OK_OPTION) {
						if(firstNameField.getText().equals("") || surnameField.getText().equals("")) {
							JOptionPane.showMessageDialog(null, "Nie podano wszystkich danych!");
							return;
						}
						clientDao.update(new Client((Long)table.getValueAt(table.getSelectedRow(), 0), firstNameField.getText(), surnameField.getText()));
						refreshTable();
							
					}
				}
			}
		});
		updateClientButton.setBounds(10, 65, 170, 23);
		contentPane.add(updateClientButton);
		
		removeClientButton = new JButton("Usu\u0144 klienta");
		removeClientButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(table.getSelectedRow() >=0) {
					
						clientDao.delete(new Client(
								(Long)table.getValueAt(table.getSelectedRow(), 0),
								table.getValueAt(table.getSelectedRow(), 1).toString(),
								table.getValueAt(table.getSelectedRow(), 2).toString()));
						refreshTable();
							
					}
				}
		});
		removeClientButton.setBounds(10, 109, 170, 23);
		contentPane.add(removeClientButton);
		setVisible(true);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void refreshTable() {
		defaultTableModel.getDataVector().removeAllElements();
		List<Client> clients = new ArrayList<>();
		clients = clientDao.getAll();
		for(Client c : clients) {
			defaultTableModel.addRow(new Vector(Arrays.asList(c.getClientId(), c.getFirstName(), c.getSurname())));
		}
	}

}
