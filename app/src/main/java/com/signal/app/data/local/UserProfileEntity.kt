package com.signal.app.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey val id: Int = 1,
    val businessName: String,
    val serviceCategory: String,
    val radius: Float,
    val positiveKeywords: String, // comma-separated
    val negativeKeywords: String  // comma-separated
)
