package hr.algebra.pi.services;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Random;

public final class PasswordService {
    private static final Random RANDOM = new SecureRandom();
    private static final int KEY_LENGTH = 256;
    private static final int ITERATIONS = 65536;

    private PasswordService(){}

    public static String generateNewSalt() {
        byte[] salt = new byte[16];
        RANDOM.nextBytes(salt);
        return new String(salt);
    }

    public static String hashStringWithSalt(String startingPassword, byte[] salt){
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        bCryptPasswordEncoder.encode(startingPassword);

        KeySpec spec = new PBEKeySpec(startingPassword.toCharArray(), salt, ITERATIONS, KEY_LENGTH);
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] hash = factory.generateSecret(spec).getEncoded();
            return new String(hash);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }
}
