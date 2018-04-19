package com.yhs.encryption;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Random;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.xml.bind.DatatypeConverter;

import org.junit.Before;
import org.junit.Test;

public class SignatureRSATest {
	 
	    
	 //RSA test
	 private static final int DEFAULT_KEY_SIZE = 2048;
	 private static final String KEY_FACTORY_ALGORITHM = "RSA";
	 private static final String SIGNATURE_ALGORITHM = "SHA256withRSA"; 
	 private static final String CHARSET = "UTF-8";
    		
	@Before
	public void before() throws UnsupportedEncodingException {		
		
	}
		
		
	@Test
	public void encryptAndDecrypt() throws NoSuchAlgorithmException {
	    String plainText = "{}";
	    KeyPair keyPair = generateKeyPair();

	    byte[] encodedPublicKey = keyPair.getPublic().getEncoded();
	    byte[] encodedPrivateKey = keyPair.getPrivate().getEncoded();

	    String cipherText = encrypt(plainText, encodedPublicKey);
	    assertThat(plainText).isEqualTo(decrypt(cipherText, encodedPrivateKey));
	}
	

	// RSA sign And Verify
	@Test
	public void excuteSignature() throws NoSuchAlgorithmException {
		String plainText = "{yrdyddd-dfgd}";
		KeyPair keyPair = generateKeyPair();
		
		byte[] encodedPrivateKey = keyPair.getPrivate().getEncoded();
		byte[] encodedPublicKey = keyPair.getPublic().getEncoded();
		
		String signature = sign(plainText, encodedPrivateKey);
		System.out.println("signature = " + signature);
		assertThat(signature).isNotNull();
		 
		boolean result = verify(plainText, signature, encodedPublicKey);
	    assertThat(result).isTrue();
	}
	
	public static String sign(String plainText, byte[] encodedPrivateKey) {
	    try {
	        Signature privateSignature = Signature.getInstance(SIGNATURE_ALGORITHM);
	        privateSignature.initSign(generatePrivateKey(encodedPrivateKey));
	        privateSignature.update(plainText.getBytes(CHARSET));
	        byte[] signature = privateSignature.sign();
	        return Base64.getEncoder().encodeToString(signature);
	    } catch (NoSuchAlgorithmException | InvalidKeyException | UnsupportedEncodingException | SignatureException e) {
	        throw new RuntimeException(e);
	    }
	}
	
	public static boolean verify(String plainText, String signature, byte[] encodedPublicKey) {
	    PublicKey publicKey = generatePublicKey(encodedPublicKey);
	    return verifySignarue(plainText, signature, publicKey);
	}
	
	private static boolean verifySignarue(String plainText, String signature, PublicKey publicKey) {
	    Signature sig;
	    try {
	        sig = Signature.getInstance(SIGNATURE_ALGORITHM);
	        sig.initVerify(publicKey);
	        sig.update(plainText.getBytes());	
	        if (!sig.verify(Base64.getDecoder().decode(signature)))
	            throw new SignatureException("It was awesome! Signature hasn't be invalid");
	    } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
	    	throw new RuntimeException(e);
	    }
	    return true;
	}
	
	 public static String encrypt(String plainText, byte[] encodedPublicKey) throws NoSuchAlgorithmException {
        PublicKey publicKey = generatePublicKey(encodedPublicKey);
        try {
            Cipher cipher = Cipher.getInstance(KEY_FACTORY_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] bytes = cipher.doFinal(plainText.getBytes(CHARSET));
            return Base64.getEncoder().encodeToString(bytes);
        } catch (NoSuchPaddingException | InvalidKeyException | UnsupportedEncodingException | IllegalBlockSizeException | BadPaddingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String decrypt(String cipherText, byte[] encodedPrivateKey) throws NoSuchAlgorithmException {
        PrivateKey privateKey = generatePrivateKey(encodedPrivateKey);
        try {
            byte[] bytes = Base64.getDecoder().decode(cipherText);
            Cipher cipher = Cipher.getInstance(KEY_FACTORY_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            return new String(cipher.doFinal(bytes), CHARSET);
        } catch (NoSuchPaddingException | InvalidKeyException | UnsupportedEncodingException | IllegalBlockSizeException | BadPaddingException e) {
            throw new RuntimeException(e);
        }
    }
    

	private static PublicKey generatePublicKey(byte[] encodedPublicKey) {
	    try {
	        KeyFactory keyFactory = KeyFactory.getInstance(KEY_FACTORY_ALGORITHM);
	        return keyFactory.generatePublic(new X509EncodedKeySpec(encodedPublicKey));
	    } catch (NoSuchAlgorithmException e) {
	    	throw new RuntimeException(e);
	    } catch (InvalidKeySpecException e) {
	        throw new IllegalArgumentException(e);
	    }
	}
	
	private static PrivateKey generatePrivateKey(byte[] encodedPrivateKey) {
	    try {
	        KeyFactory keyFactory = KeyFactory.getInstance(KEY_FACTORY_ALGORITHM);
	        return keyFactory.generatePrivate(new PKCS8EncodedKeySpec(encodedPrivateKey));
	    } catch (NoSuchAlgorithmException e) {
	    	throw new RuntimeException(e);
	    } catch (InvalidKeySpecException e) {
	        throw new IllegalArgumentException(e);
	    }
	}
	
	public static KeyPair generateKeyPair() throws NoSuchAlgorithmException {
		KeyPairGenerator generator = KeyPairGenerator.getInstance(KEY_FACTORY_ALGORITHM);
		generator.initialize(DEFAULT_KEY_SIZE, new SecureRandom());
		KeyPair pair = generator.generateKeyPair();
		
		return pair;
	}
    
	private static byte[] generateRandom(int length) {
		byte[] buf = new byte[length];
		new Random().nextBytes(buf);
		return buf;
	}

	private static String generateRandomBase64(int length) {
		return new String(Base64.getEncoder().encode(generateRandom(length)));
	}

	private static String generateRandomHex(int length) {
		return DatatypeConverter.printHexBinary(generateRandom(length));
	}
	
		
}
