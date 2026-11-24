package dragon.me.solar.api.events

import dragon.me.solar.api.queue.Queue
import org.bukkit.event.Event
import org.bukkit.event.HandlerList
import java.util.UUID

/** Fired after a player has been removed from a queue. */
class QueueLeaveEvent(val queue: Queue, val playerId: UUID) : Event(false) {
    override fun getHandlers(): HandlerList = HANDLERS

    companion object {
        private val HANDLERS = HandlerList()

        @JvmStatic
        fun getHandlerList(): HandlerList = HANDLERS
    }
}
