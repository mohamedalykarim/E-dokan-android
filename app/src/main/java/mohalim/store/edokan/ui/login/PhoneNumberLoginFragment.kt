package mohalim.store.edokan.ui.login

import android.content.Intent
import android.opengl.Visibility
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import mohalim.store.edokan.R
import mohalim.store.edokan.databinding.FragmentPhoneNumberLoginBinding

@AndroidEntryPoint
class PhoneNumberLoginFragment : Fragment() {

    lateinit var binding : FragmentPhoneNumberLoginBinding;

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_phone_number_login, container, false);
        binding.loginBtn.setOnClickListener(View.OnClickListener {
            if (!isValidPhoneNumber())return@OnClickListener
            setLoadingVisibility(View.VISIBLE)

            val loginActivity: LoginActivity = activity as LoginActivity;
            loginActivity.startLoginByPhone("+2"+binding.phoneNumberET.text.toString())
        })
        return binding.root
    }

    private fun isValidPhoneNumber() : Boolean {
        if (binding.phoneNumberET.text.length != 11){
            binding.phoneNumberET.error = "رجاء ادخال رقم صحيح"
            return false;
        }

        if (binding.phoneNumberET.text.isEmpty()){
            binding.phoneNumberET.error = "رجاء ادخال رقم تليفون صحيح"
            return false;
        }

        if (binding.phoneNumberET.text.substring(0,2) != "01"){
            binding.phoneNumberET.error = "رجاء ادخال رقم تليفون صحيح"
            return false;
        }

        return true;
    }

    fun setLoadingVisibility(visibility : Int){
        binding.loading.visibility = visibility
    }


}