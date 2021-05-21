package mohalim.store.edokan.core.utils

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import com.google.android.gms.maps.MapView
import java.util.jar.Attributes


class MyMapView(context: Context, attributes: AttributeSet) : MapView(context,attributes) {

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        this.parent.requestDisallowInterceptTouchEvent(true)

        return super.dispatchTouchEvent(ev)
    }
}