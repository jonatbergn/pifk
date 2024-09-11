package com.jonathanbergen.pifk

/**
 * Represents a standardized format for decimal input, capable of handling various separator characters and enforcing
 * a maximum number of fraction digits. This class is tailored for scenarios where a numeric input may include alternative
 * decimal separator characters, and there is a need to standardize the display of the input as a decimal number with a
 * specified fraction length limit.
 *
 * ```
 * Example Usage:
 * - Given the value "1234.5678" and maxFractions = 2, the standardized display value would be "1234.56".
 * - Given the value "1234,5678" with alternativeSeparatorChars = ",;", the standardized display value would be "1234.5678".
 * - Given the value "1,234.5678" with alternativeSeparatorChars = ",;" and maxFractions = 3, the standardized display value
 *   would be "1.234" - note that ',' is recognized as the first separator and truncates the fraction to '234'.
 * ```
 *
 * @property value The raw string value of the numeric input. This string may contain digits interspersed with any
 *        of the recognized separator characters, including both the standard separator character and any alternative
 *        separator character and arbitrary characters.
 * @property maxFractions The optional maximum number of digits allowed in the fraction part of the number. If provided,
 *        the fraction part will be truncated to this length. If null, the entire fraction part as specified by the first
 *        encountered separator character will be used.
 * @property separatorChar The standard character that will be used to represent the decimal separator in the output display
 *        value. This should be a character commonly recognized as a decimal separator, such as a period or comma.
 * @property alternativeSeparatorChars A collection of alternative characters that should be considered as decimal separators
 *        when processing the input value. The first occurrence of any these characters (or the standard separator)
 *        will mark the beginning of the fraction part in the input.
 *
 * @constructor Creates an instance of StandardDecimalInput with the specified raw value, maximum fraction digit limit,
 *              standard separator character, and alternative separator characters.
 */
data class StandardDecimalInput(
    override val value: String,
    override val separatorChar: Char = '.',
    private val alternativeSeparatorChars: CharSequence = "",
    private val maxFractions: Int? = null,
) : DecimalInput {

    private val chars = value.replace("^0+(\\d)".toRegex()) { it.groupValues[1] }
    private val separators = (alternativeSeparatorChars.toSet() + separatorChar)
    private val decimals = chars.takeWhile { it !in separators }.filter(Char::isDigit)
    private val fractions = chars.dropWhile { it !in separators }.filter(Char::isDigit)
    private val limitedFractionPart = if (maxFractions == null) fractions else fractions.take(maxFractions)

    override val digits = (decimals + limitedFractionPart).map { it.digitToInt() }
    override val separatorIndex = if (maxFractions != 0 && chars.any { it in separators }) decimals.length else -1
}