package com.yhs.encryption;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import org.junit.Before;
import org.junit.Test;
import org.springframework.util.StringUtils;

import lombok.extern.slf4j.Slf4j;


@Slf4j
public class CipherOnFileTest {
	 // 파일 처리용 버퍼 크기
	 private static final int BUFFER_SIZE = 8192;
	 
	 public static final int ENCRYPT_MODE = javax.crypto.Cipher.ENCRYPT_MODE;
	 public static final int DECRYPT_MODE = javax.crypto.Cipher.DECRYPT_MODE;
	    
	String algorithm;
	String keyAlgorithm;	
	String keyHex;
	String ivHex;
	AlgorithmParameterSpec parameterSpec;
	SecretKey secretKey;
	SecretKeyFactory keyFactory;
	
	String plainText, inputFile, outputFile, outputFile_buffer, outputFile_dec;
    		
	@Before
	public void before() throws UnsupportedEncodingException {		
		inputFile = "D://recentWk/DRM/subtitle/test/sub_org.vtt";
		outputFile = "D://recentWk/DRM/subtitle/test/sub_org_enc_" + System.currentTimeMillis() + ".vtt";
		outputFile_buffer = "D://recentWk/DRM/subtitle/test/sub_org_enc_" + System.currentTimeMillis() + "_buf.vtt";
		outputFile_dec = "D://recentWk/DRM/subtitle/test/sub_org_enc_" + System.currentTimeMillis() + "_dec.vtt";	
		
		
		
		//(AES-128-CBC)
		algorithm = "AES/CBC/PKCS5Padding";
		keyAlgorithm = algorithm.split("/")[0];
		
		//DB에 저장할 keySpec
		keyHex = "20D36266BCDB5716332F2D34024BB9D5";
		ivHex = "787C3BCA7252A701EC9E8B69666228B6";		
		//keyHex = generateRandomHex(16);
		//ivHex = generateRandomHex(16);
		
		/*SecureRandom random = new SecureRandom();
		byte[] iv = random.generateSeed(16);
		key = generateKey(iv);*/
		
				
		// iv 값 설정
		if (!StringUtils.isEmpty(ivHex)) {
			parameterSpec = new IvParameterSpec(DatatypeConverter.parseHexBinary(ivHex));
		} else {
			parameterSpec = null;
		}
		
		// keyFactory 설정
		try {
			keyFactory = SecretKeyFactory.getInstance(keyAlgorithm);
		} catch (NoSuchAlgorithmException exception) {
			keyFactory = null;
		}
		
		// secretKey 설정		
       if (!StringUtils.isEmpty(keyHex)) {
            secretKey = new SecretKeySpec(DatatypeConverter.parseHexBinary(keyHex), keyAlgorithm);
        } else {
            secretKey = null;
        }
		
	}

	
	@Test
    public void 단일_executeEncFile() throws NoSuchAlgorithmException {
				
		//cipher 초기화 (AES-128-CBC)
		Cipher cipher;
		try {
			cipher = Cipher.getInstance(algorithm);
		
		
		// hex -> byte -> Key object		
		/*byte[] keyBytes = DatatypeConverter.parseHexBinary(keyHex);
		Key key = generateKey(keyBytes);*/
		
				
		
		//cipher.init()
		cipher.init(ENCRYPT_MODE, secretKey, parameterSpec);		
		//file encyrption
		excuteEncDecFile(cipher, inputFile, outputFile);
		System.out.println("File encryption OK! ========================");
		
		//cipher.init()
		cipher.init(DECRYPT_MODE, secretKey, parameterSpec);	
		// file decrption
		excuteEncDecFile(cipher, outputFile, outputFile_dec);
		System.out.println("File decrption OK! ========================");
		
		
		} catch (Exception  e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
	 }
	
	@Test
    public void 단일_executeEncFileBuffer() throws NoSuchAlgorithmException {
				
		//cipher 초기화 (AES-128-CBC)
		Cipher cipher;
		try {
			cipher = Cipher.getInstance(algorithm);
		
		
		// hex -> byte -> Key object		
		/*byte[] keyBytes = DatatypeConverter.parseHexBinary(keyHex);
		Key key = generateKey(keyBytes);*/
		
				
		
		//cipher.init()
		cipher.init(ENCRYPT_MODE, secretKey, parameterSpec);		
		
		//file encyrption
		long startTime = System.currentTimeMillis();
		excuteEncDecFile(cipher, inputFile, outputFile);
		long endTime = System.currentTimeMillis() - startTime;
		System.out.println("일반 스트림: " + endTime);

		//file encyrption
		startTime = System.currentTimeMillis();
		excuteEncDecFileBuffer(cipher, inputFile, outputFile_buffer);
		endTime = System.currentTimeMillis() - startTime;
		System.out.println("버퍼 스트림: " + endTime);
		
		
		} catch (Exception  e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
	 }
	
	@Test
    public void 통합_같은키_executeEncFile() throws Exception {
		//path init
		String mainPath = "D://recentWk/DRM/subtitle/test";
		String sourceDir = "/sub_60sec_list";
		String targetDir = "/sub_60sec_list_3_enc";
		String targetDirDec = "/sub_60sec_list_3_dec";
		
		//m3u8 file parsing
		List<String> m3u8List = getParseM3u8List(mainPath + sourceDir + "/playlist.m3u8");
		
		//cipher init
		Cipher cipher = Cipher.getInstance(algorithm);
		cipher.init(ENCRYPT_MODE, secretKey, parameterSpec);	
		
		//segment file encrption
		//file encyrption
		for(String m3u3FileName : m3u8List) {
			String inputF = mainPath + sourceDir + "/" + m3u3FileName;
			String outputF =  mainPath + targetDir + "/" + m3u3FileName;
			
			excuteEncDecFile(cipher, inputF, outputF);
			System.out.println("File encryption OK! ========================\n" + inputF);	
		}
		
		
		//cipher init
		cipher.init(DECRYPT_MODE, secretKey, parameterSpec);	
		
		//segment file encrption
		//file decryption
		for(String m3u3FileName : m3u8List) {
			String inputF = mainPath + targetDir + "/" + m3u3FileName;
			String outputF =  mainPath + targetDirDec + "/" + m3u3FileName;
			
			excuteEncDecFile(cipher, inputF, outputF);
			System.out.println("File decrption OK! ========================\n" + inputF);	
		}
		
	}
	
	@Test
    public void 통합_다른키_executeEncFile() throws Exception {
		//path init
		String mainPath = "D://recentWk/DRM/subtitle/test";
		String sourceDir = "/sub_60sec_list";
		String targetDir = "/sub_60sec_list_8_enc";
		String targetDirDec = "/sub_60sec_list_8_dec";
		
		//m3u8 file parsing
		List<String> m3u8List = getParseM3u8List(mainPath + sourceDir + "/playlist.m3u8");
		
		//cipher create AES-128
		Cipher cipher = Cipher.getInstance(algorithm);		
		
		//segment file encrption			
		for(String m3u3FileName : m3u8List) {
			String inputF = mainPath + sourceDir + "/" + m3u3FileName;
			String outputF =  mainPath + targetDir + "/" + m3u3FileName;
			
						
			//key create 128 bit key = hmacMD5(radom key + caption counter)
			String subtitleCounter = m3u3FileName.substring(0, m3u3FileName.indexOf("."));
			String hmacmd5 = excuteHmac(keyHex + subtitleCounter , "eotnlqhem", "HmacMD5");			
			secretKey = new SecretKeySpec(DatatypeConverter.parseHexBinary(hmacmd5), keyAlgorithm);
			
			// cipher init
			cipher.init(ENCRYPT_MODE, secretKey, parameterSpec);
			
			excuteEncDecFile(cipher, inputF, outputF);
			System.out.println("File encryption OK! ========================\n" + outputF);	
			
		}
				
		//segment file decryption
		for(String m3u3FileName : m3u8List) {
			String inputF = mainPath + targetDir + "/" + m3u3FileName;
			String outputF =  mainPath + targetDirDec + "/" + m3u3FileName;
			
			//key create 128 bit key = hmacMD5(radom key + caption counter)
			String subtitleCounter = m3u3FileName.substring(0, m3u3FileName.indexOf("."));
			String hmacmd5 = excuteHmac(keyHex + subtitleCounter, "eotnlqhem", "HmacMD5");			
			secretKey = new SecretKeySpec(DatatypeConverter.parseHexBinary(hmacmd5), keyAlgorithm);
			
			//cipher init
			cipher.init(DECRYPT_MODE, secretKey, parameterSpec);	
			
			excuteEncDecFile(cipher, inputF, outputF);
			System.out.println("File decrption OK! ========================\n" + outputF);	
		}
		
	}
	
		
	// hamc
	@Test
	public void excuteHmac() {
		System.out.println("plain text: " + keyHex);
		
		// hmacSha1
		String hmacSha1 = excuteHmac(keyHex, "eotnlqhem", "HmacSHA1");
		System.out.println("HmacSHA1: " + hmacSha1);
		
		// hmacSha1 
		hmacSha1 = excuteHmac(keyHex+1, "eotnlqhem", "HmacSHA1");
		System.out.println("HmacSHA1 +1: " + hmacSha1);
		
		// hmacSha1 
		hmacSha1 = excuteHmac(keyHex+1, "eotnlqhe", "HmacSHA1");
		System.out.println("HmacSHA1 +1 +otherkey: " + hmacSha1);
		
		// hmacSha1 
		hmacSha1 = excuteHmac(keyHex+2, "eotnlqhem", "HmacSHA1");
		System.out.println("HmacSHA1 +2: " + hmacSha1);
		
		
		/////////////////////////////////////////////////////////////
		// hmacmd5
		String hmacmd5 = excuteHmac(keyHex, "eotnlqhem", "HmacMD5");
		System.out.println("Hmacmd5: " + hmacmd5);		
		
		// hmacmd5 
		hmacmd5 = excuteHmac(keyHex+1, "eotnlqhem", "HmacMD5");
		System.out.println("Hmacmd5 +1: " + hmacmd5);
		
		// hmacmd5 
		hmacmd5 = excuteHmac(keyHex+1, "eotnlqhe", "HmacMD5");
		System.out.println("Hmacmd5 +1 +otherkey: " + hmacmd5);
		
		// hmacmd5 
		hmacmd5 = excuteHmac(keyHex+2, "eotnlqhem", "HmacMD5");
		System.out.println("Hmacmd5 +2: " + hmacmd5);
	}
	
	// MD5
	@Test
	public void excuteMessageDigest() throws NoSuchAlgorithmException {
				
		System.out.println("plain text: " + keyHex);
		
		// md5 16 byte
		String md5 = excuteMessageDigest(DatatypeConverter.parseHexBinary(keyHex), "MD5");
		System.out.println("md5 (byHex): " + md5);
		md5 = excuteMessageDigest(keyHex.getBytes(), "MD5");
		System.out.println("md5: " + md5);
		
		// sha-1 20 byte (160 bit)
		String sha128 = excuteMessageDigest(DatatypeConverter.parseHexBinary(keyHex), "SHA-1");
		System.out.println("sha128 (byHex): " + sha128);
		sha128 = excuteMessageDigest(keyHex.getBytes(), "SHA-1");
		System.out.println("sha128: " + sha128);
		
		// sha-256 32 byte
		String sha256 = excuteMessageDigest(DatatypeConverter.parseHexBinary(keyHex), "SHA-256");
		System.out.println("sha256 (byHex): " + sha256);
		sha256 = excuteMessageDigest(keyHex.getBytes(), "SHA-256");
		System.out.println("sha256: " + sha256);
		
		
	}
	
	
	public String excuteHmac(String dataStr, String keyStr, String algorithm){		
		return excuteHmac(dataStr.getBytes(), keyStr.getBytes(), algorithm);
	}
	
	public String excuteHmac(byte[] mdByte, String keyStr, String algorithm){		
		return excuteHmac(mdByte, keyStr.getBytes(), algorithm);
	}
	
	// hamc(md5 + secret key)
	// hmac (sha1 + secret key)
	public String excuteHmac(byte[] mdByte, byte[] keyByte, String algorithm){		
		 SecretKeySpec signingKey = new SecretKeySpec(keyByte, algorithm);	 
		 
		 Mac mac;
		try {
			 mac = Mac.getInstance(algorithm);
			 mac.init(signingKey);
			 
			 return  DatatypeConverter.printHexBinary(mac.doFinal(mdByte));
			 
		} catch (NoSuchAlgorithmException | InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		
		return null;
	}
	
	
	public String excuteMessageDigest(String str, String algorithm){		
		return excuteMessageDigest(str.getBytes(), algorithm);
	}

	public String excuteMessageDigest(byte[] mdByte, String algorithm){
		String SHA = ""; 
		try{

			MessageDigest sh = MessageDigest.getInstance(algorithm); 
			sh.update(mdByte); 
			byte byteData[] = sh.digest();

			/*StringBuffer sb = new StringBuffer(); 
			for(int i = 0 ; i < byteData.length ; i++){
				sb.append(Integer.toString((byteData[i]&0xff) + 0x100, 16).substring(1));
			}
			SHA = sb.toString();	*/	
			// 위 작업과 동일
			SHA = DatatypeConverter.printHexBinary(byteData);
			
		}catch(NoSuchAlgorithmException e){
			e.printStackTrace(); 
			SHA = null; 
		}

		return SHA;
	}

			
	
	//m3u8 file parsing
	@SuppressWarnings("finally")
    public  List<String> getParseM3u8List(String m3u8FilePath) {
		//String m3u8FilePath = "D://recentWk/DRM/subtitle/test/sub_60sec_list/playlist.m3u8";
		List<String> rList = new ArrayList<String>();
		
		BufferedReader reader = null;	
		try {
			reader = new BufferedReader(new FileReader(m3u8FilePath));
			String m3u8LineStr = null;
			
			while (true) {
				m3u8LineStr = reader.readLine().trim();
				if (m3u8LineStr == null) {
					break;
				}			
				// data handling
				if(!m3u8LineStr.startsWith("#EXT") && m3u8LineStr.indexOf(".vtt") > 0) {
					System.out.println(m3u8LineStr);
					rList.add(m3u8LineStr);
				}
			}
			
		} catch (Exception e) {
			log.error("parsing m3u8 ERROR");
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (final IOException e) {
					log.error("parsing m3u8 file close error");
				}
			}
			
			return rList;
		}		
	}
	
			
	
	 
    public static void excuteEncDecFile(Cipher cipher, String infile, String outfile) throws Exception {
    	File targetF = new File(outfile);
        File targetParentF = targetF.getParentFile();
        
        if (!targetParentF.exists()) {  
        	targetParentF.mkdirs();
        }    
        
        FileInputStream in = new FileInputStream(infile);
        FileOutputStream fileOut = new FileOutputStream(outfile);        

        CipherOutputStream out = new CipherOutputStream(fileOut, cipher);
        byte[] buffer = new byte[BUFFER_SIZE];
        int length;
        while ((length = in.read(buffer)) != -1)
            out.write(buffer, 0, length);
        in.close();
        out.close();
    }
    
    public static void excuteEncDecFileBuffer(Cipher cipher, String infile, String outfile) throws Exception {
    	File targetF = new File(outfile);
        File targetParentF = targetF.getParentFile();
        
        if (!targetParentF.exists()) {  
        	targetParentF.mkdirs();
        }    
        
        BufferedInputStream bin = new BufferedInputStream(new FileInputStream(infile));
        
        CipherOutputStream cout = new CipherOutputStream(new FileOutputStream(outfile), cipher);
        BufferedOutputStream bout = new BufferedOutputStream(cout);
                
        int length;
        while ((length = bin.read()) != -1)
        	bout.write(length);
        
        bin.close();
        bout.close();
    }
    
    
   /* @Test
    public void testFile() throws Exception {
    	  File f = new File("D://recentWk/DRM/subtitle/test/123456/33.vtt");
          String abPath = f.getAbsolutePath();
          String path =  f.getPath();
          File paraentFile  = f.getParentFile();
          
          
          if (paraentFile.exists()) {        	
          } else {
        	  paraentFile.mkdirs();
          }
          
    }
	  */

	
	public Key generateKey(byte[] key) throws InvalidKeySpecException, InvalidKeyException {
        SecretKeySpec keySpec = new SecretKeySpec(key, keyAlgorithm);
        if (keyFactory == null) {
            return keySpec;
        } else if ("DES".equalsIgnoreCase(keyAlgorithm)) {
            return keyFactory.generateSecret(new DESKeySpec(key));
        } else {
            return keyFactory.generateSecret(keySpec);
        }
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
