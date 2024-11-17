package com.acoding.hospital.data.model
//
//import kotlinx.serialization.SerialName
//import kotlinx.serialization.Serializable
//
//
//
//
////
////
////data class PatientDto1(
////    val id: Int,
////    val name: String,
////    val code: String,
////    val age: String,
////    val gender: String,
////    val pregnancy_status: String,
////    val address: String,
////    val no_of_births: String,
////    val phone_no: String,
////    val health_status: String?,
////    val hospital_id: Int
////)
////
////data class Patient1(
////    val id: Int,
////    val name: String,
////    val code: String,
////    val age: Int,
////    val gender: String,
////    val pregnancyStatus: String,
////    val address: String,
////    val numberOfBirths: Int,
////    val phoneNumber: String,
////    val healthStatus: String?,
////    val hospitalId: Int
////)
////
////fun PatientDto1.fromDto(): Patient1 {
////    return Patient1(
////        id = id,
////        name = name,
////        code = code,
////        age = age.toIntOrNull() ?: 0, // Safely convert age to Int
////        gender = gender,
////        pregnancyStatus = pregnancy_status,
////        address = address,
////        numberOfBirths = no_of_births.toIntOrNull()
////            ?: 0, // Safely convert no_of_births to Int
////        phoneNumber = phone_no,
////        healthStatus = health_status,
////        hospitalId = hospital_id
////    )
////}
////
////fun Patient1.toDto(): PatientDto1 {
////    return PatientDto1(
////        id = id,
////        name = name,
////        code = code,
////        age = age.toString(), // Convert age to String
////        gender = gender,
////        pregnancy_status = pregnancyStatus,
////        address = address,
////        no_of_births = numberOfBirths.toString(), // Convert numberOfBirths to String
////        phone_no = phoneNumber,
////        health_status = healthStatus,
////        hospital_id = hospitalId
////    )
////}
//
//
///*
//
//    fasel
//
//
//
// */
//@Serializable
//data class PatientDto(
//    @SerialName("patient_id")
//    val id: Int,
//    @SerialName("patient_code")
//    val code: String,
//    @SerialName("patient_name")
//    val name: String,
//    @SerialName("patient_address")
//    val address: String,
//    @SerialName("patient_age")
//    val age: Int,
//    @SerialName("patient_gender")
//    val gender: String,
//    @SerialName("patient_phone_no")
//    val phone: String,
//    @SerialName("hospital_id")
//    val hospitalId: String,
//    // val sugarStatus: Int
//)
//
//data class Patient(
//    val id: Int,
//    val code: String,
//    val name: String,
//    val address: String,
//    val age: Int,
//    val gender: String,
//    val phone: String,
//    val hospitalId: String,
//    val sugarStatus: Int
//)
//
//fun PatientDto.toPatient() = Patient(
//    id = id,
//    code = code,
//    name = name,
//    address = address,
//    age = age,
//    gender = gender,
//    phone = phone,
//    hospitalId = hospitalId,
//    sugarStatus = 90
//)