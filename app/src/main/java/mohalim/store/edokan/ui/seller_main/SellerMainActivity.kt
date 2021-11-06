package mohalim.store.edokan.ui.seller_main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.databinding.DataBindingUtil
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
import mohalim.store.edokan.databinding.ActivitySellerMainBinding
import mohalim.store.edokan.databinding.RowOrderBinding
import mohalim.store.edokan.ui.extra.LoadingDialog
import mohalim.store.edokan.ui.seller_order_details.SellerOrderDetailsActivity
import mohalim.store.edokan.ui.seller_products.SellerProductsActivity

@AndroidEntryPoint
class SellerMainActivity : AppCompatActivity() {
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var adapterRV: OrderSellerMainRecyclerView
    private lateinit var binding: ActivitySellerMainBinding

    private val viewModel : SellerMainViewModel by viewModels()
    private val firebaseAuth = FirebaseAuth.getInstance()

    private lateinit var loadingDialog : LoadingDialog
    private var marketplaceId = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_seller_main)
        binding.topLinks.setContent {
            topLinksComponent()
        }

        if (!intent.hasExtra(Constants.constants.MARKETPLACE_ID)) finish()
        marketplaceId = intent.getIntExtra(Constants.constants.MARKETPLACE_ID, 0)
        if (marketplaceId == 0) finish()


        loadingDialog = LoadingDialog()

        initRecyclerView()
        subscribeObserver()

    }

    override fun onResume() {
        super.onResume()

        adapterRV.orders.clear()
        viewModel.offset = 0

        firebaseAuth.currentUser?.getIdToken(false)?.addOnSuccessListener {
            viewModel.getOrders(viewModel.limit, viewModel.offset, marketplaceId, it.token.toString())
        }

        viewModel.getMarketplaceFromCache(marketplaceId)

        showLoading()

    }

    private fun showLoading(){
        if (!loadingDialog.isAdded) loadingDialog.show(supportFragmentManager, "LoadingDialog")
    }


    private fun hideLoading(){
        if (loadingDialog.isAdded) loadingDialog.dismiss()
    }

    private fun subscribeObserver() {
        /**
         * Observe orders of the chosen marketplace
         */
        viewModel.ordersObserver.observe(this,  {
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

        /**
         * Observe the marketplace from internal cache
         */
        viewModel.marketplaceObserver.observe(this,  {
            when(it){
                is DataState.Loading ->{ }
                is DataState.Success ->{
                    binding.marketplaceNameTv.text = it.data.marketplaceName
                }
                is DataState.Failure ->{ }

            }

        })
    }


    private fun initRecyclerView() {
        adapterRV = OrderSellerMainRecyclerView(ArrayList(), marketplaceId)
        layoutManager = LinearLayoutManager(this)
        binding.orderRV.adapter = adapterRV
        binding.orderRV.layoutManager = layoutManager

        val dividerItemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.orderRV.addItemDecoration(dividerItemDecoration)

        binding.orderRV.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (layoutManager.findLastCompletelyVisibleItemPosition() == adapterRV.orders.size -1){

                    firebaseAuth.currentUser?.getIdToken(false)?.addOnSuccessListener {
                        viewModel.getOrders(viewModel.limit, viewModel.offset, marketplaceId, it.token.toString())
                    }

                    binding.moreProgressbar.visibility = View.VISIBLE


                }
            }
        })

    }


    class OrderSellerMainRecyclerView(val orders: MutableList<Order>, val marketplaceId: Int) : RecyclerView.Adapter<OrderSellerMainRecyclerView.OrderViewHolder>(){

        class OrderViewHolder(val binding : RowOrderBinding) : RecyclerView.ViewHolder(binding.root) {

            fun bindItem(order : Order, marketplaceId: Int){
                binding.orderNumberTv.text = order.order_id.toString()
                binding.orderValueTv.text = String.format("%.2f", order.value)
                binding.deliveryFeesTv.text = String.format("%.2f", order.delivery_value)
                binding.addressTv.text = order.address_line1 + ", "+ order.address_line2
                binding.totalTv.text = String.format("%.2f", (order.value + order.delivery_value))

                binding.dayTv.text = DateUtils.convertMilisToDate(order.created_at, "dd")
                binding.yearTv.text = DateUtils.convertMilisToDate(order.created_at, "yyyy")
                binding.monthTv.text = DateUtils.convertMilisToDate(order.created_at, "MMMM")
                binding.timeTv.text = DateUtils.convertMilisToDate(order.created_at, "hh:mm aaa")

                binding.root.setOnClickListener {
                    val intent = Intent(binding.root.context, SellerOrderDetailsActivity::class.java)
                    intent.putExtra(Constants.constants.ORDER_ID, order.order_id)
                    intent.putExtra(Constants.constants.MARKETPLACE_ID, marketplaceId)
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
            holder.bindItem(orders[position], marketplaceId)
        }

        override fun getItemCount(): Int {
            return orders.size
        }
    }

    @Composable
    private fun topLinksComponent() {
        Surface {
            Row (modifier = Modifier.height(IntrinsicSize.Min)) {
                /**
                 * Products button
                 */

                OutlinedButton(
                    onClick = {
                        val intent = Intent(this@SellerMainActivity, SellerProductsActivity::class.java)
                        intent.putExtra(Constants.constants.MARKETPLACE_ID, marketplaceId)
                        startActivity(intent)
                    },
                    colors = ButtonDefaults.textButtonColors(
                        backgroundColor = Color.Red,
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize(1f)
                        .padding(0.dp, 0.dp,8.dp,0.dp)
                ){
                    Column (horizontalAlignment = Alignment.CenterHorizontally){
                        Image(
                            painter = painterResource(R.drawable.market_icon),
                            contentDescription = null,
                            modifier = Modifier.width(30.dp).height(30.dp),
                        )

                        Text(text = "Products", textAlign = TextAlign.Center, color = Color.White)

                    }
                }


            }
        }
    }
}