package ua.epam.finalproject.repairagency.service;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.security.NoSuchAlgorithmException;

import static org.junit.Assert.*;

public class HashPasswordTest {

    private String expected = "534529C7BC541D7FC695138173B204A28E3A3AB6B232FB4529B316403D37E13A";

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getHash() throws NoSuchAlgorithmException {
        String hash = HashPassword.getHash("point777");
        assertEquals(expected, hash);
    }
}