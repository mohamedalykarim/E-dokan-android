package mohalim.store.edokan.ui.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import mohalim.store.edokan.R
import mohalim.store.edokan.core.di.base.BaseActivity
import mohalim.store.edokan.core.utils.BindingUtils
import mohalim.store.edokan.core.utils.IPreferenceHelper
import mohalim.store.edokan.core.utils.PreferencesUtils
import mohalim.store.edokan.ui.login.LoginActivity
import mohalim.store.edokan.ui.main.MainActivity
import javax.inject.Inject

@AndroidEntryPoint
class SplashActivity : BaseActivity() {
    @Inject lateinit var auth: FirebaseAuth;
    val preferenceHelper: IPreferenceHelper by lazy { PreferencesUtils(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        val TAG : String = "SplashActivity"
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)




        val timer = object: CountDownTimer(3000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                Log.d(TAG, "onTick: ")
            }

            override fun onFinish() {
                val intent : Intent = if (!preferenceHelper.getApiToken().equals("")  && auth.currentUser != null){
                    Intent(this@SplashActivity, MainActivity::class.java)
                }else{
                    Intent(this@SplashActivity, LoginActivity::class.java)
                }
                startActivity(intent)
                finish()
            }
        }
        timer.start()

    }
}