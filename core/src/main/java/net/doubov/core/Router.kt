package net.doubov.core

import androidx.fragment.app.Fragment

interface Router<F : Fragment> {
    val fragment: F
}