package kr.co.goalkeeper.api.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
@NoArgsConstructor(access = AccessLevel.NONE)
public class PasswordManager {
    private static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final SecureRandom rnd = new SecureRandom();
    private static final int n = 3;

    public static String randomPassword(int len) {
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++)
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        return sb.toString();
    }
    public static String sha256(String rawPassword) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            String passsword = rawPassword;
            for (int i = 0; i < n; i++) {
                byte[] hash = messageDigest.digest(passsword.getBytes(StandardCharsets.UTF_8));
                passsword = new String(hash, StandardCharsets.UTF_8);
            }
            return passsword;
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Exception thrown for incorrect algorithm: " + e);
            return null;
        }
    }
}