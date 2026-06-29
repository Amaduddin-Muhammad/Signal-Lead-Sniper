package com.signal.app.workers

import android.content.Context
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkerParameters
import com.signal.app.SignalApplication
import kotlinx.coroutines.flow.first
import java.util.concurrent.TimeUnit

class LeadSyncWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            val repository = SignalApplication.instance.repository
            val api = SignalApplication.instance.leadApi
            
            // Get user's category and radius to fetch tailored leads
            val profile = repository.getUserProfile().first()
            
            // Fetch 3 random leads from mock API
            val newLeads = api.fetchLeads(profile.serviceCategory, profile.radius)
            
            // Insert leads into database
            repository.insertLeads(newLeads)
            
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }

    companion object {
        fun schedule(context: Context) {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val workRequest = PeriodicWorkRequestBuilder<LeadSyncWorker>(15, TimeUnit.MINUTES)
                .setConstraints(constraints)
                .build()

            androidx.work.WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                "LeadSyncWork",
                androidx.work.ExistingPeriodicWorkPolicy.KEEP,
                workRequest
            )
        }
    }
}
