package net.doubov.hungryforreddit.activity

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import net.doubov.core.di.ActivityScope
import net.doubov.hungryforreddit.di.main.parent.ParentBuilder
import net.doubov.main.ParentFragment
import javax.inject.Inject

@ActivityScope
class SingleActivityFragmentFactory @Inject constructor(
    private val parentBuilder: ParentBuilder
) : FragmentFactory() {
    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return when (classLoader.loadClass(className)) {
            ParentFragment::class.java -> parentBuilder.build().fragment
            else -> super.instantiate(classLoader, className)
        }
    }
}