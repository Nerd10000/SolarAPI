package dragon.me.solar.api.kit

import dragon.me.solar.api.flags.FlagRegistry
import dragon.me.solar.api.flags.FlagValues

/**
 * Typed values for a kit. Unset keys return their declared defaults; [isConfigured]
 * distinguishes defaults from explicitly configured values.
 *
 * This mutable container is independent of the kit snapshot from which it was obtained.
 */
class KitFlags @JvmOverloads constructor(
    registry: FlagRegistry = KitFlagKeys.createRegistry()
) : FlagValues(registry) {
    override fun copy(): KitFlags {
        val result = KitFlags(registry)
        copyTo(result)
        return result
    }

    companion object {
        /** Deserializes known values and preserves unknown IDs as opaque payloads. */
        @JvmStatic
        fun deserialize(registry: FlagRegistry, values: Map<String, String>): KitFlags {
            val generic = FlagValues.deserialize(registry, values)
            val result = KitFlags(registry)
            generic.copyTo(result)
            return result
        }
    }
}
