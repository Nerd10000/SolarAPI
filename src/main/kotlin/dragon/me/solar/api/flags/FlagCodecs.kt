package dragon.me.solar.api.flags

/** Reusable codecs for the scalar types supported by the public flag system. */
object FlagCodecs {
    @JvmField
    val BOOLEAN: FlagCodec<Boolean> = object : FlagCodec<Boolean> {
        override fun encode(value: Boolean): String = value.toString()

        override fun decode(encoded: String): Boolean = when (encoded) {
            "true" -> true
            "false" -> false
            else -> throw IllegalArgumentException("Expected 'true' or 'false'")
        }
    }

    @JvmField
    val INTEGER: FlagCodec<Int> = object : FlagCodec<Int> {
        override fun encode(value: Int): String = value.toString()

        override fun decode(encoded: String): Int = encoded.toInt()
    }

    @JvmField
    val DOUBLE: FlagCodec<Double> = object : FlagCodec<Double> {
        override fun encode(value: Double): String = value.toString()

        override fun decode(encoded: String): Double = encoded.toDouble()
    }

    @JvmField
    val STRING: FlagCodec<String> = object : FlagCodec<String> {
        override fun encode(value: String): String = value

        override fun decode(encoded: String): String = encoded
    }

    @Suppress("UNCHECKED_CAST")
    internal fun <T : Any> forType(type: Class<T>): FlagCodec<T> =
        when (type) {
            Boolean::class.javaObjectType -> BOOLEAN
            Int::class.javaObjectType -> INTEGER
            Double::class.javaObjectType -> DOUBLE
            String::class.java -> STRING
            else -> throw IllegalArgumentException("No built-in codec for ${type.name}")
        } as FlagCodec<T>
}
