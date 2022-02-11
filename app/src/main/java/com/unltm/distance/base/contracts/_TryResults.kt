package com.unltm.distance.base.contracts

import kotlin.coroutines.cancellation.CancellationException

@JvmInline
value class TryResult<T>(val value: Any?)

inline fun <R> tryEval(block: () -> R): TryResult<R> {
    return try {
        TryResult(block())
    } catch (e: Throwable) {
        TryResult(e)
    }
}

infix fun <R> TryResult<R>.onCancel(block: (e: CancellationException) -> Unit): TryResult<R> {
    if (value is CancellationException) {
        block(value)
    }
    return this
}

inline infix fun <reified T : Throwable, R> TryResult<R>.catch(block: (t: T) -> R): R {
    if (value is CancellationException) throw value
    return if (value is T) {
        block(value)
    } else {
        value as R
    }
}

inline infix fun <reified R> TryResult<R>.catchAll(block: (t: Throwable) -> R): R {
    if (value is CancellationException) throw value
    return if (value is Throwable) {
        block(value)
    } else {
        value as R
    }
}

inline infix fun <reified T : Throwable, R> TryResult<R>.catching(block: (t: T) -> R): TryResult<R> {
    return if (value is T) {
        TryResult(block(value))
    } else {
        this
    }
}

inline infix fun <reified R> TryResult<R>.finally(block: () -> Unit): R {
    block()
    if (value is Throwable) throw value
    return value as R
}