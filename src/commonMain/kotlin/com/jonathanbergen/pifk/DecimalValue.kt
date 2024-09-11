package com.jonathanbergen.pifk

/**
 * DecimalValue represents a numeric value with separate integer and fraction parts.
 */
interface DecimalValue {

    /**
     *The integer part of the number
     */
    val integerPart: List<Int>

    /**
     * The fractional part of the number
     */
    val fractionPart: List<Int>

    /**
     * Scales the number to have `n` digits in the fraction part. Scales up or down as needed.
     *
     * @param n The desired scale of the fraction part.
     * @return A new DecimalValue with the scaled fraction part.
     */
    fun scale(n: Int): DecimalValue

    /**
     * Returns a new DecimalValue with the fraction part truncated (or left unchanged) to `n` digits.
     *
     * @param n The maximum scale of the fraction part.
     * @return A new DecimalValue with the possibly truncated fraction part.
     */
    fun maxScale(n: Int): DecimalValue

    /**
     * Returns a new DecimalValue with the fraction part extended to `n` digits by adding trailing zeros.
     *
     * @param n The minimum scale of the fraction part.
     * @return A new DecimalValue with the extended fraction part.
     */
    fun minScale(n: Int): DecimalValue

    /**
     * Converts the current decimal value to a string representation in scientific notation (also known as E notation),
     * following the pattern \(\d+E-\d+\). This notation expresses the number as a product of a coefficient (the digits
     * before the 'E') and a power of 10 (the exponent after the 'E'), allowing for a concise representation of numbers.
     * The format specifically is "${digits}E-${fractionPart.size}", indicating the coefficient
     * followed by 'E-' and the size of the fraction part as the negative exponent.
     *
     * If the numerical value has no digits (i.e., it is equivalent to zero), the method returns "0E-0" to consistently
     * apply scientific notation across all values. This ensures even zero values are formatted within the expected notation
     * framework.
     *
     * Note: The scientific notation format used here simplifies the display of numbers by focusing on the significant
     * digits (digits) and the scale (fractionPart.size) of the number, effectively conveying the magnitude and precision
     * of decimal values.
     *
     * Examples:
     * - A decimal value of 123.45 is represented as "12345E-2", highlighting the movement of the decimal point two places
     *   to the left.
     * - For an input with no significant digits, the output will be "0E-0", indicating a zero value in scientific notation.
     */
    override fun toString(): String

    fun toStandardDecimalInput(
        separatorChar: Char = '.',
        alternativeSeparatorChars: CharSequence = "",
        maxFractions: Int? = fractionPart.count(),
    ): StandardDecimalInput

    fun toTenKeyDecimalInput(
        separatorChar: Char = '.',
        fractions: Int = fractionPart.count(),
    ): TenKeyDecimalInput

    companion object {

        operator fun invoke(
            integerPart: List<Int>,
            fractionPart: List<Int>,
        ): DecimalValue = DecimalValueImpl(
            integerPart = integerPart,
            fractionPart = fractionPart,
        )

        operator fun invoke(
            value: DecimalInput,
        ) = with(value) {
            if (separatorIndex == -1) DecimalValue(
                integerPart = digits,
                fractionPart = emptyList(),
            ) else DecimalValue(
                integerPart = digits.take(separatorIndex),
                fractionPart = digits.drop(separatorIndex),
            )
        }

        /**
         * Creates a `DecimalValue` instance from provided integer and fractional parts.
         *
         * @param integerPart A string representing the integer part of the number. It may contain any
         * characters, but only digits (0-9) will be considered. Non-digit characters will be ignored.
         * @param fractionPart A string representing the fractional part of the number, following the
         * same rules for character filtering as the integer part.
         *
         * @return A newly constructed `DecimalValue` object representing the combined integer and
         * fractional parts as a single decimal value.
         *
         * Note: This method ensures the creation of a `DecimalValue` even if the input strings are
         * purely non-numeric or empty, resulting in a decimal value of 0.
         */
        operator fun invoke(
            integerPart: String,
            fractionPart: String,
        ) = this(
            integerPart = integerPart.mapNotNull(Char::digitToIntOrNull),
            fractionPart = fractionPart.mapNotNull(Char::digitToIntOrNull),
        )

        /**
         * Utilizing floating-point numbers in contexts demanding high precision is generally discouraged
         * due to the inherent imprecision of these types, which can lead to unpredictable outcomes.
         * A notable example of such unpredictability is the conversion of a floating-point [value] into a string,
         * potentially yielding a representation in scientific notation rather than a fixed-point notation.
         *
         * This imprecision also affects the representation of fractional parts in the [DecimalValue] class, leading
         * to variability in the length of the returned [DecimalValue.fractionPart]. The specific behavior depends on
         * the compile target, illustrating the non-deterministic nature of fractional digit length in floating-point
         * calculations across different platforms:
         *
         * ```
         * // Instantiate DecimalValue with a floating-point number
         * val decimalValue = DecimalValue(10.00)
         * // The output demonstrates variability across platforms:
         * // - Java/iOS targets print "1", indicating a single fractional digit, despite the original value having two.
         * // - JavaScript (JS) target prints "0", showing no fractional digits, diverging from the input's explicit two fractional digits.
         * println(decimalValue.fractionPart.size)
         * ```
         *
         * Developers should be aware of these platform-dependent differences when dealing with floating-point numbers,
         * especially in cross-platform projects, to ensure consistent behavior and accuracy of numerical representations.
         */
        operator fun invoke(value: Double) = this(
            value = StandardDecimalInput(value = "$value"),
        )
    }
}

private data class DecimalValueImpl(
    override val integerPart: List<Int>,
    override val fractionPart: List<Int>,
) : DecimalValue {

    private val digits by lazy {
        (integerPart + fractionPart).ifEmpty { listOf(0) }.joinToString(separator = "")
    }

    override fun minScale(n: Int) = copy(fractionPart = List(n) { fractionPart.getOrElse(it) { 0 } })
    override fun maxScale(n: Int) = copy(fractionPart = fractionPart.take(n))
    override fun scale(n: Int) = if (fractionPart.count() > n) maxScale(n) else minScale(n)

    override fun toStandardDecimalInput(
        separatorChar: Char,
        alternativeSeparatorChars: CharSequence,
        maxFractions: Int?,
    ) = StandardDecimalInput(
        value = buildString {
            integerPart.forEach(::append)
            fractionPart.forEachIndexed { index, i ->
                if (index == 0) {
                    append(separatorChar)
                }
                append(i)
            }
        },
        maxFractions = maxFractions,
        separatorChar = separatorChar,
        alternativeSeparatorChars = alternativeSeparatorChars,
    )

    override fun toTenKeyDecimalInput(
        separatorChar: Char,
        fractions: Int,
    ) = TenKeyDecimalInput(
        value = digits,
        fractions = fractions,
        separatorChar = separatorChar,
    )

    override fun toString() = "${digits}E-${fractionPart.size}"
}