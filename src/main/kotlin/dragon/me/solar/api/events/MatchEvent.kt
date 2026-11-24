package dragon.me.solar.api.events

import dragon.me.solar.api.match.Match
import org.bukkit.event.Event

/** Base for immutable match lifecycle event payloads. Events are synchronous. */
abstract class MatchEvent(val match: Match) : Event(false)
