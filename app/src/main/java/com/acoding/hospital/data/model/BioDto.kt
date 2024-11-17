package com.acoding.hospital.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BioDto(
    @SerialName("date")
    val date: String? = null,
    @SerialName("average_temperature")
    val averageTemperature: Double? = null,
    @SerialName("patient_id")
    val patientId: Int? = null,
    @SerialName("id")
    val id: Int? = null,
    @SerialName("time")
    val time: String? = null,
    @SerialName("blood_pressure")
    val bloodPressure: String? = null,
    @SerialName("health_status")
    val healthStatus: Int? = null,
    @SerialName("blood_sugar")
    val bloodSugar: Int? = null,
    @SerialName("patient_code")
    val patientCode: String? = null
)

data class Bio(
    val date: String,
    val averageTemperature: Double,
    val patientId: Int,
    val id: Int,
    val time: String,
    val bloodPressure: String,
    val healthStatus: Int,
    val bloodSugar: Int,
    val patientCode: String
)

fun BioDto.toBio(): Bio {
    return Bio(
        date = date ?: "",
        averageTemperature = averageTemperature ?: 0.0,
        patientId = patientId ?: 0,
        id = id ?: 0,
        time = time ?: "",
        bloodPressure = bloodPressure ?: "",
        healthStatus = healthStatus ?: 0,
        bloodSugar = bloodSugar ?: 0,
        patientCode = patientCode ?: ""
    )
}

data class SugarValues(
    val date: String,
    val time: String,
    val sugar: Int
)

data class PressureValues(
    val date: String,
    val time: String,
    val high: Int,
    val press: String
)

data class TemperatureValues(
    val date: String,
    val time: String,
    val temp: Double
)