package mohalim.store.edokan.core.utils

import android.graphics.Typeface
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout
import java.lang.Exception


class BindingUtils {
    private final val TAG : String = "DataBindingUtils";

    companion object {
        @JvmStatic
        @BindingAdapter("android:customFont")
        fun customFont(textView: TextView, font: String){
            try {
                textView.typeface = Typeface.createFromAsset(textView.context.assets, "fonts/$font")
            }catch (e: Exception){
            }
        }

        @JvmStatic
        @BindingAdapter("android:customFont")
        fun customFont(textInputLayout : TextInputLayout, font: String){
            try {
                textInputLayout.typeface = Typeface.createFromAsset(textInputLayout.context.assets, "fonts/$font")
            }catch (e: Exception){
            }
        }
    }



}