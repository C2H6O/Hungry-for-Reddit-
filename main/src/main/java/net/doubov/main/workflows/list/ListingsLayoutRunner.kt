package net.doubov.main.workflows.list

import android.view.View
import com.airbnb.epoxy.EpoxyRecyclerView
import com.squareup.workflow.ui.LayoutRunner
import com.squareup.workflow.ui.LayoutRunner.Companion.bind
import com.squareup.workflow.ui.ViewEnvironment
import com.squareup.workflow.ui.ViewFactory
import net.doubov.main.R
import net.doubov.main.views.headerView

class ListingsLayoutRunner(view: View) : LayoutRunner<ListingsWorkflow.Rendering> {

    companion object : ViewFactory<ListingsWorkflow.Rendering> by bind(
        R.layout.fragment_main_list,
        ::ListingsLayoutRunner
    )

    private val list = view.findViewById<EpoxyRecyclerView>(R.id.recyclerView)

    override fun showRendering(rendering: ListingsWorkflow.Rendering, viewEnvironment: ViewEnvironment) {
        list.withModels {
            rendering.newsResponse.forEachIndexed { index, childResponse ->
                headerView {
                    id(childResponse.id)
                    title(childResponse.title)
                    ups(childResponse.ups.toString())
                    clickListener(
                        View.OnClickListener {
                            rendering.onListingSelected.invoke(index)
                        }
                    )
                }
            }

        }
    }
}