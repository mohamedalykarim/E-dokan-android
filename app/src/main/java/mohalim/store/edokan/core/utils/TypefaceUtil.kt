package mohalim.store.edokan.core.utils

import android.content.Context
import android.graphics.Typeface


class TypefaceUtil {
    object font {

        @JvmStatic
        fun setDefaultFont(
            context: Context,
            staticTypefaceFieldName: String?, fontAssetName: String?
        ) {
            val regular = Typeface.createFromAsset(
                context.assets,
                fontAssetName
            )
            replaceFont(staticTypefaceFieldName, regular)
        }

        private fun replaceFont(
            staticTypefaceFieldName: String?,
            newTypeface: Typeface?
        ) {
            try {
                val staticField = Typeface::class.java
                    .getDeclaredField(staticTypefaceFieldName)
                staticField.isAccessible = true
                staticField[null] = newTypeface
            } catch (e: NoSuchFieldException) {
                e.printStackTrace()
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            }
        }

    }

}