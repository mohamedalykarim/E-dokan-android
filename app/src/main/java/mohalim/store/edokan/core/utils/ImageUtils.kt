package mohalim.store.edokan.core.utils

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.bumptech.glide.Glide
import androidx.compose.ui.platform.LocalContext
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import mohalim.store.edokan.R


@Composable
fun loadPicture(
    url : String,
    @DrawableRes defaultImage: Int,
) : MutableState<Bitmap?>{
    val bitmapState: MutableState<Bitmap?> = remember { mutableStateOf(null) }
    Glide.with(LocalContext.current)
        .asBitmap()
        .load(url)
        .override(600,600)
        .placeholder(R.drawable.loading)
        .into(object :CustomTarget<Bitmap>(){
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                bitmapState.value = resource
            }
            override fun onLoadCleared(placeholder: Drawable?) {
            }
        })

    return bitmapState
}