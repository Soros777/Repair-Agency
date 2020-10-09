package ua.epam.finalproject.repairagency.service;

import org.apache.log4j.Logger;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashPassword {

    private final static String HASH_METHOD = "SHA-1"; //todo получение из настроек
    private static MessageDigest digest;
    private static final Logger Log = Logger.getLogger(HashPassword.class);


    public static String getHash(String password) throws NoSuchAlgorithmException {
        Log.trace("getting hash");
        digest = MessageDigest.getInstance(HASH_METHOD);
        byte[] bytes = digest.digest(password.getBytes());
        Log.trace("hash is gotten");
        return new String(bytes);
    }

    public static void main(String[] args) throws NoSuchAlgorithmException {
        String pass = "point777";
        System.out.println(getHash(pass));
    }
}
