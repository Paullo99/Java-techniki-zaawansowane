package app;

import java.awt.EventQueue;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import loader.MyClassLoader;
import processing.StatusListener;
import statusThings.MyStatusListener;

import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.awt.event.ActionEvent;
import java.awt.TextArea;

public class MainWindow extends JFrame {

	private JPanel contentPane;
	private JList<String> classList;
	private JButton submitButton;
	private TextArea inputTextArea;
	private TextArea outputTextArea;
	private final JProgressBar progressBar;
	private static DefaultListModel<String> listModel;
	private static HashMap<String, String> getInfoAndClassHashMap;
	private static String directoryPath;
	
	private Class<?> loadedClass;
	private Class<?>[] parameterTypes;
	private Object object;
	private Method submitTask;
	private Method getResult;
	private MyClassLoader myClassLoader;

	/**
	 * Launch the application.
	 * 
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public static void main(String[] args) throws InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
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
	 * 
	 * @throws InterruptedException
	 */
	public MainWindow() throws InterruptedException {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1056, 479);
		contentPane = new JPanel();
		progressBar = new JProgressBar();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		listModel = new DefaultListModel<String>();
		classList = new JList<String>(listModel);
		inputTextArea = new TextArea();
		submitButton = new JButton("Przetw\u00F3rz");
		outputTextArea = new TextArea();

		outputTextArea.setEditable(false);

		submitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if (classList.getSelectedValue() != null) {
					progressBar.setValue(0);
					progressBar.setStringPainted(true);

					String className = getInfoAndClassHashMap.get(classList.getSelectedValue());

					myClassLoader = new MyClassLoader(Path.of(directoryPath));
					MyStatusListener myStatusListener = new MyStatusListener();

					try {
						loadedClass = myClassLoader.findClass(className);
						parameterTypes = new Class<?>[] { String.class, StatusListener.class };
						Object[] arguments = { inputTextArea.getText(), myStatusListener };
						object = loadedClass.getDeclaredConstructor().newInstance();
						submitTask = loadedClass.getMethod("submitTask", parameterTypes);
						getResult = loadedClass.getMethod("getResult");
						submitTask.invoke(object, arguments);
						
						new Thread(new Runnable() {
							public void run() {
								submitButton.setEnabled(false);

								while (myStatusListener.getProgress() < 100) {
									SwingUtilities.invokeLater(new Runnable() {
										public void run() {
											progressBar.setValue(myStatusListener.getProgress());
										}
									});
								}

								try {
									Thread.sleep(400);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}

								submitButton.setEnabled(true);
								try {
									outputTextArea.setText((String)getResult.invoke(object));	
									
									loadedClass = null;
									parameterTypes = null;
									object = null;
									submitTask = null;
									getResult = null;
									myClassLoader = null;
									System.gc();
								} catch (IllegalAccessException | IllegalArgumentException
										| InvocationTargetException e) {
									e.printStackTrace();
								}
							}
						}).start();
					} catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException
							| IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) {
						e1.printStackTrace();
					}

					System.out.println(className);
				}
			}
		});

		classList.setBounds(581, 24, 443, 273);
		submitButton.setBounds(690, 308, 112, 32);
		progressBar.setBounds(581, 383, 443, 22);

		contentPane.add(classList);
		contentPane.add(submitButton);
		contentPane.add(progressBar);

		inputTextArea.setBounds(40, 24, 435, 187);
		contentPane.add(inputTextArea);

		outputTextArea.setBounds(40, 235, 435, 187);
		contentPane.add(outputTextArea);

		JButton loadClassesButton = new JButton("Za\u0142aduj klasy");
		loadClassesButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				listModel.removeAllElements();
				try {
					loadClassesToList(listModel);
				} catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException
						| IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) {
					e1.printStackTrace();
				}

			}
		});
		loadClassesButton.setBounds(913, 303, 112, 23);
		contentPane.add(loadClassesButton);
	}

	private static void loadClassesToList(DefaultListModel<String> lm)
			throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException {

		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fileChooser.setCurrentDirectory(new File("D:\\target"));
		if (fileChooser.showOpenDialog(null) != 0) {
			JOptionPane.showMessageDialog(null, "Nie wybrano ¿adnego katalogu!");
		} else {
			directoryPath = fileChooser.getSelectedFile().toString();

			try {
				Stream<Path> classList = Files.walk(Path.of(directoryPath), 2);

				List<String> result = classList.filter(Files::isRegularFile).map(x -> x.toString())
						.collect(Collectors.toList());
				classList.close();

				MyClassLoader mcl = new MyClassLoader(Path.of(directoryPath));

				getInfoAndClassHashMap = new HashMap<>();

				for (String s : result) {

					String className = s.substring(directoryPath.length() + 1);

					Class<?> c = mcl.loadClass(className);
					Method m = c.getMethod("getInfo");
					Object o = c.getDeclaredConstructor().newInstance();

					getInfoAndClassHashMap.put((String) m.invoke(o), className);
					lm.addElement((String) m.invoke(o));

					o = null;
					m = null;
					c = null;
				}

				mcl = null;
				System.gc();

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
}
