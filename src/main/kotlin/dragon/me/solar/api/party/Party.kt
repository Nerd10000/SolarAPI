package dragon.me.solar.api.party

import java.util.UUID
import java.util.Collections

/** Immutable snapshot of a party. */
class Party(
    val id: UUID,
    val owner: UUID,
    members: Collection<UUID>,
    val maximumMembers: Int
) {
    val members: List<UUID> = Collections.unmodifiableList(ArrayList(members))
}
