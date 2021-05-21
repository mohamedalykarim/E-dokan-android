package mohalim.store.edokan.ui.main

import android.os.Bundle
import android.os.CountDownTimer
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
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import mohalim.store.edokan.R
import mohalim.store.edokan.core.di.base.BaseActivity
import mohalim.store.edokan.core.model.support_item.SupportItem
import mohalim.store.edokan.core.utils.*
import mohalim.store.edokan.databinding.ActivityMainBinding


@AndroidEntryPoint
class MainActivity : BaseActivity() {
    val tag : String = "MainActivity"

    lateinit var binding : ActivityMainBinding
    val viewModel : HomeViewModel by viewModels()
    private lateinit var homeFragment : HomeFragment
    lateinit var cartFragment: CartFragment
    private lateinit var accountFragment: AccountFragment
    private lateinit var techSupportDialog: TechSupportDialog
    private lateinit var techSupportMessagesDialog: TechSupportMessagesDialog
    private lateinit var cityFragment: CityFragment

    private val preferenceHelper: IPreferenceHelper by lazy { PreferencesUtils(this) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(null)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        checkInternetAvailability()

        techSupportMessagesDialog = TechSupportMessagesDialog()

        subscribeObservers()
        handleBottomClicks()

        if(preferenceHelper.getCityId()!! > 0 ){

            homeFragment = HomeFragment()
            loadFragment(homeFragment)
        }else{
            cityFragment = CityFragment()
            loadFragment(cityFragment)
            binding.bottom.visibility = View.GONE
        }

    }

    override fun onBackPressed() {
        when (viewModel.CURRENT_FRAGMENT) {
            HomeFragment::class.java.name -> {
                finish()
            }
            CartFragment::class.java.name -> {
                homeFragment = HomeFragment()
                loadFragment(homeFragment)
                homeBottom()
            }
            AccountFragment::class.java.name -> {
                homeFragment = HomeFragment()
                loadFragment(homeFragment)
                homeBottom()
            }
            CityFragment::class.java.name ->{
                homeFragment = HomeFragment()
                loadFragment(homeFragment)
                binding.bottom.visibility = View.VISIBLE
            }

        }
    }

    fun loadHome() {
        homeFragment = HomeFragment()
        loadFragment(homeFragment)
    }

    fun loadCity() {
        homeBottom()
        cityFragment = CityFragment()
        loadFragment(cityFragment)
        binding.bottom.visibility = View.GONE
    }

    fun loadFragment(fragment : Fragment){
        for (i in 0 until supportFragmentManager.backStackEntryCount) {
            supportFragmentManager.popBackStack()
        }

        viewModel.CURRENT_FRAGMENT = fragment.javaClass.name
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace(binding.fragmentContainerView.id, fragment)
            addToBackStack(fragment.toString())
        }

        binding.bottom.visibility = View.VISIBLE
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
                        if (viewModel.CURRENT_FRAGMENT == HomeFragment::class.java.name) viewModel.fetchHomeFragmentData(preferenceHelper.getCityId()!!)
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
        viewModel.noParentCategories.observe(this, {
            when (it) {
                is DataState.Loading -> { }
                is DataState.Success -> { homeFragment.updateCategoryData(it.data) }
                is DataState.Failure -> { }
            }
        })

        viewModel.offersComing.observe(this, {
            when (it) {
                is DataState.Loading -> { }
                is DataState.Success -> { homeFragment.updateOffersData(it.data) }
                is DataState.Failure -> { }
            }
        })

        viewModel.chosenProducts.observe(this, {
            when (it) {
                is DataState.Loading -> { }
                is DataState.Success -> {
                    viewModel.PAGE++
                    homeFragment.updateChosenProductsData(it.data)
                }
                is DataState.Failure -> { }
            }

        })

