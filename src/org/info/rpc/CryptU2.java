package org.info.rpc;

import java.nio.charset.StandardCharsets;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CryptU2 {

	private static final Logger logger = LoggerFactory.getLogger(CryptU2.class);
	private static final String ALGORITHM = "AES";
	private byte[] key;
	// String key = "MZygpewJsCpRrfOr";

	public CryptU2(String pas) {
		int len = pas.length();
		logger.info("", len);
		if (len != 16)
			logger.warn("Password should be length of 16");
		this.key = pas.getBytes(StandardCharsets.UTF_8);
	}

	public byte[] encrypt2(byte[] data) throws Throwable {
		SecretKeySpec secretKey = new SecretKeySpec(key, ALGORITHM);
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.ENCRYPT_MODE, secretKey);

		return cipher.doFinal(data);
	}

	public byte[] decrypt2(byte[] cipherData) throws Throwable {
		SecretKeySpec secretKey = new SecretKeySpec(key, ALGORITHM);
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.DECRYPT_MODE, secretKey);

		return cipher.doFinal(cipherData);
	}
}
