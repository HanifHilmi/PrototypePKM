package com.pkm.PrototypePKM.utils

import java.text.SimpleDateFormat
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

    return Pair(formattedDate, formattedTime)
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