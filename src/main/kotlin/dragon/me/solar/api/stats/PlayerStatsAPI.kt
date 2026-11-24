package dragon.me.solar.api.stats

import java.util.concurrent.CompletableFuture
import java.util.UUID

/**
 * Asynchronous player-statistics queries backed by Solar's persistent cache.
 * Safe to call from any thread; future completion may occur on a database worker.
 */
interface PlayerStatsAPI {
    /** Loads wins and losses for a player and kit without blocking the caller's thread. */
    fun getKitStats(playerId: UUID, kitName: String): CompletableFuture<PlayerStats>

    /** Loads the rating for a player and queue without blocking the caller's thread. */
    fun getQueueStats(playerId: UUID, queueName: String): CompletableFuture<QueueStats>
}
