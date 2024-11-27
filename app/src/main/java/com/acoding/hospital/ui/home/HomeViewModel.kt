package com.acoding.hospital.ui.home

import android.util.Log
import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.acoding.hospital.data.datastore.Language
import com.acoding.hospital.data.datastore.UserPreferences
import com.acoding.hospital.data.model.Bio
import com.acoding.hospital.data.model.LoginDataStore
import com.acoding.hospital.data.model.Patient
import com.acoding.hospital.data.model.toPressureValues
import com.acoding.hospital.data.model.toSugarValues
import com.acoding.hospital.data.model.toTemperatureValues
import com.acoding.hospital.data.repo.HospitalRepo
import com.acoding.hospital.domain.util.NetworkError
import com.acoding.hospital.domain.util.onError
import com.acoding.hospital.domain.util.onSuccess
import com.acoding.hospital.helpers.convertToEpochMillis
import com.acoding.hospital.helpers.getValueAfterSlash
import com.acoding.hospital.helpers.getValueBeforeSlash
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
    val showSearch: Boolean = false,
    val isLoading: Boolean = true,
    var tabTypeIndex: Int = 0,
    val detailsLoading: Boolean = false,
    val patients: List<Patient> = emptyList(),
    val selectedPatient: Patient? = null,
    val patientBio: List<Bio> = emptyList(),
    val sugarDataPoints: List<Double> = emptyList(),
    val temperatureDataPoints: List<Double> = emptyList(),
    val pressureHighDataPoints: List<Double> = emptyList(),
    val pressureLowDataPoints: List<Double> = emptyList(),
    val userPreferences: UserPreferences? = null,
    val datePoints: List<String> = emptyList()
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

    init {
        viewModelScope.launch {
            repo.userDate.collect { userPreferences ->
                _state.update {
                    it.copy(
                        userPreferences = userPreferences
                    )
                }
            }
        }
    }

    fun searchClicked() {
        _state.update { it.copy(showSearch = true) }
    }

    fun searchClosed() {
        _state.update { it.copy(showSearch = false) }
    }

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

    fun setTabTypeIndex(selected: Int) {
        _state.update {
            it.copy(
                tabTypeIndex = selected
            )
        }
    }

    private fun loadPatientsBio(patientId: Int) {
        _state.update { it.copy(detailsLoading = true) }
        viewModelScope.launch {
            repo.getBio(patientId)
                .onSuccess { bio ->
                    Log.i("HomeViewModel", "loadBio: $bio")

                    val sugar = bio.map { it.toSugarValues() }
                    val pressure = bio.map { it.toPressureValues() }
                    val temperature = bio.map { it.toTemperatureValues() }

                    _state.update {
                        it.copy(
                            patientBio = patientBios,
                            //patientBio = bio,
                            datePoints = patientBios.takeLast(24).map { value ->
                                DateTimeFormatter
                                    .ofPattern("ha\nM/d")
                                    .format(convertToEpochMillis(value.date, value.time))
                            },
                            sugarDataPoints = patientBios.takeLast(24).map { value ->
                                /**                             DataPoint(
                                //                                    x = value.bloodSugar.toFloat(),
                                //                                    y = value.bloodSugar.toFloat(),
                                //                                    xLabel = DateTimeFormatter
                                //                                        .ofPattern("ha\nM/d")
                                //                                        .format(convertToEpochMillis(value.date, value.time))
                                //                                )
                                 */
                                value.bloodSugar.toDouble()
                            },
                            temperatureDataPoints = patientBios.takeLast(24).map { value ->
                                /**                               DataPoint(
                                //                                    x = value.averageTemperature.toFloat(),
                                //                                    y = value.averageTemperature.toFloat(),
                                //                                    xLabel = DateTimeFormatter
                                //                                        .ofPattern("ha\nM/d")
                                //                                        .format(convertToEpochMillis(value.date, value.time))
                                //                                //
                                )*/
                                value.averageTemperature.toDouble()
                            },
                            pressureHighDataPoints = patientBios.takeLast(24).map { value ->
                                /**                                Log.i("HomeViewModel Pressure list: ", pressure.toString())
                                //                                DataPoint(
                                //                                    x = value.high.toFloat(),
                                //                                    y = value.high.toFloat(),
                                //                                    xLabel = DateTimeFormatter
                                //                                        .ofPattern("ha\nM/d")
                                //                                        .format(convertToEpochMillis(value.date, value.time))
                                //                                )
                                 */
                                value.bloodPressure.getValueBeforeSlash().toDouble()
                            },
                            pressureLowDataPoints = patientBios.takeLast(24).map { value ->
                                value.bloodPressure.getValueAfterSlash().toDouble()
                            },
                            detailsLoading = false
                        )
                    }


                }.onError { error ->
                    _state.update { it.copy(detailsLoading = false) }
                    _event.send(HomeListEvent.Error(error))
                }
        }
    }


    fun clickPatient(patientId: Int, tabTypeIndex: Int) {
        _state.update { it ->
            it.copy(
                selectedPatient = it.patients.find { it.id == patientId }
            )
        }
        loadPatientsBio(patientId)
    }

    fun setLanguage(language: Language) {
        viewModelScope.launch {
            repo.setLanguage(language)
            _state.update {
                it.copy(userPreferences = it.userPreferences?.copy(language = language))
            }
        }
    }

    /*
        TODO: make it for each graph
     */
    fun filterByDate(startDate: LocalDate, endDate: LocalDate) {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val start = startDate.format(formatter)
        val end = endDate.format(formatter)

//        _state.update {
//            it.copy(
//                sugarDataPoints = it.patientBio.filter { paBi ->
//                    paBi.date in start..end
//                }.map { mami ->
//                    DataPoint(
//                        x = mami.bloodSugar.toFloat(),
//                        y = mami.bloodSugar.toFloat(),
//                        xLabel = DateTimeFormatter
//                            .ofPattern("ha\nM/d")
//                            .format(convertToEpochMillis(mami.date, mami.time))
//                    )
//                }
//            )
//        }
    }
}

