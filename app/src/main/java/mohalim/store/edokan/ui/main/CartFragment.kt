package mohalim.store.edokan.ui.main

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.JsonArray
import dagger.hilt.android.AndroidEntryPoint
import mohalim.store.edokan.R
import mohalim.store.edokan.core.model.address.Address
import mohalim.store.edokan.core.model.cart.CartProduct
import mohalim.store.edokan.core.model.marketplace.MarketPlace
import mohalim.store.edokan.core.utils.Constants
import mohalim.store.edokan.core.utils.IPreferenceHelper
import mohalim.store.edokan.core.utils.LocationUtils
import mohalim.store.edokan.core.utils.PreferencesUtils
import mohalim.store.edokan.databinding.FragmentCartBinding
import mohalim.store.edokan.databinding.RowCartMarketplaceBinding
import mohalim.store.edokan.databinding.RowCartProductBinding
import mohalim.store.edokan.ui.extra.LoadingDialog
import mohalim.store.edokan.ui.product.ProductActivity


@AndroidEntryPoint
class CartFragment : Fragment(), OnMapReadyCallback {

    var directionAndCartDetailsDownloaded: Boolean = false
    var cartProductsFromInternalDownloaded: Boolean = false

    private lateinit var polyline: Polyline
    private lateinit var googleMap: GoogleMap
    private lateinit var defaultAddress: Address
    lateinit var loadingDialog : LoadingDialog

    var cartProducts: MutableList<CartProduct> = ArrayList()
    private var firebaseAuth = FirebaseAuth.getInstance()
    private var isGoogleMapReady = false
    private var isDirectionReady = false


    lateinit var binding : FragmentCartBinding
    lateinit var mainActivity: MainActivity


    val path: MutableList<LatLng> = ArrayList()
    private val marketPlaces : MutableList<MarketPlace> = ArrayList()
    private val userLocation = Location("userLocation")

    private val preferenceHelper: IPreferenceHelper by lazy { PreferencesUtils(activity) }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
         binding = DataBindingUtil.inflate(inflater, R.layout.fragment_cart, container, false)
        mainActivity = activity as MainActivity

        binding.map.onCreate(savedInstanceState)
        binding.map.getMapAsync(this)

        loadingDialog = LoadingDialog()

        return binding.root
    }



    override fun onResume() {
        super.onResume()
        firebaseAuth.currentUser?.getIdToken(false)?.addOnSuccessListener {
            mainActivity.viewModel.getDefaultAddress(preferenceHelper.getDefaultAddressId()!!, it.token!!)
        }
        binding.map.onResume()
        loadingDialog.show(mainActivity.supportFragmentManager, "LoadingDialog")

        if (this::defaultAddress.isInitialized){
            getDefaultAddress(defaultAddress)
        }

    }

    override fun onStart() {
        super.onStart()
        binding.map.onStart()

    }

    override fun onPause() {
        super.onPause()
        binding.map.onPause()
    }

    override fun onStop() {
        super.onStop()
        binding.map.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.map.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.map.onLowMemory()
    }


    fun updateProducts(data: List<CartProduct>) {
        marketPlaces.clear()

        val productIds = ArrayList<Int>()
        val productCounts = ArrayList<Int>()

        binding.marketPlaceContainerContainer.removeAllViews()
        binding.cartProductsContainer.removeAllViews()


        data.forEach {
            val cartProductBinding : RowCartProductBinding = DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.row_cart_product,
                null,
                false
            )

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

            binding.cartProductsContainer.addView(cartProductBinding.root)

            marketPlaces.add(MarketPlace(it.marketPlaceId, it.marketPlaceName, it.marketPlaceLat, it.marketPlaceLng,0f))
            productIds.add(it.productId)
            productCounts.add(it.productCount)
        }

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
            updateOrderValues(0f,0f)
            googleMap.clear()
            if (this::polyline.isInitialized){
                polyline.remove()
            }
            binding.distanceTv.text = ""
            return
        }


        val newMarketPlaces = marketPlaces.sortedWith(compareBy { it.distanceToUser })



        // origin location
        val originLocation = Location("origin")
        originLocation.latitude = newMarketPlaces[newMarketPlaces.size - 1].lat
        originLocation.longitude = newMarketPlaces[newMarketPlaces.size - 1].lng

        firebaseAuth.currentUser?.getIdToken(false)?.addOnSuccessListener {
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

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap
        isGoogleMapReady = true
    }

    private fun makeDirection() {
        polyline = googleMap.addPolyline(PolylineOptions().color(Color.RED).clickable(true).addAll(path))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(userLocation.latitude, userLocation.longitude), 13f))

        marketPlaces.forEach {
            val height = 50
            val width = 50
            val bitmapDraw = (ContextCompat.getDrawable(mainActivity, R.drawable.market_icon) as BitmapDrawable).bitmap
            val smallMarker = Bitmap.createScaledBitmap(bitmapDraw, width, height, false)

            googleMap.addMarker(
                MarkerOptions().position(LatLng(it.lat,it.lng))
                    .icon(BitmapDescriptorFactory.fromBitmap(smallMarker)).title(it.marketplaceName)
            )
        }

        val height = 50
        val width = 50
        val bitmapDraw = (ContextCompat.getDrawable(mainActivity, R.drawable.user_icon) as BitmapDrawable).bitmap
        val smallMarker = Bitmap.createScaledBitmap(bitmapDraw, width, height, false)

        googleMap.addMarker(
            MarkerOptions().position(LatLng(userLocation.latitude, userLocation.longitude))
                .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
        )



    }

    fun routeLegs(legsJsonArray: JsonArray?) {
        path.clear()

        if (this::polyline.isInitialized){
            polyline.remove()
            googleMap.clear()
        }

        var totalDistance = 0f

        legsJsonArray?.forEach { it ->
            val stepsJson = it.asJsonObject.get("steps").asJsonArray
            stepsJson.forEach {
                val points = it.asJsonObject.get("polyline").asJsonObject.get("points")
                path.addAll(LocationUtils.DO.decodePoly(points.asString))
            }

            val distance : Int = it.asJsonObject.get("distance").asJsonObject.get("value").asInt
            totalDistance += distance
        }

        binding.distanceTv.text = "المسافة حتى توصيل طلبك هي : " + totalDistance/1000 +" كم"

        isDirectionReady = true

        if (isGoogleMapReady){
           makeDirection()
        }


    }

    fun updateOrderValues(orderValue: Float, deliveryValue: Float) {
        binding.orderValueTv.text = orderValue.toString()
        binding.deliveryValueTv.text = deliveryValue.toString()
        binding.totalTv.text = (orderValue + deliveryValue).toString()

    }

    fun getDefaultAddress(data: Address) {
        defaultAddress = data
        userLocation.latitude = data.addressLat
        userLocation.longitude = data.addressLng
        mainActivity.viewModel.getAllCartProductFromInternal()

        binding.addressTV.text = data.addressName + "\n" + data.addressLine1 + "\n" + data.addressLine2 + " "+ data.city

    }


}