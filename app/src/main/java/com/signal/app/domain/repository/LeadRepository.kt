package com.signal.app.domain.repository

import com.signal.app.domain.model.Lead
import com.signal.app.domain.model.UserProfile
import kotlinx.coroutines.flow.Flow

interface LeadRepository {
    fun getAllLeads(): Flow<List<Lead>>
    suspend fun insertLeads(leads: List<Lead>)
    suspend fun updateLeadStatus(id: String, status: String)
    suspend fun updateLeadDraft(id: String, draft: String)
    
    fun getUserProfile(): Flow<UserProfile>
    suspend fun saveUserProfile(profile: UserProfile)
}
