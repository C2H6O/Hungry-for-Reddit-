package net.doubov.hungryforreddit

import io.kotlintest.specs.FreeSpec
import net.doubov.api.responses.Kind
import net.doubov.api.responses.ListingResponse
import net.doubov.core.di.SerializationModule
import org.junit.Assert

class ListingResponseTest : FreeSpec(body = {

    "Given a response string" - {

        val json = SerializationModule.provideKotlinSerializationJson()

        val responseString = """
            {"kind":"Listing"}
        """.trimIndent()

        "it can be deserialized into ListingResponse" {
            val listingResponse = json.parse(ListingResponse.serializer(), responseString)
            Assert.assertNotNull(listingResponse)
            Assert.assertEquals(Kind.LISTING, listingResponse.kind)
        }
    }
})