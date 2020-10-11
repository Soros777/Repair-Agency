package ua.epam.finalproject.repairagency.service;

import org.apache.log4j.Logger;
import ua.epam.finalproject.repairagency.exeption.AppException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;

public class HashPassword {

    private final static String HASH_METHOD = "SHA-256"; //todo получение из настроек
    private static MessageDigest digest;
    private static final Logger Log = Logger.getLogger(HashPassword.class);


    public static String getHash(String password) {
        Log.trace("getting hash");
        try {
            digest = MessageDigest.getInstance(HASH_METHOD);
        } catch (NoSuchAlgorithmException e) {
            Log.error("Can't get hash cause : " + e);
            throw new AppException("Can't get hash", e);
        }
        digest.update(LocalDate.now().toString().getBytes());
        byte[] bytes = digest.digest(password.getBytes());
        StringBuilder resultSb = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(b).toUpperCase();
            if(b < 0) {
                hex = hex.substring(hex.length()-2);
            }
            if(hex.length() == 1) {
                hex = "0" + hex;
            }
            resultSb.append(hex);
        }
        Log.trace("hash is gotten");
        return resultSb.toString();
    }

    public static void main(String[] args) {
        String pass = "point777";
        String hash = getHash(pass);
        System.out.println(hash.length());
        System.out.println(hash);
    }
}
