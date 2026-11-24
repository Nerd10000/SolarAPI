package dragon.me.solar.api.queue

import java.util.UUID

/** Read-only snapshot of a matchmaking queue. */
interface Queue {
    val name: String
    val kit: String
    val players: List<UUID>
    val enabled: Boolean
    val flags: QueueFlags

    @Deprecated("Use the enabled property")
    fun isEnabled(): Boolean = enabled
}
