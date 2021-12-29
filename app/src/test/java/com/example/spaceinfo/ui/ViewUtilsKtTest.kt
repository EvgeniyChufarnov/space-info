package com.example.spaceinfo.ui

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

internal class ViewUtilsKtTest {

    @Test
    fun isValidEmail_regular_email() {
        assertTrue(isValidEmail("name@email.com"))
    }

    @Test
    fun isValidEmail_empty_string() {
        assertFalse(isValidEmail(""))
    }

    @Test
    fun isValidEmail_no_suffix() {
        assertFalse(isValidEmail("name@email"))
    }

    @Test
    fun isValidEmail_no_at() {
        assertFalse(isValidEmail("email.com"))
    }

    @Test
    fun isValidEmail_wrong_order() {
        assertFalse(isValidEmail("name.email@com"))
    }

    @Test
    fun isValidEmail_double_suffix() {
        assertFalse(isValidEmail("email@email.ru.com)"))
    }
}