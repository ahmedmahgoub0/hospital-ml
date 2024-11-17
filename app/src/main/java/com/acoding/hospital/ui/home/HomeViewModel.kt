package com.acoding.hospital.ui.home

import android.util.Log
import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.acoding.hospital.data.model.Bio
import com.acoding.hospital.data.model.LoginDataStore
import com.acoding.hospital.data.model.Patient
import com.acoding.hospital.data.model.PressureValues
import com.acoding.hospital.data.model.SugarValues
import com.acoding.hospital.data.model.TemperatureValues
import com.acoding.hospital.data.repo.HospitalRepo
import com.acoding.hospital.domain.util.NetworkError
import com.acoding.hospital.domain.util.onError
import com.acoding.hospital.domain.util.onSuccess
import com.acoding.hospital.helpers.convertToEpochMillis
import com.acoding.hospital.helpers.getValueBeforeSlash
import com.acoding.hospital.ui.bio.DataPoint
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Immutable
data class HomeListState(
    val isLoading: Boolean = false,
    val detailsLoading: Boolean = false,
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

class HomeViewModel(
    private val repo: HospitalRepo
) : ViewModel() {

    private val _state = MutableStateFlow(HomeListState())
    val state = _state
        .onStart {
            loadPatients()
            loadPatientsBio(_state.value.selectedPatient?.id ?: 1)
        }
        .stateIn(
            viewModelScope,
            started = SharingStarted.WhileSubscribed(15000L),
            initialValue = HomeListState()
        )

    private val _event = Channel<HomeListEvent>()
    val event = _event.receiveAsFlow()

    private fun loadPatients() {
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val user = LoginDataStore.user
            repo.getPatients(user?.hospitalId ?: 1)
                .onSuccess { patients ->
                    Log.i("HomeViewModel", "loadPatients: $patients")
                    _state.update { it.copy(isLoading = false) }
                    _state.update {
                        it.copy(
                            patients = patients.sortedBy { patient -> patient.healthStatus >= 60 },
                            selectedPatient = patients.first()
                        )
                    }
                }.onError { error ->
                    _state.update { it.copy(isLoading = false) }
                    _event.send(HomeListEvent.Error(error))
                }
        }
    }

    private fun loadPatientsBio(patientId: Int) {
        _state.update { it.copy(detailsLoading = true) }
        viewModelScope.launch {
            repo.getBio(patientId)
                .onSuccess { bio ->
                    Log.i("HomeViewModel", "loadBio: $bio")


                    val sugar = bio.map {
                        SugarValues(
                            date = it.date,
                            time = it.time,
                            sugar = it.bloodSugar
                        )
                    }
                    val pressure = bio.map {
                        val high = getValueBeforeSlash(it.bloodPressure)
                        PressureValues(
                            date = it.date,
                            time = it.time,
                            high = high.toInt(),
                            press = it.bloodPressure
                        )
                    }
                    val temperature = bio.map {
                        TemperatureValues(
                            date = it.date,
                            time = it.time,
                            temp = it.averageTemperature
                        )
                    }

                    _state.update {
                        it.copy(
                            patientBio = bio,
                            sugarDataPoints = sugar.map { value ->
                                DataPoint(
                                    x = value.sugar.toFloat(),
                                    y = value.sugar.toFloat(),
                                    xLabel = DateTimeFormatter
                                        .ofPattern("ha\nM/d")
                                        .format(convertToEpochMillis(value.date, value.time))
                                )
                            },
                            temperatureDataPoints = temperature.map { value ->
                                DataPoint(
                                    x = value.temp.toFloat(),
                                    y = value.temp.toFloat(),
                                    xLabel = DateTimeFormatter
                                        .ofPattern("ha\nM/d")
                                        .format(convertToEpochMillis(value.date, value.time))
                                )
                            },
                            pressureDataPoints = pressure.map { value ->
                                DataPoint(
                                    x = value.high.toFloat(),
                                    y = value.high.toFloat(),
                                    xLabel = DateTimeFormatter
                                        .ofPattern("ha\nM/d")
                                        .format(convertToEpochMillis(value.date, value.time))
                                )

                            }, detailsLoading = false
                        )
                    }


                }.onError { error ->
                    _state.update { it.copy(detailsLoading = false) }
                    _event.send(HomeListEvent.Error(error))
                }
        }
    }


    fun clickPatient(patientId: Int) {
        _state.update { it ->
            it.copy(
                selectedPatient = it.patients.find { it.id == patientId }
            )
        }
        loadPatientsBio(patientId)
    }

    fun filterByDate(startDate: LocalDate, endDate: LocalDate) {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val start = startDate.format(formatter)
        val end = endDate.format(formatter)

        _state.update {
            it.copy(
                sugarDataPoints = it.patientBio.filter { paBi ->
                    paBi.date in start..end
                }.map { mami ->
                    DataPoint(
                        x = mami.bloodSugar.toFloat(),
                        y = mami.bloodSugar.toFloat(),
                        xLabel = DateTimeFormatter
                            .ofPattern("ha\nM/d")
                            .format(convertToEpochMillis(mami.date, mami.time))
                    )
                }
            )
        }
    }
}