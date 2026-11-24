package dragon.me.solar.api.flags

import java.util.Collections
import java.util.LinkedHashMap

/**
 * Registry of flag definitions. Register domain-specific built-ins explicitly.
 *
 * Registration is thread-safe. Register addon flags during plugin startup, before
 * reading persisted values that should be decoded by the addon.
 */
class FlagRegistry {
    private val registered = LinkedHashMap<String, FlagKey<*>>()

    @Synchronized
    fun register(key: FlagKey<*>): FlagKey<*> {
        val existing = registered[key.id]
        if (existing != null) {
            if (existing.type != key.type) {
                throw IllegalStateException(
                    "Flag '${key.id}' is already registered as ${existing.type.name}, not ${key.type.name}"
                )
            }
            throw IllegalArgumentException("Flag '${key.id}' is already registered")
        }
        registered[key.id] = key
        return key
    }

    @Synchronized
    fun find(id: String): FlagKey<*>? = registered[id]

    @Synchronized
    fun keys(): List<FlagKey<*>> = Collections.unmodifiableList(ArrayList(registered.values))

    @Synchronized
    internal fun requireRegistered(key: FlagKey<*>) {
        val registeredKey = registered[key.id]
            ?: throw IllegalArgumentException("Flag '${key.id}' has not been registered")
        require(registeredKey === key) {
            "Flag '${key.id}' is not the registered key instance"
        }
    }
}
