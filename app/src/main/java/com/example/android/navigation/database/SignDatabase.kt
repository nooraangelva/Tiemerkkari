package com.example.android.navigation.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.time.chrono.HijrahChronology
import java.time.chrono.HijrahChronology.INSTANCE

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
/*
    private class WordDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            HijrahChronology.INSTANCE?.let { database ->
                scope.launch {
                    populateDatabase(database.signDatabase())
                }
            }
        }

        suspend fun populateDatabase(wordDao: SignDatabase) {
            // Delete all content here.
            wordDao.deleteAll()

            // Add sample words.
            var word = Word("Hello")
            wordDao.insert(word)
            word = Word("World!")
            wordDao.insert(word)

            // TODO: Add your own words!
        }
    }*/
}




//TODO unit tests