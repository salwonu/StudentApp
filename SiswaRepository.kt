// File: data/SiswaRepository.kt
package com.example.studentapp.data

import kotlinx.coroutines.flow.Flow

class SiswaRepository(private val siswaDao: SiswaDao) {

    // Expose Flow langsung dari DAO
    val semuaSiswa: Flow<List<Siswa>> = siswaDao.getAllSiswa()

    // Fungsi-fungsi ini suspend karena perlu Coroutine scope
    suspend fun tambahSiswa(siswa: Siswa) {
        siswaDao.insertSiswa(siswa)
    }

    suspend fun perbaruiSiswa(siswa: Siswa) {
        siswaDao.updateSiswa(siswa)
    }

    suspend fun hapusSiswa(siswa: Siswa) {
        siswaDao.deleteSiswa(siswa)
    }
}
