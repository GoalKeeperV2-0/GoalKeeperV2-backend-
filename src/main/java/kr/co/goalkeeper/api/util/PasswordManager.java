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
    public static String validatePassword(String password){
        String message ="";
        if(!isValidLength(password)){
            message+="비밀번호는 최소 8자 이상이어야 합니다.";
        }
        if(!hasLetterAndDigit(password)){
            message+="비밀번호에는 알파벳 대소문자나 숫자가 포함되어야 합니다.";
        }
        if(!hasNoWhitespace(password)){
            message+="비밀번호에는 공백이 포함되어선 안됩니다.";
        }
        if(!hasSpecialCharacter(password)){
            message+="비밀번호에는 특수문자가 포함되어야 합니다.";
        }
        return message;
    }
    private static boolean isValidLength(String password) {
        return password.length() >= 8;
    }
    private static boolean hasLetterAndDigit(String password) {
        boolean hasLetter = false;
        boolean hasDigit = false;
        for (char c : password.toCharArray()) {
            if (Character.isLetter(c)) {
                hasLetter = true;
            } else if (Character.isDigit(c)) {
                hasDigit = true;
            }
            if (hasLetter && hasDigit) {
                break;
            }
        }
        return hasLetter && hasDigit;
    }
    private static boolean hasSpecialCharacter(String password) {
        for (char c : password.toCharArray()) {
            if (!Character.isLetterOrDigit(c)) {
                return true;
            }
        }
        return false;
    }
    private static boolean hasNoWhitespace(String password) {
        return !password.contains(" ");
    }
}