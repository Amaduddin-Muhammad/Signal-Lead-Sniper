package com.signal.app.ui.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.signal.app.domain.model.Lead
import com.signal.app.domain.repository.LeadRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FeedViewModel(private val repository: LeadRepository) : ViewModel() {

    private val _isScanning = MutableStateFlow(true)
    val isScanning: StateFlow<Boolean> = _isScanning.asStateFlow()

    val leadsState: StateFlow<List<Lead>> = repository.getAllLeads()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun toggleScanning(active: Boolean) {
        _isScanning.value = active
    }

    fun updateLeadStatus(id: String, status: String) {
        viewModelScope.launch {
            repository.updateLeadStatus(id, status)
        }
    }

    fun updateLeadDraft(id: String, draft: String) {
        viewModelScope.launch {
            repository.updateLeadDraft(id, draft)
        }
    }

    class Factory(private val repository: LeadRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return FeedViewModel(repository) as T
        }
    }
}
