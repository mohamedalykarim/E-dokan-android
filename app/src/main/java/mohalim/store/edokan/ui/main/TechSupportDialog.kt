package mohalim.store.edokan.ui.main

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.Window
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.google.firebase.auth.FirebaseAuth
import mohalim.store.edokan.R
import mohalim.store.edokan.core.model.support_item.SupportItem
import mohalim.store.edokan.core.utils.IPreferenceHelper
import mohalim.store.edokan.core.utils.PreferencesUtils
import mohalim.store.edokan.databinding.DialogTechSupportBinding

class TechSupportDialog : DialogFragment() {
    private val TAG = "TechSupportDialog"

    lateinit var mainActivity: MainActivity;
    val firebaseAuth : FirebaseAuth = FirebaseAuth.getInstance();

    val preferenceHelper: IPreferenceHelper by lazy { PreferencesUtils(activity) }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val binding : DialogTechSupportBinding = DataBindingUtil.inflate(inflater, R.layout.dialog_tech_support, container, false)

        mainActivity = activity as MainActivity
        firebaseAuth.currentUser?.getIdToken(false)?.addOnSuccessListener {
            mainActivity.viewmodel.getSupportItems(
                    preferenceHelper.getUserId() + "",
                    it.token + ""
            )
        }



        binding.techBackImg.setOnClickListener {
            dismiss()
        }

        return binding.root

    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(MATCH_PARENT,MATCH_PARENT)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.WHITE))


    }

    fun updateSupportItemsData(data: List<SupportItem>) {
        Log.d(TAG, "updateSupportItemsData: "+ data.size)
    }
}