val patientBios = listOf(
    Bio(
        id = 1,
        patientId = 1,
        patientCode = "PAT_1",
        date = "2024-11-27",
        time = "13:00:00",
        averageTemperature = 36.7,
        bloodPressure = "115/72",
        bloodSugar = 75,
        healthStatus = 70
    ),
    Bio(
        id = 1,
        patientId = 1,
        patientCode = "PAT_1",
        date = "2024-11-27",
        time = "12:00:00",
        averageTemperature = 36.8,
        bloodPressure = "118/75",
        bloodSugar = 77,
        healthStatus = 72
    ),
    Bio(
        id = 1,
        patientId = 1,
        patientCode = "PAT_1",
        date = "2024-11-27",
        time = "11:00:00",
        averageTemperature = 36.6,
        bloodPressure = "112/68",
        bloodSugar = 68,
        healthStatus = 62
    ),
    Bio(
        id = 1,
        patientId = 1,
        patientCode = "PAT_1",
        date = "2024-11-27",
        time = "10:00:00",
        averageTemperature = 36.8,
        bloodPressure = "110/70",
        bloodSugar = 77,
        healthStatus = 71
    ),
    Bio(
        id = 1,
        patientId = 1,
        patientCode = "PAT_1",
        date = "2024-11-27",
        time = "09:00:00",
        averageTemperature = 36.9,
        bloodPressure = "126/85",
        bloodSugar = 80,
        healthStatus = 80
    ),
    Bio(
        id = 1,
        patientId = 1,
        patientCode = "PAT_1",
        date = "2024-11-27",
        time = "08:00:00",
        averageTemperature = 36.7,
        bloodPressure = "115/72",
        bloodSugar = 75,
        healthStatus = 70
    ),
    Bio(
        id = 1,
        patientId = 1,
        patientCode = "PAT_1",
        date = "2024-11-27",
        time = "07:00:00",
        averageTemperature = 36.7,
        bloodPressure = "115/72",
        bloodSugar = 75,
        healthStatus = 70
    ),
    Bio(
        id = 1,
        patientId = 1,
        patientCode = "PAT_1",
        date = "2024-11-27",
        time = "06:00:00",
        averageTemperature = 36.8,
        bloodPressure = "118/75",
        bloodSugar = 77,
        healthStatus = 72
    ),
    Bio(
        id = 1,
        patientId = 1,
        patientCode = "PAT_1",
        date = "2024-11-27",
        time = "05:00:00",
        averageTemperature = 36.6,
        bloodPressure = "112/68",
        bloodSugar = 68,
        healthStatus = 62
    ),
    Bio(
        id = 1,
        patientId = 1,
        patientCode = "PAT_1",
        date = "2024-11-27",
        time = "04:00:00",
        averageTemperature = 36.8,
        bloodPressure = "110/70",
        bloodSugar = 77,
        healthStatus = 71
    ),
    Bio(
        id = 1,
        patientId = 1,
        patientCode = "PAT_1",
        date = "2024-11-27",
        time = "03:00:00",
        averageTemperature = 36.9,
        bloodPressure = "126/85",
        bloodSugar = 80,
        healthStatus = 80
    ),
    Bio(
        id = 1,
        patientId = 1,
        patientCode = "PAT_1",
        date = "2024-11-27",
        time = "02:00:00",
        averageTemperature = 36.7,
        bloodPressure = "115/72",
        bloodSugar = 75,
        healthStatus = 70
    ),
    Bio(
        id = 1,
        patientId = 1,
        patientCode = "PAT_1",
        date = "2024-11-27",
        time = "01:00:00",
        averageTemperature = 36.7,
        bloodPressure = "115/72",
        bloodSugar = 75,
        healthStatus = 70
    ),
    Bio(
        id = 1,
        patientId = 1,
        patientCode = "PAT_1",
        date = "2024-11-27",
        time = "00:00:00",
        averageTemperature = 36.8,
        bloodPressure = "118/75",
        bloodSugar = 77,
        healthStatus = 72
    ),
    Bio(
        id = 1,
        patientId = 1,
        patientCode = "PAT_1",
        date = "2024-11-26",
        time = "23:00:00",
        averageTemperature = 36.6,
        bloodPressure = "112/68",
        bloodSugar = 68,
        healthStatus = 62
    ),
    Bio(
        id = 1,
        patientId = 1,
        patientCode = "PAT_1",
        date = "2024-11-26",
        time = "22:00:00",
        averageTemperature = 36.8,
        bloodPressure = "110/70",
        bloodSugar = 77,
        healthStatus = 71
    ),
    Bio(
        id = 1,
        patientId = 1,
        patientCode = "PAT_1",
        date = "2024-11-26",
        time = "21:00:00",
        averageTemperature = 36.9,
        bloodPressure = "126/85",
        bloodSugar = 80,
        healthStatus = 80
    ),
    Bio(
        id = 1,
        patientId = 1,
        patientCode = "PAT_1",
        date = "2024-11-26",
        time = "20:00:00",
        averageTemperature = 36.7,
        bloodPressure = "115/72",
        bloodSugar = 75,
        healthStatus = 70
    ),
    Bio(
        id = 1,
        patientId = 1,
        patientCode = "PAT_1",
        date = "2024-11-26",
        time = "19:00:00",
        averageTemperature = 36.7,
        bloodPressure = "115/72",
        bloodSugar = 75,
        healthStatus = 70
    ),
    Bio(
        id = 1,
        patientId = 1,
        patientCode = "PAT_1",
        date = "2024-11-26",
        time = "18:00:00",
        averageTemperature = 36.8,
        bloodPressure = "118/75",
        bloodSugar = 77,
        healthStatus = 72
    ),
    Bio(
        id = 1,
        patientId = 1,
        patientCode = "PAT_1",
        date = "2024-11-26",
        time = "17:00:00",
        averageTemperature = 36.6,
        bloodPressure = "112/68",
        bloodSugar = 68,
        healthStatus = 62
    ),
    Bio(
        id = 1,
        patientId = 1,
        patientCode = "PAT_1",
        date = "2024-11-26",
        time = "16:00:00",
        averageTemperature = 36.8,
        bloodPressure = "110/70",
        bloodSugar = 77,
        healthStatus = 71
    ),
    Bio(
        id = 1,
        patientId = 1,
        patientCode = "PAT_1",
        date = "2024-11-26",
        time = "15:00:00",
        averageTemperature = 36.9,
        bloodPressure = "126/85",
        bloodSugar = 80,
        healthStatus = 80
    ),
    Bio(
        id = 1,
        patientId = 1,
        patientCode = "PAT_1",
        date = "2024-11-26",
        time = "14:00:00",
        averageTemperature = 36.7,
        bloodPressure = "115/72",
        bloodSugar = 75,
        healthStatus = 70
    ),
    Bio(
        id = 1,
        patientId = 1,
        patientCode = "PAT_1",
        date = "2024-11-26",
        time = "12:00:00",
        averageTemperature = 36.7,
        bloodPressure = "115/72",
        bloodSugar = 75,
        healthStatus = 70
    ),
    Bio(
        id = 1,
        patientId = 1,
        patientCode = "PAT_1",
        date = "2024-11-26",
        time = "11:00:00",
        averageTemperature = 36.8,
        bloodPressure = "118/75",
        bloodSugar = 77,
        healthStatus = 72
    ),
    Bio(
        id = 1,
        patientId = 1,
        patientCode = "PAT_1",
        date = "2024-11-26",
        time = "10:00:00",
        averageTemperature = 36.6,
        bloodPressure = "112/68",
        bloodSugar = 68,
        healthStatus = 62
    ),
    Bio(
        id = 1,
        patientId = 1,
        patientCode = "PAT_1",
        date = "2024-11-26",
        time = "09:00:00",
        averageTemperature = 36.8,
        bloodPressure = "110/70",
        bloodSugar = 77,
        healthStatus = 71
    ),
    Bio(
        id = 1,
        patientId = 1,
        patientCode = "PAT_1",
        date = "2024-11-26",
        time = "08:00:00",
        averageTemperature = 36.9,
        bloodPressure = "126/85",
        bloodSugar = 80,
        healthStatus = 80
    ),
    Bio(
        id = 1,
        patientId = 1,
        patientCode = "PAT_1",
        date = "2024-11-26",
        time = "07:00:00",
        averageTemperature = 36.7,
        bloodPressure = "115/72",
        bloodSugar = 75,
        healthStatus = 70
    ),
    Bio(
        id = 1,
        patientId = 1,
        patientCode = "PAT_1",
        date = "2024-11-26",
        time = "06:00:00",
        averageTemperature = 36.7,
        bloodPressure = "115/72",
        bloodSugar = 75,
        healthStatus = 70
    ),
    Bio(
        id = 1,
        patientId = 1,
        patientCode = "PAT_1",
        date = "2024-11-26",
        time = "05:00:00",
        averageTemperature = 36.8,
        bloodPressure = "118/75",
        bloodSugar = 77,
        healthStatus = 72
    ),
    Bio(
        id = 1,
        patientId = 1,
        patientCode = "PAT_1",
        date = "2024-11-26",
        time = "04:00:00",
        averageTemperature = 36.6,
        bloodPressure = "112/68",
        bloodSugar = 68,
        healthStatus = 62
    ),
    Bio(
        id = 1,
        patientId = 1,
        patientCode = "PAT_1",
        date = "2024-11-26",
        time = "03:00:00",
        averageTemperature = 36.8,
        bloodPressure = "110/70",
        bloodSugar = 77,
        healthStatus = 71
    ),
    Bio(
        id = 1,
        patientId = 1,
        patientCode = "PAT_1",
        date = "2024-11-26",
        time = "02:00:00",
        averageTemperature = 36.9,
        bloodPressure = "126/85",
        bloodSugar = 80,
        healthStatus = 80
    ),
    Bio(
        id = 1,
        patientId = 1,
        patientCode = "PAT_1",
        date = "2024-11-26",
        time = "01:00:00",
        averageTemperature = 36.7,
        bloodPressure = "115/72",
        bloodSugar = 75,
        healthStatus = 70
    ),
    Bio(
        id = 1,
        patientId = 1,
        patientCode = "PAT_1",
        date = "2024-11-26",
        time = "00:00:00",
        averageTemperature = 36.7,
        bloodPressure = "115/72",
        bloodSugar = 75,
        healthStatus = 70
    ),
    Bio(
        id = 1,
        patientId = 1,
        patientCode = "PAT_1",
        date = "2024-11-25",
        time = "23:00:00",
        averageTemperature = 36.8,
        bloodPressure = "118/75",
        bloodSugar = 77,
        healthStatus = 72
    ),
    Bio(
        id = 1,
        patientId = 1,
        patientCode = "PAT_1",
        date = "2024-11-25",
        time = "22:00:00",
        averageTemperature = 36.6,
        bloodPressure = "112/68",
        bloodSugar = 68,
        healthStatus = 62
    ),
    Bio(
        id = 1,
        patientId = 1,
        patientCode = "PAT_1",
        date = "2024-11-25",
        time = "21:00:00",
        averageTemperature = 36.8,
        bloodPressure = "110/70",
        bloodSugar = 77,
        healthStatus = 71
    ),
    Bio(
        id = 1,
        patientId = 1,
        patientCode = "PAT_1",
        date = "2024-11-25",
        time = "20:00:00",
        averageTemperature = 36.9,
        bloodPressure = "126/85",
        bloodSugar = 80,
        healthStatus = 80
    ),
    Bio(
        id = 1,
        patientId = 1,
        patientCode = "PAT_1",
        date = "2024-11-25",
        time = "19:00:00",
        averageTemperature = 36.7,
        bloodPressure = "115/72",
        bloodSugar = 75,
        healthStatus = 70
    ),
).sortedBy { it.date + it.time }