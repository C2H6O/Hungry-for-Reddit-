package net.doubov.hungryforreddit

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.squareup.workflow.diagnostic.SimpleLoggingDiagnosticListener
import com.squareup.workflow.ui.ViewRegistry
import com.squareup.workflow.ui.WorkflowRunner
import com.squareup.workflow.ui.backstack.BackStackContainer
import com.squareup.workflow.ui.setContentWorkflow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import net.doubov.core.containers.overviewdetail.OverviewDetailContainer
import net.doubov.hungryforreddit.di.DaggerSingleActivityComponent
import net.doubov.hungryforreddit.workflows.RootLayoutRunner
import net.doubov.hungryforreddit.workflows.RootWorkflow
import net.doubov.main.workflows.detail.ListingDetailLayoutRunner
import net.doubov.main.workflows.list.ListingsLayoutRunner
import javax.inject.Inject

@ExperimentalCoroutinesApi
class SingleActivity : AppCompatActivity() {

    @Inject
    lateinit var rootWorkflow: RootWorkflow

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
                    rootWorkflow,
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
            OverviewDetailContainer,
            BackStackContainer,
            ListingsLayoutRunner,
            ListingDetailLayoutRunner,
            RootLayoutRunner
        )
    }
}