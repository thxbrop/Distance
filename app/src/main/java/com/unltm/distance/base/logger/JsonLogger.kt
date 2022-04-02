package com.unltm.distance.base.logger

import com.unltm.distance.base.contracts.gson

object JsonLogger : Logger {
    override fun <E> log(message: E) {
        println(gson.toJson(message))
    }
}