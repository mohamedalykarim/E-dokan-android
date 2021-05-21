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
import mohalim.store.edokan.core.model.cart.CartProduct
import mohalim.store.edokan.core.model.marketplace.MarketPlace
import mohalim.store.edokan.core.utils.Constants
import mohalim.store.edokan.core.utils.LocationUtils
import mohalim.store.edokan.core.utils.OtherUtils
import mohalim.store.edokan.databinding.FragmentCartBinding
import mohalim.store.edokan.databinding.RowCartMarketplaceBinding
import mohalim.store.edokan.databinding.RowCartProductBinding
import mohalim.store.edokan.ui.product.ProductActivity


@AndroidEntryPoint
class CartFragment : Fragment(), OnMapReadyCallback {
    private lateinit var polyline: Polyline
    private lateinit var googleMap: GoogleMap
    var cartProducts: MutableList<CartProduct> = ArrayList()
    var firebaseAuth = FirebaseAuth.getInstance()
    var isGoogleMapReady = false
    var isDirectionReady = false


    lateinit var binding : FragmentCartBinding
    lateinit var mainActivity: MainActivity


    val path: MutableList<LatLng> = ArrayList()
    private val marketPlaces : MutableList<MarketPlace> = ArrayList()
    private val userLocation = Location("userLocation")



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
         binding = DataBindingUtil.inflate(inflater, R.layout.fragment_cart, container, false)
        mainActivity = activity as MainActivity

        binding.map.onCreate(savedInstanceState)
        binding.map.getMapAsync(this)

        return binding.root
    }



    override fun onResume() {
        super.onResume()
        mainActivity.viewModel.getAllCartProductFromInternal()
        binding.map.onResume()
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
        Log.d("TAG", "updateProducts: "+data.size)
        marketPlaces.clear()

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
            cartProductBinding.descriptionTv.text = "$subDescription..."
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
        }

        val distinctMarketplaces = marketPlaces.distinct()

        val locations = ArrayList<Location>()
        userLocation.latitude = 25.843951
        userLocation.longitude = 32.834984

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

        if (marketPlaces.size < 1) return

        marketPlaces.forEach {
            Log.d("TAG", "before: "+it.distanceToUser)
        }


        val newMarketPlaces = marketPlaces.sortedWith(compareBy { it.distanceToUser })

        newMarketPlaces.forEach {
            Log.d("TAG", "after: "+it.distanceToUser)
        }

        // origin location
        val originLocation = Location("origin")
        originLocation.latitude = newMarketPlaces[newMarketPlaces.size - 1].lat
        originLocation.longitude = newMarketPlaces[newMarketPlaces.size - 1].lng

        firebaseAuth.currentUser?.getIdToken(false)?.addOnSuccessListener {
            mainActivity.viewModel.getOrderPath(
                originLocation,
                userLocation,
                locations,
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

        var totalDistance : Float = 0f;

        legsJsonArray?.forEach {
            val stepsJson = it.asJsonObject.get("steps").asJsonArray
            stepsJson.forEach {
                val points = it.asJsonObject.get("polyline").asJsonObject.get("points")
                path.addAll(LocationUtils.DO.decodePoly(points.asString))
            }

            val distance : Int = it.asJsonObject.get("distance").asJsonObject.get("value").asInt
            totalDistance = totalDistance + distance
        }

        binding.distanceTv.text = "المسافة حتى توصيل طلبك هي : " + totalDistance/1000 +" كم"

        isDirectionReady = true

        if (isGoogleMapReady){
           makeDirection()
        }


    }


}