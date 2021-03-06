package mohalim.store.edokan.ui.main

import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintProperties
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.model.*
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import mohalim.store.edokan.R
import mohalim.store.edokan.core.model.address.Address
import mohalim.store.edokan.core.model.cart.CartProduct
import mohalim.store.edokan.core.model.marketplace.MarketPlace
import mohalim.store.edokan.core.model.order.Order
import mohalim.store.edokan.core.model.order.OrderMarketplace
import mohalim.store.edokan.core.model.order.OrderProduct
import mohalim.store.edokan.core.utils.*
import mohalim.store.edokan.databinding.FragmentCartBinding
import mohalim.store.edokan.databinding.RowCartMarketplaceBinding
import mohalim.store.edokan.databinding.RowCartProductBinding
import mohalim.store.edokan.ui.extra.LoadingDialog
import mohalim.store.edokan.ui.extra.MessageDialog
import mohalim.store.edokan.ui.order_details.OrderDetailsActivity
import mohalim.store.edokan.ui.product.ProductActivity


@AndroidEntryPoint
class CartFragment : Fragment() {

    private var deliveryValue: Float = 0f
    private var orderValue: Float = 0f
    var directionAndCartDetailsDownloaded: Boolean = false
    var cartProductsFromInternalDownloaded: Boolean = false

    private lateinit var defaultAddress: Address
    lateinit var loadingDialog : LoadingDialog
    lateinit var messagesDialog: MessageDialog

    var cartProducts: MutableList<CartProduct> = ArrayList()
    private var firebaseAuth = FirebaseAuth.getInstance()


    lateinit var binding : FragmentCartBinding
    lateinit var mainActivity: MainActivity


    val path: MutableList<LatLng> = ArrayList()
    private val marketPlaces : MutableList<MarketPlace> = ArrayList()
    private val userLocation = Location("userLocation")

