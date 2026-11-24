package dragon.me.solar.api.flags

/**
 * Converts a flag value to and from its stable textual persistence form.
 *
 * Implementations should be deterministic and should reject malformed input.
 */
interface FlagCodec<T : Any> {
    fun encode(value: T): String

    fun decode(encoded: String): T
}
