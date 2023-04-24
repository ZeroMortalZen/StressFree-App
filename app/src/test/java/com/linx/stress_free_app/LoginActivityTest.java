package com.linx.stress_free_app;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class LoginActivityTest {
    private LoginActivity loginActivity;

    @Before
    public void setUp() {
        loginActivity = new LoginActivity();
    }

    @Test
    public void isEmailValid_validEmail_returnsTrue() {
        assertTrue(loginActivity.isEmailValid("test@example.com"));
    }

    @Test
    public void isEmailValid_invalidEmail_returnsFalse() {
        assertFalse(loginActivity.isEmailValid("invalid_email"));
    }

    @Test
    public void isPasswordValid_validPassword_returnsTrue() {
        assertTrue(loginActivity.isPasswordValid("secure123"));
    }

    @Test
    public void isPasswordValid_invalidPassword_returnsFalse() {
        assertFalse(loginActivity.isPasswordValid("123"));
    }
}
