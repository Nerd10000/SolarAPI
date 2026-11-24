package dragon.me.solar.api.queue

/** Outcome of a queue join request. */
enum class QueueJoinResult {
    JOINED,
    QUEUE_NOT_FOUND,
    QUEUE_DISABLED,
    PLAYER_ALREADY_QUEUED,
    PLAYER_IN_MATCH,
    RATING_TOO_LOW,
    CANCELLED
}
