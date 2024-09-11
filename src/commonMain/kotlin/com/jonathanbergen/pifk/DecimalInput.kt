package com.jonathanbergen.pifk

interface DecimalInput {

    val value: String
    val digits: List<Int>
    val separatorIndex: Int
    val separatorChar: Char
}