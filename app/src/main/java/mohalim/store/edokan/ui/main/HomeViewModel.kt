package mohalim.store.edokan.ui.main

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
@Inject constructor() : ViewModel(){
    val HOME : String = "Home";
    val CART : String = "Cart";
    val ACCOUNT : String = "Account";

    val BOTTOM_HIDE = "bottom_hide"
    val BOTTOM_VISIBLE = "bottom_visible"

    var currentTab = HOME;
    var bottomVisibility = BOTTOM_VISIBLE;


}