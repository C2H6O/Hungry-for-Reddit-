package net.doubov.api.models

import kotlinx.serialization.Serializable

@Serializable
data class DataResponse(
    val children: List<ChildrenResponse>,
    val after: String?,
    val before: String?
)