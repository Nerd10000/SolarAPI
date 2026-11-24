package dragon.me.solar.api.events

import dragon.me.solar.api.match.Match
import dragon.me.solar.api.team.Team
import org.bukkit.event.HandlerList

class MatchRoundEndEvent(match: Match, val winner: Team) : MatchEvent(match) {
    override fun getHandlers(): HandlerList = HANDLERS

    companion object {
        private val HANDLERS = HandlerList()

        @JvmStatic
        fun getHandlerList(): HandlerList = HANDLERS
    }
}
