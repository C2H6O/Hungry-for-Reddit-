package net.doubov.api.models

import kotlinx.serialization.Serializable

@Serializable
data class NewsDataResponse(
    val id: String,
    val author: String,
    val title: String,
    val num_comments: Int,
    val created: Float,
    val thumbnail: String,
    val url: String,
    val ups: Int
)