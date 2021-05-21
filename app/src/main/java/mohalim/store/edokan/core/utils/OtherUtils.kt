package mohalim.store.edokan.core.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Build
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.google.android.gms.maps.model.BitmapDescriptor


class OtherUtils {

    companion object{

        @JvmStatic
        fun getSupportItemStatus(status : Int): String {
            if (status == 1 ){
                return "تحت المراجعة"
            }else if (status == 2 ){
                return "تم الرد من طرف الدعم الفني"
            }else if (status == 3 ){
                return "تم حل المشكلة"
            }
            return "تحت المراجعة"
        }


        @JvmStatic
        public fun getBitmapFromVectorDrawable(
            context: Context,
            @DrawableRes vectorDrawableResourceId: Int
        ): Bitmap? {
            var drawable = ContextCompat.getDrawable(context, vectorDrawableResourceId)

            val bitmap = Bitmap.createBitmap(
                drawable!!.intrinsicWidth,
                drawable.intrinsicHeight, Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)

            return bitmap
        }
    }
}