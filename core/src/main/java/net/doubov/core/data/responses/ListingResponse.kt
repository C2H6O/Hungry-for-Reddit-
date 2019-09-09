package net.doubov.core.data.responses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.SerializersModule
import net.doubov.core.data.serialization.KindSerializer

@Serializable
data class ListingResponse(
    override val kind: Kind,
    val data: ListingData
) : Listing

@Serializable
data class ListingData(
    val modhash: String,
    val dist: Int,
    val after: String,
    val before: String?,
    val children: List<Thing>
)

interface Listing {
    val kind: Kind
}

interface Thing

@Serializable
@SerialName("t3")
data class T3Thing(
    val data: T3Data
) : Thing

@Serializable
data class T3Data(val subreddit: String)

@Serializable
@SerialName("t5")
data class T5Thing(
    val data: Map<String, String>
) : Thing

@Serializable(KindSerializer::class)
enum class Kind(val value: String) {
    LISTING("Listing")
}

val thingModule = SerializersModule {
    polymorphic(Thing::class) {
        T3Thing::class with T3Thing.serializer()
        T5Thing::class with T5Thing.serializer()
    }
}