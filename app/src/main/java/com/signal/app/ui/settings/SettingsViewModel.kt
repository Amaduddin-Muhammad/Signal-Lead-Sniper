package com.signal.app.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.signal.app.domain.model.UserProfile
import com.signal.app.domain.repository.LeadRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(private val repository: LeadRepository) : ViewModel() {

    val userProfile: StateFlow<UserProfile> = repository.getUserProfile()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UserProfile()
        )

    fun saveProfile(profile: UserProfile) {
        viewModelScope.launch {
            repository.saveUserProfile(profile)
        }
    }

    fun addPositiveKeyword(keyword: String) {
        viewModelScope.launch {
            val current = userProfile.value
            if (keyword.isNotBlank() && !current.positiveKeywords.contains(keyword)) {
                repository.saveUserProfile(current.copy(positiveKeywords = current.positiveKeywords + keyword.trim()))
            }
        }
    }

    fun removePositiveKeyword(keyword: String) {
        viewModelScope.launch {
            val current = userProfile.value
            repository.saveUserProfile(current.copy(positiveKeywords = current.positiveKeywords - keyword))
        }
    }

    fun addNegativeKeyword(keyword: String) {
        viewModelScope.launch {
            val current = userProfile.value
            if (keyword.isNotBlank() && !current.negativeKeywords.contains(keyword)) {
                repository.saveUserProfile(current.copy(negativeKeywords = current.negativeKeywords + keyword.trim()))
            }
        }
    }

    fun removeNegativeKeyword(keyword: String) {
        viewModelScope.launch {
            val current = userProfile.value
            repository.saveUserProfile(current.copy(negativeKeywords = current.negativeKeywords - keyword))
        }
    }

    class Factory(private val repository: LeadRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return SettingsViewModel(repository) as T
        }
    }
}
