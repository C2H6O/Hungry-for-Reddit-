package net.doubov.api.responses

import kotlinx.serialization.Serializable
import net.doubov.api.serialization.KindSerializer

@Serializable
data class ListingResponse(val kind: Kind)

@Serializable(KindSerializer::class)
enum class Kind(val value: String) {
    LISTING("Listing")
}