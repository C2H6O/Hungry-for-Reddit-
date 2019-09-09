package net.doubov.core.data.serialization

import kotlinx.serialization.Decoder
import kotlinx.serialization.Encoder
import kotlinx.serialization.SerialDescriptor
import kotlinx.serialization.Serializer
import net.doubov.core.data.responses.Kind

@Serializer(forClass = Kind::class)
object KindSerializer {
    override val descriptor: SerialDescriptor
        get() = TODO("not implemented")

    override fun deserialize(decoder: Decoder): Kind {
        val value = decoder.decodeString()
        return Kind.values().first { it.value == value }
    }

    override fun serialize(encoder: Encoder, obj: Kind) {
        encoder.encodeString(obj.value)
    }
}