package mohalim.store.edokan.core.utils

interface IPreferenceHelper {
    fun setCityId(cityId : Int)
    fun getCityId() : Int?

    fun setCityName(cityName: String)
    fun getCityName(): String?


    fun setApiToken(apiKey: String)
    fun getApiToken(): String?

    fun setFirebaseToken(firebaseToken : String)
    fun getFirebaseToken() : String?

    fun setRefreshToken(firebaseToken : String)
    fun getRefreshToken() : String?


    fun setUserId(id : String)
    fun getUserId() : String?

    fun setCartCityId(cityId : Int)
    fun getCartCityId() : Int?

}