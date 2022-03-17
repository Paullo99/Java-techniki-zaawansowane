package com.example.imageConverter;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Image;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.event.ActionEvent;
import javax.swing.JList;
import javax.swing.SwingConstants;

public class MainWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JLabel imageAfterLabel;
	private JLabel imageBeforeLabel;
	private BufferedImage originalImage;
	private BufferedImage processedImage;
	private String imageFileName;
	private JList<String> scriptsList;
	private static DefaultListModel<String> listModel;
	private HashMap<String, File> scriptsHashMap;
	private JButton loadScriptButton;
	private JButton unloadScriptButton;
	private JButton chooseImageButton;
	private JButton downloadButton;
	
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
	
					new MainWindow();
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
		setBounds(100, 100, 1091, 985);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		setLocation(350, 30);
		
		scriptsHashMap = new HashMap<>();
		
		imageAfterLabel = new JLabel("Przetworzone zdjęcie");
		imageAfterLabel.setHorizontalAlignment(SwingConstants.CENTER);
		imageAfterLabel.setBounds(345, 478, 669, 453);
		contentPane.add(imageAfterLabel);
		Border border = BorderFactory.createLineBorder(Color.BLACK, 1);
		 
		imageAfterLabel.setBorder(border);
		
		imageBeforeLabel = new JLabel("Oryginalne zdjęcie");
		imageBeforeLabel.setHorizontalAlignment(SwingConstants.CENTER);
		imageBeforeLabel.setBounds(345, 11, 669, 453);
		contentPane.add(imageBeforeLabel);
		imageBeforeLabel.setBorder(border);
		
		JButton processImageButton = new JButton("Przetwórz");
		processImageButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");	
				if (originalImage == null) {
					JOptionPane.showMessageDialog(null, "Nie wybrano żadnego zdjęcia do obróbki!");
					return;
				}
					
				if (scriptsList.getSelectedIndex()>=0) {
					try {
						engine.eval(new FileReader(scriptsHashMap.get(scriptsList.getSelectedValue())));
						Invocable invocable = (Invocable) engine;
						processedImage = (BufferedImage) invocable.invokeFunction("process", CopyOfBufferedImage.getCopy(originalImage));
						displayPng(processedImage, imageAfterLabel);
					} catch (FileNotFoundException | ScriptException | NoSuchMethodException e1) {
						e1.printStackTrace();
					}
				}
				else {
					JOptionPane.showMessageDialog(null, "Nie wybrano żadnego skryptu!");
				}
				
			}
		});
		processImageButton.setBounds(26, 298, 279, 35);
		contentPane.add(processImageButton);
		
		listModel = new DefaultListModel<String>();
		scriptsList = new JList<String>(listModel);
		scriptsList.setBounds(26, 54, 279, 176);
		contentPane.add(scriptsList);
		
		loadScriptButton = new JButton("Załaduj skrypt .js");
		loadScriptButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				loadScript();
			}
		});
		loadScriptButton.setBounds(26, 20, 136, 23);
		contentPane.add(loadScriptButton);
		
		unloadScriptButton = new JButton("Wyładuj skrypt");
		unloadScriptButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(scriptsList.getSelectedIndex() <0)
					JOptionPane.showMessageDialog(null, "Nie wybrano żadnego skryptu!");
				else {
					scriptsHashMap.remove(scriptsList.getSelectedValue());
					refreshList();
				}
			}
		});
		unloadScriptButton.setBounds(172, 20, 133, 23);
		contentPane.add(unloadScriptButton);
		
		chooseImageButton = new JButton("Wybierz zdjęcie do przetworzenia");
		chooseImageButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				originalImage = loadImage();
				if (originalImage!= null)
					displayPng(originalImage, imageBeforeLabel);
			}
		});
		chooseImageButton.setBounds(26, 252, 279, 23);
		contentPane.add(chooseImageButton);
		
		downloadButton = new JButton("Pobierz przeróbkę");
		downloadButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(processedImage!=null) {
					try {
						String fileName = imageFileName.substring(0, imageFileName.length()-4).concat("_processed");
						ImageIO.write(processedImage, "jpg", new File(System.getProperty("user.dir")+"\\"+fileName+".jpg"));
					} catch (IOException e1) {
						System.out.println("Błąd.");
					}
				}
			}
		});
		downloadButton.setBounds(181, 908, 154, 23);
		contentPane.add(downloadButton);
		
		setVisible(true);
	}
	
	private void refreshList() {
		listModel.removeAllElements();
		for (Map.Entry<String, File> entry : scriptsHashMap.entrySet()) {
		    listModel.addElement(entry.getKey());
		}
	}
		
	private void loadScript() {
		JFileChooser fileChooser = new JFileChooser(new File(System.getProperty("user.dir")));
		FileFilter jsFilter = new FileNameExtensionFilter("js", "js");
		fileChooser.setFileFilter(jsFilter);
		if(fileChooser.showOpenDialog(null)!=0) {
			JOptionPane.showMessageDialog(null, "Nie wybrano żadnego pliku!");
		}
		else {
			File file = fileChooser.getSelectedFile();
			scriptsHashMap.put(file.getName(), file);
			refreshList();
		}
	}
	
	private BufferedImage loadImage() {
		
		JFileChooser fileChooser = new JFileChooser("D:\\");
		FileFilter imageFilter = new FileNameExtensionFilter(
			    "Image files", ImageIO.getReaderFileSuffixes());
		fileChooser.setFileFilter(imageFilter);
		if(fileChooser.showOpenDialog(null)!=0) {
			JOptionPane.showMessageDialog(null, "Nie wybrano żadnego pliku!");
		}
		else {
			File file = fileChooser.getSelectedFile();
			imageFileName = file.getName();
			try {
				BufferedImage img = ImageIO.read(file);
				imageAfterLabel.setIcon(null);
				return img;
			} catch (IOException e) {
				
			}
		}
		
		return null;
	}
	
	private void displayPng(BufferedImage img, JLabel labelForImage) {

		// warunek sprawdzający czy obrazek jest pionowy czy poziomy
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
	}
}
