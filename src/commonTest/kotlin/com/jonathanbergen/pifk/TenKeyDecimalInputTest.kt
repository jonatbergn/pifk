package com.jonathanbergen.pifk

import kotlin.test.Test
import kotlin.test.assertEquals

internal class TenKeyDecimalInputTest {

    @Test
    fun test_display_value_with_no_fractions() {
        // Given a TenKeyDecimalInput with no fraction digits
        val input = "12345"
        val separatorChar = '.'
        val fractions = 0

        // When creating a TenKeyDecimalInput instance
        val tenKeyDecimalInput = TenKeyDecimalInput(input, separatorChar, fractions)

        assertEquals(listOf(1, 2, 3, 4, 5), tenKeyDecimalInput.digits)
        // Index after all digits because there is no fractional part
        assertEquals(-1, tenKeyDecimalInput.separatorIndex)
        assertEquals(separatorChar, tenKeyDecimalInput.separatorChar)
    }

    @Test
    fun test_display_value_with_all_fractions() {
        // Given a TenKeyDecimalInput where all digits are fraction digits
        val input = "678"
        val separatorChar = '.'
        val fractions = 3

        // When creating a TenKeyDecimalInput instance
        val tenKeyDecimalInput = TenKeyDecimalInput(input, separatorChar, fractions)

        assertEquals(listOf(0, 6, 7, 8), tenKeyDecimalInput.digits)
        // Index before the digits indicating the absence of an integer part
        assertEquals(1, tenKeyDecimalInput.separatorIndex)
        assertEquals(separatorChar, tenKeyDecimalInput.separatorChar)
    }

    @Test
    fun test_display_value_with_leading_zeros_in_fractions() {
        // Given a TenKeyDecimalInput with leading zeros in fractions
        val input = "009"
        val separatorChar = '.'
        val fractions = 2

        // When creating a TenKeyDecimalInput instance
        val tenKeyDecimalInput = TenKeyDecimalInput(input, separatorChar, fractions)

        assertEquals(listOf(0, 0, 9), tenKeyDecimalInput.digits)
        // Index after the integer part (one leading zero)
        assertEquals(1, tenKeyDecimalInput.separatorIndex)
        assertEquals(separatorChar, tenKeyDecimalInput.separatorChar)
    }

    @Test
    fun test_display_value_with_more_digits_than_fractions() {
        // Given a TenKeyDecimalInput with more digits than specified for the fractional part
        val input = "123456"
        val separatorChar = '.'
        val fractions = 2

        // When creating a TenKeyDecimalInput instance
        val tenKeyDecimalInput = TenKeyDecimalInput(input, separatorChar, fractions)

        assertEquals(listOf(1, 2, 3, 4, 5, 6), tenKeyDecimalInput.digits)
        // Index aligning with the integer part of '1234' and fractions '56'
        assertEquals(4, tenKeyDecimalInput.separatorIndex)
        assertEquals(separatorChar, tenKeyDecimalInput.separatorChar)
    }

    @Test
    fun test_display_value_with_fewer_digits_than_fractions() {
        // Given a TenKeyDecimalInput with fewer digits than specified for the fractional part
        val input = "12" // only two digits provided
        val separatorChar = '.'
        val fractions = 4 // expecting four digits for the fractional part

        // When creating a TenKeyDecimalInput instance
        val tenKeyDecimalInput = TenKeyDecimalInput(input, separatorChar, fractions)

        // Note the two leading zeros to ensure 4 digits for fractions
        assertEquals(listOf(0, 0, 0, 1, 2), tenKeyDecimalInput.digits)
        // Index before any digits, indicating the leading integer part is zero
        assertEquals(1, tenKeyDecimalInput.separatorIndex)
        assertEquals(separatorChar, tenKeyDecimalInput.separatorChar)
    }

    @Test
    fun creating_decimal_input_from_decimal_value_based_on_double() {
        // this will translate to DecimalValue(90.0) on JVM and DecimalValue(90) on JS
        // therefore the scale is not deterministic
        val decimalValue = DecimalValue(90.00)
        val decimalInput = decimalValue
            // we have to set the scale manually to 2
            .scale(n = 2)
            // now we have a precise scale and can create the input value to compare
            .toTenKeyDecimalInput()
        assertEquals(TenKeyDecimalInput(value = "9000", fractions = 2), decimalInput)
    }

    @Test
    fun empty_000() {
        val decimalInput = TenKeyDecimalInput("", fractions = 2)
        assertEquals(1, decimalInput.separatorIndex)
        assertEquals(listOf(0, 0, 0), decimalInput.digits)
    }

    @Test
    fun case_001() {
        val decimalInput = TenKeyDecimalInput("1", fractions = 2)
        assertEquals(1, decimalInput.separatorIndex)
        assertEquals(listOf(0, 0, 1), decimalInput.digits)
    }

    @Test
    fun case_010() {
        val decimalInput = TenKeyDecimalInput("10", fractions = 2)
        assertEquals(1, decimalInput.separatorIndex)
        assertEquals(listOf(0, 1, 0), decimalInput.digits)
    }

    @Test
    fun case_100() {
        val decimalInput = TenKeyDecimalInput("100", fractions = 2)
        assertEquals(1, decimalInput.separatorIndex)
        assertEquals(listOf(1, 0, 0), decimalInput.digits)
    }

    @Test
    fun leading_zeros_get_trimmed() {
        val decimalInput = TenKeyDecimalInput("000000000010", fractions = 2)
        assertEquals(1, decimalInput.separatorIndex)
        assertEquals(listOf(0, 1, 0), decimalInput.digits)
    }
}