package net.doubov.hungryforreddit.di.main.parent

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import net.doubov.hungryforreddit.di.main.detail.DetailBuilder
import net.doubov.hungryforreddit.di.main.list.ListBuilder
import net.doubov.main.DetailFragment
import net.doubov.main.ListFragment
import javax.inject.Inject

@MainParentScope
class ParentFragmentFactory @Inject constructor(
    private val listBuilder: ListBuilder,
    private val detailBuilder: DetailBuilder
) : FragmentFactory() {
    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return when (classLoader.loadClass(className)) {
            ListFragment::class.java -> listBuilder.build().fragment
            DetailFragment::class.java -> detailBuilder.build().fragment
            else -> super.instantiate(classLoader, className)
        }
    }
}