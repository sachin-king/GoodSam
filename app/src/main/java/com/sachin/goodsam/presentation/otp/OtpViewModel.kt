package com.sachin.goodsam.presentation.otp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sachin.goodsam.domain.usecase.SignInUseCase
import com.sachin.goodsam.domain.usecase.VerifyOtpUseCase
import com.sachin.goodsam.util.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class OtpViewModel(
    private val verifyOtp: VerifyOtpUseCase,
    private val signIn: SignInUseCase
) : ViewModel() {

    private var mobile = ""
    private var password = ""

    private val _state = MutableStateFlow<UiState<Unit>>(UiState.Idle)
    val state: StateFlow<UiState<Unit>> = _state.asStateFlow()

    fun setup(mobile: String, password: String) {
        if (this.mobile.isEmpty()) {
            this.mobile = mobile
            this.password = password
        }
    }

    fun verify(otp: String) {
        _state.value = UiState.Loading
        viewModelScope.launch {
            verifyOtp(mobile, otp, password).fold(
                onSuccess = { _state.value = UiState.Success(Unit) },
                onFailure = { _state.value = UiState.Error(it.message ?: "Incorrect Otp") }
            )
        }
    }

    fun resend() {
        _state.value = UiState.Loading
        viewModelScope.launch {
            signIn(mobile).fold(
                onSuccess = { password = it; _state.value = UiState.Idle },
                onFailure = { _state.value = UiState.Error(it.message ?: "Resend fail ") }
            )
        }
    }

    fun consumed() { _state.value = UiState.Idle }
}