# PIFK - Precision Input Formatting Kit

PIFK is a Kotlin library that specializes in transforming arbitrary user inputs into structured `DecimalValue` instances. It offers robust functionality for parsing and formatting decimal numbers by filtering out non-digits and non-separator characters, tailored to handle input from different sources and formats.

## Features

- **Arbitrary Input Handling:** Processes raw user input strings, extracting numerical values by omitting invalid characters and recognizing separators.
- **DecimalValue Conversion:** Converts filtered input into `DecimalValue` objects for precise and consistent numerical handling.
- **User Input Classes:** Offers `TenKeyDecimalInput` and `StandardDecimalInput` classes, designed for number pad entries and standard numeric inputs, respectively.
- **Scale Management:** Adjusts the scale of the fraction part of decimal numbers, catering to the specific precision requirements of different use cases.
- **Scientific Notation Representation:** Supports presenting `DecimalValue` objects in scientific notation for ease of use in various domains.

## User Input Parsing

PIFK can interpret and process arbitrary input containing a mix of digits, potential fractional parts, and various characters. It provides mechanisms to ensure only valid numerical content is considered from the input:

- **TenKeyDecimalInput:** Targets scenarios involving numerical keypads, where the user enters a sequence of numbers that the system needs to partition into integer and fraction parts based on provided specifications. It can also add leading zeros if the number of fraction digits is specified to be greater than the length of the input.

- **StandardDecimalInput:** Suited for inputs incorporating recognized separators, like commas or periods, this class discriminates between integer and fraction components based on the first valid separator encountered. It respects predefined limits on the number of fraction digits when set, and can also process inputs with no limits on fraction digits.

PIFK is capable of handling various separators and eliminating extraneous non-numeric symbols, ensuring clean and usable decimal constructs.

## Usage Scenario

When a user inputs a number, PIFK can interpret and convert this input into a `DecimalValue`:

```kotlin
// TenKeyDecimalInput for numbers entered without an explicit separator
val tenKeyInput = TenKeyDecimalInput("12345", fractions = 2, separatorChar ='.')
val decimalValueFromTenKeyInput = DecimalValue(tenKeyInput)
// prints "12345E-2" 
println(decimalValueFromTenKeyInput)
// prints "123.45"
println(decimalValueFromTenKeyInput.displayValue)


// StandardDecimalInput for numbers with a clear separator, like comma or period
val standardInput = StandardDecimalInput("1,23456", separatorChar = '.', alternativeSeparatorChars = ",")
val decimalValueFromStandardInput = DecimalValue(standardInput)
// prints "123456E-5"
println(decimalValueFromStandardInput)
// prints "1.23456"
println(decimalValueFromStandardInput.displayValue)
```

## Integration

PIFK should be added to the Kotlin-based project by including it in the project's dependency configuration.

## Testing

PIFK includes unit tests to verify that both `TenKeyDecimalInput` and `StandardDecimalInput` correctly process various input formats and situations.

## License

Refer to the library's licensing documentation to understand the permissible use cases, distribution, and modification rights.