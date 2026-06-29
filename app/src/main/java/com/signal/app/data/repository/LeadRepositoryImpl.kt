package com.signal.app.data.repository

import com.signal.app.data.local.LeadDao
import com.signal.app.data.local.LeadEntity
import com.signal.app.data.local.UserProfileEntity
import com.signal.app.domain.model.Lead
import com.signal.app.domain.model.UserProfile
import com.signal.app.domain.repository.LeadRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LeadRepositoryImpl(private val leadDao: LeadDao) : LeadRepository {

    override fun getAllLeads(): Flow<List<Lead>> {
        return leadDao.getAllLeads().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun insertLeads(leads: List<Lead>) {
        leadDao.insertLeads(leads.map { it.toEntity() })
    }

    override suspend fun updateLeadStatus(id: String, status: String) {
        leadDao.updateLeadStatus(id, status)
    }

    override suspend fun updateLeadDraft(id: String, draft: String) {
        leadDao.updateLeadDraft(id, draft)
    }

    override fun getUserProfile(): Flow<UserProfile> {
        return leadDao.getUserProfile().map { entity ->
            entity?.toDomain() ?: UserProfile()
        }
    }

    override suspend fun saveUserProfile(profile: UserProfile) {
        leadDao.insertUserProfile(profile.toEntity())
    }

    private fun LeadEntity.toDomain() = Lead(
        id = id,
        platform = platform,
        timeAgo = timeAgo,
        distance = distance,
        intentScore = intentScore,
        postSnippet = postSnippet,
        aiDraftReply = aiDraftReply,
        status = status
    )

    private fun Lead.toEntity() = LeadEntity(
        id = id,
        platform = platform,
        timeAgo = timeAgo,
        distance = distance,
        intentScore = intentScore,
        postSnippet = postSnippet,
        aiDraftReply = aiDraftReply,
        status = status
    )

    private fun UserProfileEntity.toDomain() = UserProfile(
        businessName = businessName,
        serviceCategory = serviceCategory,
        radius = radius,
        positiveKeywords = positiveKeywords.split(",").filter { it.isNotEmpty() },
        negativeKeywords = negativeKeywords.split(",").filter { it.isNotEmpty() }
    )

    private fun UserProfile.toEntity() = UserProfileEntity(
        businessName = businessName,
        serviceCategory = serviceCategory,
        radius = radius,
        positiveKeywords = positiveKeywords.joinToString(","),
        negativeKeywords = negativeKeywords.joinToString(",")
    )
}
