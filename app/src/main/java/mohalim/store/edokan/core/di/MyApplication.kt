package mohalim.store.edokan.core.di

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import mohalim.store.edokan.core.utils.NetworkMonitor

@HiltAndroidApp
class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        NetworkMonitor(this).startNetworkCallback()
    }

    override fun onTerminate() {
        super.onTerminate()
        NetworkMonitor(this).stopNetworkCallback()
    }
}