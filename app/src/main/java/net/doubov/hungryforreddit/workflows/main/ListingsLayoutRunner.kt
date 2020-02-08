package net.doubov.hungryforreddit.workflows.main

import android.view.View
import com.airbnb.epoxy.EpoxyRecyclerView
import com.squareup.workflow.ui.ContainerHints
import com.squareup.workflow.ui.LayoutRunner
import com.squareup.workflow.ui.LayoutRunner.Companion.bind
import com.squareup.workflow.ui.ViewBinding
import net.doubov.hungryforreddit.R
import net.doubov.main.views.headerView

class ListingsLayoutRunner(view: View) : LayoutRunner<ListingsWorkflow.Rendering> {

    companion object : ViewBinding<ListingsWorkflow.Rendering> by bind(
        R.layout.fragment_main_list,
        ::ListingsLayoutRunner
    )

    private val list = view.findViewById<EpoxyRecyclerView>(R.id.recyclerView)

    override fun showRendering(rendering: ListingsWorkflow.Rendering, containerHints: ContainerHints) {
        list.withModels {
            rendering.newsResponse.forEachIndexed { index, childResponse ->
                headerView {
                    id(childResponse.id)
                    title(childResponse.title)
                    ups(childResponse.ups.toString())
                    clickListener(
                        View.OnClickListener {
                            println("LX___ clicked....")
                        }
                    )
                }
            }

        }
    }
}