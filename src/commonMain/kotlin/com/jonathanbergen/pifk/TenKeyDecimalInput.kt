package com.jonathanbergen.pifk


/**
 * Handles decimal input from a ten-key pad where digits are entered from the least to the most significant.
 * This class interprets a string of numeric characters as a decimal number, where the last 'n' characters are
 * treated as the fractional part, and any preceding characters are treated as the integer part.
 *
 * The class provides a standardized display value with a fixed decimal separator and supports leading
 * zeros for cases where the fractional part has fewer digits than specified by the 'fractions' parameter.
 *```
 * Example Usage:
 * - Input "123", fractions 2 will produce a display value "1.23" (integer part "1", fractional part "23").
 * - Input "05", fractions 2 will produce a display value "0.05" (integer part "0", fractional part "05").
 * - Input "123456", fractions 3 will produce a display value "123.456" (integer part "123", fractional part "456").
 * - Input "99", fractions 4 will produce a display value "0.0099" (integer part "0", fractional part "0099").
 * ```
 *
 * @property value The raw string value representing the numeric input from the ten-key pad. Arbitrary characters are
 *        allowed. Only digits are taken into account.
 * @property fractions The fixed number of digits to be treated as the fractional part of the number. If the actual
 *        input has fewer digits than the fractions count, leading zeros are prepended to the fraction part.
 *        If the input has more digits than the fractions count, all excess digits are treated as the integer part.
 * @property separatorChar The character used for separating the integer and fractional parts in the display value.
 *        Commonly a period ('.') or comma (',').
 *
 * @constructor Creates an instance of TenKeyDecimalInput with the specified value, separator character,
 *              and number of fraction digits.
 */
data class TenKeyDecimalInput(
    override val value: String,
    override val separatorChar: Char = '.',
    private val fractions: Int,
) : DecimalInput {

    override val digits = value.filter { it.isDigit() }
        .trimStart('0')
        .padStart(fractions + 1,'0')
        .map { it.digitToInt() }

    override val separatorIndex = if (fractions == 0) -1 else digits.size - fractions

    init {
        require(fractions >= 0) { "The number of fractions must not be negative." }
    }
}