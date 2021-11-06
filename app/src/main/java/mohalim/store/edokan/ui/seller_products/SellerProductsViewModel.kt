package mohalim.store.edokan.ui.seller_products

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import mohalim.store.edokan.core.model.product.Product
import mohalim.store.edokan.core.repository.SellerRepositoryImp
import mohalim.store.edokan.core.utils.DataState
import javax.inject.Inject

@HiltViewModel
class SellerProductsViewModel @Inject constructor(val sellerRepositoryImp: SellerRepositoryImp) : ViewModel(){
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

}