package com.example.guiViews;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

import com.example.daoImpl.ClientDao;
import com.example.daoImpl.InstallationDao;
import com.example.daoImpl.PriceListDao;
import com.example.models.Installation;

public class InstallationManagementWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private DefaultTableModel defaultTableModel;
	private JTable table;
	private InstallationDao installationDao;
	private ClientDao clientDao;
	private PriceListDao priceListDao;
	private JButton addInstallationButton;
	private JButton updateInstallationButton;
	private JButton removeInstallationButton;

	/**
	 * Create the frame.
	 */
	public InstallationManagementWindow() {
		setTitle("Zarz¹dzanie Instalacjami");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		setBounds(100, 100, 917, 461);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		installationDao= new InstallationDao();
		clientDao = new ClientDao();
		priceListDao = new PriceListDao();
		
		JScrollPane scrollPane = new JScrollPane();
		defaultTableModel = new DefaultTableModel();
		table = new JTable(defaultTableModel);
		scrollPane.setBounds(198, 11, 693, 397);
		scrollPane.setViewportView(table);
		
		defaultTableModel.setColumnIdentifiers(new Object [] {"ID", "Numer routera", "Miasto", "Adres", "Kod pocztowy", "Client_id", "Price_list_id"});
		contentPane.setLayout(null);
				
		refreshTable();
		
		
		contentPane.add(scrollPane);
		
		addInstallationButton = new JButton("Dodaj now¹ instalacjê");
		addInstallationButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JTextField routerNumberField = new JTextField();
				JTextField cityField = new JTextField();
				JTextField addressField = new JTextField();
				JTextField postcodeField = new JTextField();
				JTextField clientIdField = new JTextField();
				JTextField priceListId = new JTextField();
				Object[] fields = {
						"Numer routera: ", routerNumberField,
						"Miasto: ", cityField,
						"Adres: ", addressField,
						"Kod pocztowy: ", postcodeField,
						"Identyfikator klienta: ", clientIdField, 
						"Identyfikator us³ugi: ", priceListId
				};
				
				int result = JOptionPane.showConfirmDialog(null, fields, "Wprowadz dane nowej instalacji", JOptionPane.OK_CANCEL_OPTION);
				if(result == JOptionPane.OK_OPTION) {
					if(routerNumberField.getText().equals("") || cityField.getText().equals("")
							|| addressField.getText().equals("") || postcodeField.getText().equals("") || clientIdField.getText().equals("")
							|| priceListId.getText().equals("")) {
						JOptionPane.showMessageDialog(null, "Nie podano wszystkich danych!");
						return;
					}
					installationDao.add(new Installation(routerNumberField.getText(), cityField.getText(),
							addressField.getText(), postcodeField.getText(), clientDao.get(Integer.parseInt(clientIdField.getText())),
							priceListDao.get(Integer.parseInt(priceListId.getText()))));
					refreshTable();
						
				}
				
			}
		});
		addInstallationButton.setBounds(10, 21, 170, 23);
		contentPane.add(addInstallationButton);
		
		updateInstallationButton = new JButton("Zaktualizuj instalacjê");
		updateInstallationButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(table.getSelectedRow() >=0) {
					JTextField routerNumberField = new JTextField();
					JTextField cityField = new JTextField();
					JTextField addressField = new JTextField();
					JTextField postcodeField = new JTextField();
					JTextField clientIdField = new JTextField();
					JTextField priceListId = new JTextField();
					routerNumberField.setText(table.getValueAt(table.getSelectedRow(), 1).toString());
					cityField.setText(table.getValueAt(table.getSelectedRow(), 2).toString());
					addressField.setText(table.getValueAt(table.getSelectedRow(), 3).toString());
					postcodeField.setText(table.getValueAt(table.getSelectedRow(), 4).toString());
					clientIdField.setText(table.getValueAt(table.getSelectedRow(), 5).toString());
					priceListId.setText(table.getValueAt(table.getSelectedRow(), 6).toString());
					Object[] fields = {
							"Numer routera: ", routerNumberField,
							"Miasto: ", cityField,
							"Adres: ", addressField,
							"Kod pocztowy: ", postcodeField,
							"Identyfikator klienta: ", clientIdField, 
							"Identyfikator us³ugi: ", priceListId
					};
					int result = JOptionPane.showConfirmDialog(null, fields, "Edycja danych instalacji", JOptionPane.OK_CANCEL_OPTION);
					if(result == JOptionPane.OK_OPTION) {
						if(routerNumberField.getText().equals("") || cityField.getText().equals("")
								|| addressField.getText().equals("") || postcodeField.getText().equals("") || clientIdField.getText().equals("")
								|| priceListId.getText().equals("")) {
							JOptionPane.showMessageDialog(null, "Nie podano wszystkich danych!");
							return;
						}
						installationDao.update(new Installation((Long)table.getValueAt(table.getSelectedRow(), 0), routerNumberField.getText(), cityField.getText(),
								addressField.getText(), postcodeField.getText(), clientDao.get(Integer.parseInt(clientIdField.getText())),
								priceListDao.get(Integer.parseInt(priceListId.getText()))));
						refreshTable();
							
					}
			
				}
			}
		});
		updateInstallationButton.setBounds(10, 65, 170, 23);
		contentPane.add(updateInstallationButton);
		
		removeInstallationButton = new JButton("Usu\u0144 instalacjê");
		removeInstallationButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(table.getSelectedRow() >=0) {
					
						installationDao.delete(new Installation(
								(Long)table.getValueAt(table.getSelectedRow(), 0),
								table.getValueAt(table.getSelectedRow(), 1).toString(),
								table.getValueAt(table.getSelectedRow(), 2).toString(),
								table.getValueAt(table.getSelectedRow(), 3).toString(),
								table.getValueAt(table.getSelectedRow(), 4).toString(),
								clientDao.get(Integer.parseInt((table.getValueAt(table.getSelectedRow(), 5).toString()))),
								priceListDao.get(Integer.parseInt((table.getValueAt(table.getSelectedRow(), 6).toString())))
								));
						refreshTable();
							
					}
				}
		});
		removeInstallationButton.setBounds(10, 109, 170, 23);
		contentPane.add(removeInstallationButton);
		setVisible(true);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void refreshTable() {
		defaultTableModel.getDataVector().removeAllElements();
		List<Installation> installations = new ArrayList<>();
		installations = installationDao.getAll();
		for(Installation i : installations) {
			defaultTableModel.addRow(new Vector(Arrays.asList(i.getInstallationId(), i.getRouterNumber(),
					i.getCity(), i.getAddress(), i.getPostcode(), i.getClient().getClientId(), i.getPriceList().getPriceListId())));
		}
	}
}
