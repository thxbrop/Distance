package com.unltm.distance.base.logger

object StdOutLogger : Logger {
    override fun<E> log(message: E) {
        println(message)
    }
}