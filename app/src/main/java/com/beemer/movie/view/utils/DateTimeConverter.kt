package com.beemer.movie.view.utils

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

object DateTimeConverter {
    fun convertDate(date: String, originalFormat: String, targetFormat: String, locale: Locale): String {
        val formatter = DateTimeFormatter.ofPattern(originalFormat, locale)
        val localDate = LocalDate.parse(date, formatter)
        return localDate.format(DateTimeFormatter.ofPattern(targetFormat, locale))
    }
}
