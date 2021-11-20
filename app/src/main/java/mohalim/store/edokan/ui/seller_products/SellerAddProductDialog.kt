package mohalim.store.edokan.ui.seller_products


import android.app.DatePickerDialog
import android.graphics.Color.parseColor
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.DialogFragment
import com.google.accompanist.flowlayout.FlowRow
import mohalim.store.edokan.R
import mohalim.store.edokan.core.model.category.Category
import java.util.*
import kotlin.collections.ArrayList
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Error
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import mohalim.store.edokan.core.utils.loadPicture


class SellerAddProductDialog : DialogFragment() {
    lateinit var sellerProductsActivity : SellerProductsActivity;
    private lateinit var activityResult : ActivityResultLauncher<String>
    val categories = mutableStateListOf<Category>()
    val images = mutableStateListOf<Uri>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                SellerProductDialogMainView()
            }

            activityResult = registerForActivityResult(ActivityResultContracts.GetMultipleContents()){uriGroup: List<Uri>? ->
                images.addAll(uriGroup!!)
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
            val productDescription = rememberSaveable { mutableStateOf("") }
            val productPrice = rememberSaveable { mutableStateOf("") }
            val productDiscount = rememberSaveable { mutableStateOf("") }
            val discountDate = remember{ mutableStateOf("00/00/00") }
            val productWeight = rememberSaveable { mutableStateOf("") }
            val productLength = rememberSaveable { mutableStateOf("") }
            val productWidth = rememberSaveable { mutableStateOf("") }
            val productHeight = rememberSaveable { mutableStateOf("") }
            val productQuantity = rememberSaveable { mutableStateOf("") }

            val categoriesIds = remember{ mutableStateListOf<Int>() }

            var productNameIsError by rememberSaveable { mutableStateOf(false) }
            var productDescriptionIsError by rememberSaveable { mutableStateOf(false) }
            var productPriceIsError by rememberSaveable { mutableStateOf(false) }
            var productDiscountIsError by rememberSaveable { mutableStateOf(false) }
            var discountDateIsError by rememberSaveable { mutableStateOf(false) }
            var productWeightIsError by rememberSaveable { mutableStateOf(false) }
            var productLengthIsError by rememberSaveable { mutableStateOf(false) }
            var productWidthIsError by rememberSaveable { mutableStateOf(false) }
            var productHeightIsError by rememberSaveable { mutableStateOf(false) }
            var productQuantityIsError by rememberSaveable { mutableStateOf(false) }

            Text(
                text = "Add new Product",
                fontSize = 22.sp,
                modifier = Modifier.padding(top = 32.dp, start = 8.dp))

            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .background(Color.White)
            ){
                // Product name
                ProductName(productName, productNameIsError){
                    productName.value = it
                    productNameIsError = it.length < 6
                }

                // Product description
                ProductDescription(productDescription, productDescriptionIsError){
                    productDescription.value = it
                    productDescriptionIsError = it.length < 6
                }


                // Product price
                ProductPrice(productPrice, productPriceIsError, ){
                    if (it.toDoubleOrNull() == null ){
                        productPrice.value = ""
                        productPriceIsError = true
                    }else{
                        productPrice.value = it
                        productPriceIsError = it.toDoubleOrNull() == 0.00
                    }
                }


                // Product discount
                ProductDiscount(productDiscount, discountDate, productDiscountIsError, discountDateIsError){
                    if (it.toDoubleOrNull() == null){
                        productDiscount.value = ""
                        productDiscountIsError = true
                    }else{
                        productDiscount.value = it
                        productDiscountIsError = false
                    }
                }


                // Product weight
                ProductWeight(productWeight, productWeightIsError){
                    if (it.toDoubleOrNull() == null){
                        productWeight.value = ""
                        productWeightIsError = true
                    }else{
                        productWeight.value = it
                        productWeightIsError = false
                    }
                }

                // Product Length
                ProductLength(productLength, productLengthIsError){
                    if (it.toDoubleOrNull() == null){
                        productLength.value = ""
                        productLengthIsError = true
                    }else{
                        productLength.value = it
                        productLengthIsError = false
                    }
                }


                // Product height
                ProductHeight(productHeight, productHeightIsError){
                    if (it.toDoubleOrNull() == null){
                        productHeight.value = ""
                        productHeightIsError = true
                    }else{
                        productHeight.value = it
                        productHeightIsError = false
                    }
                }


                // Product width
                ProductWidth(productWidth, productWidthIsError){
                    if (it.toDoubleOrNull() == null){
                        productWidth.value = ""
                        productWidthIsError = true
                    }else{
                        productWidth.value = it
                        productWidthIsError = false
                    }
                }

                // Product quantity
                ProductQuantityView(productQuantity, productQuantityIsError){
                    if (it.toDoubleOrNull() == null){
                        productQuantity.value = ""
                        productQuantityIsError = true
                    }else{
                        productQuantity.value = it
                        productQuantityIsError = false
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))


                // Images picker
                ImageViewSection()

                Spacer(modifier = Modifier.height(8.dp))

                // Categories section
               CategoriesSectionView(categoriesIds)
            }


            /**
             * Add product
             */

            Button(onClick = {

                },
                colors = ButtonDefaults.textButtonColors(Color(parseColor("#f6192a"))),
                modifier = Modifier.padding(top = 8.dp)) {
                Text("Add new product", color = Color.White)
            }

            Spacer(modifier = Modifier
                .height(16.dp)
                .fillMaxWidth())



        }
    }


    /**
     * Product name Text Field
     */
    @Composable
    private fun ProductName(
        productName: MutableState<String>,
        productNameIsError: Boolean,
        onValueChange : (value :  String)->Unit
    ) {
        Row{
            Spacer(modifier = Modifier.width(32.dp))
            OutlinedTextField(
                value = productName.value,
                onValueChange = onValueChange,
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color(parseColor("#F5F5F5")),
                    textColor = Color(parseColor("#313131")),
                    focusedIndicatorColor = Color(parseColor("#f78f91")),
                    unfocusedIndicatorColor = Color(parseColor("#EBBCBC")),
                    focusedLabelColor = Color(parseColor("#f6192a")),
                ),
                trailingIcon = {
                    if (productNameIsError)
                        Icon(Icons.Filled.Error,"error", tint = MaterialTheme.colors.error)
                    else
                        if (productName.value != "")
                            Icon(Icons.Filled.Done,"Done", tint = Color(parseColor("#176300")))

                },
                label = { Text(text = "Product name")},
                modifier = Modifier
                    .padding(start = 8.dp, end = 8.dp, top = 8.dp)
                    .weight(1f),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                singleLine = true
            )
            Spacer(modifier = Modifier.width(32.dp))
        }
        if (productNameIsError) {
            Text(
                text = resources.getString(R.string.product_name_error),
                color = MaterialTheme.colors.error,
                style = MaterialTheme.typography.caption,
                modifier = Modifier.padding(start = 40.dp)
            )
        }
    }

    /**
     * Product Description Text Field
     */
    @Composable
    private fun ProductDescription(
        productDescription: MutableState<String>,
        productDescriptionIsError: Boolean,
        onValueChange: (value: String) -> Unit
    ) {
        Row {
            Spacer(modifier = Modifier.width(32.dp))
            OutlinedTextField(
                value = productDescription.value,
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color(parseColor("#F5F5F5")),
                    textColor = Color(parseColor("#313131")),
                    focusedIndicatorColor = Color(parseColor("#f78f91")),
                    unfocusedIndicatorColor = Color(parseColor("#EBBCBC")),
                    focusedLabelColor = Color(parseColor("#f6192a")),
                ),
                trailingIcon = {
                    if (productDescriptionIsError)
                        Icon(Icons.Filled.Error,"error", tint = MaterialTheme.colors.error)
                    else
                        if (productDescription.value != "")
                            Icon(Icons.Filled.Done,"Done", tint = Color(parseColor("#176300")))

                },
                onValueChange = onValueChange,
                label = { Text(text = "Product Description")},
                modifier = Modifier
                    .padding(start = 8.dp, end = 8.dp, top = 8.dp)
                    .weight(1f)
            )
            Spacer(modifier = Modifier.width(32.dp))
        }
        if (productDescriptionIsError) {
            Text(
                text = resources.getString(R.string.product_name_error),
                color = MaterialTheme.colors.error,
                style = MaterialTheme.typography.caption,
                modifier = Modifier.padding(start = 40.dp)
            )
        }

    }

    /**
     * Product price Text Field
     */
    @Composable
    private fun ProductPrice(
        productPrice: MutableState<String>,
        productPriceIsError: Boolean,
        onValueChange: (value: String) -> Unit
    ) {
        Row {
            Spacer(modifier = Modifier.width(32.dp))
            OutlinedTextField(
                value = productPrice.value,
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color(parseColor("#F5F5F5")),
                    textColor = Color(parseColor("#313131")),
                    focusedIndicatorColor = Color(parseColor("#f78f91")),
                    unfocusedIndicatorColor = Color(parseColor("#EBBCBC")),
                    focusedLabelColor = Color(parseColor("#f6192a")),
                ),
                trailingIcon = {
                    if (productPriceIsError)
                        Icon(Icons.Filled.Error,"error", tint = MaterialTheme.colors.error)
                    else
                        if (productPrice.value != "")
                            Icon(Icons.Filled.Done,"Done", tint = Color(parseColor("#176300")))

                },
                onValueChange =onValueChange,
                label = { Text(text = "Product price")},
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .padding(start = 8.dp, end = 8.dp, top = 8.dp)
                    .weight(1f),
                singleLine = true
            )
            Spacer(modifier = Modifier.width(32.dp))
        }
        if (productPriceIsError) {
            Text(
                text = resources.getString(R.string.product_name_error),
                color = MaterialTheme.colors.error,
                style = MaterialTheme.typography.caption,
                modifier = Modifier.padding(start = 40.dp)
            )
        }
    }

    /**
     * Discount Text Field
     */
    @Composable
    private fun ProductDiscount(
        productDiscount: MutableState<String>,
        discountDate: MutableState<String>,
        productDiscountIsError: Boolean,
        discountDateIsError: Boolean,
        onValueChange: (value: String) -> Unit
    ) {

        Row {
            Spacer(modifier = Modifier.width(32.dp))
            OutlinedTextField(
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color(parseColor("#F5F5F5")),
                    textColor = Color(parseColor("#313131")),
                    focusedIndicatorColor = Color(parseColor("#f78f91")),
                    unfocusedIndicatorColor = Color(parseColor("#EBBCBC")),
                    focusedLabelColor = Color(parseColor("#f6192a")),
                ),
                trailingIcon = {
                    if (productDiscountIsError)
                        Icon(Icons.Filled.Error,"error", tint = MaterialTheme.colors.error)
                    else
                        if (productDiscount.value != "")
                            Icon(Icons.Filled.Done,"Done", tint = Color(parseColor("#176300")))

                },
                value = productDiscount.value,
                onValueChange = onValueChange,
                label = { Text(text = "Product Discount")},
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .padding(start = 8.dp, end = 1.dp, top = 8.dp)
                    .weight(1f),
                singleLine = true
            )

            ShowDatePicker(discountDate)

            Spacer(modifier = Modifier.width(32.dp))
        }
        if (productDiscountIsError) {
            Text(
                text = resources.getString(R.string.product_name_error),
                color = MaterialTheme.colors.error,
                style = MaterialTheme.typography.caption,
                modifier = Modifier.padding(start = 40.dp)
            )
        }

        if (discountDateIsError) {
            Text(
                text = resources.getString(R.string.product_name_error),
                color = MaterialTheme.colors.error,
                style = MaterialTheme.typography.caption,
                modifier = Modifier.padding(start = 40.dp)
            )
        }
    }

    /**
     * Discount Date Picker
     */
    @Composable
    private fun ShowDatePicker(date: MutableState<String>) {
        val year : Int
        val month : Int
        val day : Int

        val calendar = Calendar.getInstance()
        year = calendar.get(Calendar.YEAR)
        month = calendar.get(Calendar.MONTH)
        day = calendar.get(Calendar.DAY_OF_MONTH)
        calendar.time = Date()

        val datePickerDialog = DatePickerDialog(
            sellerProductsActivity,
            {_: DatePicker, year : Int, month : Int, dayOfMonth : Int ->
                date.value = "$dayOfMonth/$month/$year"
            }, year, month, day
        )

        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(start = 4.dp, end = 8.dp, top = 20.dp)
        ) {
            Button(
                colors = ButtonDefaults.buttonColors(Color(parseColor("#f6192a"))),
                onClick = { datePickerDialog.show() }) {
                Row{
                    Image(
                        painter = painterResource(R.drawable.market_icon),
                        contentDescription = null,
                        modifier = Modifier
                            .width(30.dp)
                            .height(30.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(text= "Date",  color = Color(parseColor("#dadada")))
                        Text(text = date.value, color = Color(parseColor("#ffffff")))
                    }

                }
            }
        }


    }


    /**
     * Product Weight Text Field
     */
    @Composable
    private fun ProductWeight(
        productWeight: MutableState<String>,
        productWeightIsError: Boolean,
        onValueChange: (value: String) -> Unit
    ) {
        Row {
            Spacer(modifier = Modifier.width(32.dp))
            OutlinedTextField(
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color(parseColor("#F5F5F5")),
                    textColor = Color(parseColor("#313131")),
                    focusedIndicatorColor = Color(parseColor("#f78f91")),
                    unfocusedIndicatorColor = Color(parseColor("#EBBCBC")),
                    focusedLabelColor = Color(parseColor("#f6192a")),
                ),
                trailingIcon = {
                    if (productWeightIsError)
                        Icon(Icons.Filled.Error,"error", tint = MaterialTheme.colors.error)
                    else
                        if (productWeight.value != "")
                            Icon(Icons.Filled.Done,"Done", tint = Color(parseColor("#176300")))

                },
                value = productWeight.value,
                onValueChange = onValueChange,
                label = { Text(text = "Product Weight")},
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .padding(start = 8.dp, end = 8.dp, top = 8.dp)
                    .weight(1f),
                singleLine = true
            )
            Spacer(modifier = Modifier.width(32.dp))
        }
        if (productWeightIsError) {
            Text(
                text = resources.getString(R.string.product_name_error),
                color = MaterialTheme.colors.error,
                style = MaterialTheme.typography.caption,
                modifier = Modifier.padding(start = 40.dp)
            )
        }
    }

    /**
     * Product Length Text Field
     */
    @Composable
    private fun ProductLength(
        productLength: MutableState<String>,
        productLengthIsError: Boolean,
        onValueChange: (value: String) -> Unit
    ){
        Row {
            Spacer(modifier = Modifier.width(32.dp))
            OutlinedTextField(
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color(parseColor("#F5F5F5")),
                    textColor = Color(parseColor("#313131")),
                    focusedIndicatorColor = Color(parseColor("#f78f91")),
                    unfocusedIndicatorColor = Color(parseColor("#EBBCBC")),
                    focusedLabelColor = Color(parseColor("#f6192a")),
                ),
                trailingIcon = {
                    if (productLengthIsError)
                        Icon(Icons.Filled.Error,"error", tint = MaterialTheme.colors.error)
                    else
                        if (productLength.value != "")
                            Icon(Icons.Filled.Done,"Done", tint = Color(parseColor("#176300")))

                },
                value = productLength.value,
                onValueChange = onValueChange,
                label = { Text(text = "Product Length")},
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .padding(start = 8.dp, end = 8.dp, top = 8.dp)
                    .weight(1f),
                singleLine = true
            )
            Spacer(modifier = Modifier.width(32.dp))
        }
        if (productLengthIsError) {
            Text(
                text = resources.getString(R.string.product_name_error),
                color = MaterialTheme.colors.error,
                style = MaterialTheme.typography.caption,
                modifier = Modifier.padding(start = 40.dp)
            )
        }

    }

    /**
     * Product Height Text Field
     */
    @Composable
    private fun ProductHeight(
        productHeight: MutableState<String>,
        productHeightIsError: Boolean,
        onValueChange: (value: String) -> Unit
    ) {

        Row {
            Spacer(modifier = Modifier.width(32.dp))
            OutlinedTextField(
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color(parseColor("#F5F5F5")),
                    textColor = Color(parseColor("#313131")),
                    focusedIndicatorColor = Color(parseColor("#f78f91")),
                    unfocusedIndicatorColor = Color(parseColor("#EBBCBC")),
                    focusedLabelColor = Color(parseColor("#f6192a")),
                ),
                trailingIcon = {
                    if (productHeightIsError)
                        Icon(Icons.Filled.Error,"error", tint = MaterialTheme.colors.error)
                    else
                        if (productHeight.value != "")
                            Icon(Icons.Filled.Done,"Done", tint = Color(parseColor("#176300")))

                },
                value = productHeight.value,
                onValueChange = onValueChange,
                label = { Text(text = "Product Height")},
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .padding(start = 8.dp, end = 8.dp, top = 8.dp)
                    .weight(1f),
                singleLine = true
            )
            Spacer(modifier = Modifier.width(32.dp))
        }
        if (productHeightIsError) {
            Text(
                text = resources.getString(R.string.product_name_error),
                color = MaterialTheme.colors.error,
                style = MaterialTheme.typography.caption,
                modifier = Modifier.padding(start = 40.dp)
            )
        }

    }

    /**
     * Product Width Text Field
     */
    @Composable
    private fun ProductWidth(
        productWidth: MutableState<String>,
        productWidthIsError: Boolean,
        onValueChange: (value: String) -> Unit
    ) {
        Row {
            Spacer(modifier = Modifier.width(32.dp))
            OutlinedTextField(
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color(parseColor("#F5F5F5")),
                    textColor = Color(parseColor("#313131")),
                    focusedIndicatorColor = Color(parseColor("#f78f91")),
                    unfocusedIndicatorColor = Color(parseColor("#EBBCBC")),
                    focusedLabelColor = Color(parseColor("#f6192a")),
                ),
                trailingIcon = {
                    if (productWidthIsError)
                        Icon(Icons.Filled.Error,"error", tint = MaterialTheme.colors.error)
                    else
                        if (productWidth.value != "")
                            Icon(Icons.Filled.Done,"Done", tint = Color(parseColor("#176300")))

                },
                value = productWidth.value,
                onValueChange = onValueChange,
                label = { Text(text = "Product Width")},
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .padding(start = 8.dp, end = 8.dp, top = 8.dp)
                    .weight(1f),
                singleLine = true
            )
            Spacer(modifier = Modifier.width(32.dp))
        }
        if (productWidthIsError) {
            Text(
                text = resources.getString(R.string.product_name_error),
                color = MaterialTheme.colors.error,
                style = MaterialTheme.typography.caption,
                modifier = Modifier.padding(start = 40.dp)
            )
        }
    }

    /**
     * Product Quantity Text field View
     */
    @Composable
    private fun ProductQuantityView(
        productQuantity: MutableState<String>,
        productQuantityIsError: Boolean,
        onValueChange: (value: String) -> Unit
    ) {
        Row {
            Spacer(modifier = Modifier.width(32.dp))
            OutlinedTextField(
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color(parseColor("#F5F5F5")),
                    textColor = Color(parseColor("#313131")),
                    focusedIndicatorColor = Color(parseColor("#f78f91")),
                    unfocusedIndicatorColor = Color(parseColor("#EBBCBC")),
                    focusedLabelColor = Color(parseColor("#f6192a")),
                ),
                trailingIcon = {
                    if (productQuantityIsError)
                        Icon(Icons.Filled.Error,"error", tint = MaterialTheme.colors.error)
                    else
                        if (productQuantity.value != "")
                        Icon(Icons.Filled.Done,"Done", tint = Color(parseColor("#176300")))
                },
                value = productQuantity.value,
                onValueChange = onValueChange,
                label = { Text(text = "Product Quantity", color = Color(parseColor("#313131")))},
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .padding(start = 8.dp, end = 8.dp, top = 8.dp)
                    .weight(1f),
                singleLine = true
            )
            Spacer(modifier = Modifier.width(32.dp))
        }
        if (productQuantityIsError) {
            Text(
                text = resources.getString(R.string.product_name_error),
                color = MaterialTheme.colors.error,
                style = MaterialTheme.typography.caption,
                modifier = Modifier.padding(start = 40.dp)
            )
        }
    }


    /**
     * Image View Section
     */
    private @Composable
    fun ImageViewSection() {
        Text(text = "Images : ", modifier = Modifier.padding(start = 40.dp, top = 16.dp))
        Spacer(modifier = Modifier.height(8.dp))
        Row {
            Spacer(modifier = Modifier.width(32.dp))

            val pickVisible = remember { mutableStateOf(false)}


            // Main button
            Button(
                onClick = {
                    pickVisible.value = !pickVisible.value
                },
                modifier = Modifier.padding(start = 8.dp)
            ){
                Image(painter = painterResource(id = R.drawable.ic_image), contentDescription = null)
            }


            // Pick form gallery Button
            if(pickVisible.value){
                Button(
                    onClick = {
                        images.clear()
                        activityResult.launch("image/*")
                    },
                    modifier = Modifier.padding(start = 2.dp)
                ){
                    Image(painter = painterResource(id = R.drawable.ic_camera), contentDescription = null)
                    Text("gallery")
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Spacer(modifier = Modifier.width(32.dp))
        }

        // Images container
        FlowRow (modifier = Modifier.padding(start = 40.dp, end = 40.dp)){
            images.forEach {
                val image = loadPicture(
                    it.toString(),
                    R.drawable.market_icon
                ).value

                image?.let { img->
                    Image(
                        bitmap = img.asImageBitmap(),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        alignment = Alignment.Center,
                        modifier = Modifier
                            .width(100.dp)
                            .height(100.dp)
                            .padding(top = 8.dp, end = 8.dp, bottom = 0.dp)
                            .clip(RoundedCornerShape(20))
                    )
                }
            }
        }
    }


    /**
     * Categories View
     */

    private @Composable
    fun CategoriesSectionView(categoriesIds: SnapshotStateList<Int>) {
        Row {
            Spacer(modifier = Modifier.width(32.dp))
            Column (
                modifier = Modifier
                    .weight(1f)
            ){
                Text(text = "Categories : ", modifier = Modifier.padding(start = 16.dp, top = 16.dp))
                Spacer(modifier = Modifier.height(4.dp))
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
                Spacer(modifier = Modifier.height(16.dp))
            }
            Spacer(modifier = Modifier.width(32.dp))

        }
    }


    @Composable
    fun CategoriesView(
        categories: SnapshotStateList<Category>,
        categoriesIds: SnapshotStateList<Int>,
        onCategoryAdded: (id : Int) ->Unit
    ) {

        FlowRow(modifier = Modifier.padding(start = 8.dp)) {
            if (categories.size == 0){

            }else{

                categories.forEach{ category->

                    Log.d("TAG", "CategoriesView: test")

                    val buttonBackground by animateColorAsState(targetValue =
                        if (categoriesIds.any { it == category.categoryId }) Color(parseColor("#f6192a"))
                        else Color(parseColor("#dadada"))
                    )

                    val textColor by animateColorAsState(targetValue =
                        if (categoriesIds.any { it == category.categoryId }) Color(parseColor("#dadada"))
                        else Color(parseColor("#f6192a"))
                    )


                    OutlinedButton(
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = buttonBackground,
                        ),
                        modifier = Modifier
                            .padding(start = 4.dp, top = 4.dp)
                            .clip(
                                RoundedCornerShape(
                                    topStart = 0.dp,
                                    topEnd = 35.dp,
                                    bottomStart = 35.dp,
                                    bottomEnd = 0.dp
                                )
                            ),
                        onClick = {
                            onCategoryAdded(category.categoryId)
                        }
                    ) {
                        // Category text
                        Text(
                            text= category.categoryName,
                            color = textColor,
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

