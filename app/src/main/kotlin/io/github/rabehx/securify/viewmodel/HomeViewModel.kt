package io.github.rabehx.securify.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.rabehx.securify.utils.NetworkResult
import io.github.rabehx.securify.network.model.IntegrityResult
import io.github.rabehx.securify.repository.IntegrityRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: IntegrityRepository,
) : ViewModel() {

    private val integrityFlow = MutableStateFlow<NetworkResult<IntegrityResult>?>(null)
    val integrity: StateFlow<NetworkResult<IntegrityResult>?> = integrityFlow.asStateFlow()

    fun checkPlayIntegrity() {
        viewModelScope.launch {
            repository.checkPlayIntegrity().collect {
                integrityFlow.value = it
            }
        }
    }

    fun resetState() {
        integrityFlow.value = null
    }
}
