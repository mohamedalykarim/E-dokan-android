package mohalim.store.edokan.core.data_source.network

import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.JsonObject
import mohalim.store.edokan.core.model.user.User
import mohalim.store.edokan.core.model.user.UserNetwork
import mohalim.store.edokan.core.utils.Constants.constants.BASE_URL
import mohalim.store.edokan.core.utils.IPreferenceHelper
import mohalim.store.edokan.core.utils.PreferencesUtils
import mohalim.store.edokan.ui.splash.SplashActivity
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.http.Headers
import java.net.HttpURLConnection
import kotlin.math.log

class SignedRequestInterceptor(val context: Context) : Interceptor {

    private val preferenceHelper: IPreferenceHelper by lazy { PreferencesUtils(context) }
    private val firebaseAuth : FirebaseAuth = FirebaseAuth.getInstance()

    override fun intercept(chain: Interceptor.Chain): Response {
        val request : Request = chain.request()
        val response : Response = chain.proceed(request)


        if (response.code() == 404){
            response.close()

            val refreshTokenRequest = request.newBuilder()
                                            .addHeader("Cookie", "refresh_token="+preferenceHelper.getRefreshToken())
                                            .url(BASE_URL+"/api/users/refresh-token")
                                            .build()
            val refreshTokenResponse = chain.proceed(refreshTokenRequest)

            if (refreshTokenResponse.code() == 200){

                val cookielist = refreshTokenResponse.headers().values("Set-Cookie")
                val refreshToken : String = cookielist[0].split(";")[0].substring(14)

                val userObject : JSONObject = JSONObject(refreshTokenResponse.body()?.string())

                preferenceHelper.setRefreshToken(refreshToken)
                preferenceHelper.setApiToken("" + userObject.get("wtoken"))
            }else{
                preferenceHelper.setRefreshToken("")
                preferenceHelper.setApiToken("")
                firebaseAuth.signOut()
                val intent = Intent(context, SplashActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                context.startActivity(intent)

            }
            refreshTokenResponse.close()

            val newRequest = request.newBuilder()
                    .removeHeader("Authorization")
                    .addHeader("Authorization",
                            "Bearer "+
                                    preferenceHelper.getFirebaseToken()+
                                    "///"+
                                    preferenceHelper.getApiToken()
                    ).build()

            return chain.proceed(newRequest)
        }else{
            return response
        }
    }
}