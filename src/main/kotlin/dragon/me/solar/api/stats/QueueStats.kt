package dragon.me.solar.api.stats

import java.util.UUID

/** Immutable matchmaking rating for one player and queue. */
data class QueueStats(
    val playerId: UUID,
    val queue: String,
    val rating: Int
)
