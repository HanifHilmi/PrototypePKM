package com.pkm.PrototypePKM.utils

import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

fun formatDateAndTime(dateString: String): Pair<String, String> {
    val inputFormat = SimpleDateFormat("yyyyMMddHHmm", Locale.getDefault())
    val outputDateFormat = SimpleDateFormat("EEEE, d MMMM yyyy", Locale("id"))
    val outputTimeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())


    val date = inputFormat.parse(dateString)
    val formattedDate = outputDateFormat.format(date)
    val formattedTime = outputTimeFormat.format(date)

    val waktu = when(formattedTime){
        "00:00" -> "Malam"
        "06:00" -> "Pagi"
        "12:00" -> "Siang"
        "18:00" -> "Sore"
        else -> ""
    }

    return Pair(formattedDate, waktu)
}

fun hasPassedDateTime(dateTime: String): Boolean {
    val formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm")
    val parsedDateTime = LocalDateTime.parse(dateTime, formatter)
    val currentDateTime = LocalDateTime.now()

    return parsedDateTime.isBefore(currentDateTime)
}

fun currentDateTime():Pair<String,String> {
    val currentDateTime = LocalDateTime.now()

    val dateFormat = DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy", Locale("id"))
    val timeFormat = DateTimeFormatter.ofPattern("HH:mm:ss")

    return currentDateTime.format(dateFormat) to currentDateTime.format(timeFormat)
}

fun formatDateTimeForPredisi(input:String,addDay:Int = 0):String{
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val outputFormat = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale("id"))

    val startDate = dateFormat.parse(input)
    val calendar = Calendar.getInstance()
    calendar.time = startDate
    calendar.add(Calendar.DAY_OF_YEAR,addDay)

    val modifiedDate = calendar.time
    return  outputFormat.format(modifiedDate)
}


fun convertLocalDateToString(date: LocalDate): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    return date.format(formatter)
}