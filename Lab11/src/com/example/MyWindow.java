package com.example;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.border.EmptyBorder;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.event.ActionEvent;

public class MyWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField vectorATextField;
	private JTextField vectorBTextField;
	private JTextField resultTextField;

	public static void main(String[] args) {

		System.loadLibrary("Vector-dot-product-native");

		MyWindow frame = new MyWindow();
		frame.setVisible(true);

	}

	public MyWindow() {

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 318, 282);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblNewLabel = new JLabel("Wektora A: ");
		lblNewLabel.setBounds(10, 11, 67, 14);
		contentPane.add(lblNewLabel);

		JLabel lblWektoraB = new JLabel("Wektora B: ");
		lblWektoraB.setBounds(10, 39, 67, 14);
		contentPane.add(lblWektoraB);

		vectorATextField = new JTextField();
		vectorATextField.setBounds(79, 8, 165, 20);
		contentPane.add(vectorATextField);
		vectorATextField.setColumns(10);

		vectorBTextField = new JTextField();
		vectorBTextField.setColumns(10);
		vectorBTextField.setBounds(79, 36, 165, 20);
		contentPane.add(vectorBTextField);

		JSeparator separator = new JSeparator();
		separator.setBounds(0, 67, 302, 2);
		contentPane.add(separator);

		JButton multi01Btn = new JButton("multi01(Double[] a, Double[] b)");
		multi01Btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				try {
					VectorDotProduct vectorDotProduct = new VectorDotProduct();

					Double result = vectorDotProduct.multi01(getVectorA(), getVectorB());
					if (result != null)
						resultTextField.setText(result.toString());
				} catch (NumberFormatException e2) {
					JOptionPane.showMessageDialog(null, "Wpisz wektor jeszcze raz", "B³¹d", JOptionPane.ERROR_MESSAGE);

				}
			}
		});
		multi01Btn.setBounds(35, 80, 233, 23);
		contentPane.add(multi01Btn);

		JButton multi02Btn = new JButton("multi02(Double[] a)");
		multi02Btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				try {
					VectorDotProduct vectorDotProduct = new VectorDotProduct();
					vectorDotProduct.b = getVectorB();
					Double result = vectorDotProduct.multi02(getVectorA());
					if (result != null)
						resultTextField.setText(result.toString());
				} catch (NumberFormatException e2) {
					JOptionPane.showMessageDialog(null, "Wpisz wektor jeszcze raz", "B³¹d", JOptionPane.ERROR_MESSAGE);

				}
			}
		});
		multi02Btn.setBounds(35, 119, 233, 23);
		contentPane.add(multi02Btn);

		JButton multi03Btn = new JButton("multi03()");
		multi03Btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Thread(new Runnable() {

					@Override
					public void run() {
						VectorDotProduct vectorDotProduct = new VectorDotProduct();
						vectorDotProduct.multi03();
						if (vectorDotProduct.c != null)
							resultTextField.setText(vectorDotProduct.c.toString());
					}
				}).start();
			}
		});
		multi03Btn.setBounds(35, 167, 233, 23);
		contentPane.add(multi03Btn);

		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(0, 201, 302, 2);
		contentPane.add(separator_1);

		JLabel lblNewLabel_1 = new JLabel("Wynik: ");
		lblNewLabel_1.setBounds(10, 214, 55, 14);
		contentPane.add(lblNewLabel_1);

		resultTextField = new JTextField();
		resultTextField.setEditable(false);
		resultTextField.setBounds(64, 211, 95, 20);
		contentPane.add(resultTextField);
		resultTextField.setColumns(10);

		JSeparator separator_1_1 = new JSeparator();
		separator_1_1.setBounds(0, 154, 302, 2);
		contentPane.add(separator_1_1);
	}

	public Double[] getVectorA() throws NumberFormatException {

		String[] v = vectorATextField.getText().replaceAll(" ", "").split(",");

		ArrayList<Double> vectorArrayList = new ArrayList<>();

		for (String s : v) {
			vectorArrayList.add(Double.parseDouble(s));

		}
		return (Double[]) vectorArrayList.toArray(new Double[vectorArrayList.size()]);
	}

	public Double[] getVectorB() throws NumberFormatException {

		String[] v = vectorBTextField.getText().replaceAll(" ", "").split(",");

		ArrayList<Double> vectorArrayList = new ArrayList<>();

		for (String s : v)
			vectorArrayList.add(Double.parseDouble(s));

		return (Double[]) vectorArrayList.toArray(new Double[vectorArrayList.size()]);
	}
}
