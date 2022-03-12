package com.unltm.distance.logger

/**
 * Print elements of [Collection] line by line
 * @see StdOutLogger
 */
@Suppress("unused")
fun <E> Collection<E>.loggerOnEach(): Collection<E> {
    with(StdOutLogger) {
        forEach { log(it) }
    }
    return this
}

/**
 * Print elements of [Map] line by line
 * @see StdOutLogger
 */
@Suppress("unused")
fun <K, V> Map<K, V>.loggerOnEach(): Map<K, V> {
    with(StdOutLogger) {
        forEach { log(it) }
    }
    return this
}

/**
 * Print element as JSON
 * @see JsonLogger
 */
@Suppress("unused")
fun <T> T.loggerAsJson(): T {
    with(JsonLogger) {
        log(this@loggerAsJson)
    }
    return this
}

/**
 * Print Throwable of the code block
 * @param block The code block which may throw a Throwable
 * @return The receiver or null if a Throwable occur
 * @see loggerWithCached
 * @see StdErrLogger
 */
@Suppress("unused")
fun <T> loggerCached(block: (() -> T)): T? {
    return with(StdErrLogger) {
        try {
            block.invoke()
        } catch (e: Exception) {
            log(e)
            null
        }
    }
}

/**
 * Print Throwable of the code block
 * @param block The code block which may throw a Throwable
 * @return [Cached] contains the receiver or Throwable
 * @see loggerCached
 * @see StdErrLogger
 * @see Cached
 */
@Suppress("unused")
fun <T> loggerWithCached(block: (() -> T)): Cached<T> {
    with(StdErrLogger) {
        return try {
            Cached(data = block.invoke())
        } catch (t: Throwable) {
            log(t)
            Cached(t = t)
        }
    }
}

/**
 * Contains a data or Throwable
 * @see loggerWithCached
 */
data class Cached<T>(
    val data: T? = null,
    val t: Throwable? = null
)