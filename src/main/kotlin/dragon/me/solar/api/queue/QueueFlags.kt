package dragon.me.solar.api.queue

/** Immutable matchmaking rules for a queue. */
interface QueueFlags {
    val weight: String
    val teamSize: Int
    val partiesAllowed: Boolean
    val minimumRating: Int
    val permission: String
    val minimumPartySize: Int
    val maximumPartySize: Int

    @Deprecated("Use the partiesAllowed property")
    fun arePartiesAllowed(): Boolean = partiesAllowed

    @Deprecated("Use minimumRating")
    fun getMinRating(): Int = minimumRating

    @Deprecated("Use minimumPartySize")
    fun getMinPartySize(): Int = minimumPartySize

    @Deprecated("Use maximumPartySize")
    fun getMaxPartySize(): Int = maximumPartySize
}
