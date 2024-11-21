package com.acoding.hospital.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.acoding.hospital.data.repo.HospitalRepo
import com.acoding.hospital.domain.util.NetworkError
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class LoginUiState(
    val isLoading: Boolean = false,
    val username: String = "",
    val password: String = "",
    val showHomeScreen: Boolean = false
)

sealed class LoginEvent {
    data class ShowError(val message: NetworkError) : LoginEvent()
}

class LoginViewModel(
    private val repo: HospitalRepo
) : ViewModel() {


    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

    private val _event = Channel<LoginEvent>()
    val event = _event.receiveAsFlow()

    fun onUsernameChanged(username: CharSequence) {
        _uiState.update { it.copy(username = username.toString()) }
    }

    fun onPasswordChanged(password: CharSequence) {
        _uiState.update { it.copy(password = password.toString()) }
    }

    fun login() {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
//            repo.login(
//                username = _uiState.value.username,
//                password = _uiState.value.password
//            ).onSuccess { response ->
//                _uiState.update { it.copy(isLoading = false, showHomeScreen = true) }
//                val user = response
//                LoginDataStore.initialize(response)
//            }.onError {
//                _uiState.update { it.copy(isLoading = false) }
//                _event.send(LoginEvent.ShowError(it))
//            }
            _uiState.update { it.copy(isLoading = false, showHomeScreen = true) }
        }
    }
}