// File: data/Siswa.kt
package com.example.studentapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "siswa")
data class Siswa(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nama: String,
    val nim: String,
    val email: String,
    val jurusan: String
)
