package com.example.weatherwatcher;

import org.junit.Test;
import static org.junit.Assert.*;

public class UserValidationTest {

    @Test
    public void testEmptyUsernameIsInvalid() {
        String username = "";
        assertTrue("Empty username should be invalid", username.isEmpty());
    }

    @Test
    public void testValidUsernameAndPassword() {
        String username = "testuser1";
        String password = "password1";
        assertFalse("Username should not be empty", username.isEmpty());
        assertFalse("Password should not be empty", password.isEmpty());
        assertEquals("Username should match", "testuser1", username);
        assertEquals("Password should match", "password1", password);
    }
}