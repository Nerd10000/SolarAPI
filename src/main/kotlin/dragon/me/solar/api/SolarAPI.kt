package dragon.me.solar.api

import dragon.me.solar.api.flags.FlagRegistry
import dragon.me.solar.api.kit.KitAPI
import dragon.me.solar.api.match.MatchAPI
import dragon.me.solar.api.arena.ArenaAPI
import dragon.me.solar.api.party.PartyAPI
import dragon.me.solar.api.queue.QueueAPI
import dragon.me.solar.api.stats.PlayerStatsAPI

/**
 * Entry point supplied through Bukkit's ServicesManager after Solar finishes enabling.
 * The service becomes unavailable when Solar disables; consumers must not cache it
 * across a Solar reload.
 */
interface SolarAPI {
    /** Semantic version of these public contracts; independent of Solar's plugin version. */
    val apiVersion: String

    val flagRegistry: FlagRegistry

    /** These Paper-backed query APIs are confined to the server thread. */
    val matches: MatchAPI
    val queues: QueueAPI
    val statistics: PlayerStatsAPI
    val kits: KitAPI
    val parties: PartyAPI
    val arenas: ArenaAPI

    @Deprecated("Use the matches property")
    fun getMatchAPI(): MatchAPI = matches

    @Deprecated("Use the queues property")
    fun getQueueAPI(): QueueAPI = queues

    @Deprecated("Use the statistics property")
    fun getPlayerStatsAPI(): PlayerStatsAPI = statistics

    @Deprecated("Use the kits property")
    fun getKitAPI(): KitAPI = kits

    companion object {
        /** Current public API semantic version. */
        const val API_VERSION: String = "1.0.0"
    }
}
