package net.doubov.hungryforreddit

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import net.doubov.api.responses.AccessTokenResponse
import org.junit.Assert
import org.junit.Test

class AccessTokenResponseTest {

    private val json = Json(JsonConfiguration.Default)

    @Test
    fun `deserialize to object`() {
        val responseString =
            """
                {
                    "access_token" : "Hello there",
                    "token_type" : "bearer"
                }  
            """.trimIndent()

        val response = json.parse(AccessTokenResponse.serializer(), responseString)

        Assert.assertEquals("Hello there", response.access_token)
        Assert.assertEquals("bearer", response.token_type)
    }

    @Test
    fun `serialize to string`() {
        val accessTokenResponse = AccessTokenResponse(
            access_token = "Hello there",
            token_type = "bearer"
        )

        val responseString = json.stringify(AccessTokenResponse.serializer(), accessTokenResponse)

        Assert.assertEquals("""{"access_token":"Hello there","token_type":"bearer"}""", responseString)
    }
}