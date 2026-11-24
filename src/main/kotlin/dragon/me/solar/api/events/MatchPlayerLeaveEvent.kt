package dragon.me.solar.api.events

import dragon.me.solar.api.match.Match
import org.bukkit.event.HandlerList
import java.util.UUID

class MatchPlayerLeaveEvent(match: Match, val playerId: UUID) : MatchEvent(match) {
    override fun getHandlers(): HandlerList = handlers

    companion object {
        private val handlers = HandlerList()

        @JvmStatic
        fun getHandlerList(): HandlerList = handlers
    }
}
