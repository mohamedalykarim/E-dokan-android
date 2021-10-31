package mohalim.store.edokan.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import mohalim.store.edokan.R
import mohalim.store.edokan.core.utils.DataState
import mohalim.store.edokan.core.utils.IPreferenceHelper
import mohalim.store.edokan.core.utils.PreferencesUtils
import mohalim.store.edokan.databinding.FragmentAccountBinding
import mohalim.store.edokan.ui.address.AddressActivity
import mohalim.store.edokan.ui.orders.OrdersActivity
import mohalim.store.edokan.ui.splash.SplashActivity

@AndroidEntryPoint
class AccountFragment : Fragment() {

    private lateinit var binding: FragmentAccountBinding
    lateinit var firebaseAuth : FirebaseAuth;
    val preferenceHelper: IPreferenceHelper by lazy { PreferencesUtils(context) }

    lateinit var mainActivity : MainActivity


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_account, container, false)
        firebaseAuth = FirebaseAuth.getInstance();

        mainActivity = activity as MainActivity

        clicks()
        subscribeObservers()

        return binding.root
    }

    private fun subscribeObservers() {
        mainActivity.viewModel.updateUserDataObserver.observe(mainActivity, Observer {
            when(it){
                is DataState.Loading -> {}
                is DataState.Success -> {
                    if (preferenceHelper.getIsSeller() == true){
                        binding.traderContainer.visibility = View.VISIBLE
                        binding.traderDevider.visibility = View.VISIBLE
                    }else{
                        binding.traderContainer.visibility = View.GONE
                        binding.traderDevider.visibility = View.GONE
                    }
                }
                is DataState.Failure -> {}
            }
        })
    }

    private fun clicks() {
        binding.myOrderContainer.setOnClickListener {
            val intent = Intent(activity, OrdersActivity::class.java)
            startActivity(intent)
        }

        binding.myAddressContainer.setOnClickListener {
            val intent = Intent(activity, AddressActivity::class.java)
            startActivity(intent)
        }

        binding.supportContainer.setOnClickListener {
            mainActivity.techSupportDialog()
        }

        binding.cityContainer.setOnClickListener {
            mainActivity.loadCity()
        }

        binding.exitContainer.setOnClickListener {
            preferenceHelper.setApiToken("")
            preferenceHelper.setFirebaseToken("")
            preferenceHelper.setRefreshToken("")
            firebaseAuth.signOut()
            activity?.startActivity(Intent(activity, SplashActivity::class.java))
            activity?.finish()
        }
    }

    override fun onResume() {
        super.onResume()

        firebaseAuth.currentUser?.getIdToken(false)?.addOnSuccessListener {
            mainActivity.viewModel.updateSharedPrefUserData(it.token.toString())
        }
    }
}