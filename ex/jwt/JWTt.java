package jwt;

import org.jose4j.jwa.AlgorithmConstraints;
import org.jose4j.jwa.AlgorithmConstraints.ConstraintType;
import org.jose4j.jwe.ContentEncryptionAlgorithmIdentifiers;
import org.jose4j.jwe.JsonWebEncryption;
import org.jose4j.jwe.KeyManagementAlgorithmIdentifiers;
import org.jose4j.keys.AesKey;
import org.jose4j.lang.ByteUtil;

public class JWTt {
	// http://bitbucket.org/b_c/jose4j/wiki/Home
	// http://www.owasp.org/index.php/REST_Security_Cheat_Sheet#JWT
	// http://paragonie.com/blog/2017/03/jwt-json-web-tokens-is-bad-standard-that-everyone-should-avoid

	public static void main(String[] args) throws Throwable {
		AesKey key = new AesKey(ByteUtil.randomBytes(16));

		JsonWebEncryption jwe = new JsonWebEncryption();
		jwe.setPayload("Hello World!");

		jwe.setAlgorithmHeaderValue(KeyManagementAlgorithmIdentifiers.A128KW);
		jwe.setEncryptionMethodHeaderParameter(ContentEncryptionAlgorithmIdentifiers.AES_128_CBC_HMAC_SHA_256);
		jwe.setKey(key);
		String serializedJwe = jwe.getCompactSerialization();
		System.out.println("Serialized Encrypted JWE: " + serializedJwe);
		de(serializedJwe, key);
	}

	public static void de(String serializedJwe, AesKey key) throws Throwable {
		JsonWebEncryption jwe = new JsonWebEncryption();
		jwe.setAlgorithmConstraints(
				new AlgorithmConstraints(ConstraintType.WHITELIST, KeyManagementAlgorithmIdentifiers.A128KW));
		jwe.setContentEncryptionAlgorithmConstraints(new AlgorithmConstraints(ConstraintType.WHITELIST,
				ContentEncryptionAlgorithmIdentifiers.AES_128_CBC_HMAC_SHA_256));
		jwe.setKey(key);
		jwe.setCompactSerialization(serializedJwe);
		System.out.println("Payload: " + jwe.getPayload());
	}

}
