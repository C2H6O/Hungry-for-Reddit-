package net.doubov.hungryforreddit

import com.uber.rib.core.InteractorHelper
import com.uber.rib.core.RibTestBasePlaceholder
import net.doubov.api.RedditApi
import net.doubov.hungryforreddit.ribs.RootInteractor
import net.doubov.hungryforreddit.ribs.RootRouter
import net.doubov.hungryforreddit.ribs.TestRootInteractor
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class RootInteractorTest : RibTestBasePlaceholder() {

    @Mock
    lateinit var presenter: RootInteractor.RootRibPresenter
    @Mock
    lateinit var router: RootRouter
    @Mock
    lateinit var redditApi: RedditApi

    private var interactor: RootInteractor? = null

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        interactor = TestRootInteractor.create(presenter, redditApi)
    }

    /**
     * TODO: Delete this example and add real tests.
     */
    @Test
    fun anExampleTest_withSomeConditions_shouldPass() {
        // Use InteractorHelper to drive your interactor's lifecycle.
        InteractorHelper.attach<RootInteractor.RootRibPresenter, RootRouter>(interactor!!, presenter, router, null)
        InteractorHelper.detach(interactor!!)

        throw RuntimeException("Remove this test and add real tests.")
    }
}