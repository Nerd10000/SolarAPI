package dragon.me.solar.api.stats

import java.util.UUID

/** Immutable win/loss statistics for one player and kit. */
data class PlayerStats(
    val playerId: UUID,
    val kit: String,
    val wins: Int,
    val losses: Int
)
