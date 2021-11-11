package mohalim.store.edokan.ui.seller_products

import android.graphics.Color.parseColor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import mohalim.store.edokan.R
import mohalim.store.edokan.core.model.product.Product
import mohalim.store.edokan.core.utils.Constants
import mohalim.store.edokan.core.utils.DataState
import mohalim.store.edokan.core.utils.loadPicture

@AndroidEntryPoint
class SellerProductsActivity : AppCompatActivity() {

    val viewModel : SellerProductsViewModel by viewModels()
    val firebaseAuth = FirebaseAuth.getInstance()

    var marketplaceId = 0
    lateinit var sellerAddProductDialog : SellerAddProductDialog;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                MyApp()
            }
        }

        if (!intent.hasExtra(Constants.constants.MARKETPLACE_ID)){
            finish()
            return
        }

        marketplaceId = intent.getIntExtra(Constants.constants.MARKETPLACE_ID, 0)

        if (marketplaceId == 0){
            finish()
            return
        }

        sellerAddProductDialog = SellerAddProductDialog()

        subscribeObserver()
    }


    override fun onResume() {
        super.onResume()
        viewModel.products.clear()
        firebaseAuth.currentUser?.getIdToken(false)?.addOnSuccessListener {
            viewModel.getProducts(marketplaceId, it.token)
        }
    }


    private fun subscribeObserver() {
        /**
         * Observe products
         */
        viewModel.productsObserver.observe(this, {
            when(it){
                is DataState.Loading -> {}
                is DataState.Success -> {
                    Log.d("TAG", "subscribeObserver: "+it.data.size)
                    viewModel.products.addAll(it.data)
                }
                is DataState.Failure -> {}
            }
        })

        /**
         * Observe categories for add product
         */
        viewModel.categoriesObserver.observe(this, {
            when(it){
                is DataState.Loading->{}
                is DataState.Success->{
                    sellerAddProductDialog.addtoCategories(it.data)
                }
                is DataState.Failure->{}
            }
        })

    }

    @Composable
    fun MyApp(){
        Column(modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()

        ) {
            Scaffold(
                floatingActionButtonPosition = FabPosition.End,
                floatingActionButton = {
                    ExtendedFloatingActionButton(
                        onClick = {
                                  if (!sellerAddProductDialog.isAdded)
                                      sellerAddProductDialog.show(supportFragmentManager, "SellerAddProductDialog")
                        },
                        icon = {Icon(painter = painterResource(android.R.drawable.ic_input_add), contentDescription = null) },
                        text = {Text("Add")},
                        elevation = FloatingActionButtonDefaults.elevation(8.dp),
                        modifier = Modifier.align(Alignment.End),
                        contentColor = Color.White,
                        backgroundColor = Color(parseColor("#f6192a"))
                    )
                }

            ){
                Column(modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()) {
                    ProductsView(viewModel.products)
                }

            }



        }
    }

    @Composable
    private fun ProductsView(products : SnapshotStateList<Product>) {
        LazyColumn(
            modifier = Modifier
        ){
            Log.d("TAG", "ProductsView: "+ products.size)
            items(products){product->
                ProductItem(product)
            }
        }
    }

    @Composable
    fun ProductItem(product: Product) {
        val image = loadPicture(
            "https://images.unsplash.com/photo-1541963463532-d68292c34b19?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxleHBsb3JlLWZlZWR8Mnx8fGVufDB8fHx8&w=1000&q=80",
            R.drawable.market_icon
        ).value

        image?.let { img->
            Card(
                modifier = Modifier
                    .clip(MaterialTheme.shapes.small)
                    .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 4.dp),
                elevation = 2.dp

            ){
                Row (modifier = Modifier.height(IntrinsicSize.Min)){
                    // Left side of product
                    Column {
                        // Thump
                        Image(
                            bitmap = img.asImageBitmap(),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            alignment = Alignment.Center,
                            modifier = Modifier
                                .width(100.dp)
                                .height(100.dp)
                                .padding(8.dp, 8.dp, 0.dp, 0.dp)
                                .border(2.dp, Color.White)
                                .clip(RoundedCornerShape(30))
                        )

                        Column (
                            modifier = Modifier
                                .width(100.dp)
                                .height(50.dp)
                                .padding(start = 8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {

                            Row(){

                                Image(
                                    painter = painterResource(id = R.drawable.edit_icon),
                                    contentDescription = null,
                                    contentScale = ContentScale.Fit,
                                    modifier = Modifier
                                        .width(23.dp)
                                        .height(23.dp)
                                )

                                Spacer(modifier = Modifier.width(8.dp))


                                Image(
                                    painter = painterResource(id = R.drawable.edit_cost_icon),
                                    contentDescription = null,
                                    contentScale = ContentScale.Fit,
                                    modifier = Modifier
                                        .width(23.dp)
                                        .height(23.dp)

                                )






                            }
                        }


                    }


                    Column(modifier = Modifier.padding(8.dp, 8.dp, 8.dp, 8.dp)) {

                        // Product name
                        Text(
                            text = product.productName + "",
                            color = Color(parseColor("#f6192a")),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.ExtraBold,
                            modifier = Modifier.padding(start = 4.dp)
                        )

                        // Product Description
                        Text(
                            text = product.productDescription + "",
                            color = Color(parseColor("#313131")),
                            fontSize = 12.sp,
                            modifier = Modifier.padding(start = 4.dp)
                        )

                        Row{
                            /**
                             * Price and discount texts
                             */
                            Column() {
                                Row(modifier = Modifier.padding(start = 4.dp)){
                                    Text(
                                        text = "Price :",
                                        color = Color(parseColor("#313131")),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.ExtraBold
                                    )

                                    Text(
                                        text = String.format("%.2f", product.productPrice),
                                        fontSize = 12.sp,
                                        modifier = Modifier.padding(start = 4.dp)
                                    )
                                }
                                Row(modifier = Modifier.padding(start = 4.dp)){
                                    Text(
                                        text = "Discount :",
                                        color = Color(parseColor("#313131")),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.ExtraBold
                                    )

                                    Text(
                                        text = String.format("%.2f", product.productDiscount),
                                        fontSize = 12.sp,
                                        modifier = Modifier.padding(start = 4.dp)
                                    )
                                }
                            }

                            /**
                             * Edit and edit cost buttons
                             */


                        }




                    }
                }
            }

        }

    }
}