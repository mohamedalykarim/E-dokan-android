package mohalim.store.edokan.core.data_source.network

import android.content.Context
import android.content.Intent
import android.os.SystemClock
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay
import mohalim.store.edokan.core.utils.Constants.constants.BASE_URL
import mohalim.store.edokan.core.utils.IPreferenceHelper
import mohalim.store.edokan.core.utils.PreferencesUtils
import mohalim.store.edokan.ui.splash.SplashActivity
import okhttp3.*
import okio.BufferedSink
import okio.GzipSink
import okio.Okio
import org.json.JSONObject
import java.io.IOException
import java.lang.Exception


class SignedRequestInterceptor(val context: Context) : Interceptor {

    private val preferenceHelper: IPreferenceHelper by lazy { PreferencesUtils(context) }
    private val firebaseAuth : FirebaseAuth = FirebaseAuth.getInstance()

    override fun intercept(chain: Interceptor.Chain): Response  {
        val request : Request = chain.request()
        Log.d("TAG", "intercept: request url: "+request.url())
        Log.d("TAG", "intercept: request body: "+request.body()?.toString())
        Log.d("TAG", "intercept: request headers: "+request.headers())
        Log.d("TAG", "intercept: request method: "+request.method())
        val response : Response = chain.proceed(request)

        try {
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
                    val body : JSONObject = JSONObject(refreshTokenResponse.body()?.string())
                    preferenceHelper.setRefreshToken(refreshToken)
                    preferenceHelper.setApiToken("" + body.get("wtoken"))

                    refreshTokenResponse.close()

                    firebaseAuth.currentUser.getIdToken(false).addOnSuccessListener {
                        it.token?.let { it1 -> preferenceHelper.setFirebaseToken(it1) }
                    }

                    SystemClock.sleep(500);
                    val newRequest = request.newBuilder()
                        .url(request.url())
                        .method(request.method(), request.body())
                        .removeHeader("authorization")
                        .addHeader("authorization",
                            "Bearer "+
                                    preferenceHelper.getFirebaseToken()+
                                    "///"+
                                    preferenceHelper.getApiToken()
                        )
                        .build()

                    return chain.proceed(newRequest)

                }else{
                    preferenceHelper.setRefreshToken("")
                    preferenceHelper.setApiToken("")
                    firebaseAuth.signOut()
                    val intent = Intent(context, SplashActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                    context.startActivity(intent)
                    return response
                }

            }else{
                return response
            }
        }catch (e : Exception){
            Log.d("TAG", "intercept: exception "+ e.message)
            return response
        }
    }

    private fun gzip(body: RequestBody): RequestBody? {
        return object : RequestBody() {
            override fun contentType(): MediaType? {
                return body.contentType()
            }

            override fun contentLength(): Long {
                return -1 // We don't know the compressed length in advance!
            }

            @Throws(IOException::class)
            override fun writeTo(sink: BufferedSink) {
                val gzipSink = Okio.buffer(GzipSink(sink))
                body.writeTo(gzipSink)
                gzipSink.close()
            }
        }
    }
}