package dragon.me.solar.api.queue

import java.util.Optional

/**
 * Queue lookups and matchmaking operations.
 *
 * Thread safety: call from the server thread. Queue and flag values returned by queries
 * are immutable snapshots.
 */
interface QueueAPI {
    /** Returns an empty Optional when the queue does not exist. */
    fun getQueue(name: String): Optional<Queue>

    /** Returns immutable snapshots of all configured queues. */
    fun getQueues(): List<Queue>

    /** Attempts to join an enabled queue; must be called on the server thread. */
    fun join(playerId: java.util.UUID, queueName: String): QueueJoinResult

    /** Leaves the named queue and returns whether the player was queued there. */
    fun leave(playerId: java.util.UUID, queueName: String): Boolean
}
