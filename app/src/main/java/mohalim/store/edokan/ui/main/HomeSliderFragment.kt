package mohalim.store.edokan.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import mohalim.store.edokan.R
import mohalim.store.edokan.core.model.offer.Offer
import mohalim.store.edokan.core.utils.Constants
import mohalim.store.edokan.databinding.FragmentHomeSliderBinding

@AndroidEntryPoint
class HomeSliderFragment(val offer: Offer) : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding : FragmentHomeSliderBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home_slider,container, false)
        Glide.with(binding.root.context).load(Constants.constants.OFFER_IMAGE_BASE_URL + offer.offerImage).into(binding.imageView6)
        return binding.root;
    }
}