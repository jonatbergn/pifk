package com.jonathanbergen.pifk

import kotlin.test.Test
import kotlin.test.assertEquals

class DecimalValueTest {

    @Test
    fun integer_and_fraction_parts_are_concatenated_correctly() {
        val decimalValue = DecimalValue("123", "456")
        assertEquals(listOf(1, 2, 3), decimalValue.integerPart)
        assertEquals(listOf(4, 5, 6), decimalValue.fractionPart)
    }

    @Test
    fun toString_creates_correct_double_value() {
        val decimalValue = DecimalValue("123", "45")
        assertEquals("12345E-2", "$decimalValue")
    }
    @Test
    fun toString_creates_correct_double_value_with_empty_input() {
        val decimalValue = DecimalValue("", "")
        assertEquals("0E-0", "$decimalValue")
    }

    @Test
    fun toString_returns_the_scientific_notation_representation() {
        val decimalValue = DecimalValue("123", "4567")
        assertEquals("1234567E-4", decimalValue.toString())
    }

    @Test
    fun maxScale_reduces_the_fraction_part_to_the_given_scale() {
        val decimalValue = DecimalValue("123", "4567")
        val scaledDecimalValue = decimalValue.maxScale(2)
        assertEquals("12345E-2", scaledDecimalValue.toString())
    }

    @Test
    fun minScale_extends_the_fraction_part_to_the_given_scale() {
        val decimalValue = DecimalValue("123", "45")
        val scaledDecimalValue = decimalValue.minScale(4)
        assertEquals("1234500E-4", scaledDecimalValue.toString())
    }

    @Test
    fun scale_to_existing_scale_returns_the_same_value() {
        val decimalValue = DecimalValue("123", "45")
        val scaledDecimalValue = decimalValue.scale(2)
        assertEquals(decimalValue, scaledDecimalValue)
    }

    @Test
    fun digits_return_0_when_both_parts_are_empty() {
        val decimalValue = DecimalValue("", "")
        assertEquals(emptyList(), decimalValue.integerPart)
        assertEquals(emptyList(), decimalValue.fractionPart)
    }
}