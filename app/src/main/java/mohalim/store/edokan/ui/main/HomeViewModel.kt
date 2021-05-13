package mohalim.store.edokan.ui.main

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import mohalim.store.edokan.core.model.category.Category
import mohalim.store.edokan.core.model.offer.Offer
import mohalim.store.edokan.core.model.product.Product
import mohalim.store.edokan.core.model.support_item.SupportItem
import mohalim.store.edokan.core.model.support_item_messsage.SupportItemMessage
import mohalim.store.edokan.core.repository.CategoryRepositoryImp
import mohalim.store.edokan.core.repository.OfferRepositoryImp
import mohalim.store.edokan.core.repository.ProductRepositoryImp
import mohalim.store.edokan.core.repository.SupportItemRepositoryImp
import mohalim.store.edokan.core.utils.DataState
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
@Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val categoryRepository: CategoryRepositoryImp,
    private val productRepository: ProductRepositoryImp,
    private val offerRepository: OfferRepositoryImp,
    private val supportItemRepository : SupportItemRepositoryImp
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


    fun fetchHomeFragmentData (){
        getNoParentCategories()
        getChosenProducts(PAGE, PAGE_COUNT)
        getCurrentOffers()
    }


    private fun getNoParentCategories(){
       viewModelScope.launch {
           categoryRepository.getNoParentCategories().collect {
               _noParentCategories.value = it
           }
       }
    }

    private fun getChosenProducts(page : Int, count : Int){
        viewModelScope.launch {
            productRepository.getChosenProducts(page, count).collect {
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

}