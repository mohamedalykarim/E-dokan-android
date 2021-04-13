package mohalim.store.edokan.core.utils

sealed class DataState<out T> {
    object Loading: DataState<Nothing>()
    data class Success<out T>(val data: T) : DataState<T>()
    data class Failure(val exception: Throwable) : DataState<Nothing>()


}