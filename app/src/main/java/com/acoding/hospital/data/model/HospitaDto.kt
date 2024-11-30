package com.acoding.hospital.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HospitalDto(
    @SerialName("hospital_id")
    val hospital_id: Int? = 1,
    @SerialName("hospital_name")
    val hospital_name: String? = "",
    @SerialName("hospital_code")
    val hospital_code: String? = "",
    @SerialName("hospital_phone")
    val hospital_address: String? = "",
    @SerialName("hospital_address")
    val hospital_governorate: String? = "",
    @SerialName("hospital_nation")
    val hospital_nation: String? = ""
)

data class Hospital(
    val hospitalId: Int,
    val hospitalName: String = "",
    val hospitalCode: String = "",
    val hospitalAddress: String = "",
    val hospitalGovernorate: String = "",
    val hospitalNation: String = ""
)

fun HospitalDto.toHospital(): Hospital {
    return Hospital(
        hospitalId = hospital_id ?: 1,
        hospitalName = hospital_name ?: "Hospital",
        hospitalCode = hospital_code ?: "",
        hospitalAddress = hospital_address ?: "",
        hospitalGovernorate = hospital_governorate ?: "",
        hospitalNation = hospital_nation ?: ""
    )
}

@Serializable
data class HospitalBody(
    val hospital_id: Int,
)