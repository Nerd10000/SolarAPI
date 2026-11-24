package dragon.me.solar.api.kit

import dragon.me.solar.api.flags.FlagCodec
import dragon.me.solar.api.flags.FlagKey
import dragon.me.solar.api.flags.FlagSerializationException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class KitFlagsTest {
    private val registry = KitFlagKeys.createRegistry()

    @Test
    fun `built in defaults are not explicitly configured and reset restores defaults`() {
        val flags = KitFlags(registry)

        assertTrue(flags.get(KitFlagKeys.NATURAL_REGENERATION))
        assertFalse(flags.isConfigured(KitFlagKeys.NATURAL_REGENERATION))

        flags.set(KitFlagKeys.NATURAL_REGENERATION, false)
        assertFalse(flags.get(KitFlagKeys.NATURAL_REGENERATION))
        assertTrue(flags.isConfigured(KitFlagKeys.NATURAL_REGENERATION))

        flags.reset(KitFlagKeys.NATURAL_REGENERATION)
        assertTrue(flags.get(KitFlagKeys.NATURAL_REGENERATION))
        assertFalse(flags.isConfigured(KitFlagKeys.NATURAL_REGENERATION))

        flags.set(KitFlagKeys.NATURAL_REGENERATION, true)
        assertTrue(flags.isConfigured(KitFlagKeys.NATURAL_REGENERATION))
    }

    @Test
    fun `registry rejects duplicate ids and type conflicts`() {
        val integerKey = key("addon:counter", Int::class.javaObjectType, 0, intCodec)
        registry.register(integerKey)

        assertThrows(IllegalArgumentException::class.java) {
            registry.register(integerKey)
        }
        assertThrows(IllegalStateException::class.java) {
            registry.register(key("addon:counter", String::class.java, "", stringCodec))
        }
    }

    @Test
    fun `built in primitive types and addon codecs serialize and deserialize`() {
        val booleanKey = key("test:boolean", Boolean::class.javaObjectType, false, booleanCodec)
        val integerKey = key("test:integer", Int::class.javaObjectType, 0, intCodec)
        val doubleKey = key("test:double", Double::class.javaObjectType, 0.0, doubleCodec)
        val stringKey = key("test:string", String::class.java, "", stringCodec)
        val pointKey = key("test:point", Point::class.java, Point(0, 0), pointCodec)
        listOf(booleanKey, integerKey, doubleKey, stringKey, pointKey).forEach(registry::register)

        val flags = KitFlags(registry)
        flags.set(booleanKey, true)
        flags.set(integerKey, 42)
        flags.set(doubleKey, 1.25)
        flags.set(stringKey, "value")
        flags.set(pointKey, Point(3, 5))
        flags.set(KitFlagKeys.MAPS, listOf("", "map,with,commas", "*"))

        val restored = KitFlags.deserialize(registry, flags.serialize())
        assertTrue(restored.get(booleanKey))
        assertEquals(42, restored.get(integerKey))
        assertEquals(1.25, restored.get(doubleKey))
        assertEquals("value", restored.get(stringKey))
        assertEquals(Point(3, 5), restored.get(pointKey))
        assertEquals(listOf("", "map,with,commas", "*"), restored.get(KitFlagKeys.MAPS))
    }

    @Test
    fun `copy isolates mutable values and preserves unknown payloads`() {
        val boxKey = key("addon:box", Box::class.java, Box(0), boxCodec)
        registry.register(boxKey)
        val unknown = mapOf("future-addon:payload" to "v9|opaque")
        val flags = KitFlags.deserialize(registry, unknown)
        val originalBox = Box(5)
        flags.set(boxKey, originalBox)
        originalBox.value = 9

        val copy = flags.copy()
        val copiedBox = copy.get(boxKey)
        copiedBox.value = 12
        copy.set(boxKey, copiedBox)

        assertEquals(5, flags.get(boxKey).value)
        assertEquals(12, copy.get(boxKey).value)
        assertEquals("v9|opaque", copy.serialize()["future-addon:payload"])
        assertTrue(copy.isConfigured(boxKey))
    }

    @Test
    fun `unknown flag is decoded lazily after its key is registered`() {
        val flags = KitFlags.deserialize(registry, mapOf("addon:late" to "17"))
        val lateKey = key("addon:late", Int::class.javaObjectType, 0, intCodec)
        registry.register(lateKey)

        assertEquals(17, flags.get(lateKey))
        assertEquals("17", flags.serialize()["addon:late"])
    }

    @Test
    fun `malformed known values fail explicitly`() {
        val key = key("addon:bad", Int::class.javaObjectType, 0, intCodec)
        registry.register(key)

        assertThrows(FlagSerializationException::class.java) {
            KitFlags.deserialize(registry, mapOf(key.id to "not-an-int"))
        }
    }

    private fun <T : Any> key(id: String, type: Class<T>, default: T, codec: FlagCodec<T>) =
        FlagKey.of(id, type, default, codec)

    private data class Point(val x: Int, val y: Int)

    private class Box(var value: Int)

    private val booleanCodec = object : FlagCodec<Boolean> {
        override fun encode(value: Boolean) = value.toString()
        override fun decode(encoded: String) = encoded.toBooleanStrict()
    }
    private val intCodec = object : FlagCodec<Int> {
        override fun encode(value: Int) = value.toString()
        override fun decode(encoded: String) = encoded.toInt()
    }
    private val doubleCodec = object : FlagCodec<Double> {
        override fun encode(value: Double) = value.toString()
        override fun decode(encoded: String) = encoded.toDouble()
    }
    private val stringCodec = object : FlagCodec<String> {
        override fun encode(value: String) = value
        override fun decode(encoded: String) = encoded
    }
    private val pointCodec = object : FlagCodec<Point> {
        override fun encode(value: Point) = "${value.x},${value.y}"
        override fun decode(encoded: String): Point {
            val (x, y) = encoded.split(",").map(String::toInt)
            return Point(x, y)
        }
    }
    private val boxCodec = object : FlagCodec<Box> {
        override fun encode(value: Box) = value.value.toString()
        override fun decode(encoded: String) = Box(encoded.toInt())
    }
}
