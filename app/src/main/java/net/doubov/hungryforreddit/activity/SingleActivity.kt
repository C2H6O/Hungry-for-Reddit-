package net.doubov.hungryforreddit.activity

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.squareup.workflow.RenderContext
import com.squareup.workflow.Snapshot
import com.squareup.workflow.StatefulWorkflow
import com.squareup.workflow.diagnostic.SimpleLoggingDiagnosticListener
import com.squareup.workflow.ui.*
import com.squareup.workflow.ui.LayoutRunner.Companion.bind
import net.doubov.hungryforreddit.R


object RootWorkflow : StatefulWorkflow<Unit, RootWorkflow.State, Nothing, RootWorkflow.Rendering>() {

    sealed class State {
        object Init : State()
    }

    data class Rendering(val blah: String)

    override fun initialState(props: Unit, snapshot: Snapshot?): State = State.Init
    override fun snapshotState(state: State): Snapshot = Snapshot.EMPTY

    override fun render(props: Unit, state: State, context: RenderContext<State, Nothing>): Rendering {
        return Rendering("HIHI")
    }

}

class RootLayoutRunner(view: View) : LayoutRunner<RootWorkflow.Rendering> {

    companion object : ViewBinding<RootWorkflow.Rendering> by bind(R.layout.activity_main, ::RootLayoutRunner)

    private val text = view.findViewById<TextView>(R.id.welcome)

    override fun showRendering(rendering: RootWorkflow.Rendering, containerHints: ContainerHints) {
        text.text = rendering.blah
    }
}

class SingleActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setContentWorkflow(
            registry = viewRegistry,
            configure = {
                WorkflowRunner.Config(
                    RootWorkflow,
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
            RootLayoutRunner
        )
    }
}