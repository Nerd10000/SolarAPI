package dragon.me.solar.api.events

import dragon.me.solar.api.queue.Queue
import org.bukkit.event.Event
import org.bukkit.event.HandlerList
import java.util.UUID

/** Fired after a player has been removed from a queue. */
class QueueLeaveEvent(val queue: Queue, val playerId: UUID) : Event(false) {
    override fun getHandlers(): HandlerList = handlers

    companion object {
        private val handlers = HandlerList()

        @JvmStatic
        fun getHandlerList(): HandlerList = handlers
    }
}
