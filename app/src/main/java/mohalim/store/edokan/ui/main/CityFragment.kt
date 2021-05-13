package mohalim.store.edokan.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import mohalim.store.edokan.R
import mohalim.store.edokan.core.model.city.City
import mohalim.store.edokan.core.utils.IPreferenceHelper
import mohalim.store.edokan.core.utils.PreferencesUtils
import mohalim.store.edokan.databinding.FragmentCityBinding
import mohalim.store.edokan.databinding.RowCityBinding

class CityFragment : Fragment() {

    lateinit var binding : FragmentCityBinding
    lateinit var layoutManager: LinearLayoutManager;
    lateinit var cityAdapter: CityAdapter;
    lateinit var mainActivity : MainActivity

    private val preferenceHelper: IPreferenceHelper by lazy { PreferencesUtils(mainActivity) }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_city,
            container,
            false
        )

         mainActivity = activity as MainActivity

        mainActivity.viewModel.getAllCities()

        initCityRV()


        return binding.root
    }

    private fun initCityRV() {
        layoutManager = LinearLayoutManager(activity)
        cityAdapter = CityAdapter(preferenceHelper, mainActivity)

        binding.citiesRV.layoutManager = layoutManager
        binding.citiesRV.adapter = cityAdapter
    }

    fun updateCities(data: List<City>) {
        cityAdapter.items.clear()
        cityAdapter.items.addAll(data)
        cityAdapter.notifyDataSetChanged()
    }

    class CityAdapter(val preferenceHelper: IPreferenceHelper, val mainActivity: MainActivity) : RecyclerView.Adapter<CityAdapter.CityViewHolder>(){
        val items : MutableList<City> = ArrayList()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityViewHolder {
            val binding : RowCityBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.row_city,
                parent,
                false
            )

            return CityViewHolder(binding, preferenceHelper, mainActivity)
        }

        override fun onBindViewHolder(holder: CityViewHolder, position: Int) {
            holder.bindItem(items[position])
        }

        override fun getItemCount(): Int {
            return items.size
        }


        class CityViewHolder(
            val binding: RowCityBinding,
            val preferenceHelper: IPreferenceHelper,
            val mainActivity: MainActivity
        ) : RecyclerView.ViewHolder(binding.root){
            fun bindItem(city : City){
                binding.cityName.text = city.cityName
                binding.root.setOnClickListener {
                    preferenceHelper.setCityId(city.cityId)
                    preferenceHelper.setCityName(city.cityName)
                    mainActivity.loadHome()
                }
            }
        }
    }
}