        viewModel.supportItemObserver.observe(this,  {
            when (it) {
                is DataState.Loading -> {
                    techSupportDialog.changeProgressBarVisibility(View.VISIBLE)
                }
                is DataState.Success -> {
                    techSupportDialog.changeProgressBarVisibility(View.GONE)
                    techSupportDialog.updateSupportItemsData(it.data)
                }

                is DataState.Failure -> {
                    techSupportDialog.changeProgressBarVisibility(View.GONE)
                }
            }
        })

        viewModel.addSupportItemObserver.observe(this, {
            when (it) {
                is DataState.Loading -> {
                    techSupportDialog.changeAddProgressBarVisibility(View.VISIBLE, btnEnable = false, clearED = false)
                }

                is DataState.Success -> {
                    techSupportDialog.changeAddProgressBarVisibility(View.GONE, btnEnable = false, clearED = false)
                    techSupportDialog.addNewSupportItemToAdapter(it.data)
                }

                is DataState.Failure -> {
                    techSupportDialog.changeAddProgressBarVisibility(View.GONE, btnEnable = false, clearED = false)
                }
            }

        })

        viewModel.supportItemMessageObserver.observe(this, {
            when (it) {
                is DataState.Loading -> { }
                is DataState.Success -> {
                    techSupportMessagesDialog.updateMessages(it.data)
                }
                is DataState.Failure -> { }
            }

        })

        viewModel.citiesObserver.observe(this, {
            when (it) {
                is DataState.Loading -> { }
                is DataState.Success -> {
                    cityFragment.updateCities(it.data)
                }
                is DataState.Failure -> { }
            }

        })

        viewModel.cartProductsObserver.observe(this, {
            when (it) {
                is DataState.Loading -> { }
                is DataState.Success -> {
                    cartFragment.cartProducts.clear()
                    cartFragment.cartProducts.addAll(it.data)
                    cartFragment.updateProducts(it.data)
                }
                is DataState.Failure -> { }
            }
        })

