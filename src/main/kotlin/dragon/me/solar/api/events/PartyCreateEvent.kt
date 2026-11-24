package dragon.me.solar.api.events

import dragon.me.solar.api.party.Party
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

class PartyCreateEvent(val party: Party) : Event(false) {
    override fun getHandlers(): HandlerList = HANDLERS

    companion object {
        private val HANDLERS = HandlerList()

        @JvmStatic
        fun getHandlerList(): HandlerList = HANDLERS
    }
}
