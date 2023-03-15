package com.example.grocerysystem.util

import android.content.Context
import androidx.datastore.preferences.clear
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.preferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserManager(context: Context) {

    // Create the dataStore and give it a name same as shared preferences
    private val dataStore = context.createDataStore(name = "user_prefs")

    // Create some keys we will use them to store and retrieve the data
    companion object {
        val USER_ID = preferencesKey<String>("USER_ID")
        val USER_NAME = preferencesKey<String>("USER_NAME")
    }

    // Store user data
    // refer to the data store and using edit
    // we can store values using the keys
    suspend fun storeUser(userId: String, name: String) {
        dataStore.edit {
            it[USER_ID] = userId
            it[USER_NAME] = name

        }
    }

    suspend fun removeUser() {
        dataStore.edit {
            it[USER_ID] = ""
            it[USER_NAME] = ""
            it.clear()
        }
    }


    val userIdFlow: Flow<String> = dataStore.data.map {
        it[USER_ID] ?: ""
    }

    // Create a name flow to retrieve name from the preferences
    val userNameFlow: Flow<String> = dataStore.data.map {
        it[USER_NAME] ?: ""
    }
}