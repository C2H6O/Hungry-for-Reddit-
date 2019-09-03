package net.doubov.hungryforreddit

import androidx.test.core.app.ApplicationProvider
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides
import io.mockk.every
import io.mockk.mockk
import kotlinx.serialization.json.Json
import net.doubov.api.AccessTokenResponse
import net.doubov.api.ApiResponse
import net.doubov.api.RedditApi
import net.doubov.hungryforreddit.di.AppComponent
import net.doubov.hungryforreddit.di.AppModule
import net.doubov.hungryforreddit.di.AppScope
import okhttp3.OkHttpClient
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
    fun redditApi(okHttpClient: OkHttpClient, json: Json): RedditApi {
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
@Config(application = OverrideRedditApiModuleTestApp::class)
class ExampleOverridingModule {
    @Test
    fun `RedditApi mock is working properly`() {
        val app = ApplicationProvider.getApplicationContext<OverrideRedditApiModuleTestApp>()
        val redditApi = app.getAppComponent().redditApi()

        val apiResponseSuccess = ApiResponse.Success(AccessTokenResponse("hello", "bearer"))

        every { redditApi.fetchAnonymousAccessToken() } returns apiResponseSuccess

        val response = redditApi.fetchAnonymousAccessToken()

        assertEquals(response, apiResponseSuccess)
    }
}