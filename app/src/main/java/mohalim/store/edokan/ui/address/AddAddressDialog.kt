package mohalim.store.edokan.ui.address

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.Window
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import mohalim.store.edokan.R
import mohalim.store.edokan.core.model.support_item.SupportItem
import mohalim.store.edokan.core.utils.DateUtils
import mohalim.store.edokan.core.utils.IPreferenceHelper
import mohalim.store.edokan.core.utils.OtherUtils
import mohalim.store.edokan.core.utils.PreferencesUtils
import mohalim.store.edokan.databinding.DialogAddAddressBinding
import mohalim.store.edokan.databinding.DialogTechSupportBinding
import mohalim.store.edokan.databinding.RowSupportItemBinding

class AddAddressDialog : DialogFragment() {
    private lateinit var binding : DialogAddAddressBinding;
    private lateinit var layoutManager: LinearLayoutManager

    private val firebaseAuth : FirebaseAuth = FirebaseAuth.getInstance()
    private val preferenceHelper: IPreferenceHelper by lazy { PreferencesUtils(activity) }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_add_address, container, false)



        return binding.root

    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(MATCH_PARENT,MATCH_PARENT)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.WHITE))
    }


}