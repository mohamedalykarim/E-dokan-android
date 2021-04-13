package mohalim.store.edokan.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.Observer
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import mohalim.store.edokan.R
import mohalim.store.edokan.core.utils.DataState
import mohalim.store.edokan.core.utils.ErrorHandler
import mohalim.store.edokan.core.utils.IPreferenceHelper
import mohalim.store.edokan.core.utils.PreferencesUtils
import mohalim.store.edokan.databinding.ActivityLoginBinding
import mohalim.store.edokan.ui.main.MainActivity
import retrofit2.HttpException
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private final val TAG : String = "LoginActivity"

    lateinit var binding : ActivityLoginBinding;
    lateinit var phoneNumberLoginFragment: PhoneNumberLoginFragment;
    lateinit var phoneTokenFragment: PhoneTokenFragment;
    lateinit var enterPasswordFragment: EnterPasswordFragment;

    lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    var storedVerificationId: String? = ""

    val preferenceHelper: IPreferenceHelper by lazy { PreferencesUtils(this) }



    val viewModel : LoginViewModel by viewModels()
    @Inject lateinit var auth: FirebaseAuth;

    val callbacks   = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks(){

        override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
            storedVerificationId = verificationId
            resendToken = token

            loadFragment(phoneTokenFragment)
        }

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
        }

        override fun onVerificationFailed(exception: FirebaseException) {

        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        phoneNumberLoginFragment = PhoneNumberLoginFragment()
        phoneTokenFragment = PhoneTokenFragment()
        enterPasswordFragment = EnterPasswordFragment()

        if(savedInstanceState == null){
            loadFragment(phoneNumberLoginFragment)
        }

        subscribeObservers()
    }

    fun loadFragment(fragment : Fragment){
        supportFragmentManager.commit {
            setCustomAnimations(
                R.anim.enter_from_left,
                R.anim.exit_to_right,
                R.anim.enter_from_right,
                R.anim.exit_to_left
            )

            setReorderingAllowed(true)
            replace(binding.fragmentContainerView.id, fragment)
        }
    }

    fun startLoginByPhone(phoneNumber : String){
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(callbacks)
            .build()

        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    fun resendVerificationCode(
        phoneNumber: String,
        token: PhoneAuthProvider.ForceResendingToken?
    ) {
        val optionsBuilder = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)       // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this)                 // Activity (for callback binding)
            .setCallbacks(callbacks)          // OnVerificationStateChangedCallbacks
        if (token != null) {
            optionsBuilder.setForceResendingToken(token) // callback's ForceResendingToken
        }
        PhoneAuthProvider.verifyPhoneNumber(optionsBuilder.build())
    }


    fun verifyPhoneNumberWithCode(verificationId: String?, code: String) {
        val credential = PhoneAuthProvider.getCredential(verificationId!!, code)
        signInWithPhoneAuthCredential(credential)
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = task.result?.user
                    phoneNumberLoginFragment.setLoadingVisibility(View.GONE)
                    loginToApi(user, "")

                } else {
                    // Sign in failed, display a message and update the UI
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                    }
                    // Update UI
                    phoneNumberLoginFragment.setLoadingVisibility(View.GONE)
                }
            }
    }

    private fun loginToApi(user: FirebaseUser?, passowrd : String) {
        user?.getIdToken(false)?.addOnSuccessListener {
            Log.d(TAG, "loginToApi: "+ it.token)
            preferenceHelper.setFirebaseToken(it.token.toString())
            viewModel.loginAfterPhone(user, passowrd)
        }
    }

    private fun subscribeObservers(){
        viewModel.userDataState.observe(this, Observer {
            when (it) {
                is DataState.Success -> {
                    phoneTokenFragment.setLoadingVisibility(View.GONE)

                    Log.d(TAG, "subscribeObservers: " + it.data.phoneNumber)

                    preferenceHelper.setApiToken(it.data.wtoken)
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    finish()
                }

                is DataState.Loading -> {
                    phoneTokenFragment.setLoadingVisibility(View.VISIBLE)
                }

                is DataState.Failure -> {
                    preferenceHelper.setApiToken("")
                    preferenceHelper.setFirebaseToken("")

                    when (it.exception) {
                        is HttpException -> {


                            val error = ErrorHandler.DO.getResponseMapFromHTTPException(httpException = it.exception)

                            Log.d(TAG, "subscribeObservers: "+ error.toString())

                                if (error[ErrorHandler.ERROR_ENTRIES.ERROR_NUMBER]?.equals(ErrorHandler.ERRORS.AUTH_FAILED) == true) {
                                    Toast.makeText(this, error[ErrorHandler.ERROR_ENTRIES.ERROR_MESSAGE].toString(), Toast.LENGTH_LONG).show();
                                }else if (error[ErrorHandler.ERROR_ENTRIES.ERROR_NUMBER]?.equals(ErrorHandler.ERRORS.AUTH_FAILED_EMPTY_PASSWORD) == true) {
                                    loadFragment(enterPasswordFragment)
                                }

                            }
                    }

                    phoneTokenFragment.setLoadingVisibility(View.GONE)
                }
            }
        })
    }





}