package com.acoding.hospital.ui.home

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.acoding.hospital.data.model.Bio
import com.acoding.hospital.data.model.Patient
import com.acoding.hospital.domain.util.NetworkError
import com.acoding.hospital.helpers.convertToEpochMillis
import com.acoding.hospital.ui.bio.DataPoint
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.random.Random

@Immutable
data class HomeListState(
    val isLoading: Boolean = false,
    val patients: List<Patient> = emptyList(),
    val selectedPatient: Patient? = null,
    val patientBio: List<Bio> = emptyList(),
    val sugarDataPoints: List<DataPoint> = emptyList(),
    val temperatureDataPoints: List<DataPoint> = emptyList(),
    val pressureDataPoints: List<DataPoint> = emptyList()
)

sealed interface HomeListAction {

}

sealed interface HomeListEvent {
    data class Error(val error: NetworkError) : HomeListEvent
}

class HomeViewModel : ViewModel() {

    private val _state = MutableStateFlow(HomeListState())
    val state = _state
        .onStart { loadPatients() }
        .stateIn(
            viewModelScope,
            started = SharingStarted.WhileSubscribed(10000L),
            initialValue = HomeListState()
        )

    private val _event = Channel<HomeListEvent>()
    val event = _event.receiveAsFlow()

    private fun loadPatients() {
        _state.update { it.copy(isLoading = true) }
        val patient = Patient(
            id = 0,
            code = "PT001",
            name = "Guy Lawrence",
            address = "theophrastus",
            age = 42,
            gender = "Male",
            phone = "(583) 257-9690",
            hospitalId = "iisque",
            sugarStatus = 90
        )
        viewModelScope.launch {
            delay(1000L)
            // val patients = getPatients()
            val patients = (1..20).map {
                patient.copy(
                    id = it,
                    name = it.toString(),
                    sugarStatus = Random.nextInt(30, 150)
                )
            }

            _state.update {
                it.copy(
                    patients = patients.sortedBy { patient -> patient.sugarStatus >= 90 },
                    selectedPatient = patients.first(),
                )
            }

            val bio = Bio(
                id = 0,
                patientCode = "PT001",
                date = "24-10-2024",
                time = "10:30 AM",
                avgTemperature = 36.6,
                pressure = "120/80",
                sugar = 90,
                status = "stable",
                patientId = 1
            )
//            loadPatientBio(patient.code)

            _state.update {
                it.copy(
                    patientBio = (1..9).map { value ->
                        bio.copy(
                            id = value,
                            patientCode = "PT001",
                            date = "2024-10-0$value",
                            time = "0$value:30 AM",
                            avgTemperature = 36.6,
                            pressure = "120/80",
                            sugar = value * 2 + 90,
                            status = "stable",
                            patientId = 1
                        )
                    },
                )
            }

            _state.update { it ->
                it.copy(
                    sugarDataPoints = (0..50).map { value ->
                        DataPoint(
                            x = value.toFloat(),
                            y = Random.nextInt(30, 150).toFloat(),
                            xLabel = value.toString()
                        )
                    },
                    temperatureDataPoints = (0..40).map { value ->
                        DataPoint(
                            x = value.toFloat(),
                            y = Random.nextInt(35, 42).toFloat(),
                            xLabel = value.toString()
                        )
                    },
                    pressureDataPoints = _state.value.patientBio.map {
                        DataPoint(
                            x = it.sugar.toFloat(),
                            y = it.sugar.toFloat(),
                            xLabel = DateTimeFormatter
                                .ofPattern("ha\nM/d")
                                .format(convertToEpochMillis(it.date, it.time))
                        )
                    }
//                    (0..50).map { value ->
//                        DataPoint(
//                            x = value.toFloat(),
//                            y = Random.nextInt(30, 150).toFloat(),
//                            xLabel = value.toString()
//                        )
//                    }
                    ,
                    isLoading = false
                )
            }
        }
    }

    fun clickPatient(patientCode: String) {
        /*
                TODO:
         */
        _state.update {
            it.copy(
                selectedPatient = it.patients[Random.nextInt(0, 20)]
            )
        }
    }

    fun filterByDate(startDate: LocalDate, endDate: LocalDate) {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val start = startDate.format(formatter)
        val end = endDate.format(formatter)

        _state.update {
            it.copy(
                sugarDataPoints = it.patientBio.filter {
                    it.date in start..end
                }.map {
                    DataPoint(
                        x = it.sugar.toFloat(),
                        y = it.sugar.toFloat(),
                        xLabel = DateTimeFormatter
                            .ofPattern("ha\nM/d")
                            .format(convertToEpochMillis(it.date, it.time))
                    )
                }
            )
        }
    }


}