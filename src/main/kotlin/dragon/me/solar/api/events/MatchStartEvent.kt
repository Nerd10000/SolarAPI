package dragon.me.solar.api.events

import dragon.me.solar.api.match.Match
import org.bukkit.event.HandlerList

class MatchStartEvent(match: Match) : MatchEvent(match) {
    override fun getHandlers(): HandlerList = handlers

    companion object {
        private val handlers = HandlerList()

        @JvmStatic
        fun getHandlerList(): HandlerList = handlers
    }
}
