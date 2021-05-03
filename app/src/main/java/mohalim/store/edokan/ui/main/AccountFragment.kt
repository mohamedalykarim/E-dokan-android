package mohalim.store.edokan.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import mohalim.store.edokan.R
import mohalim.store.edokan.core.utils.IPreferenceHelper
import mohalim.store.edokan.core.utils.PreferencesUtils
import mohalim.store.edokan.databinding.FragmentAccountBinding
import mohalim.store.edokan.ui.splash.SplashActivity

@AndroidEntryPoint
class AccountFragment : Fragment() {

    lateinit var firebaseAuth : FirebaseAuth;
    val preferenceHelper: IPreferenceHelper by lazy { PreferencesUtils(context) }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding : FragmentAccountBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_account, container, false)
        firebaseAuth = FirebaseAuth.getInstance();

        binding.exitContainer.setOnClickListener {
            preferenceHelper.setApiToken("")
            preferenceHelper.setFirebaseToken("")
            preferenceHelper.setRefreshToken("")
            firebaseAuth.signOut()
            activity?.startActivity(Intent(activity, SplashActivity::class.java))
            activity?.finish()
        }

        return binding.root
    }

}