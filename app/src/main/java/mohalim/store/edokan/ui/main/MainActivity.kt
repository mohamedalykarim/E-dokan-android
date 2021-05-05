package mohalim.store.edokan.ui.main

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.Observer
import dagger.hilt.android.AndroidEntryPoint
import mohalim.store.edokan.R
import mohalim.store.edokan.core.di.base.BaseActivity
import mohalim.store.edokan.core.utils.DataState
import mohalim.store.edokan.core.utils.NetworkAvailabilityInterface
import mohalim.store.edokan.core.utils.NetworkVariables
import mohalim.store.edokan.databinding.ActivityMainBinding
import retrofit2.HttpException


@AndroidEntryPoint
class MainActivity : BaseActivity() {
    val TAG : String = "MainActivity";

    lateinit var binding : ActivityMainBinding;
    val viewmodel : HomeViewModel by viewModels()
    lateinit var homeFragment : HomeFragment;
    lateinit var cartFragment: CartFragment;
    lateinit var accountFragment: AccountFragment;
    lateinit var techSupportDialog: TechSupportDialog;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        checkInternetAvailability()

        homeFragment = HomeFragment()
        cartFragment = CartFragment()
        accountFragment = AccountFragment()


        subscribeObservers();
        handleBottomClicks();
        loadFragment(homeFragment)
    }

    override fun onBackPressed() {
        if(viewmodel.CURRENT_FRAGMENT == HomeFragment::class.java.name){
            Log.d(TAG, "onBackPressed: "+HomeFragment::class.java.name)
            finish()
        }else if(viewmodel.CURRENT_FRAGMENT == CartFragment::class.java.name){
            Log.d(TAG, "onBackPressed: "+CartFragment::class.java.name)
            loadFragment(homeFragment)
            homeBottom()
        }else if(viewmodel.CURRENT_FRAGMENT == AccountFragment::class.java.name){
            Log.d(TAG, "onBackPressed: "+AccountFragment::class.java.name)
            loadFragment(homeFragment)
            homeBottom()
        }
    }

    fun loadFragment(fragment : Fragment){
        viewmodel.CURRENT_FRAGMENT = fragment.javaClass.name

        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace(binding.fragmentContainerView.id, fragment)
            addToBackStack(fragment.toString())
        }
    }

    private fun checkInternetAvailability() {
        //first check
        if (NetworkVariables.isNetworkConnected){
            binding.internetAvailabilityContainer.visibility = View.GONE
        }else{
            binding.internetAvailabilityContainer.visibility = View.VISIBLE
        }

        // listener
        NetworkVariables.isNetworkAvailabeListListeners.add(object : NetworkAvailabilityInterface {
            override fun isInternetAvailable(boolean: Boolean) {

                if (boolean) {

                    runOnUiThread {
                        if (viewmodel.CURRENT_FRAGMENT == HomeFragment::class.java.name) viewmodel.fetchHomeFragmentData()
                        binding.internetAvailabilityContainer.visibility = View.GONE
                    }

                } else {

                    runOnUiThread {
                        binding.internetAvailabilityContainer.visibility = View.VISIBLE
                    }


                }
            }

        })
    }

    private fun subscribeObservers() {
        viewmodel.noParentCategories.observe(this, Observer {
            when (it) {
                is DataState.Loading -> {

                }

                is DataState.Success -> {
                    homeFragment.updateCategoryData(it.data)
                }

                is DataState.Failure -> {
                }
            }
        })

        viewmodel.offersComing.observe(this, Observer {
            when (it) {
                is DataState.Loading -> {

                }

                is DataState.Success -> {
                    homeFragment.updateOffersData(it.data)
                }

                is DataState.Failure -> {
                    when (it) {
                        is HttpException -> {

                        }
                    }
                }
            }
        })

        viewmodel.chosenProducts.observe(this, Observer {
            when (it) {
                is DataState.Loading -> {

                }

                is DataState.Success -> {
                    viewmodel.PAGE++
                    homeFragment.updateChosenProductsData(it.data)
                }

                is DataState.Failure -> {
                    when (it) {
                        is HttpException -> {
                        }
                    }
                }
            }

        })

        viewmodel.supportItemObserver.observe(this, Observer {
            when (it) {
                is DataState.Loading -> {

                }

                is DataState.Success -> {
                    techSupportDialog.updateSupportItemsData(it.data)
                    Log.d(TAG, "subscribeObservers: "+ it.data)

                }

                is DataState.Failure -> {
                    Log.d(TAG, "subscribeObservers: "+ it.exception.message)
                }
            }
        })
    }

    private fun handleBottomClicks() {

        binding.homeIcon.setOnClickListener(View.OnClickListener {
            homeBottom();
        })

        binding.cartIcon.setOnClickListener(View.OnClickListener {
            cartBottom()
        })

        binding.accountContainer.setOnClickListener(View.OnClickListener {
            accountBottom()
        })

        binding.bottomHeaderView.setOnClickListener(View.OnClickListener {
            if (viewmodel.bottomVisibility == viewmodel.BOTTOM_VISIBLE) {
                val value: Float = binding.bottomContainer.height.toFloat()
                binding.bottom.animate().translationY(value).setListener(null)
                viewmodel.bottomVisibility = viewmodel.BOTTOM_HIDE
                binding.arrowIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_up_arrow))

            } else if (viewmodel.bottomVisibility == viewmodel.BOTTOM_HIDE) {
                binding.bottom.animate().translationY(0f).setListener(null)
                viewmodel.bottomVisibility = viewmodel.BOTTOM_VISIBLE
                binding.arrowIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_down_arrow))

            }
        })
    }

    private fun accountBottom() {
        val params1 : LinearLayout.LayoutParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT);
        val params2 : LinearLayout.LayoutParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT);

        if (viewmodel.currentTab == viewmodel.ACCOUNT) return
        viewmodel.currentTab = viewmodel.ACCOUNT

        binding.accountContainerBG.setBackgroundResource(R.drawable.bottom_bar_item_bg)
        binding.homeContainerBG.setBackgroundColor(ContextCompat.getColor(this@MainActivity, R.color.transparent))
        binding.cartContainerBG.setBackgroundColor(ContextCompat.getColor(this@MainActivity, R.color.transparent))

        binding.accountContainerBG.translationY = (binding.bottomContainer.height - binding.accountContainerBG.height).toFloat()
        binding.accountContainerBG.animate().setDuration(200).translationY(0f).setListener(null)


        params1.weight = 2f
        binding.accountContainer.layoutParams = params1;

        params2.weight = 3f
        binding.cartContainer.layoutParams = params2;
        binding.homeContainer.layoutParams = params2;

        binding.homeIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_main_not_active))
        binding.cartIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_cart_not_active))
        binding.accountIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_account_active))

        binding.homeIcontTv.visibility = View.GONE
        binding.cartIconTv.visibility = View.GONE
        binding.accountIconTv.visibility = View.VISIBLE
        loadFragment(accountFragment)

    }

    private fun cartBottom() {
        val params1 : LinearLayout.LayoutParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT);
        val params2 : LinearLayout.LayoutParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT);

        if (viewmodel.currentTab == viewmodel.CART) return
        viewmodel.currentTab = viewmodel.CART

        binding.cartContainerBG.setBackgroundResource(R.drawable.bottom_bar_item_bg)
        binding.homeContainerBG.setBackgroundColor(ContextCompat.getColor(this, R.color.transparent))
        binding.accountContainerBG.setBackgroundColor(ContextCompat.getColor(this, R.color.transparent))

        binding.cartContainerBG.translationY = (binding.bottomContainer.height - binding.cartContainerBG.height).toFloat()
        binding.cartContainerBG.animate().setDuration(200).translationY(0f).setListener(null)



        params1.weight = 2f
        binding.cartContainer.layoutParams = params1;

        params2.weight = 3f
        binding.homeContainer.layoutParams = params2;
        binding.accountContainer.layoutParams = params2;

        binding.homeIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_main_not_active))
        binding.cartIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_cart_active))
        binding.accountIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_account_not_active))

        binding.homeIcontTv.visibility = View.GONE
        binding.cartIconTv.visibility = View.VISIBLE
        binding.accountIconTv.visibility = View.GONE
        loadFragment(cartFragment)

    }

    private fun homeBottom() {
        val params1 : LinearLayout.LayoutParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT);
        val params2 : LinearLayout.LayoutParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT);

        if (viewmodel.currentTab == viewmodel.HOME) return
        viewmodel.currentTab = viewmodel.HOME

        binding.homeContainerBG.setBackgroundResource(R.drawable.bottom_bar_item_bg)
        binding.cartContainerBG.setBackgroundColor(ContextCompat.getColor(this, R.color.transparent))
        binding.accountContainerBG.setBackgroundColor(ContextCompat.getColor(this, R.color.transparent))

        binding.homeContainerBG.translationY = (binding.bottomContainer.height- binding.homeContainerBG.height).toFloat()
        binding.homeContainerBG.animate().setDuration(200).translationY(0f).setListener(null)


        params1.weight = 2f
        binding.homeContainer.layoutParams = params1;

        params2.weight = 3f
        binding.cartContainer.layoutParams = params2;
        binding.accountContainer.layoutParams = params2;

        binding.homeIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_main_active))
        binding.cartIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_cart_not_active))
        binding.accountIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_account_not_active))

        binding.homeIcontTv.visibility = View.VISIBLE
        binding.cartIconTv.visibility = View.GONE
        binding.accountIconTv.visibility = View.GONE

        loadFragment(homeFragment)
    }

    fun techSupportDialog(){
        techSupportDialog = TechSupportDialog()

        techSupportDialog.show(supportFragmentManager, "techSupportDialog")

    }

}