package com.signal.app

import android.app.Application
import androidx.room.Room
import com.signal.app.data.local.AppDatabase
import com.signal.app.data.remote.LeadApi
import com.signal.app.data.remote.LeadApiImpl
import com.signal.app.data.repository.LeadRepositoryImpl
import com.signal.app.domain.repository.LeadRepository

class SignalApplication : Application() {

    lateinit var database: AppDatabase
    lateinit var leadApi: LeadApi
    lateinit var repository: LeadRepository

    override fun onCreate() {
        super.onCreate()
        instance = this

        database = Room.databaseBuilder(
            this,
            AppDatabase::class.java,
            "signal_database"
        ).fallbackToDestructiveMigration().build()

        leadApi = LeadApiImpl()
        repository = LeadRepositoryImpl(database.leadDao())
    }

    companion object {
        lateinit var instance: SignalApplication
            private set
    }
}
