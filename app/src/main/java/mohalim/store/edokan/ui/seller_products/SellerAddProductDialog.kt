package mohalim.store.edokan.ui.seller_products


import android.graphics.Color.parseColor
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.google.accompanist.flowlayout.FlowColumn
import com.google.accompanist.flowlayout.FlowRow
import mohalim.store.edokan.R
import mohalim.store.edokan.core.model.category.Category

class SellerAddProductDialog : DialogFragment() {
    lateinit var sellerProductsActivity : SellerProductsActivity;
    val categories = mutableStateListOf<Category>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                SellerProductDialogMainView()
            }

            sellerProductsActivity = activity as SellerProductsActivity
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        dialog?.window?.setBackgroundDrawable(ColorDrawable(android.graphics.Color.WHITE))
    }

    override fun onResume() {
        super.onResume()
        sellerProductsActivity.viewModel.startGetMainCategory()

    }

    fun addtoCategories(newCategories: List<Category>) {
        val allCategories = ArrayList<Category>()
        allCategories.addAll(categories)
        allCategories.addAll(newCategories)

        categories.clear()
        categories.addAll(allCategories.distinct())
    }



    @Composable
    fun SellerProductDialogMainView(){
        val scrollState = rememberScrollState()

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ){
            val productName = rememberSaveable { mutableStateOf("") }
            val productDiscription = rememberSaveable { mutableStateOf("") }
            val productPrice = rememberSaveable { mutableStateOf("") }
            val productDiscount = rememberSaveable { mutableStateOf("") }
            val productWeight = rememberSaveable { mutableStateOf("") }
            val productLength = rememberSaveable { mutableStateOf("") }
            val productWidth = rememberSaveable { mutableStateOf("") }
            val productHeight = rememberSaveable { mutableStateOf("") }
            val productQuantity = rememberSaveable { mutableStateOf("") }
            val categoriesIds = remember{ mutableStateListOf<Int>() }

            Text(
                text = "Add new Product",
                fontSize = 22.sp,
                modifier = Modifier.padding(top = 32.dp, start = 8.dp))

            Row {
                Spacer(modifier = Modifier
                    .width(8.dp)
                    .fillMaxHeight())

                Column(modifier = Modifier
                    .background(Color.White)
                    .weight(1f)
                ){

                    OutlinedTextField(
                        value = productName.value,
                        onValueChange ={
                            productName.value = it
                        },
                        label = { Text(text = "Product name")},
                        modifier = Modifier.padding(start = 8.dp, end = 8.dp, top = 8.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = productDiscription.value,
                        onValueChange ={ productDiscription.value = it},
                        label = { Text(text = "Product Description")},
                        modifier = Modifier.padding(start = 8.dp, end = 8.dp, top = 8.dp)
                    )

                    OutlinedTextField(
                        value = productPrice.value,
                        onValueChange ={
                            if (it.toDoubleOrNull() == null){
                                productPrice.value = ""
                            }else{
                                productPrice.value = it
                            }
                        },
                        label = { Text(text = "Product price")},
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.padding(start = 8.dp, end = 8.dp, top = 8.dp),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = productDiscount.value,
                        onValueChange ={
                            if (it.toDoubleOrNull() == null){
                                productDiscount.value = ""
                            }else{
                                productDiscount.value = it
                            }                },
                        label = { Text(text = "Product Discount")},
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.padding(start = 8.dp, end = 8.dp, top = 8.dp),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = productWeight.value,
                        onValueChange ={
                            if (it.toDoubleOrNull() == null){
                                productWeight.value = ""
                            }else{
                                productWeight.value = it
                            }                },
                        label = { Text(text = "Product Weight")},
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.padding(start = 8.dp, end = 8.dp, top = 8.dp),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = productLength.value,
                        onValueChange ={
                            if (it.toDoubleOrNull() == null){
                                productLength.value = ""
                            }else{
                                productLength.value = it
                            }
                        },
                        label = { Text(text = "Product Length")},
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.padding(start = 8.dp, end = 8.dp, top = 8.dp),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = productHeight.value,
                        onValueChange ={
                            if (it.toDoubleOrNull() == null){
                                productHeight.value = ""
                            }else{
                                productHeight.value = it
                            }
                        },
                        label = { Text(text = "Product Height")},
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.padding(start = 8.dp, end = 8.dp, top = 8.dp),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = productWidth.value,
                        onValueChange ={
                            if (it.toDoubleOrNull() == null){
                                productWidth.value = ""
                            }else{
                                productWidth.value = it
                            }
                        },
                        label = { Text(text = "Product Width")},
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.padding(start = 8.dp, end = 8.dp, top = 8.dp),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = productQuantity.value,
                        onValueChange ={
                            if (it.toDoubleOrNull() == null){
                                productQuantity.value = ""
                            }else{
                                productQuantity.value = it
                            }
                        },
                        label = { Text(text = "Product Quantity")},
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.padding(start = 8.dp, end = 8.dp, top = 8.dp),
                        singleLine = true
                    )



                }

               Column(
                   horizontalAlignment = Alignment.CenterHorizontally,
                   modifier = Modifier
                       .width(96.dp)
                       .padding(top = 8.dp)
                       .background(Color.White)
               ) {
                   Image(
                       painter = painterResource(id = R.drawable.upload_image),
                       contentDescription = null,
                       modifier = Modifier
                           .width(80.dp)
                           .height(80.dp)
                           .background(Color(parseColor("#FFF7F4F4")))
                           .clip(MaterialTheme.shapes.medium)
                           .padding(end = 8.dp)

                   )

                   Spacer(modifier = Modifier.height(8.dp))

                   Image(
                       painter = painterResource(id = R.drawable.upload_image),
                       contentDescription = null,
                       modifier = Modifier
                           .width(80.dp)
                           .height(80.dp)
                           .background(Color(parseColor("#FFF7F4F4")))
                           .clip(MaterialTheme.shapes.medium)
                           .padding(end = 8.dp)

                   )

                   Spacer(modifier = Modifier.height(8.dp))

                   Image(
                       painter = painterResource(id = R.drawable.upload_image),
                       contentDescription = null,
                       modifier = Modifier
                           .width(80.dp)
                           .height(80.dp)
                           .background(Color(parseColor("#FFF7F4F4")))
                           .clip(MaterialTheme.shapes.medium)
                           .padding(end = 8.dp)
                   )


               }
                
                Spacer(modifier = Modifier
                    .width(8.dp)
                    .fillMaxHeight())


            }
            
            Spacer(modifier = Modifier.height(8.dp))

            Row(modifier = Modifier.padding(vertical = 8.dp)){

                Spacer(modifier = Modifier.width(16.dp))

                Column (
                    modifier = Modifier
                        .border(
                            width = 1.dp,
                            color = Color(parseColor("#f6192a")),
                            shape = RoundedCornerShape(25.dp)
                        )
                        .fillMaxSize()
                        .weight(1f)
                ){
                    Text(text = "Categories : ", modifier = Modifier.padding(start = 8.dp, top = 8.dp))

                    CategoriesView(categories, categoriesIds) { categoryId ->
                        val isExists = categoriesIds.any { it == categoryId}
                        if (isExists){
                            categoriesIds.remove(categoryId)

                            val removeCategories = ArrayList<Category>()
                            val removeCategoryIds = ArrayList<Int>()

                            categories.forEach {category ->
                                if(category.categoryParent == categoryId){
                                    removeCategories.add(category)
                                    removeCategoryIds.add(category.categoryId)
                                }
                            }

                            categories.removeAll(removeCategories)
                            categoriesIds.removeAll(removeCategoryIds)

                        }else{
                            categoriesIds.add(categoryId)
                            sellerProductsActivity.viewModel.getCategoriesByParentId(categoryId)
                        }
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

            }




            Button(onClick = { /*TODO*/ },
                colors = ButtonDefaults.textButtonColors(Color(parseColor("#f6192a"))),
                modifier = Modifier.padding(top = 8.dp)) {
                Text("Add new product", color = Color.White)
            }



        }
    }

    @Composable
    fun CategoriesView(
        mutableStatecategories: SnapshotStateList<Category>,
        categoriesIds: SnapshotStateList<Int>,
        onCategoryAdded: (id : Int) ->Unit
    ) {
        val categories = mutableStatecategories

        Log.d("TAG", "CategoriesView: "+ categories.size)
        FlowRow(modifier = Modifier.padding(start = 8.dp)) {
            if (categories.size == 0){

            }else{

                categories.forEach{ category->

                    Log.d("TAG", "CategoriesView: test")

                    val buttonBackground = animateColorAsState(targetValue =
                        if (categoriesIds.any { it == category.categoryId })
                            Color(parseColor("#f6192a"))
                        else
                            Color(parseColor("#dadada"))
                    )


                    OutlinedButton(
                        colors = ButtonDefaults.buttonColors(backgroundColor = buttonBackground.value),
                        modifier = Modifier
                            .padding(start = 4.dp, top = 4.dp)
                            .clip(RoundedCornerShape(35.dp)),
                        onClick = {
                            onCategoryAdded(category.categoryId)
                        }
                    ) {

                        Text(
                            text= category.categoryName,
                            modifier = Modifier
                                .padding(4.dp)
                                .clip(RoundedCornerShape(20.dp))
                        )
                    }


                }
            }
        }
    }


}

