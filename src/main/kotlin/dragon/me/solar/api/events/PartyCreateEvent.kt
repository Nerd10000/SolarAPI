package dragon.me.solar.api.events

import dragon.me.solar.api.party.Party
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

class PartyCreateEvent(val party: Party) : Event(false) {
    override fun getHandlers(): HandlerList = handlers

    companion object {
        private val handlers = HandlerList()

        @JvmStatic
        fun getHandlerList(): HandlerList = handlers
    }
}
