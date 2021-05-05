package mohalim.store.edokan.core.utils

interface IPreferenceHelper {
    fun setApiToken(apiKey: String)
    fun getApiToken(): String?

    fun setFirebaseToken(firebaseToken : String)
    fun getFirebaseToken() : String?

    fun setRefreshToken(firebaseToken : String)
    fun getRefreshToken() : String?


    fun setUserId(id : String)
    fun getUserId() : String?


}