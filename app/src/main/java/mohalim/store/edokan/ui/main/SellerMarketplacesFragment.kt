package mohalim.store.edokan.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import mohalim.store.edokan.R
import mohalim.store.edokan.core.model.city.City
import mohalim.store.edokan.core.model.marketplace.MarketPlace
import mohalim.store.edokan.core.utils.Constants
import mohalim.store.edokan.core.utils.IPreferenceHelper
import mohalim.store.edokan.core.utils.PreferencesUtils
import mohalim.store.edokan.databinding.FragmentCityBinding
import mohalim.store.edokan.databinding.FragmentSellerMarketplacesBinding
import mohalim.store.edokan.databinding.RowCityBinding
import mohalim.store.edokan.databinding.RowMarketplaceBinding
import mohalim.store.edokan.ui.seller_main.SellerMainActivity

class SellerMarketplacesFragment : Fragment() {

    lateinit var binding : FragmentSellerMarketplacesBinding
    lateinit var layoutManager: LinearLayoutManager;
    lateinit var marketplacesAdapter: MarketplacesAdapter
    lateinit var mainActivity : MainActivity

    private val preferenceHelper: IPreferenceHelper by lazy { PreferencesUtils(mainActivity) }

    private val firebaseAuth : FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_seller_marketplaces,
            container,
            false
        )

         mainActivity = activity as MainActivity


        initCityRV()


        return binding.root
    }

    override fun onResume() {
        super.onResume()

        firebaseAuth.currentUser?.getIdToken(false)?.addOnSuccessListener {
            mainActivity.viewModel.getSellerMarketplaces(it.token.toString())
        }

    }

    private fun initCityRV() {
        layoutManager = LinearLayoutManager(activity)
        marketplacesAdapter = MarketplacesAdapter(preferenceHelper, mainActivity)

        binding.citiesRV.layoutManager = layoutManager
        binding.citiesRV.adapter = marketplacesAdapter
    }

    fun updateMarketplaces(data: List<MarketPlace>) {
        marketplacesAdapter.items.clear()
        marketplacesAdapter.items.addAll(data)
        marketplacesAdapter.notifyDataSetChanged()
    }

    class MarketplacesAdapter(val preferenceHelper: IPreferenceHelper, val mainActivity: MainActivity) : RecyclerView.Adapter<MarketplacesAdapter.MarketplacesViewHolder>(){
        val items : MutableList<MarketPlace> = ArrayList()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarketplacesViewHolder {
            val binding : RowMarketplaceBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.row_marketplace,
                parent,
                false
            )

            return MarketplacesViewHolder(binding, preferenceHelper, mainActivity)
        }

        override fun onBindViewHolder(holder: MarketplacesViewHolder, position: Int) {
            holder.bindItem(items[position])
        }

        override fun getItemCount(): Int {
            return items.size
        }


        class MarketplacesViewHolder(
            val binding: RowMarketplaceBinding,
            val preferenceHelper: IPreferenceHelper,
            val mainActivity: MainActivity
        ) : RecyclerView.ViewHolder(binding.root){
            fun bindItem(marketPlace: MarketPlace){
                binding.marketplaceNameTV.text = marketPlace.marketplaceName
                binding.root.setOnClickListener {
                    val intent = Intent(mainActivity, SellerMainActivity::class.java)
                    intent.putExtra(Constants.constants.MARKETPLACE_ID, marketPlace.marketplaceId)
                    mainActivity.startActivity(intent)
                }
            }
        }
    }
}