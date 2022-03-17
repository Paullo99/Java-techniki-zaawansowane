package com.chat.app;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import com.chat.library.ChatController;
import com.chat.library.ChattingListener;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.security.NoSuchAlgorithmException;
import java.awt.event.ActionEvent;

public class Window extends JFrame implements ChattingListener {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField hostTF;
	private JTextField sendPortTF;
	private JTextField receivePortTF;
	private JButton listenButton;
	private JTextArea receivedMessageTF;
	private JTextArea sentMessageTF;
	private JLabel youLabel;
	private JLabel strangerLabel;
	private JButton sendButton;

	private ChatController chatController;
	private JButton setupRSAKeysButton;

	public static void main(String[] args) {
		//System.setProperty("java.security.policy", "./java.policy");
		//System.setSecurityManager(new SecurityManager());
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Window frame = new Window();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Window() {
		setTitle("Aplikacja dla Javy 10");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 493, 325);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		hostTF = new JTextField();
		sendPortTF = new JTextField();
		receivePortTF = new JTextField();
		listenButton = new JButton("Rozpocznij czat");
		receivedMessageTF = new JTextArea();
		receivedMessageTF.setLineWrap(true);
		sentMessageTF = new JTextArea();
		sentMessageTF.setLineWrap(true);
		sendButton = new JButton("Wy\u015Blij");
		setupRSAKeysButton = new JButton("W\u0142\u0105cz szyfrowanie");

		JLabel hostLabel = new JLabel("Host: ");
		hostLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		hostLabel.setBounds(10, 11, 199, 14);
		contentPane.add(hostLabel);

		JLabel sendPortLabel = new JLabel("Port wysy\u0142anych wiadomo\u015Bci: ");
		sendPortLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		sendPortLabel.setBounds(10, 39, 199, 14);
		contentPane.add(sendPortLabel);

		JLabel receivePortLabel = new JLabel("Port otrzymywanych wiadomo\u015Bci: ");
		receivePortLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		receivePortLabel.setBounds(10, 68, 199, 14);
		contentPane.add(receivePortLabel);

		hostTF.setBounds(219, 8, 99, 20);
		contentPane.add(hostTF);
		hostTF.setColumns(10);

		sendPortTF.setColumns(10);
		sendPortTF.setBounds(219, 36, 99, 20);
		contentPane.add(sendPortTF);

		receivePortTF.setColumns(10);
		receivePortTF.setBounds(219, 65, 99, 20);
		contentPane.add(receivePortTF);

		listenButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				chatController = new ChatController(Integer.parseInt(sendPortTF.getText()),
						Integer.parseInt(receivePortTF.getText()), hostTF.getText());
				chatController.startReceiverThread(Window.this);

				receivePortTF.setEnabled(false);
				sendPortTF.setEnabled(false);
				hostTF.setEnabled(false);
				listenButton.setEnabled(false);
				setupRSAKeysButton.setEnabled(true);
				
			}
		});
		listenButton.setBounds(302, 96, 148, 23);
		contentPane.add(listenButton);

		receivedMessageTF.setEditable(false);
		receivedMessageTF.setBounds(82, 161, 368, 36);
		contentPane.add(receivedMessageTF);
		receivedMessageTF.setColumns(10);

		sentMessageTF.setColumns(10);
		sentMessageTF.setBounds(82, 208, 368, 36);
		contentPane.add(sentMessageTF);

		youLabel = new JLabel("Ty:");
		youLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		youLabel.setBounds(10, 219, 62, 14);
		contentPane.add(youLabel);

		strangerLabel = new JLabel("Otrzymano:");
		strangerLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		strangerLabel.setBounds(0, 172, 72, 14);
		contentPane.add(strangerLabel);

		sendButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if(sentMessageTF.getText().length() > 117)
					JOptionPane.showMessageDialog(null, "D�ugo�� wiadomo�ci nie mo�e by� d�u�sza ni� 117 znak�w!");
				else
					chatController.sendMessage(sentMessageTF.getText());
				
			}
		});
		sendButton.setBounds(361, 252, 89, 23);
		contentPane.add(sendButton);
		setupRSAKeysButton.setEnabled(false);
		sendButton.setEnabled(false);

		
		setupRSAKeysButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					chatController.cipherConversation();
					sendButton.setEnabled(true);
					setupRSAKeysButton.setEnabled(false);
				} catch (NoSuchAlgorithmException e1) {
					e1.printStackTrace();
				}

			}
		});
		setupRSAKeysButton.setBounds(302, 130, 148, 20);
		contentPane.add(setupRSAKeysButton);
	}

	@Override
	public void messageReceived(String theLine) {
		receivedMessageTF.setText(theLine);
	}
}
