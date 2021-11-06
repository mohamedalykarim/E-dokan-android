package mohalim.store.edokan.ui.seller_products

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import mohalim.store.edokan.R
import mohalim.store.edokan.core.model.product.Product
import mohalim.store.edokan.core.utils.Constants
import mohalim.store.edokan.core.utils.loadPicture

@AndroidEntryPoint
class SellerProductsActivity : AppCompatActivity() {

    val viewModel : SellerProductsViewModel by viewModels()
    val firebaseAuth = FirebaseAuth.getInstance()

    var marketplaceId = 0

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
    }

    override fun onResume() {
        super.onResume()
        firebaseAuth.currentUser?.getIdToken(false)?.addOnSuccessListener {
            viewModel.getProducts(marketplaceId, it.token)
        }
    }


    @Composable
    fun MyApp(){
        Column {
            ProductItem()
            bottomLinksComponent()

        }
    }

    @Composable
    private fun bottomLinksComponent() {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp,8.dp,0.dp,0.dp)
        ) {
            Row (modifier = Modifier.height(IntrinsicSize.Min)) {
                /**
                 * Products button
                 */

                /**
                 * Products button
                 */

                OutlinedButton(
                    onClick = {
                    },
                    colors = ButtonDefaults.textButtonColors(
                        backgroundColor = Color.Red,
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize(1f)
                        .padding(8.dp, 0.dp,8.dp,0.dp)
                ){
                    Column (horizontalAlignment = Alignment.CenterHorizontally){
                        Image(
                            painter = painterResource(R.drawable.market_icon),
                            contentDescription = null,
                            modifier = Modifier.width(30.dp).height(30.dp),
                        )

                        Text(text = "Add new Product", textAlign = TextAlign.Center, color = Color.White)

                    }
                }


            }
        }
    }

    @Preview
    @Composable
    fun ProductItem(){
        val image = loadPicture(
            "https://images.unsplash.com/photo-1541963463532-d68292c34b19?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxleHBsb3JlLWZlZWR8Mnx8fGVufDB8fHx8&w=1000&q=80",
            R.drawable.market_icon
        ).value

        image?.let { img->
            Row (modifier = Modifier.height(116.dp)){
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
                        .clip(RoundedCornerShape(15.dp))
                )

                Column(modifier = Modifier.padding(8.dp, 8.dp, 8.dp, 8.dp)) {
                    Text(
                        text = "Product name here",
                        color = MaterialTheme.colors.primary,
                        modifier = Modifier
                            .weight(1f)
                    )
                    Text(
                        text = "Product description is here Product description is here Product description is here ",
                        color = MaterialTheme.colors.primary,
                        modifier = Modifier
                            .weight(1f)
                    )
                }
            }
        }

    }
}