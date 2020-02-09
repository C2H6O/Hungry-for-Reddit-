package net.doubov.hungryforreddit

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.squareup.workflow.diagnostic.SimpleLoggingDiagnosticListener
import com.squareup.workflow.ui.ViewRegistry
import com.squareup.workflow.ui.WorkflowRunner
import com.squareup.workflow.ui.backstack.BackStackContainer
import com.squareup.workflow.ui.setContentWorkflow
import net.doubov.api.RedditApi
import net.doubov.hungryforreddit.containers.masterdetail.MasterDetailContainer
import net.doubov.hungryforreddit.di.DaggerSingleActivityComponent
import net.doubov.hungryforreddit.workflows.RootLayoutRunner
import net.doubov.hungryforreddit.workflows.RootWorkflow
import net.doubov.hungryforreddit.workflows.detail.ListingDetailLayoutRunner
import net.doubov.hungryforreddit.workflows.list.ListingsLayoutRunner
import javax.inject.Inject

class SingleActivity : AppCompatActivity() {

    @Inject
    lateinit var redditApi: RedditApi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        DaggerSingleActivityComponent
            .builder()
            .appComponent(App.getApp(this).getAppComponent())
            .build()
            .inject(this)

        setContentWorkflow(
            registry = viewRegistry,
            configure = {
                WorkflowRunner.Config(
                    RootWorkflow(redditApi),
                    diagnosticListener = SimpleLoggingDiagnosticListener()
                )
            },
            onResult = {
                finish()
            }
        )
    }

    private companion object {
        private val viewRegistry = ViewRegistry(
            MasterDetailContainer,
            BackStackContainer,
            ListingsLayoutRunner,
            ListingDetailLayoutRunner,
            RootLayoutRunner
        )
    }
}