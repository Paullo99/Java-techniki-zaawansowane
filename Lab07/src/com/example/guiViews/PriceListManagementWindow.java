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

import com.example.daoImpl.PriceListDao;
import com.example.models.PriceList;

public class PriceListManagementWindow extends JFrame {

	private JPanel contentPane;
	private static final long serialVersionUID = 1L;
	private DefaultTableModel defaultTableModel;
	private JTable table;
	private PriceListDao priceListDao;
	private JButton addPriceButton;
	private JButton updatePriceButton;
	private JButton removePriceButton;

	/**
	 * Create the frame.
	 */
	public PriceListManagementWindow() {
		setTitle("Zarz¹dzanie Cennikiem");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		setBounds(100, 100, 654, 461);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		priceListDao = new PriceListDao();
		
		JScrollPane scrollPane = new JScrollPane();
		defaultTableModel = new DefaultTableModel();
		table = new JTable(defaultTableModel);
		scrollPane.setBounds(198, 11, 430, 397);
		scrollPane.setViewportView(table);
		
		defaultTableModel.setColumnIdentifiers(new Object [] {"ID", "Typ us³ugi", "Cena"});
		contentPane.setLayout(null);
				
		refreshTable();
		
		
		contentPane.add(scrollPane);
		
		addPriceButton = new JButton("Dodaj now¹ us³ugê");
		addPriceButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JTextField serviceTypeField = new JTextField();
				JTextField priceField = new JTextField();
				Object[] fields = {
						"Typ us³ugi: ", serviceTypeField,
						"Cena: ", priceField
				};
				
				int result = JOptionPane.showConfirmDialog(null, fields, "Wprowadz dane nowej us³ugi", JOptionPane.OK_CANCEL_OPTION);
				if(result == JOptionPane.OK_OPTION) {
					if(serviceTypeField.getText().equals("") || priceField.getText().equals("")) {
						JOptionPane.showMessageDialog(null, "Nie podano wszystkich danych!");
						return;
					}
					priceListDao.add(new PriceList(serviceTypeField.getText(), Float.parseFloat(priceField.getText())));
					refreshTable();
						
				}
				
			}
		});
		addPriceButton.setBounds(10, 21, 170, 23);
		contentPane.add(addPriceButton);
		
		updatePriceButton = new JButton("Zaktualizuj dane us³ugi");
		updatePriceButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(table.getSelectedRow() >=0) {
					JTextField serviceTypeField = new JTextField();
					JTextField priceField = new JTextField();
					serviceTypeField.setText(table.getValueAt(table.getSelectedRow(), 1).toString());
					priceField.setText(table.getValueAt(table.getSelectedRow(), 2).toString());
					Object[] fields = {
							"Typ us³ugi: ", serviceTypeField,
							"Cena: ", priceField
					};
					
					int result = JOptionPane.showConfirmDialog(null, fields, "Edycja danych us³ugi", JOptionPane.OK_CANCEL_OPTION);
					if(result == JOptionPane.OK_OPTION) {
						if(serviceTypeField.getText().equals("") || priceField.getText().equals("")) {
							JOptionPane.showMessageDialog(null, "Nie podano wszystkich danych!");
							return;
						}
						priceListDao.update(new PriceList((Long)table.getValueAt(table.getSelectedRow(), 0), serviceTypeField.getText(), 
								Float.parseFloat(priceField.getText())));
						refreshTable();
							
					}
				}
			}
		});
		updatePriceButton.setBounds(10, 65, 170, 23);
		contentPane.add(updatePriceButton);
		
		removePriceButton = new JButton("Usu\u0144 us³ugê");
		removePriceButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(table.getSelectedRow() >=0) {
					
						priceListDao.delete(new PriceList(
								(Long)table.getValueAt(table.getSelectedRow(), 0),
								table.getValueAt(table.getSelectedRow(), 1).toString(),
								Float.parseFloat(table.getValueAt(table.getSelectedRow(), 2).toString())));
						refreshTable();
							
					}
				}
		});
		removePriceButton.setBounds(10, 109, 170, 23);
		contentPane.add(removePriceButton);
		setVisible(true);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void refreshTable() {
		defaultTableModel.getDataVector().removeAllElements();
		List<PriceList> prices = new ArrayList<>();
		prices = priceListDao.getAll();
		for(PriceList p : prices) {
			defaultTableModel.addRow(new Vector(Arrays.asList(p.getPriceListId(), p.getServiceType(), p.getPrice())));
		}
	}
}
