package ua.epam.finalproject.repairagency.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashPassword {

    private final static String HASH_METHOD = "SHA-1"; //todo получение из настроек
    private static MessageDigest digest;


    public static String getHash(String password) throws NoSuchAlgorithmException {
         digest = MessageDigest.getInstance(HASH_METHOD);
         byte[] bytes = digest.digest(password.getBytes());
         return new String(bytes);
    }
}
