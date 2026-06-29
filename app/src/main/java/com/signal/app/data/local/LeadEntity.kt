package com.signal.app.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "leads")
data class LeadEntity(
    @PrimaryKey val id: String,
    val platform: String,
    val timeAgo: String,
    val distance: String,
    val intentScore: String,
    val postSnippet: String,
    val aiDraftReply: String,
    val status: String
)
