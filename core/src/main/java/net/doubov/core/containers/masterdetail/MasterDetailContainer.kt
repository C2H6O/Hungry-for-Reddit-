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
package net.doubov.core.containers.masterdetail

import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import com.squareup.workflow.ui.ContainerHints
import com.squareup.workflow.ui.LayoutRunner
import com.squareup.workflow.ui.ViewBinding
import com.squareup.workflow.ui.WorkflowViewStub
import com.squareup.workflow.ui.backstack.BackStackScreen
import net.doubov.core.R
import net.doubov.core.containers.masterdetail.MasterDetailConfig.*

/**
 * Displays [MasterDetailScreen] renderings in either split pane or single pane
 * treatment, depending on the setup of the given [View]. The view must provide
 * either a single [WorkflowViewStub] with id [R.id.master_detail_single_stub],
 * or else two with ids [R.id.master_stub] and [R.id.detail_stub].
 *
 * For single pane layouts, [MasterDetailScreen] is repackaged as a [BackStackScreen]
 * with [MasterDetailScreen.masterRendering] as the base of the stack.
 */
class MasterDetailContainer(view: View) : LayoutRunner<MasterDetailScreen> {

    private val masterStub: WorkflowViewStub? = view.findViewById(R.id.master_stub)
    private val detailStub: WorkflowViewStub? = view.findViewById(R.id.detail_stub)
    private val singleStub: WorkflowViewStub? = view.findViewById(R.id.master_detail_single_stub)

    init {
        check((singleStub == null) xor (masterStub == null && detailStub == null)) {
            "Layout must define only R.id.master_detail_single_stub, " +
                    "or else both R.id.master_stub and R.id.detail_stub"
        }
    }

    override fun showRendering(
        rendering: MasterDetailScreen,
        containerHints: ContainerHints
    ) {
        if (singleStub == null) renderSplitView(rendering, containerHints)
        else renderSingleView(rendering, containerHints, singleStub)
    }

    private fun renderSplitView(
        rendering: MasterDetailScreen,
        containerHints: ContainerHints
    ) {
        if (rendering.detailRendering == null && rendering.selectDefault != null) {
            rendering.selectDefault.invoke()
        } else {
            masterStub!!.update(
                rendering.masterRendering,
                containerHints + (MasterDetailConfig to Master)
            )
            rendering.detailRendering
                ?.let { detail ->
                    detailStub!!.actual.visibility = VISIBLE
                    detailStub.update(
                        detail,
                        containerHints + (MasterDetailConfig to Detail)
                    )
                }
                ?: run {
                    detailStub!!.actual.visibility = INVISIBLE
                }
        }
    }

    private fun renderSingleView(
        rendering: MasterDetailScreen,
        containerHints: ContainerHints,
        stub: WorkflowViewStub
    ) {
        val combined: BackStackScreen<*> = rendering.detailRendering
            ?.let { rendering.masterRendering + it }
            ?: rendering.masterRendering

        stub.update(combined, containerHints + (MasterDetailConfig to Single))
    }

    companion object : ViewBinding<MasterDetailScreen> by LayoutRunner.Binding(
        type = MasterDetailScreen::class,
        layoutId = R.layout.master_detail,
        runnerConstructor = ::MasterDetailContainer
    )
}
