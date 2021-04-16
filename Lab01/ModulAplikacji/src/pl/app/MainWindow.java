package pl.app;

import pl.md5.FileChecker;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.awt.event.ActionEvent;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import java.awt.Font;

public class MainWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable table;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		System.setProperty("java.security.policy", "./java.policy");
		System.setSecurityManager(new SecurityManager());
		
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
		setResizable(false);
		setTitle("lab01");
		setBounds(100, 100, 835, 554);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton makeSnapshotButton = new JButton("Zapisz bie¿acy stan plików");
		makeSnapshotButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				final JFileChooser fc = new JFileChooser();
				fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int result = fc.showOpenDialog(makeSnapshotButton);
				
				if(result == JFileChooser.APPROVE_OPTION)
				{
					String directoryName = fc.getSelectedFile().getName();
					try {
						HashMap<String, String> snapshotHashMap = new HashMap<>();
						snapshotHashMap = FileChecker.generateMd5HashMap(fc.getSelectedFile().toString());
						FileChecker.writeToFile(snapshotHashMap, directoryName);
						JOptionPane.showMessageDialog(makeSnapshotButton, "Operacja przebieg³a pomyœlnie.");
					} catch (NoSuchAlgorithmException e1) {
						JOptionPane.showMessageDialog(makeSnapshotButton, "Error");
						e1.printStackTrace();
					} catch (IOException e1) {
						JOptionPane.showMessageDialog(makeSnapshotButton, "Error");
						e1.printStackTrace();
					}
				}

				else {
					JOptionPane.showMessageDialog(makeSnapshotButton, "Nie wybrano ¿adnego folderu.");
				}
			}
		});
		makeSnapshotButton.setBounds(27, 25, 196, 34);
		contentPane.add(makeSnapshotButton);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(253, 26, 547, 461);
		contentPane.add(scrollPane);
		DefaultTableModel defaultTableModel = new DefaultTableModel();
		table = new JTable(defaultTableModel);
		
		JButton verifyButton = new JButton("Sprawdz pliki w katalogu");
		verifyButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				HashMap<String, String> snapshotHashMap = new HashMap<String, String>();
				HashMap<String, String> currentHashMap = new HashMap<String, String>();
				HashMap<String, Boolean> resultHashMap = new HashMap<String, Boolean>();
				final JFileChooser fc = new JFileChooser();
				fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int result = fc.showOpenDialog(makeSnapshotButton);
				
				if(result == JFileChooser.APPROVE_OPTION) {
					String directoryName = fc.getSelectedFile().getName();
					try {
						currentHashMap = FileChecker.generateMd5HashMap(fc.getSelectedFile().toString());
						
						try {
							snapshotHashMap = FileChecker.readFromFile(directoryName);
						} catch (FileNotFoundException e2) {
							JOptionPane.showMessageDialog(verifyButton, "Dla wybranego katalogu nie utworzono wczeœniej snapshota");
						}
						
						resultHashMap = FileChecker.compare(snapshotHashMap, currentHashMap);	
						
						DefaultTableModel defaultTableModel = new DefaultTableModel();
						table = new JTable(defaultTableModel);
						defaultTableModel.addColumn("Nazwa pliku");
						defaultTableModel.addColumn("Status");
						table.getTableHeader().setResizingAllowed(false);
						table.setEnabled(false);
						
						for (Map.Entry<String, Boolean> entry : resultHashMap.entrySet()) {
				            String fileName = entry.getKey();
				            Boolean fileStatus = entry.getValue();
				            Vector<String> vector = new Vector<>();
				            vector.add(fileName);
				            if(fileStatus)
				            	vector.add("ZMODYFIKOWANY");
				            else 
				            	vector.add("brak modyfikacji");
							
							defaultTableModel.addRow(vector);
							
							scrollPane.setViewportView(table);
				        }

						
					} catch (NoSuchAlgorithmException | IOException e1) {
						e1.printStackTrace();
						JOptionPane.showMessageDialog(verifyButton, "Error");
					}
					
				}
				else 
					JOptionPane.showMessageDialog(makeSnapshotButton, "Nie wybrano ¿adnego folderu.");
			}
		});
		verifyButton.setBounds(27, 70, 196, 34);
		contentPane.add(verifyButton);
		
		JLabel lblNewLabel = new JLabel("Pawe³ Macioñczyk, 248837");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblNewLabel.setBounds(10, 487, 202, 27);
		contentPane.add(lblNewLabel);
	}
}
