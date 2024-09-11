package com.jonathanbergen.pifk

import kotlin.test.Test
import kotlin.test.assertEquals

class StandardDecimalInputTest {

    @Test
    fun test_display_value_without_fraction_limit() {
        val value = "123.456"
        val separatorChar = '.'
        val alternativeSeparatorChars = ",;"
        val maxFractions: Int? = null  // No limit on the number of fraction digits

        val decimalInput = StandardDecimalInput(value, separatorChar, alternativeSeparatorChars, maxFractions)

        // All digits are included
        assertEquals(listOf(1, 2, 3, 4, 5, 6), decimalInput.digits)
        // Separator index after the 3rd digit
        assertEquals(3, decimalInput.separatorIndex)
        assertEquals(separatorChar, decimalInput.separatorChar)
    }

    @Test
    fun test_display_value_with_fraction_limit() {
        val value = "123.45678"
        val separatorChar = '.'
        val alternativeSeparatorChars = ",;"
        val maxFractions = 2  // Limit to 2 fraction digits

        val decimalInput = StandardDecimalInput(value, separatorChar, alternativeSeparatorChars, maxFractions)

        // Fraction part is limited to 2 digits
        assertEquals(listOf(1, 2, 3, 4, 5), decimalInput.digits)
        // Separator index after the 3rd digit
        assertEquals(3, decimalInput.separatorIndex)
        assertEquals(separatorChar, decimalInput.separatorChar)
    }

    @Test
    fun test_display_value_with_alternative_separator_chars() {
        val value = "123?456;789.012"
        val separatorChar = '.'
        val alternativeSeparatorChars = ",;" // Note that the '?' is not in the list of alternative separators
        val maxFractions = 4  // Limit to 4 fraction digits

        val decimalInput = StandardDecimalInput(value, separatorChar, alternativeSeparatorChars, maxFractions)

        // In the test value `"123?456;789.012"`, the `?` character is not listed as an `alternativeSeparatorChar`,
        // so it should be ignored. The first valid separator encountered is the `;`, which comes after `123456`.
        // This confirms that `123456` is the integer part and the following digits starting from `789.012`
        // are the fraction part. However, `maxFractions` limits the number of fraction digits to 4,
        // so only `7890` should be considered.

        // Including integer part, and up to 4 digits of the fraction
        assertEquals(listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 0), decimalInput.digits)
        // Separator positioned after the integer part '123456', since '?' is not taken into account
        assertEquals(6, decimalInput.separatorIndex)
        assertEquals(separatorChar, decimalInput.separatorChar)
    }

    @Test
    fun test_display_value_when_no_fraction_part_is_present() {
        val value = "123456"
        val separatorChar = '.'
        val alternativeSeparatorChars = ",;"
        val maxFractions = 2  // Limit to 2 fraction digits

        val decimalInput = StandardDecimalInput(value, separatorChar, alternativeSeparatorChars, maxFractions)

        // All digits are considered as the integer part
        assertEquals(listOf(1, 2, 3, 4, 5, 6), decimalInput.digits)
        // No separator index due to absence of fraction part
        assertEquals(-1, decimalInput.separatorIndex)
        assertEquals(separatorChar, decimalInput.separatorChar)
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
            .toStandardDecimalInput()
        assertEquals(StandardDecimalInput(value = "90.00", maxFractions = 2), decimalInput)
    }
}