package net.doubov.hungryforreddit

import io.kotlintest.specs.FreeSpec
import net.doubov.core.data.responses.*
import net.doubov.core.di.SerializationModule
import org.junit.Assert
import java.util.*

class ListingResponseTest : FreeSpec(body = {

    fun randomString() = UUID.randomUUID().toString()

    val json = SerializationModule.provideKotlinSerializationJson()

    "Given a response string" - {

        val modhash = randomString()
        val dist = 666
        val after = randomString()

        val responseString = """
            {
                "kind":"Listing",
                "data":
                {
                    "modhash":"$modhash",
                    "dist":$dist,
                    "after":"$after",
                    "before":null,
                    "children":
                    [
                        {
                            "kind":"t3",
                            "data": 
                            {
                                "subreddit":"investing"
                            }
                        }
                    ]
                }
            }
        """.trimIndent()

        "it can be deserialized into ListingResponse" {
            val listingResponse = json.parse(ListingResponse.serializer(), responseString)
            Assert.assertNotNull(listingResponse)
            Assert.assertEquals(Kind.LISTING, listingResponse.kind)
            Assert.assertNull(listingResponse.data.before)
            Assert.assertEquals(modhash, listingResponse.data.modhash)
            Assert.assertEquals(dist, listingResponse.data.dist)
            Assert.assertEquals(after, listingResponse.data.after)
            val firstChild = listingResponse.data.children.first()
            Assert.assertTrue(firstChild is T3Thing)
            val t3Child = firstChild as T3Thing
            Assert.assertEquals("investing", t3Child.data.subreddit)
        }
    }

    "Given a ListingResponse" - {
        val listingResponse = ListingResponse(
            kind = Kind.LISTING,
            data = ListingData(
                modhash = randomString(),
                dist = 666,
                after = randomString(),
                before = randomString(),
                children = listOf(T3Thing(T3Data(subreddit = "investing")), T5Thing(mapOf("name" to "economics")))
            )
        )

        "it can be serialized into json string" {
            val expected = """
                {
                    "kind":"Listing",
                    "data":
                    {
                        "modhash":"${listingResponse.data.modhash}",
                        "dist":${listingResponse.data.dist},
                        "after":"${listingResponse.data.after}",
                        "before":"${listingResponse.data.before}",
                        "children":
                        [
                            {
                                "kind":"t3",
                                "data":
                                {
                                    "subreddit":"investing"
                                }
                            },
                            {
                                "kind":"t5",
                                "data":
                                {
                                    "name":"economics"
                                }
                            }
                        ]
                    }
                }"""
                .replace(" ", "")
                .replace("\n", "")
            val actual = json.stringify(ListingResponse.serializer(), listingResponse)
            Assert.assertEquals(expected, actual)
        }
    }
})