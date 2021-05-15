package mohalim.store.edokan.core.di

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import mohalim.store.edokan.core.utils.NetworkMonitor
import mohalim.store.edokan.core.utils.TypefaceUtil

@HiltAndroidApp
class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        NetworkMonitor(this).startNetworkCallback()
        TypefaceUtil.font.setDefaultFont(applicationContext, "DEFAULT", "font/ge_ss_two_light.otf")
        TypefaceUtil.font.setDefaultFont(applicationContext, "MONOSPACE", "font/ge_ss_two_light.otf")
        TypefaceUtil.font.setDefaultFont(applicationContext, "SERIF", "font/ge_ss_two_light.otf")
        TypefaceUtil.font.setDefaultFont(applicationContext, "SANS_SERIF", "font/ge_ss_two_light.otf")
    }

    override fun onTerminate() {
        super.onTerminate()
        NetworkMonitor(this).stopNetworkCallback()
    }
}