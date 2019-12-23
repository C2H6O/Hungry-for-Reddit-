package net.doubov.core.extensions

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

fun AppCompatActivity.onFragmentPreAttached(action: (Fragment) -> Unit) {
    supportFragmentManager
        .registerFragmentLifecycleCallbacks(
            object : FragmentManager.FragmentLifecycleCallbacks() {
                override fun onFragmentPreAttached(fm: FragmentManager, f: Fragment, context: Context) {
                    action(f)
                }
            },
            true
        )
}