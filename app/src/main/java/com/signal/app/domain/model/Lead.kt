package com.signal.app.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Lead(
    val id: String,
    val platform: String,
    val timeAgo: String,
    val distance: String,
    val intentScore: String, // "HIGH", "MEDIUM", "LOW"
    val postSnippet: String,
    val aiDraftReply: String,
    val status: String // "NEW", "WON", "LOST"
)
