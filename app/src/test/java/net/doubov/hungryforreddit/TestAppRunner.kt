package net.doubov.hungryforreddit

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner

class TestAppRunner : AndroidJUnitRunner() {
    override fun newApplication(cl: ClassLoader?, className: String?, context: Context?): Application {
        println("LX___ this is run!??!")
        return super.newApplication(cl, TestApp::class.java.name, context)
    }
}