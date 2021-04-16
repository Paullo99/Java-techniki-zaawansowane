package pl.app;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.stream.Collectors;
import javax.imageio.ImageIO;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.JButton;
import javax.swing.JPanel;

import java.awt.Font;
import java.awt.Image;
import java.awt.TextArea;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;
import java.awt.EventQueue;
import javax.swing.SwingConstants;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class MainWindow {

	private JFrame frame;
	private JLabel labelForImage;
	private TextArea textArea;
	private JTextField selectedDirectoryTextField;
	private File filePath;
	private JPanel panel;
	private WeakHashMap<String, File> weakHashMap;
	private JLabel fileLoadedFromInfoLabel;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow window = new MainWindow();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public MainWindow() throws IOException {
		weakHashMap = new WeakHashMap<>();
		initialize();
	}

	private void initialize() throws IOException {
		frame = new JFrame();
		frame.setBounds(100, 100, 1042, 641);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Aktualnie wybrany katalog:");
		DefaultListModel<String> defaultListModel = new DefaultListModel<String>();
		JScrollPane scrollPane = new JScrollPane();
		JList<String> filesList = new JList<String>(defaultListModel);
		JButton selectDirectoryButton = new JButton("Wybierz katalog");
		Font font = new Font("Serif", 0, 20);
		
		fileLoadedFromInfoLabel = new JLabel("");	
		selectedDirectoryTextField = new JTextField();
		panel = new JPanel();
		textArea = new TextArea();
		labelForImage = new JLabel();
		
		lblNewLabel.setBounds(23, 32, 180, 26);
		panel.setBounds(23, 69, 493, 450);
		textArea.setBounds(532, 35, 467, 484); 
		labelForImage.setBounds(532, 11, 467, 512);
		scrollPane.setBounds(10, 11, 473, 434);
		selectDirectoryButton.setBounds(189, 526, 171, 40);
		textArea.setBounds(532, 35, 467, 484);		
		selectedDirectoryTextField.setBounds(189, 32, 311, 26);
		fileLoadedFromInfoLabel.setBounds(532, 523, 467, 40);
		
		panel.setLayout(null);
		panel.add(scrollPane);

		scrollPane.setViewportView(filesList);
		
		selectedDirectoryTextField.setText("brak");
		selectedDirectoryTextField.setEditable(false);

		textArea.setBackground(Color.white);
		textArea.setEditable(false); 
		textArea.setFocusable(false);
		textArea.setFont(font);
		
		fileLoadedFromInfoLabel.setHorizontalAlignment(SwingConstants.CENTER);
		fileLoadedFromInfoLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
		fileLoadedFromInfoLabel.setForeground(Color.RED);
		
		textArea.setVisible(false);
		labelForImage.setVisible(false);

		selectDirectoryButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				textArea.setVisible(false);
				labelForImage.setVisible(false);
				fileLoadedFromInfoLabel.setText("");
				
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				if(fileChooser.showOpenDialog(null)!=0) {
					JOptionPane.showMessageDialog(null, "Nie wybrano ¿adnego katalogu!");
				}
				else {
					filePath = fileChooser.getSelectedFile();
					String directoryPath = fileChooser.getSelectedFile().toString();

					Set<String> fileSet = createSetOfFiles(directoryPath);
					
					defaultListModel.removeAllElements();
					
					for (String fileName : fileSet) 
						defaultListModel.addElement(fileName);

					selectedDirectoryTextField.setText(directoryPath);
				}
				
			}
		});

		
		filesList.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
						
				if (!e.getValueIsAdjusting() && filesList.getSelectedValue() != null) {
					
					File file = loadFile(filesList.getSelectedValue());
					
					if (file!=null && file.getName().endsWith(".png")) {
						
						labelForImage.setVisible(true);
						textArea.setVisible(false);
						
						displayPng(file);
					}
	
					else if (file!=null && file.getName().endsWith(".txt")) {
						
						labelForImage.setVisible(false);
						textArea.setVisible(true);
						textArea.setText("");
						
						displayTxt(file);
					}
					
					else {
						JOptionPane.showMessageDialog(null, "Wybrany plik nie jest obs³ugiwany");
					}
				}
			}
		});
		
		frame.getContentPane().add(selectDirectoryButton);
		frame.getContentPane().add(lblNewLabel);
		frame.getContentPane().add(labelForImage);
		frame.getContentPane().add(panel);
		frame.getContentPane().add(textArea);
		frame.getContentPane().add(selectedDirectoryTextField);
		frame.getContentPane().add(textArea);
		frame.getContentPane().add(fileLoadedFromInfoLabel);
	}

	/*
	 * Zwraca zbiór wszystkich plików (a dok³adniej ich nazwy) znajduj¹cych siê w
	 * danym katalogu
	 */
	public static Set<String> createSetOfFiles(String dir) {
		Set<String> fileNameSet;
		try {
			fileNameSet = Files.list(Paths.get(dir)).filter(Files::isRegularFile).map(Path::getFileName)
					.map(Path::toString).collect(Collectors.toSet());

			return fileNameSet;
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Err");
		}

		return null;
	}

	/*
	 * Laduje plik z pamiêci lub z wykorzystaniem WeakReferences
	 */
	public File loadFile(String fileName) {
		
		File file;
		
		try {
            if (weakHashMap.containsKey(fileName) == false) {
            	file = new File(filePath.getAbsolutePath() + "\\" + fileName);
            	weakHashMap.put(fileName, file);                  
            	fileLoadedFromInfoLabel.setText("Zawartoœæ za³adowana z pliku");
            	return file;
            } else { 	
            	fileLoadedFromInfoLabel.setText("Zawartoœæ za³adowana z wykorzystaniem WeakReference");
                file = weakHashMap.get(fileName);
                return file;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
		
		return null;
	}
	
	/*
	 * Funkcja wyœwietlaj¹ca obrazek .png jako ikonê w JLabel
	 */
	public void displayPng(File file) {
		try {
			Image img = ImageIO.read(file);
			
			// warunek sprawdzaj¹cy czy obrazek jest pionowy czy poziomy
			if (img.getWidth(null) >= img.getHeight(null)) {
				int newHeight = labelForImage.getWidth() * img.getHeight(null) / img.getWidth(null);
				Image dimg = img.getScaledInstance(labelForImage.getWidth(), newHeight, Image.SCALE_SMOOTH);
				ImageIcon icon = new ImageIcon(dimg);
				labelForImage.setIcon(icon);
			} else {
				int newWidth = labelForImage.getHeight() * img.getWidth(null) / img.getHeight(null);
				Image dimg = img.getScaledInstance(newWidth, labelForImage.getHeight(), Image.SCALE_SMOOTH);
				ImageIcon icon = new ImageIcon(dimg);
				labelForImage.setIcon(icon);
			}
			
		} catch (IOException e1) {
			JOptionPane.showMessageDialog(null, "Nie mo¿na otworzyæ pliku.");
			e1.printStackTrace();
		}
	}

	/*
	 * Funkcja wyœwietlaj¹ca plik tekstowy .txt w TextArea
	 */
	public void displayTxt(File file) {
		ArrayList<String> readedList = new ArrayList<>(); 
		String line;
		
		BufferedReader bf;
		try {
			bf = new BufferedReader(new FileReader(file));
			while((line = bf.readLine())!=null) 
				readedList.add(line); 
			bf.close(); 
		} catch (FileNotFoundException e2) {
			e2.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		for(String record : readedList) { 
			textArea.append(record + "\n\r"); 
		}
	}
}
