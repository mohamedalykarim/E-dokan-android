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
import androidx.lifecycle.Observer
import dagger.hilt.android.AndroidEntryPoint
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
    private lateinit var sellerMarketplacesFragment: SellerMarketplacesFragment

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

    /**
     * Handle Back Button press
     */
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
                accountFragment = AccountFragment()
                loadFragment(accountFragment)
                binding.bottom.visibility = View.VISIBLE
            }
            SellerMarketplacesFragment::class.java.name ->{
                accountFragment = AccountFragment()
                loadFragment(accountFragment)
                binding.bottom.visibility = View.VISIBLE
            }

        }
    }

    /**
     * Load Home Fragment
     */
    fun loadHome() {
        homeFragment = HomeFragment()
        loadFragment(homeFragment)
        homeBottom()
    }

    /**
     * Load City Fragment
     */
    fun loadCity() {
        cityFragment = CityFragment()
        loadFragment(cityFragment)
        binding.bottom.visibility = View.GONE
    }

    /**
     * Load Seller Marketplace Fragment
     */
    fun loadSellerMarketplaces() {
        sellerMarketplacesFragment = SellerMarketplacesFragment()
        loadFragment(sellerMarketplacesFragment)
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

    /**
     * Check Internet Availability
     */
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

    /**
     * Subscribe Observers
     */
    private fun subscribeObservers() {
        /**
         * Observe Main Categories
         */
        viewModel.noParentCategories.observe(this, {
            when (it) {
                is DataState.Loading -> { }
                is DataState.Success -> {
                    homeFragment.updateCategoryData(it.data)
                    homeFragment.noParentCategoriesDownloaded = true

                   if (homeFragment.offersComingDownloaded && homeFragment.chosenProductsDownloaded){
                       homeFragment.loadingDialog.dismiss()
                   }

                }
                is DataState.Failure -> {
                    homeFragment.noParentCategoriesDownloaded = true
                    if (homeFragment.offersComingDownloaded && homeFragment.chosenProductsDownloaded){
                        homeFragment.loadingDialog.dismiss()
                    }
                }
            }
        })

        /**
         * Observe Offers
         * Home Fragment
         */
        viewModel.offersComing.observe(this, {
            when (it) {
                is DataState.Loading -> { }
                is DataState.Success -> {
                    homeFragment.updateOffersData(it.data)
                    homeFragment.offersComingDownloaded = true

                    if (homeFragment.noParentCategoriesDownloaded && homeFragment.chosenProductsDownloaded){
                        homeFragment.loadingDialog.dismiss()
                    }

                }
                is DataState.Failure -> {
                    homeFragment.offersComingDownloaded = true
                    if (homeFragment.noParentCategoriesDownloaded && homeFragment.chosenProductsDownloaded){
                        homeFragment.loadingDialog.dismiss()
                    }
                }
            }
        })

        /**
         * Observe Chosen Products
         * Home Fragment
         */
        viewModel.chosenProducts.observe(this, {
            when (it) {
                is DataState.Loading -> { }
                is DataState.Success -> {
                    viewModel.PAGE++
                    homeFragment.updateChosenProductsData(it.data)
                    homeFragment.chosenProductsDownloaded = true

                    if (homeFragment.noParentCategoriesDownloaded && homeFragment.offersComingDownloaded){
                        homeFragment.loadingDialog.dismiss()
                    }
                }
                is DataState.Failure -> {
                    homeFragment.chosenProductsDownloaded = true
                    if (homeFragment.noParentCategoriesDownloaded && homeFragment.offersComingDownloaded){
                        homeFragment.loadingDialog.dismiss()
                    }

                }
            }

        })

        /**
         * Observe Support Items
         * Support Item Fragment
         */
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


        /**
         * Observe Adding Support Item
         * Support Item Fragment
         */
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

        /**
         * Observe Support Tech Item Message
         * Tech Support Message Fragment
         */
        viewModel.supportItemMessageObserver.observe(this, {
            when (it) {
                is DataState.Loading -> { }
                is DataState.Success -> {
                    techSupportMessagesDialog.updateMessages(it.data)
                }
                is DataState.Failure -> { }
            }

        })

        /**
         * Observe cities and it them to city fragments
         */
        viewModel.citiesObserver.observe(this, {
            when (it) {
                is DataState.Loading -> { }
                is DataState.Success -> {
                    cityFragment.updateCities(it.data)
                }
                is DataState.Failure -> { }
            }

        })

        /**
         * After getting the default address in cart fragment and request cart products :
         * Observe Cart products and add them to cart fragment
         * Update products in cart fragment
         * Step two of cart fragment
         */
        viewModel.cartProductsObserver.observe(this, {
            when (it) {
                is DataState.Loading -> { }
                is DataState.Success -> {
                    cartFragment.cartProducts.clear()
                    cartFragment.cartProducts.addAll(it.data)
                    cartFragment.updateProducts(it.data)
                    cartFragment.cartProductsFromInternalDownloaded = true
                    if (cartFragment.directionAndCartDetailsDownloaded){
                        cartFragment.loadingDialog.dismiss()
                    }
                    if (it.data.isEmpty()){
                        cartFragment.showNoProducts(true)

                        cartFragment.loadingDialog.dismiss()
                    }else{
                        cartFragment.showNoProducts(false)
                    }



                }
                is DataState.Failure -> {
                    cartFragment.cartProductsFromInternalDownloaded = true
                    if (cartFragment.directionAndCartDetailsDownloaded){
                        cartFragment.loadingDialog.dismiss()
                    }

                    cartFragment.showNoProducts(true)

                }
            }
        })

        /**
         * CART FRAGMENT
         * First step : Get default address in Cartfragment.class
         * Second step : Observe default address in MainActivity.class
         * Three step : Update Products of cart in cart fragment
         * Four Step: Ask for Order path in cart fragment at @updateProducts() function
         * Then Observe path direction
         */

        viewModel.directionObserver.observe(this, {
            when (it) {
                is DataState.Loading -> {
                }
                is DataState.Success -> {
                    val legsJsonArray = it.data.get("directions").asJsonObject.get("routes").asJsonArray[0].asJsonObject.get("legs").asJsonArray

                    val timer = object : CountDownTimer(500,500){
                        override fun onTick(millisUntilFinished: Long) {}
                        override fun onFinish() {
                            /**
                             * CART FRAGMENT
                             * First step : Get default address in Cartfragment.class
                             * Second step : Observe default address in MainActivity.class
                             * Three step : Update Products of cart in cart fragment
                             * Four Step : Ask for Order path in cart fragment at @updateProducts() function
                             * Five step :  Observe path direction
                             * Then Send direction legs to cart fragment
                             */
                            cartFragment.routeLegs(legsJsonArray)
                        }
                    }

                    timer.start()

                    val orderValue = it.data.get("order_value").asFloat
                    val deliveryValue = it.data.get("delivery_value").asFloat

                    cartFragment.updateOrderValues(orderValue, deliveryValue)
                    cartFragment.directionAndCartDetailsDownloaded = true
                    if (cartFragment.cartProductsFromInternalDownloaded){
                        cartFragment.loadingDialog.dismiss()
                    }

                }
                is DataState.Failure -> {
                    cartFragment.directionAndCartDetailsDownloaded = true
                    if (cartFragment.cartProductsFromInternalDownloaded){
                        cartFragment.loadingDialog.dismiss()
                    }
                }
            }
        })

        /**
         * Observe Default address
         */
        viewModel.defaultAddressObserver.observe(this, Observer {
            when (it) {
                is DataState.Loading -> { }
                is DataState.Success -> {
                    cartFragment.updateCartAddressUIandStartGetCartProducts(it.data)
                }
                is DataState.Failure -> {
                    Log.d("TAG", "subscribeObservers: "+it.exception.message)
                }
            }

        })

        /**
         * After hit add order button Observe the adding process
         */

        viewModel.addOrderObserver.observe(this, Observer {
            when(it){
                is DataState.Loading ->{
                    cartFragment.showLoading()
                }

                is DataState.Success ->{
                    cartFragment.hideLoading()
                    cartFragment.finishingAndStartOrder(it.data.order_id)
                }

                is DataState.Failure ->{
                    cartFragment.hideLoading()
                }
            }
        })


        /**
         * Seller Marketplaces Observer
         */
        viewModel.sellerMarketplacesObserver.observe(this, Observer {
            when(it){
                is DataState.Loading ->{}
                is DataState.Success ->{
                    sellerMarketplacesFragment.updateMarketplaces(it.data)
                }
                is DataState.Failure ->{}
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

    fun homeBottom() {
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