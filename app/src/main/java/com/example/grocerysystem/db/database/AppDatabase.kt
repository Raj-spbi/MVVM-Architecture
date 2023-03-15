package com.example.grocerysystem.db.database

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.grocerysystem.db.dao.CartDao
import com.example.grocerysystem.db.entity.CartModel


@Database(entities = [CartModel::class],version = 1, exportSchema = false)
abstract class AppDatabase():RoomDatabase()
{
    abstract val cartDao:CartDao

    companion object{

        @Volatile
        private var INSTANCE:AppDatabase? = null

        fun createInstance(application: Application):AppDatabase
        {
            synchronized(this)
            {
                var instance = INSTANCE
                if(instance == null)
                {
                    instance = Room.databaseBuilder(
                        application.applicationContext,
                        AppDatabase::class.java,
                        "GroceryAppDb")
                        .fallbackToDestructiveMigration()
                        .build()
                }
                return instance
            }
        }
    }
}
