package net.doubov.core.extensions

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

fun Fragment.onFragmentAttached(action: (Fragment) -> Unit) {
    childFragmentManager
        .registerFragmentLifecycleCallbacks(
            object : FragmentManager.FragmentLifecycleCallbacks() {
                override fun onFragmentAttached(fm: FragmentManager, f: Fragment, context: Context) {
                    action(f)
                }
            },
            true
        )
}

fun Fragment.onFragmentDetached(action: (Fragment) -> Unit) {
    childFragmentManager
        .registerFragmentLifecycleCallbacks(
            object : FragmentManager.FragmentLifecycleCallbacks() {
                override fun onFragmentDetached(fm: FragmentManager, f: Fragment) {
                    action(f)
                }
            },
            true
        )
}