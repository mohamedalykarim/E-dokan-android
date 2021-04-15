package mohalim.store.edokan.core.utils

import android.util.Log
import kotlin.properties.Delegates

object NetworkVariables {
    var isNetworkAvailabeListListeners = ArrayList<NetworkAvailabilityInterface>()


    var isNetworkConnected: Boolean by Delegates.observable(false) { property, oldValue, newValue ->
        isNetworkAvailabeListListeners.forEach{
            it.isInternetAvailable(newValue)
            Log.d("TAG", "add: "+newValue)
        }
    }

}

interface NetworkAvailabilityInterface {
    fun isInternetAvailable(boolean: Boolean)
}

