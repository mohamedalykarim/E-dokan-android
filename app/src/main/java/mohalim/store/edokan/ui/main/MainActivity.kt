package mohalim.store.edokan.ui.main

import android.animation.Animator
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.TransitionDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.transition.Slide
import android.transition.Transition
import android.transition.TransitionManager
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.animation.Animation
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
import dagger.hilt.android.AndroidEntryPoint
import mohalim.store.edokan.R
import mohalim.store.edokan.databinding.ActivityMainBinding
import mohalim.store.edokan.ui.login.LoginActivity

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    val TAG : String = "MainActivity";

    lateinit var binding : ActivityMainBinding;
    val viewmodel : HomeViewModel by viewModels()
    lateinit var homeFragment : HomeFragment;
    lateinit var cartFragment: CartFragment;
    lateinit var accountFragment: AccountFragment;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)

        homeFragment = HomeFragment();
        cartFragment = CartFragment();
        accountFragment = AccountFragment()

        binding.pager.adapter = HomePagerAdapter(
                supportFragmentManager,
                BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,
                homeFragment,
                cartFragment,
                accountFragment
        )

        binding.pager.currentItem = 2

        handleBottomClicks();
    }

    private fun handleBottomClicks() {
        val params1 : LinearLayout.LayoutParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT);
        val params2 : LinearLayout.LayoutParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT);


        binding.homeIcon.setOnClickListener(View.OnClickListener {
            if (viewmodel.currentTab == viewmodel.HOME) return@OnClickListener
            viewmodel.currentTab = viewmodel.HOME
            binding.pager.currentItem = 2

            binding.homeContainerBG.setBackgroundResource(R.drawable.bottom_bar_item_bg)
            binding.cartContainerBG.setBackgroundColor(ContextCompat.getColor(this, R.color.transparent))
            binding.accountContainerBG.setBackgroundColor(ContextCompat.getColor(this, R.color.transparent))

            binding.homeContainerBG.translationY = (binding.bottomContainer.height- binding.homeContainerBG.height).toFloat()
            binding.homeContainerBG.animate().setDuration(200).translationY(0f).setListener(null)


            params1.weight = 2f
            binding.homeContainer.layoutParams = params1;

            params2.weight = 3f
            binding.cartContainer.layoutParams = params2;
            binding.accountContainer.layoutParams = params2;

            binding.homeIcon.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_main_active))
            binding.cartIcon.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_cart_not_active))
            binding.accountIcon.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_account_not_active))

            binding.homeIcontTv.visibility = View.VISIBLE
            binding.cartIconTv.visibility = View.GONE
            binding.accountIconTv.visibility = View.GONE


        })

        binding.cartIcon.setOnClickListener(View.OnClickListener {
            if (viewmodel.currentTab == viewmodel.CART) return@OnClickListener
            viewmodel.currentTab = viewmodel.CART
            binding.pager.currentItem = 1

            binding.cartContainerBG.setBackgroundResource(R.drawable.bottom_bar_item_bg)
            binding.homeContainerBG.setBackgroundColor(ContextCompat.getColor(this, R.color.transparent))
            binding.accountContainerBG.setBackgroundColor(ContextCompat.getColor(this, R.color.transparent))

            binding.cartContainerBG.translationY = (binding.bottomContainer.height - binding.cartContainerBG.height).toFloat()
            binding.cartContainerBG.animate().setDuration(200).translationY(0f).setListener(null)



            params1.weight = 2f
            binding.cartContainer.layoutParams = params1;

            params2.weight = 3f
            binding.homeContainer.layoutParams = params2;
            binding.accountContainer.layoutParams = params2;

            binding.homeIcon.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_main_not_active))
            binding.cartIcon.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_cart_active))
            binding.accountIcon.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_account_not_active))

            binding.homeIcontTv.visibility = View.GONE
            binding.cartIconTv.visibility = View.VISIBLE
            binding.accountIconTv.visibility = View.GONE
        })

        binding.accountContainer.setOnClickListener(View.OnClickListener {
            if (viewmodel.currentTab == viewmodel.ACCOUNT) return@OnClickListener
            viewmodel.currentTab = viewmodel.ACCOUNT
            binding.pager.currentItem = 0

            binding.accountContainerBG.setBackgroundResource(R.drawable.bottom_bar_item_bg)
            binding.homeContainerBG.setBackgroundColor(ContextCompat.getColor(this@MainActivity, R.color.transparent))
            binding.cartContainerBG.setBackgroundColor(ContextCompat.getColor(this@MainActivity, R.color.transparent))

            binding.accountContainerBG.translationY = (binding.bottomContainer.height - binding.accountContainerBG.height).toFloat()
            binding.accountContainerBG.animate().setDuration(200).translationY(0f).setListener(null)


            params1.weight = 2f
            binding.accountContainer.layoutParams = params1;

            params2.weight = 3f
            binding.cartContainer.layoutParams = params2;
            binding.homeContainer.layoutParams = params2;

            binding.homeIcon.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_main_not_active))
            binding.cartIcon.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_cart_not_active))
            binding.accountIcon.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_account_active))

            binding.homeIcontTv.visibility = View.GONE
            binding.cartIconTv.visibility = View.GONE
            binding.accountIconTv.visibility = View.VISIBLE


        })

        binding.bottomHeaderView.setOnClickListener(View.OnClickListener {
            if (viewmodel.bottomVisibility == viewmodel.BOTTOM_VISIBLE){
                val value : Float = binding.bottomContainer.height.toFloat()
                binding.bottom.animate().translationY(value).setListener(null)
                viewmodel.bottomVisibility = viewmodel.BOTTOM_HIDE
                binding.arrowIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_up_arrow))

            }else if (viewmodel.bottomVisibility == viewmodel.BOTTOM_HIDE){
                binding.bottom.animate().translationY(0f).setListener(null)
                viewmodel.bottomVisibility = viewmodel.BOTTOM_VISIBLE
                binding.arrowIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_down_arrow))

            }
        })
    }


    private inner class HomePagerAdapter constructor(
            fm : FragmentManager,
            behavior: Int,
            val homefragment : HomeFragment,
            val cartFragment: CartFragment,
            val accountFragment: AccountFragment
    ) : FragmentStatePagerAdapter(fm, behavior){


        override fun getCount(): Int = 3

        override fun getItem(position: Int): Fragment {
            if (position == 0) return accountFragment
            if (position == 1) return cartFragment
            if (position == 2) return homefragment
            return homefragment
        }


    }


}