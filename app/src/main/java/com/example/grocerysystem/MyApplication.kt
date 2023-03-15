package com.example.grocerysystem

import android.app.Application
import com.example.grocerysystem.db.database.AppDatabase
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        AppDatabase.createInstance(this); //--AppDatabase_Impl does not exist
    }
}