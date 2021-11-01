package mohalim.store.edokan.core.utils

class Constants {
    object constants {



        val BASE_URL: String = "http://192.168.1.105:3001"
//        val BASE_URL: String = "https://e-dokan-93b7c.uc.r.appspot.com"


        const val CATEGORY_IMAGE_BASE_URL = "https://firebasestorage.googleapis.com/v0/b/e-dokan-93b7c.appspot.com/o/E-dokan%2Fcategories%2F"
        const val PRODUCT_IMAGE_BASE_URL = "https://firebasestorage.googleapis.com/v0/b/e-dokan-93b7c.appspot.com/o/E-dokan%2Fproducts%2F"
        const val OFFER_IMAGE_BASE_URL = "https://firebasestorage.googleapis.com/v0/b/e-dokan-93b7c.appspot.com/o/E-dokan%2Foffers%2F"

        const val CATEGORY_ID : String = "category_id"
        const val PRODUCT_ID : String = "product_id"
        const val ORDER_ID : String = "order_id"
        const val MARKETPLACE_ID : String = "marketplace_id"


        const val MESSAGE_DIALOG_DEFAULT_STYLE: Int = 1
        const val MESSAGE_DIALOG_ADD_ADDRESS_STYLE : Int = 2
        const val MESSAGE_DIALOG_DELETE_ADDRESS_STYLE : Int = 3


        const val ORDER_STATUS_PENDING = 100
        const val ORDER_STATUS_RECEIVED = 200
        const val ORDER_STATUS_PROCESSING = 300
        const val ORDER_STATUS_DELIVERING = 400
        const val ORDER_STATUS_COMPLETED = 500



    }


}