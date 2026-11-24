package dragon.me.solar.api.events

import dragon.me.solar.api.match.Match
import org.bukkit.event.HandlerList

class MatchRoundStartEvent(match: Match) : MatchEvent(match) {
    override fun getHandlers(): HandlerList = HANDLERS

    companion object {
        private val HANDLERS = HandlerList()

        @JvmStatic
        fun getHandlerList(): HandlerList = HANDLERS
    }
}
