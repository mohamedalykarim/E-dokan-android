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
import mohalim.store.edokan.databinding.DialogTechSupportBinding
import mohalim.store.edokan.databinding.DialogTechSupportMessagesBinding
import mohalim.store.edokan.databinding.RowSupportItemBinding
import mohalim.store.edokan.databinding.RowSupportMessageItemBinding

class TechSupportMessagesDialog : DialogFragment() {

    private lateinit var mainActivity: MainActivity
    private lateinit var binding : DialogTechSupportMessagesBinding
    private lateinit var layoutManager: LinearLayoutManager

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
        firebaseAuth.currentUser?.getIdToken(false)?.addOnSuccessListener {
            mainActivity.viewModel.getSupportItems(
                    preferenceHelper.getUserId() + "",
                    it.token + ""
            )
        }

        return binding.root
    }



    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(MATCH_PARENT,MATCH_PARENT)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.WHITE))
    }


   class TechMessageAdapter : RecyclerView.Adapter<TechMessageAdapter.TechMessageViewHolder>(){
        var items : MutableList<SupportItem> = ArrayList()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TechMessageViewHolder {
            val binding : RowSupportMessageItemBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.row_support_message_item,
                parent,
                false
            )
            return TechMessageViewHolder(binding)
        }

        override fun onBindViewHolder(holder: TechMessageViewHolder, position: Int) {
            holder.bindItem(items[position])
        }

        override fun getItemCount(): Int {
            return items.size
        }


        class TechMessageViewHolder(val binding: RowSupportMessageItemBinding) : RecyclerView.ViewHolder(binding.root){
            fun bindItem(supportItem : SupportItem){
            }
        }


    }
}