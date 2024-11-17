package com.acoding.hospital.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BaseResponse<T>(
    val message: String,
    val data: T,
    val status: Boolean,
    val statusCode: Int
)

@Serializable
data class PatientDto(
    @SerialName("phone_no")
    val phoneNo: String? = null,
    @SerialName("code")
    val code: String? = null,
    @SerialName("address")
    val address: String? = null,
    @SerialName("gender")
    val gender: String? = null,
    @SerialName("no_of_births")
    val noOfBirths: String? = null,
    @SerialName("name")
    val name: String? = null,
    @SerialName("pregnancy_status")
    val pregnancyStatus: String? = null,
    @SerialName("id")
    val id: Int? = null,
    @SerialName("health_status")
    val healthStatus: Int? = null,
    @SerialName("hospital_id")
    val hospitalId: Int? = null,
    @SerialName("age")
    val age: Int? = null
)

data class Patient(
    val phoneNo: String,
    val code: String,
    val address: String,
    val gender: String,
    val noOfBirths: String,
    val name: String,
    val pregnancyStatus: String,
    val id: Int,
    val healthStatus: Int,
    val hospitalId: Int,
    val age: Int
)

fun PatientDto.toPatient(): Patient {
    return Patient(
        phoneNo = phoneNo ?: "",
        code = code ?: "",
        address = address ?: "",
        gender = gender ?: "",
        noOfBirths = noOfBirths ?: "",
        name = name ?: "",
        pregnancyStatus = pregnancyStatus ?: "",
        id = id ?: 0,
        healthStatus = healthStatus ?: 0,
        hospitalId = hospitalId ?: 0,
        age = age ?: 0
    )
}