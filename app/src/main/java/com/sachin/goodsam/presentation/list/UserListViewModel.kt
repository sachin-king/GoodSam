package com.sachin.goodsam.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sachin.goodsam.domain.model.User
import com.sachin.goodsam.domain.usecase.GetUsersUseCase
import com.sachin.goodsam.util.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UserListViewModel(private val getUsers: GetUsersUseCase) : ViewModel() {

    private val _state = MutableStateFlow<UiState<List<User>>>(UiState.Idle)
    val state: StateFlow<UiState<List<User>>> = _state.asStateFlow()

    init { load() }

    fun load() {
        _state.value = UiState.Loading
        viewModelScope.launch {
            getUsers().fold(
                onSuccess = { _state.value = UiState.Success(it) },
                onFailure = { _state.value = UiState.Error(it.message ?: "Data did not load") }
            )
        }
    }
}