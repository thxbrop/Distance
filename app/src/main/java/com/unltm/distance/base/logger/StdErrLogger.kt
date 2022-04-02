package com.unltm.distance.base.logger

object StdErrLogger : Logger {
    override fun<E> log(message: E) {
        System.err.println(message)
    }
}