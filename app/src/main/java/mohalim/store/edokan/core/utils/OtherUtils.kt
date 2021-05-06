package mohalim.store.edokan.core.utils

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
    }
}