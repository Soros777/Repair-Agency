package ua.epam.finalproject.repairagency.service;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.security.NoSuchAlgorithmException;

import static org.junit.Assert.*;

public class HashPasswordTest {

    private String expected = "ѕ*йїEп:Ю%\u0016УNбЄ\u000FЭ Ъ–�";

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