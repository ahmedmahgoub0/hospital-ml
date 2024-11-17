package com.acoding.hospital.helpers

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

fun convertToEpochMillis(
    dateString: String?,
    timeString: String?
): ZonedDateTime {
    return try {
        // Define the date and time formatters
        val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss")

        // Handle null date or time by using defaults
        val date = if (dateString.isNullOrEmpty()) LocalDate.now() else LocalDate.parse(
            dateString,
            dateFormatter
        )
        val time = if (timeString.isNullOrEmpty()) LocalTime.MIDNIGHT else LocalTime.parse(
            timeString,
            timeFormatter
        )

        // Combine date and time into a LocalDateTime object
        val dateTime = LocalDateTime.of(date, time)

        // Convert LocalDateTime to ZonedDateTime
        dateTime.atZone(ZoneId.systemDefault()) // Get ZonedDateTime based on the system default timezone
    } catch (e: Exception) {
        e.printStackTrace()
        // In case of an error, return the current time in the system's default zone
        LocalDateTime.now().atZone(ZoneId.systemDefault())
    }
}
