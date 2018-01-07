package com.smx.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import android.util.Base64;

@SuppressWarnings({"rawtypes", "unchecked"})
public class EncryptDecrypt {
	
	public final static String strKey="DD5FEF9C1C1DA1394D6D34B248C51BE2AD740840";

	/**
	 * 
	 * @param rawKeyData
	 * @param str
	 * @return
	 * @throws InvalidKeyException
	 * @throws NoSuchAlgorithmException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 * @throws NoSuchPaddingException
	 * @throws InvalidKeySpecException
	 * @throws UnsupportedEncodingException 
	 */
	public static String encrypt(byte rawKeyData[], String str)
			throws InvalidKeyException, NoSuchAlgorithmException,
			IllegalBlockSizeException, BadPaddingException,
			NoSuchPaddingException, InvalidKeySpecException, UnsupportedEncodingException {

		SecureRandom sr = new SecureRandom();
		DESKeySpec dks = new DESKeySpec(rawKeyData);
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		SecretKey key = keyFactory.generateSecret(dks);
		Cipher cipher = Cipher.getInstance("DES");
		cipher.init(Cipher.ENCRYPT_MODE, key, sr);
		byte data[] = str.getBytes();
		byte[] encryptedData = cipher.doFinal(data);
		
		String encode = URLEncoder.encode(Base64.encodeToString(encryptedData,3),"UTF8");
		return encode;
	}

	/**
	 * 
	 * @param rawKeyData
	 * @param encryptedData
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 * @throws InvalidKeyException
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 * @throws InvalidKeySpecException
	 * @throws UnsupportedEncodingException 
	 */
	/*
	public static String decrypt(byte rawKeyData[], String encryptedData)
			throws IllegalBlockSizeException, BadPaddingException,
			InvalidKeyException, NoSuchAlgorithmException,
			NoSuchPaddingException, InvalidKeySpecException, UnsupportedEncodingException {
		SecureRandom sr = new SecureRandom();
		DESKeySpec dks = new DESKeySpec(rawKeyData);
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		SecretKey key = keyFactory.generateSecret(dks);
		Cipher cipher = Cipher.getInstance("DES");
		cipher.init(Cipher.DECRYPT_MODE, key, sr);
		
//		String decode = URLDecoder.decode(encryptedData);
		
		byte decryptedData[] = cipher.doFinal(Base64.decode(encryptedData));
		return new String(decryptedData);
	}
	*/
	public static Map getParamMap(String param){
		if(param == null)
			return null;
		String[] item = param.split("\\|");
		if(item != null && item.length>0){
			Map paramMap = new HashMap();
			for(int i=0; i<item.length; i++){
				String[] entry = item[i].split("=");
				if(entry != null && entry.length>1){
					paramMap.put(entry[0], entry[1]);
				}
			}
			return paramMap;
		}
		
		return null;
		
	}
}

