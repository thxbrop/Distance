package com.unltm.distance.logger

object StdErrLogger : Logger {
    override fun<E> log(message: E) {
        System.err.println(message)
    }
}