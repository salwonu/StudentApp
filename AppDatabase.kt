// File: data/AppDatabase.kt
package com.example.studentapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [Siswa::class],  // Daftar semua Entity
    version = 1,                 // Versi database, naikkan jika ada perubahan skema
    exportSchema = false         // Nonaktifkan export schema untuk project sederhana
)
abstract class AppDatabase : RoomDatabase() {

    // Room generate implementasi SiswaDao secara otomatis
    abstract fun siswaDao(): SiswaDao

    companion object {
        // @Volatile → nilai INSTANCE selalu up-to-date di semua thread
        @Volatile
        private var INSTANCE: AppDatabase? = null

        // Singleton Pattern — pastikan hanya ada SATU instance database
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "student_database"   // Nama file .db di storage HP
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
