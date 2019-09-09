package net.doubov.core.data.responses

import kotlinx.serialization.Serializable

@Serializable
data class AccessTokenResponse(val access_token: String, val token_type: String)