    private val preferenceHelper: IPreferenceHelper by lazy { PreferencesUtils(activity) }




    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_cart, container, false)
        mainActivity = activity as MainActivity

        loadingDialog = LoadingDialog()
        messagesDialog = MessageDialog()
        
        click()

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        /**
         * Handle address
         */
        firebaseAuth.currentUser?.getIdToken(false)?.addOnSuccessListener {
            if(preferenceHelper.getDefaultAddressId() == 0){
                loadingDialog.dismiss()

                messagesDialog.setStyle(Constants.constants.MESSAGE_DIALOG_ADD_ADDRESS_STYLE)
                if(!messagesDialog.isAdded){
                    messagesDialog.show(mainActivity.supportFragmentManager, "MessageDialog")
                }

            }else{
                // start request default address from the api
                mainActivity.viewModel.getDefaultAddress(preferenceHelper.getDefaultAddressId()!!, it.token!!)
                
            }
        }

        if (!loadingDialog.isAdded){
            loadingDialog.show(mainActivity.supportFragmentManager, "LoadingDialog")
        }

    }


    private fun click() {



        binding.addOrderBtn.setOnClickListener {

            val productForOrder = ArrayList<OrderProduct>()
            val marketplacesForOrder = ArrayList<OrderMarketplace>()

            cartProducts.forEach{
                productForOrder.add(
                    OrderProduct(
                        1,
                        1,
                        it.productId,
                        it.productCount,
                        it.productPrice.toDouble(),
                        0.0
                    )
                )
            }


            marketPlaces.forEach{
                marketplacesForOrder.add(
                    OrderMarketplace(
                        1,
                        1,
                        it.marketplaceId,
                        "",
                        it.lat,
                        it.lng
                    )
                )
            }

            val order = Order(
                0,
                preferenceHelper.getUserId().toString(),
                defaultAddress.addressName,
                defaultAddress.addressLine1,
                defaultAddress.addressLine2,
                preferenceHelper.getCityId()!!,
                preferenceHelper.getCityName()!!,
                defaultAddress.addressLat, 
                defaultAddress.addressLng,
                1,
                0.0,
                0.0,
                1,
                1.0,
                1,
                orderValue.toDouble(),
                deliveryValue.toDouble(),
                0.0,
                productForOrder,
                marketplacesForOrder,
                (0).toBigInteger()
            )


            firebaseAuth.currentUser?.getIdToken(false)?.addOnSuccessListener {
                Log.d("TAG", "click: the button")
                mainActivity.viewModel.addOrder(order, it.token.toString())
            }
        }



    }


    /**
     * First step : get default address
     * Second step : Observe default address in MainActivity.class
     * Then : Update Products of cart
     * Step three of Cart Fragment
     */
    fun updateProducts(data: List<CartProduct>) {
        this.cartProducts.clear()
        this.cartProducts.addAll(data)

        marketPlaces.clear()

        val productIds = ArrayList<Int>()
        val productCounts = ArrayList<Int>()

        binding.marketPlaceContainerContainer.removeAllViews()
        binding.cartProductsContainer.removeAllViews()


        data.forEach {

            // Product UI Inflating
            val cartProductBinding : RowCartProductBinding = DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.row_cart_product,
                null,
                false
            )

            // Update every item ui in product cart
            cartProductBinding.productTitle.text = it.productName
            val subDescription = it.productDescription.substring(0,25)
            cartProductBinding.descriptionTv.text = subDescription
            cartProductBinding.priceTv.text = (it.productPrice - it.productDiscount).toString()
            cartProductBinding.countTv.text = it.productCount.toString()
            cartProductBinding.marketplaceTv.text = it.marketPlaceName


            val product = it
            cartProductBinding.root.setOnClickListener {
                val intent = Intent(mainActivity, ProductActivity::class.java)
                intent.putExtra(Constants.constants.PRODUCT_ID, product.productId)
                startActivity(intent)
            }

            val params = ConstraintLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ConstraintProperties.WRAP_CONTENT
            )
            params.setMargins(
                DensityUtil.dipToPx(mainActivity, 16f),
                DensityUtil.dipToPx(mainActivity, 10f),
                DensityUtil.dipToPx(mainActivity, 16f),
                DensityUtil.dipToPx(mainActivity, 10f)
            )

            cartProductBinding.root.layoutParams = params

            binding.cartProductsContainer.addView(cartProductBinding.root)

            marketPlaces.add(MarketPlace(it.marketPlaceId, it.marketPlaceName, it.marketPlaceLat, it.marketPlaceLng,0f,0,""))
            productIds.add(it.productId)
            productCounts.add(it.productCount)
        }

        Log.d("TAG", "updateProducts: "+marketPlaces.size)

        val distinctMarketplaces = marketPlaces.distinct()

        val locations = ArrayList<Location>()

        distinctMarketplaces.forEach {
            val marketplaceBinding : RowCartMarketplaceBinding = DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.row_cart_marketplace,
                binding.marketPlaceContainerContainer,
                false
            )

            marketplaceBinding.marketplaceNameTv.text = it.marketplaceName
            binding.marketPlaceContainerContainer.addView(marketplaceBinding.root)

            val location = Location(it.marketplaceName)
            location.latitude = it.lat
            location.longitude = it.lng


            locations.add(location)


            it.distanceToUser = location.distanceTo(userLocation)
        }

        if (marketPlaces.size < 1) {
            updateOrderValues(0f,0f, 0f)
            return
        }


        val newMarketPlaces = marketPlaces.sortedWith(compareBy { it.distanceToUser })



        // origin location
        val originLocation = Location("origin")
        originLocation.latitude = newMarketPlaces[newMarketPlaces.size - 1].lat
        originLocation.longitude = newMarketPlaces[newMarketPlaces.size - 1].lng

        firebaseAuth.currentUser?.getIdToken(false)?.addOnSuccessListener {
            /**
             * First step : get default address
             * Second step : Observe default address in MainActivity.class
             * Three step : Update Products of cart in cart fragment
             * Then ask for Order path in cart fragment at @updateProducts() function
             */

            mainActivity.viewModel.getOrderPath(
                originLocation,
                userLocation,
                locations,
                productIds,
                productCounts,
                it.token + ""
            )
        }


    }

    fun updateOrderValues(orderValue: Float, deliveryValue: Float, totalDistance: Float) {
        binding.orderValueTv.text = orderValue.toString()
        binding.deliveryValueTv.text = deliveryValue.toString()
        binding.totalTv.text = (orderValue + deliveryValue).toString()
        
        this.orderValue = orderValue

        this.deliveryValue = deliveryValue

    }

    /**
     * Update cart Address UI
     * Start get All cart products
     * Step one of the cart
     */
    fun updateCartAddressUIandStartGetCartProducts(data: Address) {
        defaultAddress = data
        userLocation.latitude = data.addressLat
        userLocation.longitude = data.addressLng
        mainActivity.viewModel.getAllCartProductFromInternal()

        binding.addressTV.text = data.addressName + "\n" + data.addressLine1 + "\n" + data.addressLine2 + " "+ data.city_name
    }

    fun showLoading() {
        if (!loadingDialog.isAdded) loadingDialog.show(mainActivity.supportFragmentManager, "LoadingDialog")
    }

    fun hideLoading(){
        loadingDialog.dismiss()
    }

    fun finishingAndStartOrder(orderId : Int) {
        mainActivity.viewModel.removeInternalCartProducts()

        val intent = Intent(mainActivity, OrderDetailsActivity::class.java)
        intent.putExtra(Constants.constants.ORDER_ID, orderId)
        startActivity(intent)
    }

    fun showNoProducts(isEmpty: Boolean) {
        Log.d("TAG", "showNoProducts: "+isEmpty)
        if (isEmpty){
            binding.emptyContainer.visibility = View.VISIBLE
            binding.cartAllContainer.visibility = View.GONE
        }else{
            binding.emptyContainer.visibility = View.GONE
            binding.cartAllContainer.visibility = View.VISIBLE
        }
    }

}