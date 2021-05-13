package mohalim.store.edokan.core.utils

import android.content.Context
import android.content.SharedPreferences

open class PreferencesUtils(context: Context?) : IPreferenceHelper{
    private val PREFS_NAME = "SharedPreference"
    private var preferences: SharedPreferences? = context?.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    override fun setCityId(cityId: Int) {
        preferences?.set(CITY_ID, cityId)
    }

    override fun getCityId(): Int? {
        return preferences?.get(CITY_ID)
    }

    override fun setCityName(cityName: String) {
        preferences?.set(CITY_NAME, cityName)
    }

    override fun getCityName(): String? {
        return preferences?.get(CITY_NAME) ?: ""
    }

    override fun setApiToken(apiKey: String) {
        preferences?.set(API_TOKEN, apiKey)
    }

    override fun getApiToken(): String? {
        return preferences?.get(API_TOKEN) ?: ""
    }

    override fun setFirebaseToken(firebaseToken: String) {
        preferences?.set(FIREBASE_TOKEN, firebaseToken)
    }

    override fun getFirebaseToken(): String? {
        return preferences?.get(FIREBASE_TOKEN) ?: ""
    }

    override fun setRefreshToken(token: String) {
        preferences?.set(REFRESH_TOKEN, token)
    }

    override fun getRefreshToken(): String? {
        return preferences?.get(REFRESH_TOKEN) ?: ""
    }

    override fun setUserId(id: String) {
        preferences?.set(USER_ID, id)
    }

    override fun getUserId(): String? {
        return preferences?.get(USER_ID) ?: ""
    }

    companion object {
        const val CITY_ID = "city_id"
        const val CITY_NAME = "city_name"
        const val API_TOKEN = "api_key"
        const val FIREBASE_TOKEN = "firebase_key"
        const val REFRESH_TOKEN = "refresh_token"
        const val USER_ID = "user_id"
    }

}

/**
 * SharedPreferences extension function, to listen the edit() and apply() fun calls
 * on every SharedPreferences operation.
 */
private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
    val editor = this.edit()
    operation(editor)
    editor.apply()
}
/**
 * puts a key value pair in shared prefs if doesn't exists, otherwise updates value on given [key]
 */
private operator fun SharedPreferences.set(key: String, value: Any?) {
    when (value) {
        is String? -> edit { it.putString(key, value) }
        is Int -> edit { it.putInt(key, value) }
        is Boolean -> edit { it.putBoolean(key, value) }
        is Float -> edit { it.putFloat(key, value) }
        is Long -> edit { it.putLong(key, value) }
        else -> throw UnsupportedOperationException("Not yet implemented")
    }
}
/**
 * finds value on given key.
 * [T] is the type of value
 * @param defaultValue optional default value - will take null for strings, false for bool and -1 for numeric values if [defaultValue] is not specified
 */
private inline operator fun <reified T : Any> SharedPreferences.get(
    key: String,
    defaultValue: T? = null
): T? {
    return when (T::class) {
        String::class -> getString(key, defaultValue as? String) as T?
        Int::class -> getInt(key, defaultValue as? Int ?: -1) as T?
        Boolean::class -> getBoolean(key, defaultValue as? Boolean ?: false) as T?
        Float::class -> getFloat(key, defaultValue as? Float ?: -1f) as T?
        Long::class -> getLong(key, defaultValue as? Long ?: -1) as T?
        else -> throw UnsupportedOperationException("Not yet implemented")
    }
}