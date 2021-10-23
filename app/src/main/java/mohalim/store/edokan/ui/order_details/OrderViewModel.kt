package mohalim.store.edokan.ui.order_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import mohalim.store.edokan.core.repository.OrderRepositoryImp
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(val orderRepository : OrderRepositoryImp) : ViewModel(){


    fun startGetOrderDetails(orderId: Int, fToken: String) {
        viewModelScope.launch {
            orderRepository.getOrderDetails(orderId, fToken).collect {

            }
        }
    }


}