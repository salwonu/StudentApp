// File: data/SiswaDao.kt
package com.example.studentapp.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface SiswaDao {

    // INSERT — Tambah siswa baru, jika sudah ada ganti (REPLACE)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSiswa(siswa: Siswa)

    // UPDATE — Perbarui data siswa yang sudah ada
    @Update
    suspend fun updateSiswa(siswa: Siswa)

    // DELETE — Hapus satu siswa berdasarkan objek
    @Delete
    suspend fun deleteSiswa(siswa: Siswa)

    // SELECT ALL — Ambil semua siswa, diurutkan nama A-Z
    // Flow<> → otomatis update UI saat data berubah (reaktif)
    @Query("SELECT * FROM siswa ORDER BY nama ASC")
    fun getAllSiswa(): Flow<List<Siswa>>

    // SELECT BY ID — Cari siswa berdasarkan ID
    @Query("SELECT * FROM siswa WHERE id = :id")
    suspend fun getSiswaById(id: Int): Siswa?
}
