package mohalim.store.edokan.ui.main

import android.location.Location
import android.util.Log
import androidx.lifecycle.*
import com.google.gson.JsonObject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import mohalim.store.edokan.core.model.address.Address
import mohalim.store.edokan.core.model.cart.CartProduct
import mohalim.store.edokan.core.model.category.Category
import mohalim.store.edokan.core.model.city.City
import mohalim.store.edokan.core.model.offer.Offer
import mohalim.store.edokan.core.model.order.Order
import mohalim.store.edokan.core.model.product.Product
import mohalim.store.edokan.core.model.support_item.SupportItem
import mohalim.store.edokan.core.model.support_item_messsage.SupportItemMessage
import mohalim.store.edokan.core.repository.*
import mohalim.store.edokan.core.utils.DataState
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
@Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val categoryRepository: CategoryRepositoryImp,
    private val productRepository: ProductRepositoryImp,
    private val offerRepository: OfferRepositoryImp,
    private val supportItemRepository : SupportItemRepositoryImp,
    private val cityRepository : CityRepositoryImp,
    private val orderRepository: OrderRepositoryImp,
    private val addressRepository: AddressRepositoryImp
    ) : ViewModel(){
    var CURRENT_FRAGMENT: String = HomeFragment::class.java.toString();
    val HOME : String = "Home";
    val CART : String = "Cart";
    val ACCOUNT : String = "Account";

    val BOTTOM_HIDE = "bottom_hide"
    val BOTTOM_VISIBLE = "bottom_visible"

    val PAGE_COUNT = 10;
    var PAGE = 1;

    var currentTab = MainActivity::class.java.toString();
    var bottomVisibility = BOTTOM_VISIBLE;

    var categories : List<Category> = ArrayList();
    var products : List<Product> = ArrayList();
    var offers : List<Offer> = ArrayList();

    private val _noParentCategories : MutableLiveData<DataState<List<Category>>> = MutableLiveData()
    val noParentCategories : LiveData<DataState<List<Category>>> get() = _noParentCategories


    private val _chosenProducts : MutableLiveData<DataState<List<Product>>> = MutableLiveData()
    val chosenProducts : LiveData<DataState<List<Product>>> get() = _chosenProducts

    private val _offersComing : MutableLiveData<DataState<List<Offer>>> = MutableLiveData()
    val offersComing : LiveData<DataState<List<Offer>>> get() = _offersComing

    private val _supportItemObserver : MutableLiveData<DataState<List<SupportItem>>> = MutableLiveData()
    val supportItemObserver : LiveData<DataState<List<SupportItem>>> get() = _supportItemObserver

    private val _supportItemMessageObserver : MutableLiveData<DataState<List<SupportItemMessage>>> = MutableLiveData()
    val supportItemMessageObserver : LiveData<DataState<List<SupportItemMessage>>> get() = _supportItemMessageObserver


    private val _addSupportItemObserver : MutableLiveData<DataState<SupportItem>> = MutableLiveData()
    val addSupportItemObserver : LiveData<DataState<SupportItem>> get() = _addSupportItemObserver


    private val _citiesObserver : MutableLiveData<DataState<List<City>>> = MutableLiveData()
    val citiesObserver : LiveData<DataState<List<City>>> get() = _citiesObserver

    private val _cartProductsObserver : MutableLiveData<DataState<List<CartProduct>>> = MutableLiveData()
    val cartProductsObserver : LiveData<DataState<List<CartProduct>>> get() = _cartProductsObserver

    private val _directionObserver : MutableLiveData<DataState<JsonObject>> = MutableLiveData()
    val directionObserver : LiveData<DataState<JsonObject>> get() = _directionObserver

    private val _defaultAddressObserver : MutableLiveData<DataState<Address>> = MutableLiveData()
    val defaultAddressObserver : LiveData<DataState<Address>> get() = _defaultAddressObserver



    fun fetchHomeFragmentData (cityId: Int){
        getNoParentCategories()
        getChosenProducts(PAGE, PAGE_COUNT, cityId)
        getCurrentOffers()
    }


    private fun getNoParentCategories(){
       viewModelScope.launch {
           categoryRepository.getNoParentCategories().collect {
               _noParentCategories.value = it
           }
       }
    }

    private fun getChosenProducts(page : Int, count : Int, cityId : Int){
        viewModelScope.launch {
            productRepository.getChosenProducts(page, count, cityId).collect {
                _chosenProducts.value = it
            }
        }
    }

    private fun getCurrentOffers(){
        viewModelScope.launch {
            offerRepository.getCurrentOffers().collect {
                _offersComing.value = it
            }
        }
    }

    fun getSupportItems(userId : String, fToken : String){
        viewModelScope.launch {
            supportItemRepository.getSupportItems(userId, fToken).collect {
                _supportItemObserver.value = it
            }
        }
    }

    fun getSupportItemAllMessages(itemId: Int, fToken: String){
        viewModelScope.launch {
            supportItemRepository.getAllMessages(itemId, fToken).collect {
                _supportItemMessageObserver.value = it
            }
        }
    }

    fun addSupportItems(userId : String, fToken : String, message : String){
        viewModelScope.launch {
            supportItemRepository.addSupportItem(userId, fToken, message).collect {
                _addSupportItemObserver.value = it
            }
        }
    }

    fun getAllCities(){
        viewModelScope.launch {
            cityRepository.getAllCities().collect {
                _citiesObserver.value = it
            }
        }
    }

    fun getAllCartProductFromInternal() {
        viewModelScope.launch {
            productRepository.getAllCartProductFromInternal().collect {
                _cartProductsObserver.value = it
            }
        }
    }

    fun getOrderPath(
        origin : Location,
        destination : Location,
        locations : MutableList<Location>,
        productIds : List<Int>,
        productCounts : List<Int>,
        fToken: String
    ){
        viewModelScope.launch {
            orderRepository.getOrderPath(origin, destination, locations, productIds, productCounts, fToken).collect {
                _directionObserver.value = it
            }
        }
    }

    fun  getDefaultAddress(addressId : Int, fToken: String) {
        viewModelScope.launch {
            addressRepository.getAddress(addressId, fToken).collect {
                _defaultAddressObserver.value = it
            }
        }
    }

    fun addOrder(order: Order, fToken: String) {
        viewModelScope.launch{

            orderRepository.addOrder(order, fToken).collect {

            }
        }
    }

}