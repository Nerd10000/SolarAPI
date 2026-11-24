package dragon.me.solar.api.flags

import java.util.Collections
import java.util.LinkedHashMap

/**
 * Generic typed flag values. Unset keys return their declared defaults; [isConfigured]
 * distinguishes those defaults from explicit values.
 *
 * Values used with custom mutable types should have codecs that round-trip to copies.
 */
open class FlagValues @JvmOverloads constructor(
    val registry: FlagRegistry = FlagRegistry()
) {
    private val configured = LinkedHashMap<String, Any>()
    private val unknownValues = LinkedHashMap<String, String>()

    @Synchronized
    fun <T : Any> get(key: FlagKey<T>): T {
        registry.requireRegistered(key)
        val explicit = configured[key.id]
        if (explicit != null) return copyValue(key, explicit)

        val encoded = unknownValues[key.id] ?: return copyValue(key, key.defaultValue)
        val decoded = decode(key, encoded)
        configured[key.id] = decoded
        unknownValues.remove(key.id)
        return copyValue(key, decoded)
    }

    @Synchronized
    fun <T : Any> set(key: FlagKey<T>, value: T) {
        registry.requireRegistered(key)
        require(key.type.isInstance(value)) {
            "Value for '${key.id}' must be ${key.type.name}, got ${value.javaClass.name}"
        }
        configured[key.id] = copyValue(key, value)
        unknownValues.remove(key.id)
    }

    @Synchronized
    fun isConfigured(key: FlagKey<*>): Boolean {
        registry.requireRegistered(key)
        return configured.containsKey(key.id) || unknownValues.containsKey(key.id)
    }

    /** Returns explicitly configured, registered keys in registration order. */
    @Synchronized
    fun configuredKeys(): List<FlagKey<*>> = Collections.unmodifiableList(
        configured.keys.mapNotNull(registry::find)
    )

    @Synchronized
    fun reset(key: FlagKey<*>) {
        registry.requireRegistered(key)
        configured.remove(key.id)
        unknownValues.remove(key.id)
    }

    @Synchronized
    fun resetAll() {
        configured.clear()
        unknownValues.clear()
    }

    @Synchronized
    open fun copy(): FlagValues {
        val result = FlagValues(registry)
        copyTo(result)
        return result
    }

    internal fun copyTo(result: FlagValues) {
        unknownValues.forEach { (id, value) -> result.unknownValues[id] = value }
        configured.forEach { (id, value) ->
            val key = registry.find(id)
                ?: throw IllegalStateException("Configured flag '$id' is not registered")
            result.configured[id] = encodeAndDecode(key, value)
        }
    }

    /**
     * Serializes explicit values only. Unknown identifiers are returned unchanged,
     * allowing values from newer Solar or addon versions to survive a load/save cycle.
     */
    @Synchronized
    fun serialize(): Map<String, String> {
        val result = LinkedHashMap(unknownValues)
        configured.forEach { (id, value) ->
            val key = registry.find(id)
                ?: throw IllegalStateException("Configured flag '$id' is not registered")
            result[id] = encode(key, value)
        }
        return Collections.unmodifiableMap(result)
    }

    private fun <T : Any> decode(key: FlagKey<T>, encoded: String): T = try {
        key.codec.decode(encoded).also {
            require(key.type.isInstance(it)) {
                "Codec for '${key.id}' returned ${it.javaClass.name}, expected ${key.type.name}"
            }
        }
    } catch (exception: Exception) {
        throw FlagSerializationException("Could not decode flag '${key.id}'", exception)
    }

    private fun <T : Any> copyValue(key: FlagKey<T>, value: Any): T {
        require(key.type.isInstance(value)) {
            "Value for '${key.id}' must be ${key.type.name}, got ${value.javaClass.name}"
        }
        return decode(key, encode(key, value))
    }

    private fun encode(key: FlagKey<*>, value: Any): String = try {
        encodeUntyped(key, value)
    } catch (exception: Exception) {
        throw FlagSerializationException("Could not encode flag '${key.id}'", exception)
    }

    private fun encodeAndDecode(key: FlagKey<*>, value: Any): Any =
        decodeUntyped(key, encode(key, value))

    @Suppress("UNCHECKED_CAST")
    private fun encodeUntyped(key: FlagKey<*>, value: Any): String =
        (key.codec as dragon.me.solar.api.flags.FlagCodec<Any>).encode(value)

    @Suppress("UNCHECKED_CAST")
    private fun decodeUntyped(key: FlagKey<*>, encoded: String): Any =
        decode(key as FlagKey<Any>, encoded)

    companion object {
        /** Deserializes known flags and retains every unrecognized entry verbatim. */
        @JvmStatic
        fun deserialize(registry: FlagRegistry, values: Map<String, String>): FlagValues {
            val flags = FlagValues(registry)
            values.forEach { (id, encoded) ->
                val key = registry.find(id)
                if (key == null) {
                    flags.unknownValues[id] = encoded
                } else {
                    flags.configured[id] = flags.decodeUntyped(key, encoded)
                }
            }
            return flags
        }
    }
}
