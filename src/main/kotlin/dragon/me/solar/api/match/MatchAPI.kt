package dragon.me.solar.api.match

import java.util.Optional
import java.util.UUID

/**
 * Queries for currently active matches.
 *
 * Thread safety: call from the server thread. Returned objects and lists are immutable
 * snapshots and remain readable after the match ends.
 */
interface MatchAPI {
    /** Returns an empty Optional when the match is not active. */
    fun getMatch(id: UUID): Optional<Match>

    /** Returns the active match containing the player, if any. */
    fun getMatchByPlayer(playerId: UUID): Optional<Match>

    /** An immutable snapshot of all active matches. */
    fun getActiveMatches(): List<Match>
}
