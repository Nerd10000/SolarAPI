package dragon.me.solar.api.match

import dragon.me.solar.api.team.Team
import java.util.UUID

/** Read-only snapshot of a match; collection results cannot mutate the live match. */
interface Match {
    val id: UUID
    val state: MatchState
    val players: List<UUID>
    val kit: String
    val arena: String
    val spectators: List<UUID>
    val roundsToWin: Int
    val currentRound: Int
    val freeForAll: Boolean
    val startTimeMillis: Long
    val teams: List<Team>

    @Deprecated("Use the id property")
    fun getUUID(): UUID = id

    @Deprecated("Use the state property")
    fun getMatchState(): MatchState = state

    @Deprecated("Use roundsToWin")
    fun getRounds(): Int = roundsToWin

    @Deprecated("Use freeForAll")
    fun isFFA(): Boolean = freeForAll
}
