package dragon.me.solar.api.events

import dragon.me.solar.api.match.Match
import dragon.me.solar.api.match.MatchEndReason
import dragon.me.solar.api.match.MatchResult
import dragon.me.solar.api.team.Team
import org.bukkit.event.HandlerList

class MatchEndEvent(match: Match, val result: MatchResult) : MatchEvent(match) {
    val reason: MatchEndReason
        get() = result.reason

    val winner: Team?
        get() = result.winner

    constructor(match: Match, reason: MatchEndReason, winner: Team?) :
        this(match, MatchResult(reason, winner))

    override fun getHandlers(): HandlerList = HANDLERS

    companion object {
        private val HANDLERS = HandlerList()

        @JvmStatic
        fun getHandlerList(): HandlerList = HANDLERS
    }
}
