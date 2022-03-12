package com.unltm.distance.logger

interface Logger {
    fun <E> log(message: E)
}