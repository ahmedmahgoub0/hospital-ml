package com.acoding.hospital.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PatientDto(
    @SerialName("patient_id")
    val id: Int,
    @SerialName("patient_code")
    val code: String,
    @SerialName("patient_name")
    val name: String,
    @SerialName("patient_address")
    val address: String,
    @SerialName("patient_age")
    val age: Int,
    @SerialName("patient_gender")
    val gender: String,
    @SerialName("patient_phone_no")
    val phone: String,
    @SerialName("hospital_id")
    val hospitalId: String,
    // val sugarStatus: Int
)

data class Patient(
    val id: Int,
    val code: String,
    val name: String,
    val address: String,
    val age: Int,
    val gender: String,
    val phone: String,
    val hospitalId: String,
    val sugarStatus: Int
)

fun PatientDto.toPatient() = Patient(
    id = id,
    code = code,
    name = name,
    address = address,
    age = age,
    gender = gender,
    phone = phone,
    hospitalId = hospitalId,
    sugarStatus = 90
    )