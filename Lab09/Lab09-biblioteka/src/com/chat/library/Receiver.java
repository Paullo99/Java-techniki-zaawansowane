package com.chat.library;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class Receiver {
	private List<ChattingListener> ml = new ArrayList<ChattingListener>();
	private Thread t = null;
	private int port = 0;
	private ServerSocket s = null;
	private String othersPublicKey = null;
	private Encryptor encryptor = null;

	public void stop() {
		t.interrupt();
		try {
			s.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void start(RSAKeyGenerator rsaKeyGenerator) {		
		t = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					s = new ServerSocket(port);
					while (true) {
						Socket sc = s.accept();
						InputStream is = sc.getInputStream();
						InputStreamReader isr = new InputStreamReader(is);
						BufferedReader br = new BufferedReader(isr);
						String theLine = br.readLine();
						if (othersPublicKey == null) {
							try {
								othersPublicKey = theLine;
								encryptor = new Encryptor(Receiver.this, rsaKeyGenerator.getPrivateKey(), othersPublicKey);
							} catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
								e.printStackTrace();
							}
						}

						else {
								ml.forEach((item) -> {
									try {
										item.messageReceived(encryptor.decrypt(theLine));
									} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException
											| IllegalBlockSizeException | BadPaddingException e) {
										e.printStackTrace();
									}
								});

						}

						sc.close();
					}
				} catch (SocketException e) {

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		t.start();
	}

	public void addMyListener(ChattingListener m) {
		ml.add(m);
	}

	public void removeMyListener(ChattingListener m) {
		ml.remove(m);
	}

	Receiver(int port) {
		this.port = port;
	}

	public String getOthersPublicKey() {
		return othersPublicKey;
	}


	public void setEncryptor(Encryptor encryptor) {
		this.encryptor = encryptor;
	}

}
