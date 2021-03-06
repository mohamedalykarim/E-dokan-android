package mohalim.store.edokan.ui.orders

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
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
import mohalim.store.edokan.core.utils.Constants
import mohalim.store.edokan.core.utils.DataState
import mohalim.store.edokan.core.utils.DateUtils
import mohalim.store.edokan.databinding.ActivityOrdersBinding
import mohalim.store.edokan.databinding.RowOrderBinding
import mohalim.store.edokan.ui.extra.LoadingDialog
import mohalim.store.edokan.ui.order_details.OrderDetailsActivity

@AndroidEntryPoint
class OrdersActivity : AppCompatActivity() {
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var adapterRV: OrderRecyclerView
    private lateinit var binding: ActivityOrdersBinding

    private val viewModel : OrdersViewModel by viewModels()
    private val firebaseAuth = FirebaseAuth.getInstance()

    private lateinit var loadingDialog : LoadingDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_orders)

        loadingDialog = LoadingDialog()

        initRecyclerView()
        subscribeObserver()

    }


    override fun onResume() {
        super.onResume()

        adapterRV.orders.clear()
        viewModel.offset = 0

        firebaseAuth.currentUser?.getIdToken(false)?.addOnSuccessListener {
            viewModel.getOrders(viewModel.limit, viewModel.offset, it.token.toString())
        }
        showLoading()

    }

    fun showLoading(){
        if (!loadingDialog.isAdded) loadingDialog.show(supportFragmentManager, "LoadingDialog")
    }


    fun hideLoading(){
        if (loadingDialog.isAdded) loadingDialog.dismiss()
    }

    private fun subscribeObserver() {
        viewModel.ordersObserver.observe(this, Observer {
            when(it){
                is DataState.Loading ->{ }
                is DataState.Success ->{
                    adapterRV.orders.addAll(it.data)
                    adapterRV.notifyDataSetChanged()
                    viewModel.offset += it.data.size

                    hideLoading()
                    binding.moreProgressbar.visibility = View.GONE

                }
                is DataState.Failure ->{
                    hideLoading()
                    binding.moreProgressbar.visibility = View.GONE
                }
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

        binding.orderRV.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (layoutManager.findLastCompletelyVisibleItemPosition() == adapterRV.orders.size -1){

                    firebaseAuth.currentUser?.getIdToken(false)?.addOnSuccessListener {
                        viewModel.getOrders(viewModel.limit, viewModel.offset, it.token.toString())
                    }

                    binding.moreProgressbar.visibility = View.VISIBLE


                }
            }
        })

    }


    class OrderRecyclerView(val orders: MutableList<Order>) : RecyclerView.Adapter<OrderRecyclerView.OrderViewHolder>(){

        class OrderViewHolder(val binding : RowOrderBinding) : RecyclerView.ViewHolder(binding.root) {

            fun bindItem(order : Order){
                binding.orderNumberTv.setText(order.order_id.toString())
                binding.orderValueTv.text = String.format("%.2f", order.value)
                binding.deliveryFeesTv.text = String.format("%.2f", order.delivery_value)
                binding.addressTv.text = order.address_line1 + ", "+ order.address_line2
                binding.totalTv.text = String.format("%.2f", (order.value + order.delivery_value))

                binding.dayTv.text = DateUtils.convertMilisToDate(order.created_at, "dd")
                binding.yearTv.text = DateUtils.convertMilisToDate(order.created_at, "yyyy")
                binding.monthTv.text = DateUtils.convertMilisToDate(order.created_at, "MMMM")
                binding.timeTv.text = DateUtils.convertMilisToDate(order.created_at, "hh:mm aaa")

                binding.root.setOnClickListener {
                    val intent = Intent(binding.root.context, OrderDetailsActivity::class.java)
                    intent.putExtra(Constants.constants.ORDER_ID, order.order_id)
                    binding.root.context.startActivity(intent)
                }
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