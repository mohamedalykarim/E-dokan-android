package mohalim.store.edokan.core.utils

import com.google.gson.Gson
import retrofit2.HttpException
import java.net.ConnectException

class ErrorHandler {

    object ERROR_ENTRIES{
        const val ERROR_NUMBER = "ErrorNumber"
        const val ERROR_MESSAGE = "Message"
    }

    object ERRORS{
        const val AUTH_FAILED = "10000";
        const val AUTH_FAILED_EMPTY_PASSWORD = "10001";
        const val AUTH_FAILED_WRONG_PASSWORD = "10002";

        const val NOT_FOUND = "20000";
    }

    object DO {
        @JvmStatic
        fun getResponseMapFromHTTPException(httpException: HttpException) : Map<String, Any> {
            val errorStringRaw: String? = httpException.response()?.errorBody()?.string()
            var map: Map<String, Any> = HashMap()
            map = Gson().fromJson(errorStringRaw, map.javaClass)

            return map;
        }
    }



}