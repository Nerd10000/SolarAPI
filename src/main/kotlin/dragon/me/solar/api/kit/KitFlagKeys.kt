package dragon.me.solar.api.kit

import dragon.me.solar.api.flags.FlagCodecs
import dragon.me.solar.api.flags.FlagCodec
import dragon.me.solar.api.flags.FlagKey
import dragon.me.solar.api.flags.FlagRegistry
import java.nio.charset.StandardCharsets
import java.util.Base64
import java.util.Collections

/** Solar's built-in kit flag definitions. Identifiers remain stable across API releases. */
object KitFlagKeys {
    private val stringListCodec = object : FlagCodec<List<String>> {
        override fun encode(value: List<String>): String = value.joinToString(".") {
            "x" + Base64.getUrlEncoder().withoutPadding().encodeToString(it.toByteArray(StandardCharsets.UTF_8))
        }

        override fun decode(encoded: String): List<String> {
            if (encoded.isEmpty()) return emptyList()
            return encoded.split('.').map {
                require(it.startsWith("x")) { "Invalid encoded string-list item" }
                String(Base64.getUrlDecoder().decode(it.substring(1)), StandardCharsets.UTF_8)
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    private val stringListClass = List::class.java as Class<List<String>>

    @JvmField
    val MAX_HEALTH: FlagKey<Double> =
        FlagKey.of("solar:max-health", Double::class.javaObjectType, 20.0, FlagCodecs.DOUBLE)

    @JvmField
    val NATURAL_REGENERATION: FlagKey<Boolean> =
        FlagKey.of("solar:natural-regeneration", Boolean::class.javaObjectType, true, FlagCodecs.BOOLEAN)

    @JvmField
    val NATURAL_SATURATION: FlagKey<Boolean> =
        FlagKey.of("solar:natural-saturation", Boolean::class.javaObjectType, true, FlagCodecs.BOOLEAN)

    @JvmField
    val HUNGER_LOSS: FlagKey<Boolean> =
        FlagKey.of("solar:hunger-loss", Boolean::class.javaObjectType, true, FlagCodecs.BOOLEAN)

    @JvmField
    val PEARL_COOLDOWN: FlagKey<Int> =
        FlagKey.of("solar:pearl-cooldown", Int::class.javaObjectType, -1, FlagCodecs.INTEGER)

    @JvmField
    val GOLDEN_APPLE_COOLDOWN: FlagKey<Int> =
        FlagKey.of("solar:golden-apple-cooldown", Int::class.javaObjectType, -1, FlagCodecs.INTEGER)

    @JvmField
    val WIND_CHARGE_COOLDOWN: FlagKey<Int> =
        FlagKey.of("solar:wind-charge-cooldown", Int::class.javaObjectType, -1, FlagCodecs.INTEGER)

    @JvmField
    val PREVENT_BLOCK_PLACE: FlagKey<Boolean> =
        FlagKey.of("solar:prevent-block-place", Boolean::class.javaObjectType, false, FlagCodecs.BOOLEAN)

    @JvmField
    val PREVENT_BLOCK_BREAK: FlagKey<Boolean> =
        FlagKey.of("solar:prevent-block-break", Boolean::class.javaObjectType, false, FlagCodecs.BOOLEAN)

    @JvmField
    val PREVENT_ITEM_DROP: FlagKey<Boolean> =
        FlagKey.of("solar:prevent-item-drop", Boolean::class.javaObjectType, false, FlagCodecs.BOOLEAN)

    @JvmField
    val PREVENT_MOVEMENT_BEFORE_START: FlagKey<Boolean> =
        FlagKey.of("solar:prevent-movement-before-start", Boolean::class.javaObjectType, false, FlagCodecs.BOOLEAN)

    @JvmField
    val ALLOW_ITEM_PICKUP: FlagKey<Boolean> =
        FlagKey.of("solar:allow-item-pickup", Boolean::class.javaObjectType, true, FlagCodecs.BOOLEAN)

    @JvmField
    val MAPS: FlagKey<List<String>> =
        FlagKey.of("solar:maps", stringListClass, listOf("*"), stringListCodec)

    @JvmStatic
    fun all(): List<FlagKey<*>> = Collections.unmodifiableList(listOf(
        MAX_HEALTH,
        NATURAL_REGENERATION,
        NATURAL_SATURATION,
        HUNGER_LOSS,
        PEARL_COOLDOWN,
        GOLDEN_APPLE_COOLDOWN,
        WIND_CHARGE_COOLDOWN,
        PREVENT_BLOCK_PLACE,
        PREVENT_BLOCK_BREAK,
        PREVENT_ITEM_DROP,
        PREVENT_MOVEMENT_BEFORE_START,
        ALLOW_ITEM_PICKUP,
        MAPS
    ))

    /** Registers Solar's built-ins in an existing registry. */
    @JvmStatic
    fun register(registry: FlagRegistry) {
        all().forEach(registry::register)
    }

    /** Creates a registry initialized with Solar's built-in kit flags. */
    @JvmStatic
    fun createRegistry(): FlagRegistry = FlagRegistry().also { register(it) }
}
