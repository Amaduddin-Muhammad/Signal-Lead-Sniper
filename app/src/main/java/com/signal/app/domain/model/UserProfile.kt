package com.signal.app.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class UserProfile(
    val businessName: String = "",
    val serviceCategory: String = "",
    val radius: Float = 15f, // miles
    val positiveKeywords: List<String> = emptyList(),
    val negativeKeywords: List<String> = emptyList()
)
