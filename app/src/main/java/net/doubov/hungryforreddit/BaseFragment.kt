package net.doubov.hungryforreddit

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import java.util.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

open class BaseFragment(
    @LayoutRes layoutId: Int
) : Fragment(layoutId), CoroutineScope {

    @Inject
    lateinit var fragmentFactory: Optional<FragmentFactory>

    override fun onAttach(context: Context) {
        if (fragmentFactory.isPresent) {
            childFragmentManager.fragmentFactory = fragmentFactory.get()
        }

        super.onAttach(context)
    }

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