package mohalim.store.edokan.core.utils

import android.content.Context
import android.content.SharedPreferences

open class PreferencesUtils constructor(context: Context) : IPreferenceHelper{
    private val PREFS_NAME = "SharedPreference"
    private var preferences: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    override fun setApiToken(apiKey: String) {
        preferences[API_TOKEN] = apiKey
    }

    override fun getApiToken(): String? {
        return preferences[API_TOKEN] ?: ""
    }

    override fun setFirebaseToken(firebaseToken: String) {
        preferences[FIREBASE_TOKEN] = firebaseToken
    }

    override fun getFirebaseToken(): String? {
        return preferences[FIREBASE_TOKEN] ?: ""
    }

    companion object {
        const val API_TOKEN = "api_key"
        const val FIREBASE_TOKEN = "api_key"
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