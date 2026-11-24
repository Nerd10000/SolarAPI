package dragon.me.solar.api.flags

/**
 * A namespaced, typed flag definition. Keys are immutable and may be registered once
 * with a [FlagRegistry].
 */
class FlagKey<T : Any> private constructor(
    val id: String,
    val type: Class<T>,
    val defaultValue: T,
    val codec: FlagCodec<T>
) {
    init {
        require(ID_PATTERN.matches(id)) {
            "Flag id must be namespaced (for example 'my-addon:feature'): $id"
        }
        require(type.isInstance(defaultValue)) {
            "Default value for '$id' is not an instance of ${type.name}"
        }
    }

    companion object {
        private val ID_PATTERN = Regex("[a-z0-9][a-z0-9._-]*:[a-z0-9][a-z0-9._-]*")

        /** Java-friendly factory for addon-defined flag keys. */
        @JvmStatic
        fun <T : Any> of(
            id: String,
            type: Class<T>,
            defaultValue: T,
            codec: FlagCodec<T>
        ): FlagKey<T> = FlagKey(id, type, defaultValue, codec)

        /** Creates a key using a standard codec for Boolean, Integer, Double, or String. */
        @JvmStatic
        fun <T : Any> of(id: String, type: Class<T>, defaultValue: T): FlagKey<T> =
            FlagKey(id, type, defaultValue, FlagCodecs.forType(type))
    }
}
