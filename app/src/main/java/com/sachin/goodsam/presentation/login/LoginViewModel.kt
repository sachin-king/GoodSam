package com.sachin.goodsam.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sachin.goodsam.domain.usecase.SignInUseCase
import com.sachin.goodsam.util.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(private val signIn: SignInUseCase) : ViewModel() {

    private val _state = MutableStateFlow<UiState<String>>(UiState.Idle)
    val state: StateFlow<UiState<String>> = _state.asStateFlow()

    fun login(mobile: String) {
        _state.value = UiState.Loading
        viewModelScope.launch {
            signIn(mobile).fold(
                onSuccess = { _state.value = UiState.Success(it) }, // it = password
                onFailure = { _state.value = UiState.Error(it.message ?: "Login fail ") }
            )
        }
    }

    fun consumed() { _state.value = UiState.Idle }
}