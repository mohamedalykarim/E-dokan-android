package mohalim.store.edokan.ui.main

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
import mohalim.store.edokan.databinding.DialogTechSupportBinding
import mohalim.store.edokan.databinding.RowSupportItemBinding

class TechSupportDialog : DialogFragment() {

    private lateinit var mainActivity: MainActivity
    private lateinit var binding : DialogTechSupportBinding
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var adapter: TechAdapter

    private val firebaseAuth : FirebaseAuth = FirebaseAuth.getInstance()
    private val preferenceHelper: IPreferenceHelper by lazy { PreferencesUtils(activity) }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_tech_support, container, false)

        mainActivity = activity as MainActivity
        firebaseAuth.currentUser?.getIdToken(false)?.addOnSuccessListener {
            mainActivity.viewModel.getSupportItems(
                    preferenceHelper.getUserId() + "",
                    it.token + ""
            )
        }

        initRecyclerView()

        binding.addNewSupportItemBtn.setOnClickListener {

            if (binding.techMessageET.text.toString() == ""){
                binding.techMessageET.error = "رجاء استيفاء مشكلتك بشكل واضح ومبسط واذكر كل التفاصيل"
                return@setOnClickListener
            }

            firebaseAuth.currentUser?.getIdToken(false)?.addOnSuccessListener {
                mainActivity.viewModel.addSupportItems(
                    preferenceHelper.getUserId() + "",
                    it.token + "",
                    binding.techMessageET.text.toString()
                    )

            }
        }

        binding.techBackImg.setOnClickListener {
            dismiss()
        }



        return binding.root

    }

    private fun initRecyclerView() {
        layoutManager = LinearLayoutManager(mainActivity)
        adapter = TechAdapter(mainActivity)
        binding.techItemsRV.layoutManager = layoutManager
        binding.techItemsRV.adapter = adapter
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(MATCH_PARENT,MATCH_PARENT)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.WHITE))
    }

    fun updateSupportItemsData(data: List<SupportItem>) {
        adapter.items.clear()
        adapter.items.addAll(data)
        adapter.notifyDataSetChanged()
    }

    fun addNewSupportItemToAdapter(data: SupportItem) {
        adapter.items.add(0,data)
        adapter.notifyDataSetChanged()
        binding.techMessageET.text.clear()
        Toast.makeText(context, "تم اضافة المشكلة بنجاح", Toast.LENGTH_LONG).show()
    }

    fun changeProgressBarVisibility(visibility: Int) {
        binding.progressBar.visibility = visibility
    }

    fun changeAddProgressBarVisibility(visibility: Int, btnEnable : Boolean, clearED : Boolean) {
        binding.addProgressBar.visibility = visibility
        binding.addProgressBarTv.visibility = visibility
        binding.addNewSupportItemBtn.isEnabled = btnEnable

        if (clearED){
            binding.techMessageET.text.clear()
        }

    }


    class TechAdapter (val mainActivity: MainActivity) : RecyclerView.Adapter<TechAdapter.TechViewHolder>(){
        var items : MutableList<SupportItem> = ArrayList()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TechViewHolder {
            val binding : RowSupportItemBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.row_support_item,
                    parent,
                    false
            )

            return TechViewHolder(binding, mainActivity)
        }

        override fun onBindViewHolder(holder: TechViewHolder, position: Int) {
            holder.bindItem(items[position])
        }

        override fun getItemCount(): Int {
            return items.size
        }


        class TechViewHolder(val binding: RowSupportItemBinding, val mainActivity: MainActivity) : RecyclerView.ViewHolder(binding.root){

            fun bindItem(supportItem : SupportItem){
                binding.dateTv.text = DateUtils.convertMilisToDate(supportItem.supportItemDate)
                binding.statustTv.text = OtherUtils.getSupportItemStatus(supportItem.supportItemStatus)
                binding.numberTv.text = supportItem.supportItemId.toString()
                binding.messageTv.text = supportItem.message

                binding.root.setOnClickListener{
                    mainActivity.showTechSupportMessageDialog(supportItem)
                }
            }
        }


    }
}