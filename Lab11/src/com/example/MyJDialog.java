package com.example;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.event.ActionEvent;

public class MyJDialog extends JFrame {

	private static final long serialVersionUID = 2605835095452924570L;
	private JPanel contentPane;
	private JTextField vectorATextField;
	private JLabel lblWektorB;
	private JTextField vectorBTextField;
	private boolean isBtnClicked = false;

	/**
	 * Create the frame.
	 */
	public MyJDialog() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 229, 157);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel vectorALbl = new JLabel("Wektor A [                              ]");
		vectorALbl.setFont(new Font("Tahoma", Font.PLAIN, 13));
		vectorALbl.setBounds(10, 11, 262, 14);
		contentPane.add(vectorALbl);

		vectorATextField = new JTextField();
		vectorATextField.setBounds(75, 8, 110, 20);
		contentPane.add(vectorATextField);
		vectorATextField.setColumns(10);

		lblWektorB = new JLabel("Wektor B [                              ]");
		lblWektorB.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblWektorB.setBounds(10, 46, 235, 14);
		contentPane.add(lblWektorB);

		vectorBTextField = new JTextField();
		vectorBTextField.setColumns(10);
		vectorBTextField.setBounds(75, 43, 110, 20);
		contentPane.add(vectorBTextField);

		JButton confirmButton = new JButton("Zatwierdz");
		confirmButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				isBtnClicked = true;
			}
		});
		confirmButton.setBounds(102, 84, 101, 23);
		contentPane.add(confirmButton);

		setVisible(true);
	}

	public boolean getIsBtnClicked() {
		return this.isBtnClicked;
	}

	public Double[] getVectorA() {

		String[] v = vectorATextField.getText().replaceAll(" ", "").split(",");

		ArrayList<Double> vectorArrayList = new ArrayList<>();

		for (String s : v)
			vectorArrayList.add(Double.parseDouble(s));

		return (Double[]) vectorArrayList.toArray(new Double[vectorArrayList.size()]);
	}

	public Double[] getVectorB() {

		String[] v = vectorBTextField.getText().replaceAll(" ", "").split(",");

		ArrayList<Double> vectorArrayList = new ArrayList<>();

		for (String s : v)
			vectorArrayList.add(Double.parseDouble(s));

		return (Double[]) vectorArrayList.toArray(new Double[vectorArrayList.size()]);
	}
}
