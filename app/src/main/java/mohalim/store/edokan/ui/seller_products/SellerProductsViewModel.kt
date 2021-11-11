package mohalim.store.edokan.ui.seller_products

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import mohalim.store.edokan.core.model.category.Category
import mohalim.store.edokan.core.model.product.Product
import mohalim.store.edokan.core.repository.CategoryRepositoryImp
import mohalim.store.edokan.core.repository.SellerRepositoryImp
import mohalim.store.edokan.core.utils.DataState
import javax.inject.Inject

@HiltViewModel
class SellerProductsViewModel @Inject constructor(
    private val categoryRepositoryImp: CategoryRepositoryImp,
    private val sellerRepositoryImp: SellerRepositoryImp
    ) : ViewModel(){

    private val _categoriesObserver : MutableLiveData<DataState<List<Category>>> = MutableLiveData()
    val categoriesObserver get() = _categoriesObserver

    var products = mutableStateListOf<Product>()

    var offset = 0
    var limit = 0

    private val _productsObserver : MutableLiveData<DataState<List<Product>>> = MutableLiveData()
    val productsObserver get() = _productsObserver

    fun getProducts(marketplaceId: Int, fToken: String?) {
        viewModelScope.launch {
            sellerRepositoryImp.getProducts(marketplaceId, fToken).collect {
                _productsObserver.value = it
            }
        }
    }

    fun startGetMainCategory() {
        viewModelScope.launch{
            categoryRepositoryImp.getNoParentCategories().collect {
                _categoriesObserver.value = it
            }
        }
    }

    fun getCategoriesByParentId(categoryId: Int) {
        viewModelScope.launch{
            categoryRepositoryImp.getCategoriesByParentId(categoryId).collect {
                _categoriesObserver.value = it
            }
        }
    }

}