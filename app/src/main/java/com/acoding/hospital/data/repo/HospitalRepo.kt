package com.acoding.hospital.data.repo

import com.acoding.hospital.data.model.BaseResponse
import com.acoding.hospital.data.model.Bio
import com.acoding.hospital.data.model.BioDto
import com.acoding.hospital.data.model.LoginResponse
import com.acoding.hospital.data.model.Patient
import com.acoding.hospital.data.model.PatientDto
import com.acoding.hospital.data.model.toBio
import com.acoding.hospital.data.model.toPatient
import com.acoding.hospital.data.networking.constructUrl
import com.acoding.hospital.data.networking.safeCall
import com.acoding.hospital.domain.util.NetworkError
import com.acoding.hospital.domain.util.Result
import com.acoding.hospital.domain.util.map
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.Serializable

@Serializable
data class PatientsBody(
    val hospital_id: Int,
)

@Serializable
data class BioBody(
    val patient_id: Int,
)

@Serializable
data class Loginbody(
    val user_username: String,
    val user_password: String,
)


interface HospitalRepo {
    suspend fun getPatients(hospitalId: Int): Result<List<Patient>, NetworkError>

    suspend fun getBio(patientId: Int): Result<List<Bio>, NetworkError>

    suspend fun login(username: String, password: String): Result<LoginResponse, NetworkError>

}

class HospitalRepoImpl(
    private val client: HttpClient
) : HospitalRepo {

    override suspend fun getPatients(hospitalId: Int): Result<List<Patient>, NetworkError> {
        return safeCall<BaseResponse<List<PatientDto>>> {
            client.post(
                urlString = constructUrl("patients/fetchall")
            ) {
                val patientBody = PatientsBody(
                    hospital_id = hospitalId
                )
                contentType(ContentType.Application.Json)
                setBody(patientBody)
            }
        }.map { response ->
            response.data.map { it.toPatient() }
        }
    }

    override suspend fun login(
        username: String,
        password: String
    ): Result<LoginResponse, NetworkError> {
        return safeCall<LoginResponse> {
            client.post(
                urlString = constructUrl("user/login")
            ) {
                val body = Loginbody(
                    user_username = username,
                    user_password = password
                )

                contentType(ContentType.Application.Json)
                setBody(body)
            }
        }
    }

    override suspend fun getBio(patientId: Int): Result<List<Bio>, NetworkError> {
        return safeCall<BaseResponse<List<BioDto>>> {
            client.post(
                urlString = constructUrl("bio_info/fetch")
            ) {
                val bioBody = BioBody(
                    patient_id = patientId
                )
                contentType(ContentType.Application.Json)
                setBody(bioBody)
            }
        }.map { response ->
            response.data.map {
                it.toBio(
                )
            }
        }
    }
}

/*

api/patients/fetchall(hospital_id)

 */