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

    override fun onAttach(context: Context) {
        component = componentFactory.create()
        childFragmentManager.fragmentFactory = object : FragmentFactory() {
            override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
                return when (classLoader.loadClass(className)) {
                    MainListFragment::class.java -> component.listFragmentFactory().create(this@MainParentFragment)
                    else -> super.instantiate(classLoader, className)
                }
            }
        }
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
                .add(R.id.fragmentContainer, component.listFragmentFactory().create(this))
                .commit()
        }
    }

    override fun onEventReceived(event: MainListFragment.Event) {
        println("onEventReceived: $event")
    }

}