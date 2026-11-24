# Solar Public API
![Static Badge](https://img.shields.io/badge/Discord-here-world?style=flat-square&link=https%3A%2F%2Fdiscord.gg%2FaEjRCvCGue)
[![GitHub license](https://badgen.net/github/license/Naereen/Strapdown.js)](https://github.com/Naereen/StrapDown.js/blob/master/LICENSE)

`solar-api` contains Solar's public contracts and stable value types. It is a separate
Gradle module; Solar's Paper implementation adapts its existing managers and services
to these contracts. Addons should depend on this artifact, not Solar's implementation
classes.

### GitHub authentication

Solar API is hosted on GitHub Packages. You need a GitHub Personal Access Token (classic)
with the `read:packages` permission to download it.

Create one here: https://github.com/settings/tokens

Add your credentials to your Gradle user-level `gradle.properties` file:

- Windows: `%USERPROFILE%\.gradle\gradle.properties`
- Linux/macOS: `~/.gradle/gradle.properties`

```properties
gpr.user=YOUR_GITHUB_USERNAME
gpr.key=YOUR_GITHUB_TOKEN
```

```kotlin
repositories {
    maven {
        url = uri("https://maven.pkg.github.com/Nerd10000/SolarAPI")

        credentials {
            username = providers.gradleProperty("gpr.user").orNull // You need to set these in <USER_DIR>/.gradle/gradle.properties
            password = providers.gradleProperty("gpr.key").orNull // You need to set these in <USER_DIR>/.gradle/gradle.properties
        }
    } 
}

dependencies {
    compileOnly("dragon.me:solar-api:1.0.0")
}
```

The API is published independently of the plugin release version. Solar's `apiVersion`
uses semantic versioning: PATCH releases fix behavior without breaking callers, MINOR
releases add compatible API, and MAJOR releases may break source or binary compatibility.
The first redesigned contract is `1.0.0`; older Solar alpha API packages and signatures
are not a stable compatibility target.

## Obtaining the API

Declare Solar as a plugin dependency so its service is available before your addon enables:

```yaml
depend: [Solar]
```

Java:

```java
SolarAPI solar = getServer().getServicesManager().load(SolarAPI.class);
if (solar == null) {
    throw new IllegalStateException("Solar API is unavailable");
}

Optional<Match> match = solar.getMatches().getMatchByPlayer(player.getUniqueId());
```

Kotlin:

```kotlin
val solar = server.servicesManager.load(SolarAPI::class.java)
    ?: error("Solar API is unavailable")

val match = solar.matches.getMatchByPlayer(player.uniqueId)
```

Solar registers the service after its managers and configuration have initialized and
unregisters it on disable. Do not retain the service across a Solar disable/reload.
The match, queue, kit, party, and arena query APIs access Paper-owned state and must be
called on the server thread. Returned domain objects and collections are snapshots;
they never expose Solar managers, records, caches, or mutable collections.

## Querying domains

The API exposes read-only match, kit, queue, party, and arena snapshots. It does not
currently let addons create arenas, edit kits, or create parties; those operations
remain Solar-owned commands and workflows.

```java
Optional<Kit> kit = solar.getKits().getKit("NoDebuff");
kit.ifPresent(value -> solar.getKits().applyKit(value, player));

List<Queue> queues = solar.getQueues().getQueues();
Optional<Party> party = solar.getParties().getPartyByMember(player.getUniqueId());
List<Arena> arenas = solar.getArenas().getArenas();
```

Queue joins and leaves run on the server thread. A join returns a `QueueJoinResult`;
`CANCELLED` means a listener cancelled the valid join request.

```java
QueueJoinResult result = solar.getQueues().join(player.getUniqueId(), "NoDebuff");
if (result == QueueJoinResult.JOINED) {
    // The request was accepted.
}
```

Player-stat reads are asynchronous and return immutable snapshots:

```java
solar.getStatistics().getKitStats(player.getUniqueId(), "NoDebuff")
    .thenAccept(stats -> getLogger().info("Wins: " + stats.getWins()));
```

## Typed flags

`FlagKey<T>` declares a namespaced ID, Java class token, non-null default, and codec.
Register addon keys once with Solar's `FlagRegistry`; duplicate IDs are rejected, and
reusing an ID with another type is an error. Built-in keys are available from
`KitFlagKeys`. Solar's service registry includes these built-ins; a standalone
`FlagRegistry` is empty, while `KitFlagKeys.createRegistry()` creates one with kit keys.
The generic `FlagValues` container shares the same registry/codec model for future
domain flags. Flag definitions are data only; Solar's gameplay listeners decide what
each built-in value does.

Java:

```java
FlagKey<Integer> rounds = FlagKey.of(
    "example-addon:round-limit", Integer.class, 3, FlagCodecs.INTEGER);
solar.getFlagRegistry().register(rounds);

KitFlags flags = kit.get().getFlags(); // independent mutable snapshot
flags.set(KitFlagKeys.NATURAL_REGENERATION, false);
int limit = flags.get(rounds);
boolean explicit = flags.isConfigured(rounds);
flags.reset(rounds);
Map<String, String> stored = flags.serialize();
```

Kotlin:

```kotlin
val roundLimit = FlagKey.of(
    "example-addon:round-limit",
    Int::class.javaObjectType,
    3,
    FlagCodecs.INTEGER
)
solar.flagRegistry.register(roundLimit)

val flags = kit.flags
flags.set(KitFlagKeys.NATURAL_REGENERATION, false)
val limit = flags.get(roundLimit)
```

Flag containers are thread-safe. Their `get` results and `copy()` use codec round-trips,
so mutable addon values are independent when the codec correctly copies on decode.
Unconfigured values read as defaults. `serialize()` stores explicit values only. During
deserialization unknown IDs are retained as opaque strings and survive serialization;
if an addon registers the key later, its value is decoded lazily. Malformed data for a
registered key throws `FlagSerializationException` rather than silently falling back.

Solar continues to read and write the existing built-in kit YAML fields
(`max-health`, cooldowns, map restrictions, and so on). Addon-defined values are stored
under the additive `custom-flags` map; no existing field or database format is renamed.

## Events

Solar lifecycle events use Bukkit/Paper's `Event` and `HandlerList` conventions and
carry immutable snapshots or IDs. Match, party, kit, and queue event handlers execute
synchronously on the server thread. Only `QueueJoinEvent` is cancellable: cancellation
prevents that valid join request from being queued. Leave, match lifecycle, party, and
kit events are notifications and cannot veto Solar state transitions.

```java
@EventHandler
public void onMatchEnd(MatchEndEvent event) {
    MatchResult result = event.getResult();
    getLogger().info("Match " + event.getMatch().getId() + " ended: " + result.getReason());
}
```

Register and unregister listeners using Bukkit's plugin manager as usual. Queue join
events fire before the player is added; queue leave events fire after removal. Match
player join/leave notifications accompany the active match's participant lifecycle.

## Threading and errors

* Match, queue, kit, party, and arena operations that touch live Paper state are
  server-thread operations. Kit layout application returns a `CompletableFuture`; its
  inventory changes are scheduled on the server thread.
* Statistics return `CompletableFuture` and do not block the caller.
* Empty lookups use `Optional`; invalid flag registration and malformed known flag data
  throw documented exceptions.
* `KitFlags` mutations apply to that flags value. A kit's `getFlags()` returns an
  independent copy; this API version does not expose a kit-edit/persistence operation.
