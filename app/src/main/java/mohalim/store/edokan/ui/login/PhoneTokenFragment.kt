package mohalim.store.edokan.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import kotlinx.coroutines.ExperimentalCoroutinesApi
import mohalim.store.edokan.R
import mohalim.store.edokan.databinding.FragmentPhoneTokenBinding

class PhoneTokenFragment : Fragment() {
    lateinit var binding : FragmentPhoneTokenBinding;

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_phone_token, container, false)

        binding.loginBtn.setOnClickListener(View.OnClickListener {
            if (!isValidToken())return@OnClickListener
            setLoadingVisibility(View.VISIBLE)
            val loginActivity: LoginActivity = activity as LoginActivity;
            loginActivity.verifyPhoneNumberWithCode(loginActivity.storedVerificationId, binding.tokenET.text.toString())
        })

        return binding.root
    }

    private fun isValidToken() : Boolean {
        if (binding.tokenET.text.length != 6){
            binding.tokenET.error = "رجاء ادخال الرقم التأكيدي بشكل صحيح"
            return false;
        }

        if (binding.tokenET.text.isEmpty()){
            binding.tokenET.error = "رجاء ادخال الرقم التأكيدي بشكل صحيح"
            return false;
        }

        return true;
    }

    fun setLoadingVisibility(visibility : Int){
        binding.loading.visibility = visibility

    }
}

