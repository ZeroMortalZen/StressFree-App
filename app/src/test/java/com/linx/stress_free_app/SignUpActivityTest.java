package com.linx.stress_free_app;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SignUpActivityTest {
    private SignUpActivity signUpActivity;

    @Before
    public void setUp() {
        signUpActivity = new SignUpActivity();
    }

    @Test
    public void isEmailValid_validEmail_returnsTrue() {
        assertTrue(signUpActivity.isEmailValid("test@example.com"));
    }

    @Test
    public void isEmailValid_invalidEmail_returnsFalse() {
        assertFalse(signUpActivity.isEmailValid("invalid_email"));
    }

    @Test
    public void isPasswordValid_validPassword_returnsTrue() {
        assertTrue(signUpActivity.isPasswordValid("secure123"));
    }

    @Test
    public void isPasswordValid_invalidPassword_returnsFalse() {
        assertFalse(signUpActivity.isPasswordValid("123"));
    }
}
