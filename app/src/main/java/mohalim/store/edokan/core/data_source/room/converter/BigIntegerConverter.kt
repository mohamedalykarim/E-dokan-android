package mohalim.store.edokan.core.data_source.room.converter

import androidx.room.TypeConverter
import java.math.BigInteger

class BigIntegerConverter {
    @TypeConverter
    fun fromBigInt(value: BigInteger?): String? {
        return value?.toString()
    }

    @TypeConverter
    fun toBigInt(string: String?): BigInteger? {
        return string.toString().toBigInteger()
    }

}