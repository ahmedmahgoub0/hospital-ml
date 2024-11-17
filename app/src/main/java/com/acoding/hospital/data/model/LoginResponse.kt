package com.acoding.hospital.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(

    @SerialName("data")
    val data: User? = null,

    @SerialName("auth")
    val auth: Boolean? = null,

    @SerialName("admin")
    val admin: Boolean? = null,

    @SerialName("message")
    val message: String? = null,

    @SerialName("error")
    val error: Boolean? = null,

    @SerialName("statusCode")
    val statusCode: Int? = null,

    @SerialName("status")
    val status: Boolean? = null
)

@Serializable
data class User(

    @SerialName("user_password")
    val userPassword: String? = null,

    @SerialName("user_code")
    val userCode: String? = null,

    @SerialName("user_name")
    val userName: String? = null,

    @SerialName("user_username")
    val userUsername: String? = null,

    @SerialName("user_gender")
    val userGender: String? = null,

    @SerialName("user_additional_info")
    val userAdditionalInfo: String? = null,

    @SerialName("hospital_id")
    val hospitalId: Int? = null
)
