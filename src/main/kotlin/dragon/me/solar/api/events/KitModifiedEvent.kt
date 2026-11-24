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

    override fun getHandlers(): HandlerList = HANDLERS

    companion object {
        private val HANDLERS = HandlerList()

        @JvmStatic
        fun getHandlerList(): HandlerList = HANDLERS
    }
}
