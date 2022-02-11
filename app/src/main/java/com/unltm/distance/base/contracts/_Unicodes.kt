package com.unltm.distance.base.contracts

fun Int.toHexString(): String = Integer.toHexString(this)

//char ->unicode
fun encodeUnicode(char: Char) = "\\u${char.code.toHexString()}"

//String ->unicode
fun String.encodeUnicode() =
    toCharArray().joinToString(separator = "", truncated = "") { encodeUnicode(it) }

//unicode ->String
fun String.decodeUnicode(): String {
    fun decode1(unicode: String) = unicode.toInt(16).toChar()
    val unicode = split("\\u").mapNotNull { if (it.isNotBlank()) decode1(it) else null }
    return String(unicode.toCharArray())
}