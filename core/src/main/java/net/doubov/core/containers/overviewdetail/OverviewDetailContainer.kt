package net.doubov.core.containers.overviewdetail

/*
 * Copyright 2019 Square Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import com.squareup.workflow.ui.LayoutRunner
import com.squareup.workflow.ui.ViewEnvironment
import com.squareup.workflow.ui.ViewFactory
import com.squareup.workflow.ui.WorkflowViewStub
import com.squareup.workflow.ui.backstack.BackStackScreen
import net.doubov.core.R

/**
 * Displays [OverviewDetailScreen] renderings in either split pane or single pane
 * treatment, depending on the setup of the given [View]. The view must provide
 * either a single [WorkflowViewStub] with id [R.id.overview_detail_single_stub],
 * or else two with ids [R.id.overview_stub] and [R.id.detail_stub].
 *
 * For single pane layouts, [OverviewDetailScreen] is repackaged as a [BackStackScreen]
 * with [OverviewDetailScreen.overviewRendering] as the base of the stack.
 */
class OverviewDetailContainer(view: View) : LayoutRunner<OverviewDetailScreen> {

    private val overviewStub: WorkflowViewStub? = view.findViewById(R.id.master_stub)
    private val detailStub: WorkflowViewStub? = view.findViewById(R.id.detail_stub)
    private val singleStub: WorkflowViewStub? = view.findViewById(R.id.master_detail_single_stub)

    init {
        check((singleStub == null) xor (overviewStub == null && detailStub == null)) {
            "Layout must define only R.id.overview_detail_single_stub, " +
                    "or else both R.id.overview_stub and R.id.detail_stub"
        }
    }

    override fun showRendering(
        rendering: OverviewDetailScreen,
        viewEnvironment: ViewEnvironment
    ) {
        if (singleStub == null) renderSplitView(rendering, viewEnvironment)
        else renderSingleView(rendering, viewEnvironment, singleStub)
    }

    private fun renderSplitView(
        rendering: OverviewDetailScreen,
        viewEnvironment: ViewEnvironment
    ) {
        if (rendering.detailRendering == null && rendering.selectDefault != null) {
            rendering.selectDefault.invoke()
        } else {
            overviewStub!!.update(
                rendering.overviewRendering,
                viewEnvironment + (OverviewDetailConfig to OverviewDetailConfig.Overview)
            )
            rendering.detailRendering
                ?.let { detail ->
                    detailStub!!.actual.visibility = VISIBLE
                    detailStub.update(
                        detail,
                        viewEnvironment + (OverviewDetailConfig to OverviewDetailConfig.Detail)
                    )
                }
                ?: run {
                    detailStub!!.actual.visibility = INVISIBLE
                }
        }
    }

    private fun renderSingleView(
        rendering: OverviewDetailScreen,
        viewEnvironment: ViewEnvironment,
        stub: WorkflowViewStub
    ) {
        val combined: BackStackScreen<*> = rendering.detailRendering
            ?.let { rendering.overviewRendering + it }
            ?: rendering.overviewRendering

        stub.update(combined, viewEnvironment + (OverviewDetailConfig to OverviewDetailConfig.Single))
    }

    companion object : ViewFactory<OverviewDetailScreen> by LayoutRunner.Binding(
        type = OverviewDetailScreen::class,
        layoutId = R.layout.master_detail,
        runnerConstructor = ::OverviewDetailContainer
    )
}