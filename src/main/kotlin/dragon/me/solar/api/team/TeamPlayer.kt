package dragon.me.solar.api.team

import java.util.UUID

/** A player participating in a match team. */
interface TeamPlayer {
    val uuid: UUID
    val alive: Boolean

    @Deprecated("Use the uuid property")
    fun getUUID(): UUID = uuid

    @Deprecated("Use the alive property")
    fun isAlive(): Boolean = alive
}
