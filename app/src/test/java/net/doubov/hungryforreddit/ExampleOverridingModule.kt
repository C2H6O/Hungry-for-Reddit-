package net.doubov.hungryforreddit

import android.os.Build
import androidx.test.core.app.ApplicationProvider
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides
import io.mockk.every
import io.mockk.mockk
import net.doubov.api.RedditApi
import net.doubov.api.models.ChildrenResponse
import net.doubov.api.models.DataResponse
import net.doubov.api.models.NewsDataResponse
import net.doubov.api.models.NewsResponse
import net.doubov.core.di.AppScope
import net.doubov.core.network.ApiResponse
import net.doubov.hungryforreddit.di.AppComponent
import net.doubov.hungryforreddit.di.AppModule
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@AppScope
@Component(modules = [AppModule::class, TestRedditApiModule::class])
interface TestAppComponent : AppComponent {
    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance app: App,
            appModule: AppModule,
            redditApiModule: TestRedditApiModule
        ): AppComponent
    }
}

@Module
object TestRedditApiModule {
    @Provides
    @AppScope
    fun redditApi(): RedditApi {
        return mockk()
    }
}

class OverrideRedditApiModuleTestApp : App() {
    override fun getAppComponent(): AppComponent {
        if (_appComponent == null) {
            _appComponent = DaggerTestAppComponent.factory().create(this, AppModule, TestRedditApiModule)
        }
        return _appComponent ?: throw IllegalStateException("TestAppComponent must not be null at this point")
    }
}

@RunWith(RobolectricTestRunner::class)
@Config(application = OverrideRedditApiModuleTestApp::class, sdk = [Build.VERSION_CODES.P])
class ExampleOverridingModule {
    @Test
    fun `RedditApi mock is working properly`() {
        val app = ApplicationProvider.getApplicationContext<OverrideRedditApiModuleTestApp>()
        val redditApi = app.getAppComponent().redditApi()

        val apiResponseSuccess = ApiResponse.Success(
            NewsResponse(
                DataResponse(
                    children = listOf(
                        ChildrenResponse(
                            NewsDataResponse(
                                id = "1",
                                author = "author1",
                                title = "title1",
                                num_comments = 11,
                                created = 123432f,
                                thumbnail = "thumbnail url",
                                url = "url",
                                ups = 15
                            )
                        )
                    ), after = null, before = null
                )
            )
        )

        every { redditApi.fetchFrontPage() } returns apiResponseSuccess

        val response = redditApi.fetchFrontPage()

        assertEquals(response, apiResponseSuccess)
    }
}