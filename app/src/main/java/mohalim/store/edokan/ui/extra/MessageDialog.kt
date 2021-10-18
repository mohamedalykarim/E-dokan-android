package mohalim.store.edokan.ui.extra

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import dagger.hilt.android.AndroidEntryPoint
import mohalim.store.edokan.R
import mohalim.store.edokan.core.utils.Constants
import mohalim.store.edokan.databinding.AlertMessageBinding
import mohalim.store.edokan.ui.address.AddressActivity
import mohalim.store.edokan.ui.main.MainActivity
import kotlin.math.log

@AndroidEntryPoint
class MessageDialog() : DialogFragment() {
    private var style: Int = Constants.constants.MESSAGE_DIALOG_DEFAULT_STYLE
    lateinit var binding : AlertMessageBinding

    lateinit var mainActivity: MainActivity


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.alert_message, container, false)

        if (style == Constants.constants.MESSAGE_DIALOG_ADD_ADDRESS_STYLE){
            binding.messageTextTv.text = "You have not any address please add one"
            binding.btn.text = "Add new Address"
            binding.btn.setOnClickListener {

                val intent = Intent(activity , AddressActivity::class.java)
                startActivity(intent)

            }
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#33000000")))
    }

    fun setStyle( style : Int) {
        this.style = style
    }


}