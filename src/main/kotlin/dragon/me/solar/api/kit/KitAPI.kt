package dragon.me.solar.api.kit

import org.bukkit.entity.Player
import java.util.Optional
import java.util.concurrent.CompletableFuture

/**
 * Read-only kit lookup and Paper-thread kit application operations.
 * The returned kit and flags are independent snapshots. Layout application returns
 * a future that completes when the server-thread inventory update finishes.
 */
interface KitAPI {
    /** Returns an empty Optional when no kit has this name. */
    fun getKit(name: String): Optional<Kit>

    /** Applies the configured default kit contents to a player on the server thread. */
    fun applyKit(kit: Kit, player: Player)

    /**
     * Loads and applies the player's saved layout asynchronously. Inventory mutation is
     * scheduled on the server thread; persistence errors are reported by Solar's logger.
     */
    fun applyLayoutKit(kit: Kit, player: Player): CompletableFuture<Void>
}
