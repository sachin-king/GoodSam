package com.sachin.goodsam.presentation.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sachin.goodsam.domain.usecase.RegisterDeviceUseCase
import com.sachin.goodsam.util.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SplashViewModel(private val registerDevice: RegisterDeviceUseCase) : ViewModel() {

    private val _state = MutableStateFlow<UiState<Unit>>(UiState.Idle)
    val state: StateFlow<UiState<Unit>> = _state.asStateFlow()

    fun start() {
        _state.value = UiState.Loading
        viewModelScope.launch {
            registerDevice().fold(
                onSuccess = { _state.value = UiState.Success(Unit) },
                onFailure = { _state.value = UiState.Error(it.message ?: "Something went wrong") }
            )
        }
    }
}