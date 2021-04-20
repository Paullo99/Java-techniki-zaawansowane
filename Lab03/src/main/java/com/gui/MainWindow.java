package com.gui;

import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.data.DataGetter;

import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.event.ActionListener;
import java.util.Locale;
import java.util.ResourceBundle;
import java.awt.event.ActionEvent;
import java.awt.Font;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import java.text.ChoiceFormat;
import java.text.Format;
import java.text.MessageFormat;
import java.text.NumberFormat;

import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;

public class MainWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JLabel firstQuestionLabel;
	private JLabel secondQuestionLabel;
	private JLabel secondQuestionPart2Label;
	private JLabel thirdQuestionLabel;
	private JButton checkFirstQButton;
	private JButton checkSecondQButton;
	private JButton checkThirdQButton;
	private JButton plButton;
	private JButton enButton;
	private JComboBox<String> firstQComboBox;
	private JComboBox<String> secondQComboBox;
	private JSpinner secondQAmount;
	private JTextField firstQField;
	private JLabel yourAns1Label;
	private JLabel firstQCorrectAnswerLabel;
	private Locale l;
	private JLabel yourAns2Label;
	private JTextField secondQField;
	private JLabel secondQCorrectAnswerLabel;
	private JLabel thousandLabel;
	private JTextField thirdQField;
	private JComboBox<String> thirdQComboBox;
	private JLabel thirdQuestionPart2Label;
	private JLabel yourAns3Label;
	private JLabel thirdQCorrectAnswerLabel;
	private JLabel questionMarkLabel;
	private JLabel questionMarkLabel_1;
	private JLabel questionMarkLabel_2;

	public static void main(String[] args) {
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

	public MainWindow() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 440, 508);
		setTitle("Lab03");
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		initializeMainWindowElements();

		setBounds();

		setFonts();

		l = new Locale("pl", "PL");
		changeLanguage(l);

		plButton.setEnabled(false);

		firstQComboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				firstQField.setBackground(Color.white);
				firstQField.setText("");
				firstQCorrectAnswerLabel.setText("");
			}
		});

		secondQComboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				secondQField.setBackground(Color.white);
				secondQField.setText("");
				secondQCorrectAnswerLabel.setText("");
			}
		});

		thirdQComboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				thirdQField.setBackground(Color.white);
				thirdQField.setText("");
				thirdQCorrectAnswerLabel.setText("");
			}
		});

		plButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				l = new Locale("pl", "PL");

				changeLanguage(l);

				plButton.setEnabled(false);
				enButton.setEnabled(true);
				firstQCorrectAnswerLabel.setText("");
				secondQCorrectAnswerLabel.setText("");
				thirdQCorrectAnswerLabel.setText("");
				secondQAmount.setBounds(199, 175, 80, 20);
				firstQField.setBounds(132, 59, 52, 23);
				secondQField.setBounds(132, 214, 52, 23);
				thirdQField.setBounds(132, 363, 52, 23);
				questionMarkLabel_1.setBounds(282, 171, 20, 23);
			}
		});

		enButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				l = new Locale("en", "EN");

				changeLanguage(l);

				plButton.setEnabled(true);
				enButton.setEnabled(false);
				firstQCorrectAnswerLabel.setText("");
				secondQCorrectAnswerLabel.setText("");
				thirdQCorrectAnswerLabel.setText("");
				firstQField.setBounds(105, 59, 70, 23);
				secondQField.setBounds(105, 214, 70, 23);
				thirdQField.setBounds(105, 363, 70, 23);
				secondQAmount.setBounds(160, 175, 80, 20);
				questionMarkLabel_1.setBounds(247, 171, 20, 23);
			}
		});

		checkFirstQButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				int index = firstQComboBox.getSelectedIndex();

				long i = DataGetter.getDataForFirstQuestion(index);

				int answer;

				try {
					answer = Integer.parseInt(firstQField.getText());
				} catch (Exception e2) {
					answer = 0;
				}

				if (answer == i)
					firstQField.setBackground(Color.GREEN);
				else
					firstQField.setBackground(Color.red);

				displayCorrectAnswerForQ1(String.valueOf(i), l);
			}
		});

		checkSecondQButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				int index = secondQComboBox.getSelectedIndex();
				String population = secondQAmount.getValue().toString();

				long i = DataGetter.getDataForSecondQuestion(index, population);
				int answer;

				try {
					answer = Integer.parseInt(secondQField.getText());
				} catch (Exception e2) {
					answer = 0;
				}

				if (answer == i)
					secondQField.setBackground(Color.GREEN);
				else
					secondQField.setBackground(Color.red);

				displayCorrectAnswerForQ2(String.valueOf(i), l);
			}
		});

		checkThirdQButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				int index = thirdQComboBox.getSelectedIndex();

				long i = DataGetter.getDataForThirdQuestion(index);

				int answer;

				try {
					answer = Integer.parseInt(thirdQField.getText());
				} catch (Exception e2) {
					answer = 0;
				}

				if (Math.abs(answer - i) <= 100)
					thirdQField.setBackground(Color.GREEN);
				else
					thirdQField.setBackground(Color.red);

				displayCorrectAnswerForQ3(String.valueOf(i), l);
			}
		});

		firstQField.setColumns(10);
		secondQField.setColumns(10);
		thirdQField.setColumns(10);

		secondQAmount.setModel(new SpinnerNumberModel(0, 0, null, 20000));

		addElementsToPane();
	}

	public void changeLanguage(Locale l) {

		ResourceBundle rb = ResourceBundle.getBundle("MyBundle", l);

		firstQComboBox.removeAllItems();
		secondQComboBox.removeAllItems();
		thirdQComboBox.removeAllItems();

		firstQuestionLabel.setText(rb.getString("firstQuestion"));
		secondQuestionLabel.setText(rb.getString("secondQuestion"));
		secondQuestionPart2Label.setText(rb.getString("secondQuestionPart2"));
		thirdQuestionLabel.setText(rb.getString("thirdQuestion"));
		thirdQuestionPart2Label.setText(rb.getString("thirdQuestionPart2"));
		yourAns1Label.setText(rb.getString("yourAnswer"));
		yourAns2Label.setText(rb.getString("yourAnswer"));
		yourAns3Label.setText(rb.getString("yourAnswer"));
		thousandLabel.setText(rb.getString("thousand"));

		checkFirstQButton.setText(rb.getString("solve"));
		checkSecondQButton.setText(rb.getString("solve"));
		checkThirdQButton.setText(rb.getString("solve"));

		String[] q1 = rb.getString("polishCityList").split(",");

		for (String s : q1)
			firstQComboBox.addItem(s);

		String[] q2 = rb.getString("polishRegionList").split(",");

		for (String s : q2)
			secondQComboBox.addItem(s);

		String[] q3 = rb.getString("countryList").split(",");

		for (String s : q3)
			thirdQComboBox.addItem(s);
	}

	public void displayCorrectAnswerForQ1(String correctAnswer, Locale l) {
		ResourceBundle rb = ResourceBundle.getBundle("MyBundle", l);
		MessageFormat mf = new MessageFormat("");
		mf.setLocale(l);

		String[] arguments = { firstQComboBox.getSelectedItem().toString(), correctAnswer };
		mf.applyPattern(rb.getString("firstAnswer"));
		String output = mf.format(arguments);
		firstQCorrectAnswerLabel.setText(output);

	}

	public void displayCorrectAnswerForQ2(String correctAnswer, Locale l) {
		ResourceBundle rb = ResourceBundle.getBundle("MyBundle", l);
		MessageFormat mf = new MessageFormat("");
		mf.setLocale(l);

		int correctAnswerInt = Integer.parseInt(correctAnswer);

		if (l.getCountry().toString().equals("PL")) {

			double[] limits = {0, 2, 5 };
			String[] cityVariant = { rb.getString("secondAnswerZero"), rb.getString("secondAnswerTwo"),
					rb.getString("secondAnswerFive") };
			ChoiceFormat cf = new ChoiceFormat(limits, cityVariant);
			String pattern = rb.getString("secondAnswer");

			Format[] formats = { cf, null, NumberFormat.getInstance() };
			mf.applyPattern(pattern);

			mf.setFormats(formats);

			if (correctAnswerInt == 0)
				secondQCorrectAnswerLabel.setText(
						"W województwie " + secondQComboBox.getSelectedItem().toString() + " nie ma takich miast.");
			else if (correctAnswerInt == 1) {
				secondQCorrectAnswerLabel.setText(
						"W województwie " + secondQComboBox.getSelectedItem().toString() + " jest 1 takie miasto.");
			} else if (correctAnswerInt >=11 && correctAnswerInt <=19){
				Object[] sth = { correctAnswerInt, secondQComboBox.getSelectedItem().toString(), correctAnswer };
				String output = mf.format(sth);
				secondQCorrectAnswerLabel.setText(output);
			}else {
				Object[] sth = { correctAnswerInt % 10, secondQComboBox.getSelectedItem().toString(), correctAnswer };
				String output = mf.format(sth);
				secondQCorrectAnswerLabel.setText(output);
			}
		} else if (l.getCountry().toString().equals("EN")) {
			double[] limits = { 0, 1, 2 };
			String[] cityVariant = { rb.getString("noCities"), rb.getString("oneCity"), rb.getString("moreCities") };
			ChoiceFormat cf = new ChoiceFormat(limits, cityVariant);
			String pattern = rb.getString("secondAnswer");
			Format[] formats = { cf, null, NumberFormat.getInstance() };
			mf.applyPattern(pattern);

			mf.setFormats(formats);

			Object[] sth = { correctAnswerInt, secondQComboBox.getSelectedItem().toString(), correctAnswer };
			String output = mf.format(sth);
			secondQCorrectAnswerLabel.setText(output);
		}

	}

	public void displayCorrectAnswerForQ3(String correctAnswer, Locale l) {
		ResourceBundle rb = ResourceBundle.getBundle("MyBundle", l);
		MessageFormat mf = new MessageFormat("");
		mf.setLocale(l);

		String[] arguments = { thirdQComboBox.getSelectedItem().toString(), correctAnswer };
		mf.applyPattern(rb.getString("thirdAnswer"));
		String output = mf.format(arguments);
		thirdQCorrectAnswerLabel.setText(output);

	}

	private void initializeMainWindowElements() {
		checkFirstQButton = new JButton();
		checkSecondQButton = new JButton();
		checkThirdQButton = new JButton();
		firstQuestionLabel = new JLabel();
		secondQuestionLabel = new JLabel();
		secondQuestionPart2Label = new JLabel();
		thirdQuestionLabel = new JLabel();
		yourAns1Label = new JLabel();
		yourAns2Label = new JLabel();
		firstQCorrectAnswerLabel = new JLabel();
		firstQField = new JTextField();
		firstQField.setHorizontalAlignment(SwingConstants.RIGHT);
		firstQComboBox = new JComboBox<String>();
		secondQComboBox = new JComboBox<String>();
		secondQAmount = new JSpinner();
		thousandLabel = new JLabel();
		secondQField = new JTextField();
		secondQCorrectAnswerLabel = new JLabel();
		thirdQComboBox = new JComboBox<String>();
		thirdQCorrectAnswerLabel = new JLabel();
		thirdQField = new JTextField();
		thirdQuestionPart2Label = new JLabel();
		yourAns3Label = new JLabel();

		enButton = new JButton("EN");
		plButton = new JButton("PL");

		questionMarkLabel = new JLabel("?");
		questionMarkLabel_1 = new JLabel("?");
		questionMarkLabel_2 = new JLabel("?");
	}

	private void setBounds() {
		questionMarkLabel.setBounds(385, 25, 20, 23);
		questionMarkLabel_1.setBounds(282, 171, 20, 23);
		questionMarkLabel_2.setBounds(410, 293, 29, 23);
		checkSecondQButton.setBounds(221, 213, 97, 23);
		enButton.setBounds(353, 439, 52, 23);
		checkThirdQButton.setBounds(221, 364, 97, 23);
		secondQuestionLabel.setBounds(24, 133, 222, 31);
		thirdQuestionLabel.setBounds(24, 289, 291, 31);
		plButton.setBounds(291, 439, 52, 23);
		checkFirstQButton.setBounds(221, 59, 97, 23);
		firstQuestionLabel.setBounds(24, 25, 307, 23);
		firstQField.setBounds(132, 59, 52, 23);
		firstQComboBox.setBounds(255, 26, 125, 22);
		firstQCorrectAnswerLabel.setBounds(24, 90, 267, 20);
		yourAns1Label.setBounds(24, 59, 111, 20);
		secondQComboBox.setBounds(177, 138, 125, 22);
		secondQuestionPart2Label.setBounds(24, 171, 222, 31);
		secondQAmount.setBounds(199, 175, 80, 20);
		secondQCorrectAnswerLabel.setBounds(24, 247, 345, 20);
		thousandLabel.setBounds(177, 62, 57, 14);
		yourAns2Label.setBounds(24, 217, 111, 20);
		secondQField.setBounds(132, 214, 52, 23);
		thirdQComboBox.setBounds(302, 294, 103, 22);
		thirdQuestionPart2Label.setBounds(24, 327, 291, 31);
		yourAns3Label.setBounds(24, 366, 111, 20);
		thirdQField.setBounds(132, 363, 52, 23);
		thirdQCorrectAnswerLabel.setBounds(24, 397, 345, 20);
	}

	private void setFonts() {

		checkFirstQButton
				.setFont(checkFirstQButton.getFont().deriveFont(checkFirstQButton.getFont().getStyle() | Font.BOLD));
		checkSecondQButton
				.setFont(checkSecondQButton.getFont().deriveFont(checkSecondQButton.getFont().getStyle() | Font.BOLD));
		checkThirdQButton
				.setFont(checkThirdQButton.getFont().deriveFont(checkThirdQButton.getFont().getStyle() | Font.BOLD));
		secondQCorrectAnswerLabel.setFont(secondQCorrectAnswerLabel.getFont()
				.deriveFont(secondQCorrectAnswerLabel.getFont().getStyle() | Font.BOLD));
		thirdQCorrectAnswerLabel.setFont(thirdQCorrectAnswerLabel.getFont()
				.deriveFont(thirdQCorrectAnswerLabel.getFont().getStyle() | Font.BOLD));
		firstQuestionLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		secondQuestionLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		thirdQuestionLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		yourAns1Label.setFont(new Font("Tahoma", Font.PLAIN, 12));
		firstQCorrectAnswerLabel.setFont(firstQCorrectAnswerLabel.getFont()
				.deriveFont(firstQCorrectAnswerLabel.getFont().getStyle() | Font.BOLD));
		firstQField.setFont(new Font("Tahoma", Font.PLAIN, 12));
		thousandLabel.setHorizontalAlignment(SwingConstants.LEFT);
		thousandLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		thirdQuestionPart2Label.setFont(new Font("Tahoma", Font.PLAIN, 12));
		secondQuestionPart2Label.setFont(new Font("Tahoma", Font.PLAIN, 12));
		secondQField.setFont(new Font("Tahoma", Font.PLAIN, 12));
		yourAns2Label.setFont(new Font("Tahoma", Font.PLAIN, 12));
		yourAns3Label.setFont(new Font("Tahoma", Font.PLAIN, 12));
		thirdQField.setFont(new Font("Tahoma", Font.PLAIN, 12));
		questionMarkLabel_2.setFont(new Font("Tahoma", Font.PLAIN, 12));
		questionMarkLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		questionMarkLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
	}

	private void addElementsToPane() {
		contentPane.add(thirdQuestionLabel);
		contentPane.add(checkSecondQButton);
		contentPane.add(secondQuestionLabel);
		contentPane.add(checkThirdQButton);
		contentPane.add(enButton);
		contentPane.add(plButton);
		contentPane.add(firstQuestionLabel);
		contentPane.add(checkFirstQButton);
		contentPane.add(firstQComboBox);
		contentPane.add(firstQField);
		contentPane.add(firstQCorrectAnswerLabel);
		contentPane.add(yourAns1Label);
		contentPane.add(secondQComboBox);
		contentPane.add(secondQuestionPart2Label);
		contentPane.add(secondQAmount);
		contentPane.add(yourAns2Label);
		contentPane.add(secondQField);
		contentPane.add(secondQCorrectAnswerLabel);
		contentPane.add(thousandLabel);
		contentPane.add(thirdQuestionPart2Label);
		contentPane.add(yourAns3Label);
		contentPane.add(thirdQComboBox);
		contentPane.add(thirdQField);
		contentPane.add(thirdQCorrectAnswerLabel);
		contentPane.add(questionMarkLabel);
		contentPane.add(questionMarkLabel_1);
		contentPane.add(questionMarkLabel_2);
	}
}
