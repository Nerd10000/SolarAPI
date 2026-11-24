package dragon.me.solar.api.events

import org.bukkit.event.Event
import org.bukkit.event.HandlerList

/** Fired after a kit edit has been applied by a Solar command. */
class KitModifiedEvent(val kitName: String, val change: Change) : Event(false) {
    enum class Change {
        ITEMS,
        EFFECTS,
        FLAGS
    }

    override fun getHandlers(): HandlerList = handlers

    companion object {
        private val handlers = HandlerList()

        @JvmStatic
        fun getHandlerList(): HandlerList = handlers
    }
}
