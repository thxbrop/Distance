package com.unltm.distance.base.qrcode

open class Result(
    val result: String?
) : CharSequence {
    companion object {
        val EMPTY_RESULT = Result(null)
    }

    override val length: Int
        get() = result?.length ?: 0

    override fun get(index: Int): Char {
        return result?.get(index) ?: throw IndexOutOfBoundsException()
    }

    override fun subSequence(startIndex: Int, endIndex: Int): CharSequence {
        return result?.subSequence(startIndex, endIndex) ?: throw IndexOutOfBoundsException()
    }
}