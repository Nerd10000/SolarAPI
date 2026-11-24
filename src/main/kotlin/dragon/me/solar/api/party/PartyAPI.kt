package dragon.me.solar.api.party

import java.util.Optional
import java.util.UUID

/** Read-only active-party queries. Calls must be made on the server thread. */
interface PartyAPI {
    /** Returns the party containing a player, if the player is currently in one. */
    fun getPartyByMember(playerId: UUID): Optional<Party>

    /** Returns an immutable snapshot of all active parties. */
    fun getParties(): List<Party>
}
