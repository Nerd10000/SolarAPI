package dragon.me.solar.api.events

import dragon.me.solar.api.queue.Queue
import org.bukkit.event.Cancellable
import org.bukkit.event.HandlerList
import java.util.UUID

/** Cancellation prevents this valid queue join from being applied. */
class QueueJoinEvent(val queue: Queue, val playerId: UUID) : org.bukkit.event.Event(false), Cancellable {
    private var cancelled = false

    override fun isCancelled(): Boolean = cancelled

    override fun setCancelled(cancel: Boolean) {
        cancelled = cancel
    }

    override fun getHandlers(): HandlerList = HANDLERS

    companion object {
        private val HANDLERS = HandlerList()

        @JvmStatic
        fun getHandlerList(): HandlerList = HANDLERS
    }
}
