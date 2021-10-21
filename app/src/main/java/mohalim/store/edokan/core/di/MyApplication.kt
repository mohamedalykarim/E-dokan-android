package mohalim.store.edokan.core.di

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import mohalim.store.edokan.R
import mohalim.store.edokan.core.utils.NetworkMonitor
import mohalim.store.edokan.core.utils.TypefaceUtil

@HiltAndroidApp
class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        NetworkMonitor(this).startNetworkCallback()
        TypefaceUtil.font.setDefaultFont(applicationContext, "DEFAULT", "font/"+resources.getString(R.string.font))
        TypefaceUtil.font.setDefaultFont(applicationContext, "MONOSPACE", "font/"+resources.getString(R.string.font))
        TypefaceUtil.font.setDefaultFont(applicationContext, "SERIF", "font/"+resources.getString(R.string.font))
        TypefaceUtil.font.setDefaultFont(applicationContext, "SANS_SERIF", "font/"+resources.getString(R.string.font))
    }

    override fun onTerminate() {
        super.onTerminate()
        NetworkMonitor(this).stopNetworkCallback()
    }
}