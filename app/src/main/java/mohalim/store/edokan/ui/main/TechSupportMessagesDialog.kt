package mohalim.store.edokan.ui.main

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
import mohalim.store.edokan.core.model.support_item_messsage.SupportItemMessage
import mohalim.store.edokan.core.utils.DateUtils
import mohalim.store.edokan.core.utils.IPreferenceHelper
import mohalim.store.edokan.core.utils.OtherUtils
import mohalim.store.edokan.core.utils.PreferencesUtils
import mohalim.store.edokan.databinding.DialogTechSupportMessagesBinding
import java.text.SimpleDateFormat
import java.util.*

class TechSupportMessagesDialog : DialogFragment() {

    private lateinit var mainActivity: MainActivity
    private lateinit var binding : DialogTechSupportMessagesBinding

    lateinit var supportItem : SupportItem

    private val firebaseAuth : FirebaseAuth = FirebaseAuth.getInstance()
    private val preferenceHelper: IPreferenceHelper by lazy { PreferencesUtils(activity) }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_tech_support_messages, container, false)

        mainActivity = activity as MainActivity

        binding.techMessageBackImg.setOnClickListener {
            dismiss()
        }

        binding.dateTv.text = DateUtils.convertMilisToDate(supportItem.supportItemDate)
        binding.statustTv.text = OtherUtils.getSupportItemStatus(supportItem.supportItemStatus)
        binding.numberTv.text = supportItem.supportItemId.toString()
        binding.messageTv.text = supportItem.message

        firebaseAuth.currentUser?.getIdToken(false)?.addOnSuccessListener {
            mainActivity.viewModel.getSupportItemAllMessages(
                supportItem.supportItemId,
                it.token +""
            )
        }


        return binding.root
    }



    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(MATCH_PARENT,MATCH_PARENT)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.WHITE))
    }

    fun updateMessages(data: List<SupportItemMessage>) {

        data.forEach {
            if (it.senderId  != preferenceHelper.getUserId()){

                val sdf = SimpleDateFormat("yyyy/MM/dd - h:mm a", Locale.FRANCE)

                binding.responses.append("الدعم الفني [" +sdf.format(it.date)+"]\n")
                binding.responses.append(it.message + "\n\n")
            }
        }



    }


}