package net.doubov.api.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NewsDataResponse(
    @SerialName("id")
    val uid: String,
    val author: String,
    val title: String,
    val num_comments: Int,
    val created: Float,
    val thumbnail: String,
    val url: String,
    val ups: Int
)