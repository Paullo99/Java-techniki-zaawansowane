package com.chat.library;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class ChatController {

	private int senderPort;
	private String host;
	private Receiver receiver;
	private RSAKeyGenerator rsaKeyGenerator;
	private Encryptor encryptor = null;

	public ChatController(int senderPort, int receiverPort, String host) {
		this.senderPort = senderPort;
		this.host = host;
		receiver = new Receiver(receiverPort);
		try {
			rsaKeyGenerator = new RSAKeyGenerator();
		} catch (NoSuchAlgorithmException e) {
		}
	}

	public void startReceiverThread(ChattingListener chattingListener) {
		receiver.addMyListener(chattingListener);
		receiver.start(rsaKeyGenerator);
	}

	public void sendMessage(String msg) {
		try {
			if (encryptor == null)
				try {
					encryptor = new Encryptor(receiver, rsaKeyGenerator.getPrivateKey(), receiver.getOthersPublicKey());
				} catch (InvalidKeySpecException e) {}
			
			new Sender().send(encryptor.encrypt(msg), host, senderPort);
		} catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException | NoSuchAlgorithmException
				| NoSuchPaddingException e) {
			e.printStackTrace();
		}
	}

	public void cipherConversation() throws NoSuchAlgorithmException {
		new Sender().send(rsaKeyGenerator.getPublicKeyToString(), host, senderPort);
	}


}
