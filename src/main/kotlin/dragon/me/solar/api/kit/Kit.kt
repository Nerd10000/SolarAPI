package dragon.me.solar.api.kit

import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect

/** Immutable snapshot of a kit definition. Bukkit values are defensive copies. */
interface Kit {
    val name: String
    val items: List<ItemStack?>
    val armor: List<ItemStack?>
    val offhand: ItemStack?
    val potionEffects: List<PotionEffect>
    val flags: KitFlags

    @Deprecated("Use the flags property")
    fun getKitFlags(): KitFlags = flags
}
