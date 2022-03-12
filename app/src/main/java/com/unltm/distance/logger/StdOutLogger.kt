package com.unltm.distance.logger

object StdOutLogger : Logger {
    override fun<E> log(message: E) {
        println(message)
    }
}