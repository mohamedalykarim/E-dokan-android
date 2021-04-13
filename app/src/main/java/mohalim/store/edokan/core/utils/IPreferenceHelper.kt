package mohalim.store.edokan.core.utils

interface IPreferenceHelper {
    fun setApiToken(apiKey: String)
    fun getApiToken(): String?

    fun setFirebaseToken(firebaseToken : String)
    fun getFirebaseToken() : String?

}