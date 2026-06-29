package com.signal.app.data.remote

import com.signal.app.domain.model.Lead

interface LeadApi {
    suspend fun fetchLeads(category: String, radius: Float): List<Lead>
}
