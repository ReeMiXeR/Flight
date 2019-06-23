package vs.test.aviasales.data

import vs.test.aviasales.data.exception.ConvertException

fun <T> getOrDie(item: T?, binding: String): T = item ?: throw ConvertException("'$binding' must not be null")