//package com.acoding.hospital.data.model
//
//import kotlinx.serialization.SerialName
//import kotlinx.serialization.Serializable
//
//@Serializable
//data class BioDto(
//    @SerialName("bio_indicators_id")
//    val id: Int,
//    @SerialName("bio_indicators_patient_code")
//    val patientCode: String,
//    @SerialName("bio_indicators_date")
//    val date: String,
//    @SerialName("bio_indicators_time")
//    val time: String,
//    @SerialName("bio_indicators_average_temperature")
//    val avgTemperature: Double,
//    @SerialName("bio_indicators_blood_pressure")
//    val pressure: String,
//    @SerialName("bio_indicators_blood_sugar")
//    val sugar: Int,
//    @SerialName("bio_indicators_health_status")
//    val status: String,
//    @SerialName("patient_id")
//    val patientId: Int,
//)
//
//data class Bio(
//    val id: Int,
//    val patientCode: String,
//    val date: String,
//    val time: String,
//    val avgTemperature: Double,
//    val pressure: String,
//    val sugar: Int,
//    val status: String,
//    val patientId: Int,
//)
//
//fun BioDto.toBio() = Bio(
//    id = id,
//    patientCode = patientCode,
//    date = date,
//    time = time,
//    avgTemperature = avgTemperature,
//    pressure = pressure,
//    sugar = sugar,
//    status = status,
//    patientId = patientId,
//)
