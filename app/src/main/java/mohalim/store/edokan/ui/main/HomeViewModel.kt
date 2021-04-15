package mohalim.store.edokan.ui.main

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import mohalim.store.edokan.core.model.category.Category
import mohalim.store.edokan.core.model.product.Product
import mohalim.store.edokan.core.model.user.User
import mohalim.store.edokan.core.repository.CategoryRepositoryImp
import mohalim.store.edokan.core.repository.ProductRepositoryImp
import mohalim.store.edokan.core.utils.DataState
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
@Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val categoryRepository: CategoryRepositoryImp,
    private val productRepository: ProductRepositoryImp
    ) : ViewModel(){
    val HOME : String = "Home";
    val CART : String = "Cart";
    val ACCOUNT : String = "Account";

    val BOTTOM_HIDE = "bottom_hide"
    val BOTTOM_VISIBLE = "bottom_visible"

    val PAGE_COUNT = 10;
    var PAGE = 1;

    var currentTab = HOME;
    var bottomVisibility = BOTTOM_VISIBLE;

    var categories : List<Category> = ArrayList();
    var products : List<Product> = ArrayList();

    private val _noParentCategories : MutableLiveData<DataState<List<Category>>> = MutableLiveData()
    val noParentCategories : LiveData<DataState<List<Category>>> get() = _noParentCategories


    private val _chosenProducts : MutableLiveData<DataState<List<Product>>> = MutableLiveData()
    val chosenProducts : LiveData<DataState<List<Product>>> get() = _chosenProducts




    init {
        getNoParentCategories()
        getChosenProducts(PAGE, PAGE_COUNT)
    }

    fun getNoParentCategories(){
       viewModelScope.launch {
           categoryRepository.getNoParentCategories().collect {
               _noParentCategories.value = it
           }
       }
    }

    fun getChosenProducts(page : Int, count : Int){
        viewModelScope.launch {
            productRepository.getChosenProducts(page, count).collect {
                _chosenProducts.value = it
            }
        }
    }

}