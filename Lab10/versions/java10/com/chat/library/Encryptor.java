package com.chat.library;

import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class Encryptor {
	private PrivateKey privateKey;
	private String othersPublicKeyString;
	private PublicKey othersPublicKey;
	private Receiver receiver;
	private Cipher cipher;

	public Encryptor() {
	}

	public Encryptor(Receiver receiver, PrivateKey privateKey, String othersPublicKey)
			throws InvalidKeySpecException, NoSuchAlgorithmException {
		super();
		this.receiver = receiver;
		this.privateKey = privateKey;
		this.othersPublicKeyString = othersPublicKey;
		this.othersPublicKey = convertStringToPublicKey();
		this.receiver.setEncryptor(this);
	}


	public String getOthersPublicKey() {
		return othersPublicKeyString;
	}


	private PublicKey convertStringToPublicKey() throws InvalidKeySpecException, NoSuchAlgorithmException {
		return KeyFactory.getInstance("RSA")
				.generatePublic(new X509EncodedKeySpec(Base64.getDecoder().decode(othersPublicKeyString)));
	}
	
	public String encrypt(String msg) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException {
		cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.ENCRYPT_MODE, othersPublicKey);
        byte[] encrypt = cipher.doFinal(msg.getBytes());
        return Base64.getEncoder().encodeToString(encrypt);
	}
	
	public String decrypt(String msg) throws NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
		cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] decode = cipher.doFinal(Base64.getDecoder().decode(msg));
        return new String(decode);
	}

}