        viewModel.directionObserver.observe(this, {
            when (it) {
                is DataState.Loading -> {
                    Log.d("TAG", "subscribeObservers: loading" )
                }
                is DataState.Success -> {
                    val legsJsonArray = it.data.get("routes").asJsonArray[0].asJsonObject.get("legs").asJsonArray

                    val timer = object : CountDownTimer(500,500){
                        override fun onTick(millisUntilFinished: Long) {}
                        override fun onFinish() {
                            cartFragment.routeLegs(legsJsonArray)
                        }
                    }

                    timer.start()

                }
                is DataState.Failure -> {
                    Log.d("TAG", "subscribeObservers: "+ it.exception.message)
                }
            }
        })

    }

    private fun handleBottomClicks() {

        binding.homeIcon.setOnClickListener {
            homeFragment = HomeFragment()
            loadFragment(homeFragment)
            homeBottom()
        }

        binding.cartIcon.setOnClickListener{
            cartFragment = CartFragment()
            loadFragment(cartFragment)
            cartBottom()
        }

        binding.accountContainer.setOnClickListener{
            accountFragment = AccountFragment()
            loadFragment(accountFragment)
            accountBottom()
        }

        binding.bottomHeaderView.setOnClickListener{
            if (viewModel.bottomVisibility == viewModel.BOTTOM_VISIBLE) {
                val value: Float = binding.bottomContainer.height.toFloat()
                binding.bottom.animate().translationY(value).setListener(null)
                viewModel.bottomVisibility = viewModel.BOTTOM_HIDE
                binding.arrowIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_up_arrow))

            } else if (viewModel.bottomVisibility == viewModel.BOTTOM_HIDE) {
                binding.bottom.animate().translationY(0f).setListener(null)
                viewModel.bottomVisibility = viewModel.BOTTOM_VISIBLE
                binding.arrowIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_down_arrow))

            }
        }
    }

    private fun accountBottom() {
        val params1 : LinearLayout.LayoutParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
        val params2 : LinearLayout.LayoutParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)

        if (viewModel.currentTab == viewModel.ACCOUNT) return
        viewModel.currentTab = viewModel.ACCOUNT

        binding.accountContainerBG.setBackgroundResource(R.drawable.bottom_bar_item_bg)
        binding.homeContainerBG.setBackgroundColor(ContextCompat.getColor(this@MainActivity, R.color.transparent))
        binding.cartContainerBG.setBackgroundColor(ContextCompat.getColor(this@MainActivity, R.color.transparent))

        binding.accountContainerBG.translationY = (binding.bottomContainer.height - binding.accountContainerBG.height).toFloat()
        binding.accountContainerBG.animate().setDuration(200).translationY(0f).setListener(null)


        params1.weight = 2f
        binding.accountContainer.layoutParams = params1

        params2.weight = 3f
        binding.cartContainer.layoutParams = params2
        binding.homeContainer.layoutParams = params2

        binding.homeIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_main_not_active))
        binding.cartIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_cart_not_active))
        binding.accountIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_account_active))

        binding.homeIcontTv.visibility = View.GONE
        binding.cartIconTv.visibility = View.GONE
        binding.accountIconTv.visibility = View.VISIBLE
    }

    private fun cartBottom() {
        val params1 : LinearLayout.LayoutParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
        val params2 : LinearLayout.LayoutParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)

        if (viewModel.currentTab == viewModel.CART) return
        viewModel.currentTab = viewModel.CART

        binding.cartContainerBG.setBackgroundResource(R.drawable.bottom_bar_item_bg)
        binding.homeContainerBG.setBackgroundColor(ContextCompat.getColor(this, R.color.transparent))
        binding.accountContainerBG.setBackgroundColor(ContextCompat.getColor(this, R.color.transparent))

        binding.cartContainerBG.translationY = (binding.bottomContainer.height - binding.cartContainerBG.height).toFloat()
        binding.cartContainerBG.animate().setDuration(200).translationY(0f).setListener(null)



        params1.weight = 2f
        binding.cartContainer.layoutParams = params1

        params2.weight = 3f
        binding.homeContainer.layoutParams = params2
        binding.accountContainer.layoutParams = params2

        binding.homeIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_main_not_active))
        binding.cartIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_cart_active))
        binding.accountIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_account_not_active))

        binding.homeIcontTv.visibility = View.GONE
        binding.cartIconTv.visibility = View.VISIBLE
        binding.accountIconTv.visibility = View.GONE
    }

    private fun homeBottom() {
        val params1 : LinearLayout.LayoutParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
        val params2 : LinearLayout.LayoutParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)

        if (viewModel.currentTab == viewModel.HOME) return
        viewModel.currentTab = viewModel.HOME

        binding.homeContainerBG.setBackgroundResource(R.drawable.bottom_bar_item_bg)
        binding.cartContainerBG.setBackgroundColor(ContextCompat.getColor(this, R.color.transparent))
        binding.accountContainerBG.setBackgroundColor(ContextCompat.getColor(this, R.color.transparent))

        binding.homeContainerBG.translationY = (binding.bottomContainer.height- binding.homeContainerBG.height).toFloat()
        binding.homeContainerBG.animate().setDuration(200).translationY(0f).setListener(null)


        params1.weight = 2f
        binding.homeContainer.layoutParams = params1

        params2.weight = 3f
        binding.cartContainer.layoutParams = params2
        binding.accountContainer.layoutParams = params2

        binding.homeIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_main_active))
        binding.cartIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_cart_not_active))
        binding.accountIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_account_not_active))

        binding.homeIcontTv.visibility = View.VISIBLE
        binding.cartIconTv.visibility = View.GONE
        binding.accountIconTv.visibility = View.GONE

   }

    fun techSupportDialog(){
        techSupportDialog = TechSupportDialog()
        if(techSupportDialog.isAdded) return
        techSupportDialog.show(supportFragmentManager, "techSupportDialog")
    }

    fun showTechSupportMessageDialog(supportItem: SupportItem) {
        if(techSupportMessagesDialog.isAdded) return
        techSupportMessagesDialog.supportItem = supportItem
        techSupportMessagesDialog.show(supportFragmentManager, "techSupportMessagesDialog")
    }





}