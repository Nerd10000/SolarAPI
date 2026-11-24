package dragon.me.solar.api.events

import org.bukkit.event.Event
import org.bukkit.event.HandlerList
import java.util.Collections

/** Fired after Solar has loaded the configured kit set. */
class KitLoadEvent(kitNames: Collection<String>) : Event(false) {
    val kitNames: List<String> = Collections.unmodifiableList(ArrayList(kitNames))

    override fun getHandlers(): HandlerList = HANDLERS

    companion object {
        private val HANDLERS = HandlerList()

        @JvmStatic
        fun getHandlerList(): HandlerList = HANDLERS
    }
}
