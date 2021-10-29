package mohalim.store.edokan.core.utils

import android.content.Context

class DensityUtil {
    companion object{
        @JvmStatic
        fun dipToPx(context: Context, dpValue: Float): Int {
            val scale: Float = context.resources.displayMetrics.density
            return (dpValue * scale + 0.5f).toInt()
        }


        @JvmStatic
        fun pxToDp(context: Context, pxValue: Float): Int {
            val scale: Float = context.resources.displayMetrics.density
            return (pxValue / scale + 0.5f).toInt()
        }

    }
}