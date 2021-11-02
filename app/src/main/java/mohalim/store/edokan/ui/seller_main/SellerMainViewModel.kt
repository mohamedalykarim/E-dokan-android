package mohalim.store.edokan.ui.seller_main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import mohalim.store.edokan.core.model.marketplace.MarketPlace
import mohalim.store.edokan.core.model.order.Order
import mohalim.store.edokan.core.repository.SellerRepositoryImp
import mohalim.store.edokan.core.utils.DataState
import javax.inject.Inject

@HiltViewModel
class SellerMainViewModel @Inject constructor(private val sellerRepositoryImp: SellerRepositoryImp) : ViewModel() {
    var limit = 10
    var offset = 0

    private val _ordersObserver : MutableLiveData<DataState<List<Order>>> = MutableLiveData()
    val ordersObserver get() = _ordersObserver

    private val _marketplaceObserver : MutableLiveData<DataState<MarketPlace>> = MutableLiveData()
    val marketplaceObserver get() = _marketplaceObserver

    fun getOrders(limit: Int, offset: Int, marketplaceId : Int, fToken: String) {
        viewModelScope.launch {
            sellerRepositoryImp.getOrders(limit, offset, marketplaceId, fToken).collect {
                _ordersObserver.value = it
            }
        }
    }

    fun getMarketplaceFromCache(marketplaceId: Int) {
        viewModelScope.launch {
            sellerRepositoryImp.getMarketplaceFromCache(marketplaceId).collect {
                _marketplaceObserver.value = it
            }
        }
    }

}