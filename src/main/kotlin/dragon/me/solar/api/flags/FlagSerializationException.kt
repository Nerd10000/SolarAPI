package dragon.me.solar.api.flags

/** Indicates that a known flag value could not be encoded or decoded. */
class FlagSerializationException(message: String, cause: Throwable? = null) : RuntimeException(message, cause)
