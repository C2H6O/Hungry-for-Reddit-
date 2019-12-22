package net.doubov.core.ui

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

open class BaseFragment(
    @LayoutRes layoutId: Int
) : Fragment(layoutId), CoroutineScope {

    private var viewJob: Job = Job()

    override val coroutineContext: CoroutineContext
        get() = viewJob + Dispatchers.Main

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewJob = Job()
    }

    override fun onDestroyView() {
        viewJob.cancel()
        super.onDestroyView()
    }
}