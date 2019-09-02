package net.doubov.api

import kotlinx.serialization.Serializable

@Serializable
data class AccessTokenResponse(val access_token: String, val token_type: String)