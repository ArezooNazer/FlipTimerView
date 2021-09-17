package com.arezoo.fliptimerview.util

import java.util.*

private const val PERSIAN_ZERO: Char = 0x06f0.toChar()
private const val DIGIT_DIFF = (PERSIAN_ZERO - '0').toChar()

fun String.persianDigitsIfPersian(locale: Locale): String {
    val lang = locale.language
    val country = locale.country
    return if ("fa" == lang && "TJ" != country) {
        persianDigits()
    } else {
        this
    }
}

private fun String.persianDigits(): String {
    var result = ""
    var char: Char
    var index = 0

    while (index < length) {
        char = toCharArray()[index]
        when (char) {
            in '0'..'9' -> result += (DIGIT_DIFF.code + char.code).toChar().toString()
            ',' -> result += '٬'.toString()
            else -> result += char
        }
        index++
    }
    return result
}