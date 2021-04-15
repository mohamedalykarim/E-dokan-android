package mohalim.store.edokan.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import mohalim.store.edokan.R
import mohalim.store.edokan.core.di.base.BaseActivity
import mohalim.store.edokan.core.utils.DataState
import mohalim.store.edokan.core.utils.ErrorHandler
import mohalim.store.edokan.core.utils.NetworkAvailabilityInterface
import mohalim.store.edokan.core.utils.NetworkVariables
import mohalim.store.edokan.databinding.ActivityMainBinding
import retrofit2.HttpException
import java.net.ConnectException

@AndroidEntryPoint
class MainActivity : BaseActivity() {
    val TAG : String = "MainActivity";

    lateinit var binding : ActivityMainBinding;
    val viewmodel : HomeViewModel by viewModels()
    lateinit var homeFragment : HomeFragment;
    lateinit var cartFragment: CartFragment;
    lateinit var accountFragment: AccountFragment;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)

        checkInternetAvailability()

        homeFragment = HomeFragment();
        cartFragment = CartFragment();
        accountFragment = AccountFragment()


        binding.pager.adapter = HomePagerAdapter(
                supportFragmentManager,
                lifecycle,
                homeFragment,
                cartFragment,
                accountFragment
        )

        binding.pager.currentItem = 2



        binding.pager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if(position == 2) {
                    homeBottom()
                    viewmodel.CURRENT_FRAGMENT = HomeFragment::class.java.name
                }
                if (position == 1) {
                    cartBottom()
                    viewmodel.CURRENT_FRAGMENT = CartFragment::class.java.name
                }
                if (position == 0) {
                    accountBottom()
                    viewmodel.CURRENT_FRAGMENT = AccountFragment::class.java.name
                }
            }
        })

        subscribeObservers();

        handleBottomClicks();
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
                        binding.pager.setCurrentItem(2)
                        if (viewmodel.CURRENT_FRAGMENT == HomeFragment::class.java.name) viewmodel.fetchHomeFragmentData()

                        binding.internetAvailabilityContainer.visibility = View.GONE
                        binding.pager.visibility = View.VISIBLE
                    }

                } else {

                    runOnUiThread {
                        binding.internetAvailabilityContainer.visibility = View.VISIBLE
                        binding.pager.visibility = View.GONE

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
                    lifecycleScope.launch {
                        homeFragment.updateCategoryData(it.data)
                    }

                }

                is DataState.Failure -> {
                }
            }
        })

        viewmodel.chosenProducts.observe(this, Observer {
            when (it) {
                is DataState.Loading -> {

                }

                is DataState.Success -> {
                    viewmodel.PAGE++
                    lifecycleScope.launch {
                        homeFragment.updateChosenProductsData(it.data)
                    }

                }

                is DataState.Failure -> {
                    when (it) {
                        is HttpException -> {
                            Log.d(TAG, "Failure: " + ErrorHandler.DO.getResponseMapFromHTTPException(it.exception as HttpException))
                        }
                    }
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
            if (viewmodel.bottomVisibility == viewmodel.BOTTOM_VISIBLE){
                val value : Float = binding.bottomContainer.height.toFloat()
                binding.bottom.animate().translationY(value).setListener(null)
                viewmodel.bottomVisibility = viewmodel.BOTTOM_HIDE
                binding.arrowIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_up_arrow))

            }else if (viewmodel.bottomVisibility == viewmodel.BOTTOM_HIDE){
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
        binding.pager.currentItem = 0

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

        binding.homeIcon.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_main_not_active))
        binding.cartIcon.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_cart_not_active))
        binding.accountIcon.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_account_active))

        binding.homeIcontTv.visibility = View.GONE
        binding.cartIconTv.visibility = View.GONE
        binding.accountIconTv.visibility = View.VISIBLE

    }

    private fun cartBottom() {
        val params1 : LinearLayout.LayoutParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT);
        val params2 : LinearLayout.LayoutParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT);

        if (viewmodel.currentTab == viewmodel.CART) return
        viewmodel.currentTab = viewmodel.CART
        binding.pager.currentItem = 1

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

        binding.homeIcon.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_main_not_active))
        binding.cartIcon.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_cart_active))
        binding.accountIcon.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_account_not_active))

        binding.homeIcontTv.visibility = View.GONE
        binding.cartIconTv.visibility = View.VISIBLE
        binding.accountIconTv.visibility = View.GONE
    }

    private fun homeBottom() {
        val params1 : LinearLayout.LayoutParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT);
        val params2 : LinearLayout.LayoutParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT);

        if (viewmodel.currentTab == viewmodel.HOME) return
        viewmodel.currentTab = viewmodel.HOME
        binding.pager.currentItem = 2

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

        binding.homeIcon.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_main_active))
        binding.cartIcon.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_cart_not_active))
        binding.accountIcon.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_account_not_active))

        binding.homeIcontTv.visibility = View.VISIBLE
        binding.cartIconTv.visibility = View.GONE
        binding.accountIconTv.visibility = View.GONE
    }


    private inner class HomePagerAdapter constructor(
            fm : FragmentManager,
            lifecycle: Lifecycle,
            val homefragment : HomeFragment,
            val cartFragment: CartFragment,
            val accountFragment: AccountFragment
    ) : FragmentStateAdapter(fm,lifecycle){


        override fun getItemCount(): Int = 3

        override fun createFragment(position: Int): Fragment {
            if (position == 0) return accountFragment
            if (position == 1) return cartFragment
            if (position == 2) return homefragment
            return homefragment
        }


    }


}