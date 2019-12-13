package net.doubov.main

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import javax.inject.Inject

class MainParentFragment @Inject constructor(
    private val componentFactory: MainParentFragmentComponent.Factory
) : Fragment(), MainListFragment.Listener {

    private lateinit var component: MainParentFragmentComponent
    private lateinit var fragmentFactory: MainParentFragmentFactory

    override fun onAttach(context: Context) {
        component = componentFactory.create()
        childFragmentManager.fragmentFactory = fragmentFactory
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return layoutInflater.inflate(R.layout.fragment_main_parent, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState == null) {
            childFragmentManager
                .beginTransaction()
                .add(R.id.fragmentContainer, fragmentFactory.createListFragmentInstance())
                .commit()
        }
    }

    override fun onEventReceived(event: MainListFragment.Event) {
        println("onEventReceived: $event")
    }
}

class MainParentFragmentFactory(
    private val fragment: MainParentFragment,
    private val fragmentComponent: MainParentFragmentComponent
) : FragmentFactory() {
    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return when (classLoader.loadClass(className)) {
            MainListFragment::class.java -> createListFragmentInstance()
            else -> super.instantiate(classLoader, className)
        }
    }

    fun createListFragmentInstance(): MainListFragment {
        return fragmentComponent.listFragmentFactory().create(fragment) as MainListFragment
    }
}