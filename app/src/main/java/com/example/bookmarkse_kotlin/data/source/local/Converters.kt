package com.example.bookmarkse_kotlin.data.source.local

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.TypeConverter
import java.sql.Date
import java.time.LocalDate
import java.time.ZoneId.systemDefault

@RequiresApi(Build.VERSION_CODES.O)
class Converters {
    @TypeConverter
    fun fromTimestamp(value: LocalDate?): Date? {
        return if (value == null) null else Date.valueOf(value.toString())
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): LocalDate? {
        val defaultZoneId = systemDefault()
        val instant = date?.toInstant()
        return instant?.atZone(defaultZoneId)?.toLocalDate()
    }
}