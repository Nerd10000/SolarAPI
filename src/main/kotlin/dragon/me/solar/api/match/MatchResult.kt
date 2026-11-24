package dragon.me.solar.api.match

import dragon.me.solar.api.team.Team

/** Immutable outcome snapshot. Winner is absent for draws or administrative termination. */
data class MatchResult(val reason: MatchEndReason, val winner: Team?)
