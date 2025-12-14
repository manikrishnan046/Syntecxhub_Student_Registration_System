package Student_Registration_System;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class PBKDF2Util {
    private static final String ALGO = "PBKDF2WithHmacSHA256";
    private static final int SALT_BYTES = 16;
    private static final int HASH_BYTES = 32; // 256 bits
    private static final int ITERATIONS = 100_000;

    public static byte[] generateSalt() {
        SecureRandom sr = new SecureRandom();
        byte[] salt = new byte[SALT_BYTES];
        sr.nextBytes(salt);
        return salt;
    }

	public static boolean verifyPassword(char[] password, byte[] salt, byte[] hash) {
		// TODO Auto-generated method stub
		return false;
	}

	public static byte[] hashPassword(char[] password, byte[] salt) {
		// TODO Auto-generated method stub
		return null;
	}
}