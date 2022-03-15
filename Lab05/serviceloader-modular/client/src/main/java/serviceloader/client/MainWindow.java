package serviceloader.client;

import java.util.HashMap;
import java.util.ServiceLoader;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import ex.api.ClusterAnalysisService;
import ex.api.ClusteringException;
import ex.api.DataSet;

import javax.swing.JTable;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JCheckBox;

public class MainWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable table;
	private DefaultTableModel defaultTableModel;
	private Object[][] data;
	private JList<String> servicesList;
	private DefaultListModel<String> defaultListModel;
	private HashMap<String, ClusterAnalysisService> servicesHashMap;
	private JButton processData;
	private ClusterAnalysisService clusterAnalysisService;
	private JCheckBox groupingCheckBox;
	private DataSet dataSet;
	private JButton editButton;
	private JButton saveButton;

	/**
	 * Create the frame.
	 */
	public MainWindow() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setTitle("lab05");
		setBounds(100, 100, 971, 507);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		groupingCheckBox = new JCheckBox("Pogrupowa\u0107?");
		defaultTableModel = new DefaultTableModel();
		table = new JTable(defaultTableModel);
		processData = new JButton("Przetw\u00F3rz dane");
		editButton = new JButton("Edytuj");
		saveButton = new JButton("Zapisz");
		JButton loadServicesButton = new JButton("Za\u0142aduj serwisy");
		defaultListModel = new DefaultListModel<>();
		servicesList = new JList<String>(defaultListModel);
		JButton generateDataButton = new JButton("Wygeneruj przyk\u0142adowe dane");
		JButton showProcessedData = new JButton("Wy\u015Bwietl przetworzone dane");
		
		groupingCheckBox.setBounds(555, 147, 224, 23);
		scrollPane.setBounds(25, 30, 486, 386);
		showProcessedData.setBounds(555, 211, 232, 23);
		generateDataButton.setBounds(25, 427, 217, 23);
		processData.setBounds(555, 177, 154, 23);
		editButton.setBounds(328, 427, 89, 23);
		loadServicesButton.setBounds(781, 109, 139, 23);
		saveButton.setBounds(427, 427, 89, 23);
		servicesList.setBounds(555, 30, 364, 68);
		
		processData.setEnabled(false);
		table.setEnabled(false);
		saveButton.setEnabled(false);
		
		scrollPane.setViewportView(table);
		
		processData.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (servicesList.getSelectedIndex() >= 0) {
					clusterAnalysisService = servicesHashMap.get(servicesList.getSelectedValue());
					try {
						if (groupingCheckBox.isSelected()) {
							String[] options = { "grouping" };
							clusterAnalysisService.setOptions(options);
						}
						clusterAnalysisService.submit(dataSet);
					} catch (ClusteringException e1) {
						e1.printStackTrace();
					}
				} else {
					JOptionPane.showMessageDialog(null, "Nie wybrano ¿adnego serwisu");
				}

			}
		});

		editButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				table.setEnabled(true);
				saveButton.setEnabled(true);
			}
		});
		
		saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			
				table.setRowSelectionAllowed(false);
				table.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
				table.editCellAt(-1, -1);
				
				data = new String[defaultTableModel.getRowCount()][defaultTableModel.getColumnCount()];
				for (int i = 0; i < defaultTableModel.getRowCount(); i++)
					for (int j = 0; j < defaultTableModel.getColumnCount(); j++)
						data[i][j] = (String) defaultTableModel.getValueAt(i, j);

				table.setEnabled(false);

				dataSet = new DataSet();
				dataSet.setData((String[][]) data);
				
				saveButton.setEnabled(false);
			}
		});

		loadServicesButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				servicesHashMap = new HashMap<>();
				defaultListModel.removeAllElements();

				ServiceLoader<ClusterAnalysisService> loader = ServiceLoader.load(ClusterAnalysisService.class);
				System.out.println("Services loading done");

				for (ClusterAnalysisService service : loader) {
					servicesHashMap.put(service.getName(), service);
					defaultListModel.addElement(service.getName());
				}
				processData.setEnabled(true);
			}
		});

		showProcessedData.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				defaultTableModel.getDataVector().removeAllElements();

				try {
					DataSet data = clusterAnalysisService.retrieve(false);
					String[][] dataToShow = data.getData();
					for (String[] row : dataToShow) {
						Vector<String> vector = new Vector<>();
						for (String s : row)
							vector.add(s);

						defaultTableModel.addRow(vector);
					}

				} catch (ClusteringException e1) {
					JOptionPane.showMessageDialog(null, "Przetwarzanie danych jeszcze siê nie zakoñczy³o", "ERROR",
							JOptionPane.ERROR_MESSAGE);
				}

			}
		});

		generateDataButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				defaultTableModel.getDataVector().removeAllElements();
				defaultTableModel.setColumnCount(0);

				dataSet = generateData();

				for (String s : dataSet.getHeader()) {
					defaultTableModel.addColumn(s);
				}

				for (String[] row : dataSet.getData()) {
					Vector<String> vector = new Vector<>();
					for (String s : row)
						vector.add(s);

					defaultTableModel.addRow(vector);
				}
			}
		});
		
		contentPane.add(scrollPane);
		contentPane.add(loadServicesButton);
		contentPane.add(processData);
		contentPane.add(showProcessedData);
		contentPane.add(generateDataButton);
		contentPane.add(groupingCheckBox);
		contentPane.add(editButton);
		contentPane.add(servicesList);
		contentPane.add(saveButton);
	}

	public DataSet generateData() {
		DataSet dataSet = new DataSet();

		String[] headers = { "ID", "Kategoria", "Data" };
		String[][] data = { { "0", "", "30-05-1970" }, { "1", "", "23-06-1992" }, { "2", "", "17-03-2012" },
				{ "3", "", "10-02-2000" }, { "4", "", "18-07-2014" }, { "5", "", "11-09-1999" },
				{ "6", "", "28-01-2001" }, { "7", "", "01-02-2003" }, { "8", "", "14-12-2015" },
				{ "9", "", "15-04-1980" }, { "10", "", "20-08-2008" }, { "11", "", "19-10-2004" },
				{ "12", "", "06-05-2009" }, { "13", "", "13-01-1997" }, { "14", "", "15-09-1999" },
				{ "15", "", "29-06-2019" }, { "16", "", "20-03-2005" }, { "17", "", "12-01-2015" },
				{ "18", "", "25-02-2010" }, { "19", "", "09-07-1999" }, { "20", "", "28-10-2018" } };

		dataSet.setData(data);
		dataSet.setHeader(headers);

		return dataSet;
	}
}
