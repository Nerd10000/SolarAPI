package dragon.me.solar.api.team

/** Read-only team snapshot within a match. */
interface Team {
    val players: List<TeamPlayer>
    val roundWins: Int
    val alive: Boolean

    /** Returns the team's average rating for the named queue. */
    fun ratingFor(queueName: String): Int

    @Deprecated("Use the alive property")
    fun isAlive(): Boolean = alive

    @Deprecated("Use ratingFor")
    fun getRating(queueName: String): Int = ratingFor(queueName)
}
