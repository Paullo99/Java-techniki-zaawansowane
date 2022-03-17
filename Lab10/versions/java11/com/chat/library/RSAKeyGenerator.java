package com.chat.library;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

public class RSAKeyGenerator {
	
	private PublicKey publicKey;
	private PrivateKey privateKey;
	
	public RSAKeyGenerator() throws NoSuchAlgorithmException {	
		KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(1024);
        KeyPair pair = keyGen.generateKeyPair();
        this.privateKey =  pair.getPrivate();
        this.publicKey = pair.getPublic();
	}


	public PublicKey getPublicKey() {
		return publicKey;
	}
	
	public String getPublicKeyToString() {
		return Base64.getEncoder().encodeToString(publicKey.getEncoded());
	}

	public PrivateKey getPrivateKey() {
		return privateKey;
	}
		
}
