package mohalim.store.edokan.ui.seller_products

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import mohalim.store.edokan.core.repository.SellerRepositoryImp
import javax.inject.Inject

@HiltViewModel
class SellerProductsViewModel @Inject constructor(val sellerRepositoryImp: SellerRepositoryImp) : ViewModel(){
    var offset = 0
    var limit = 0

    fun getProducts(marketplaceId: Int, fToken: String?) {
        viewModelScope.launch {
            sellerRepositoryImp.getProducts(marketplaceId, fToken).collect {

            }

        }
    }

}