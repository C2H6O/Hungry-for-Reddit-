package net.doubov.hungryforreddit

import androidx.fragment.app.Fragment

open class Router<C : DependenciesComponent, F : Fragment>(
    val component: C,
    val fragment: F
) {

    private val childRouters: MutableList<Router<*, *>> = mutableListOf()

    fun addChild(router: Router<*, *>) {
        childRouters.add(router)
    }

}

interface DependenciesComponent