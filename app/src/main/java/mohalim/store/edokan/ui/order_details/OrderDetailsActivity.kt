package mohalim.store.edokan.ui.order_details

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import mohalim.store.edokan.R
import mohalim.store.edokan.core.utils.Constants

@AndroidEntryPoint
class OrderDetailsActivity : AppCompatActivity() {
    val viewModel: OrderViewModel by viewModels()
    val firebaseAuth = FirebaseAuth.getInstance()

    var orderId : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_details)

        if (!intent.hasExtra(Constants.constants.ORDER_ID)){
            finish()
            return
        }

        orderId = intent.getIntExtra(Constants.constants.ORDER_ID, 0)



    }

    override fun onResume() {
        super.onResume()

        firebaseAuth.currentUser?.getIdToken(false)?.addOnSuccessListener {
            viewModel.startGetOrderDetails(orderId, it.token.toString())
        }

    }
}