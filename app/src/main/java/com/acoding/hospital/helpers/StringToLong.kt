package com.acoding.hospital.helpers

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

fun convertToEpochMillis(
    dateString: String,
    timeString: String
): ZonedDateTime {
    return try {
        val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val timeFormatter = DateTimeFormatter.ofPattern("hh:mm a")

        // Parse date and time strings
        val date = LocalDate.parse(dateString, dateFormatter)
        val time = LocalTime.parse(timeString, timeFormatter)

        // Combine date and time into a LocalDateTime
        val dateTime = LocalDateTime.of(date, time)

        // Convert LocalDateTime to epoch milliseconds
        dateTime.atZone(ZoneId.systemDefault())
    } catch (e: Exception) {
        e.printStackTrace()
        val dateTime = LocalDateTime.now()
        dateTime.atZone(ZoneId.systemDefault())
    }
    // Define formatters for date and time

}