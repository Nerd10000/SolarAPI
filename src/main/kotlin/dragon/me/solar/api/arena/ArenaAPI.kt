package dragon.me.solar.api.arena

import java.util.Optional

/** Read-only configured-arena queries. Calls must be made on the server thread. */
interface ArenaAPI {
    /** Returns an empty Optional when no arena has this name. */
    fun getArena(name: String): Optional<Arena>

    /** Returns immutable snapshots of all configured arenas. */
    fun getArenas(): List<Arena>
}
