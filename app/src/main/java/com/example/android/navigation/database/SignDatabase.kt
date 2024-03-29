package com.example.android.navigation.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// Database creation
@Database(entities = [Signs::class, Instructions::class], version = 2,  exportSchema = false)
abstract class SignDatabase : RoomDatabase() {

    abstract val signDatabaseDao: SignDatabaseDao

    companion object {

        @Volatile
        private var INSTANCE: SignDatabase? = null

        fun getInstance(context: Context): SignDatabase {

            synchronized(this) {

                var instance = INSTANCE

                if (instance == null) {

                    instance = Room.databaseBuilder(
                            context.applicationContext,
                            SignDatabase::class.java,
                            "signs_database"
                            )
                        .fallbackToDestructiveMigration()
                        //.addCallback(WordDatabaseCallback(scope))
                        .build()

                }

                INSTANCE = instance

                return instance

            }

        }

    }

}
