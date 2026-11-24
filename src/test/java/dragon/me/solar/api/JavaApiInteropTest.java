package dragon.me.solar.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import dragon.me.solar.api.flags.FlagCodecs;
import dragon.me.solar.api.flags.FlagKey;
import dragon.me.solar.api.events.MatchStartEvent;
import dragon.me.solar.api.kit.KitFlagKeys;
import dragon.me.solar.api.kit.KitFlags;
import org.junit.jupiter.api.Test;

class JavaApiInteropTest {

    @Test
    void javaCanRegisterAndUseTypedKeys() {
        FlagKey<Integer> key = FlagKey.of("java-addon:limit", Integer.class, 3, FlagCodecs.INTEGER);
        FlagKey<String> label = FlagKey.of("java-addon:label", String.class, "", FlagCodecs.STRING);
        KitFlags flags = new KitFlags();
        flags.getRegistry().register(key);
        flags.getRegistry().register(label);
        flags.set(key, 7);
        flags.set(label, "typed");

        assertEquals(7, flags.get(key));
        assertEquals("typed", flags.get(label));
        flags.set(KitFlagKeys.NATURAL_REGENERATION, false);
        assertFalse(flags.get(KitFlagKeys.NATURAL_REGENERATION));

        @SuppressWarnings({"rawtypes", "unchecked"})
        FlagKey rawKey = KitFlagKeys.NATURAL_REGENERATION;
        assertThrows(IllegalArgumentException.class, () -> flags.set(rawKey, "not a boolean"));
        assertEquals("1.0.0", SolarAPI.API_VERSION);
        assertNotNull(MatchStartEvent.getHandlerList());
    }
}
