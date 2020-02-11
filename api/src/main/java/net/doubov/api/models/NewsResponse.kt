package net.doubov.api.models

import kotlinx.serialization.Serializable
import net.doubov.core.network.ApiResponse

@Serializable
data class NewsResponse(val data: DataResponse)

fun ApiResponse.Success<NewsResponse>.toListings(): List<NewsDataResponse> {
    return data.data.children.map { it.data }
}