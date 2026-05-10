package com.example.weatherwatcher;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.weatherwatcher.database.AppDatabase;
import com.example.weatherwatcher.database.User;
import com.example.weatherwatcher.database.UserDAO;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class DatabaseTest {

    private AppDatabase db;
    private UserDAO userDAO;

    @Before
    public void setup() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase.class)
                .allowMainThreadQueries()
                .build();
        userDAO = db.userDAO();
    }

    @Test
    public void testInsertAndRetrieveUser() {
        User user = new User();
        user.username = "testuser1";
        user.password = "password1";
        user.isAdmin  = false;

        userDAO.insert(user);

        User retrieved = userDAO.getUserByUsername("testuser1");
        assertNotNull("User should exist after insert", retrieved);
        assertEquals("Username should match", "testuser1", retrieved.username);
        assertEquals("Password should match", "password1", retrieved.password);
        assertFalse("User should not be admin", retrieved.isAdmin);
    }

    @Test
    public void testLoginWithCorrectCredentials() {
        User user = new User();
        user.username = "admin2";
        user.password = "adminpass";
        user.isAdmin  = true;

        userDAO.insert(user);

        User loggedIn = userDAO.login("admin2", "adminpass");
        assertNotNull("Login should succeed with correct credentials", loggedIn);
        assertEquals("Username should match", "admin2", loggedIn.username);
        assertTrue("User should be admin", loggedIn.isAdmin);

        User badLogin = userDAO.login("admin2", "wrongpassword");
        assertNull("Login should fail with wrong password", badLogin);
    }

    @After
    public void teardown() {
        db.close();
    }
}
