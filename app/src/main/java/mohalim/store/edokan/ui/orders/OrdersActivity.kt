package mohalim.store.edokan.ui.orders

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import mohalim.store.edokan.R
import mohalim.store.edokan.core.model.order.Order
import mohalim.store.edokan.core.utils.DataState
import mohalim.store.edokan.databinding.ActivityOrdersBinding
import mohalim.store.edokan.databinding.RowOrderBinding

@AndroidEntryPoint
class OrdersActivity : AppCompatActivity() {
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var adapterRV: OrderRecyclerView
    private lateinit var binding: ActivityOrdersBinding

    private val viewModel : OrdersViewModel by viewModels()
    private val firebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_orders)

        initRecyclerView()
        subscribeObserver()

    }


    override fun onResume() {
        super.onResume()

        firebaseAuth.currentUser?.getIdToken(false)?.addOnSuccessListener {
            val limit = 10
            val offset = 0
            viewModel.getOrders(limit, offset, it.token.toString())
        }

    }

    private fun subscribeObserver() {
        viewModel.ordersObserver.observe(this, Observer {
            when(it){
                is DataState.Loading ->{}
                is DataState.Success ->{
                    adapterRV.orders.clear()
                    adapterRV.orders.addAll(it.data)
                    adapterRV.notifyDataSetChanged()
                }
                is DataState.Failure ->{}
            }
        })
    }



    private fun initRecyclerView() {
        adapterRV = OrderRecyclerView(ArrayList())
        layoutManager = LinearLayoutManager(this)
        binding.orderRV.adapter = adapterRV
        binding.orderRV.layoutManager = layoutManager

        val dividerItemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.orderRV.addItemDecoration(dividerItemDecoration)

    }


    class OrderRecyclerView(val orders : MutableList<Order>) : RecyclerView.Adapter<OrderRecyclerView.OrderViewHolder>(){

        class OrderViewHolder(val binding : RowOrderBinding) : RecyclerView.ViewHolder(binding.root) {

            fun bindItem(order : Order){

            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
            val binding : RowOrderBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.row_order,
                parent,
                false
            )

            return OrderViewHolder(binding)
        }

        override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
            holder.bindItem(orders[position])
        }

        override fun getItemCount(): Int {
            return orders.size
        }


    }
}