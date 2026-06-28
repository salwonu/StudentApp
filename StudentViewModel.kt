// File: viewmodel/StudentViewModel.kt
package com.example.studentapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.studentapp.data.AppDatabase
import com.example.studentapp.data.Siswa
import com.example.studentapp.data.SiswaRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

// Data class untuk menyimpan semua UI State sekaligus
data class StudentUiState(
    val listSiswa: List<Siswa> = emptyList(),
    val siswaYangDiedit: Siswa? = null,   // null = mode tambah, isi = mode edit
    val isFormVisible: Boolean = false,
    val nama: String = "",
    val nim: String = "",
    val email: String = "",
    val jurusan: String = "",
    // Error messages untuk validasi
    val namaError: String? = null,
    val nimError: String? = null,
    val emailError: String? = null,
    val jurusanError: String? = null,
    val pesanSukses: String? = null
)

class StudentViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: SiswaRepository
    private val _uiState = MutableStateFlow(StudentUiState())
    val uiState: StateFlow<StudentUiState> = _uiState.asStateFlow()

    init {
        // Inisialisasi database dan repository
        val siswaDao = AppDatabase.getDatabase(application).siswaDao()
        repository = SiswaRepository(siswaDao)

        // Collect data dari Flow dan update UI State
        viewModelScope.launch {
            repository.semuaSiswa.collect { listSiswa ->
                _uiState.update { it.copy(listSiswa = listSiswa) }
            }
        }
    }

    // ─── Form Actions ───────────────────────────────────────────────

    fun tampilkanFormTambah() {
        _uiState.update {
            it.copy(
                isFormVisible = true,
                siswaYangDiedit = null,
                nama = "", nim = "", email = "", jurusan = "",
                namaError = null, nimError = null,
                emailError = null, jurusanError = null
            )
        }
    }

    fun tampilkanFormEdit(siswa: Siswa) {
        _uiState.update {
            it.copy(
                isFormVisible = true,
                siswaYangDiedit = siswa,
                nama = siswa.nama,
                nim = siswa.nim,
                email = siswa.email,
                jurusan = siswa.jurusan,
                namaError = null, nimError = null,
                emailError = null, jurusanError = null
            )
        }
    }

    fun sembunyikanForm() {
        _uiState.update { it.copy(isFormVisible = false) }
    }

    // ─── Field Update ────────────────────────────────────────────────

    fun onNamaChange(value: String) {
        _uiState.update { it.copy(nama = value, namaError = null) }
    }

    fun onNimChange(value: String) {
        _uiState.update { it.copy(nim = value, nimError = null) }
    }

    fun onEmailChange(value: String) {
        _uiState.update { it.copy(email = value, emailError = null) }
    }

    fun onJurusanChange(value: String) {
        _uiState.update { it.copy(jurusan = value, jurusanError = null) }
    }

    // ─── Validasi ────────────────────────────────────────────────────

    private fun validasiForm(): Boolean {
        val state = _uiState.value
        var valid = true

        val namaError = if (state.nama.isBlank()) {
            valid = false; "Nama tidak boleh kosong"
        } else null

        val nimError = if (state.nim.isBlank()) {
            valid = false; "NIM tidak boleh kosong"
        } else null

        val emailError = when {
            state.email.isBlank() -> { valid = false; "Email tidak boleh kosong" }
            !state.email.contains("@") -> { valid = false; "Format email tidak valid (harus mengandung @)" }
            else -> null
        }

        val jurusanError = if (state.jurusan.isBlank()) {
            valid = false; "Jurusan tidak boleh kosong"
        } else null

        _uiState.update {
            it.copy(
                namaError = namaError,
                nimError = nimError,
                emailError = emailError,
                jurusanError = jurusanError
            )
        }

        return valid
    }

    // ─── CRUD Operations ─────────────────────────────────────────────

    fun simpanSiswa() {
        if (!validasiForm()) return  // Berhenti jika validasi gagal

        val state = _uiState.value
        viewModelScope.launch {
            val siswa = Siswa(
                id = state.siswaYangDiedit?.id ?: 0,  // 0 = baru, isi = update
                nama = state.nama.trim(),
                nim = state.nim.trim(),
                email = state.email.trim(),
                jurusan = state.jurusan.trim()
            )

            if (state.siswaYangDiedit == null) {
                repository.tambahSiswa(siswa)
                _uiState.update { it.copy(pesanSukses = "Siswa berhasil ditambahkan!") }
            } else {
                repository.perbaruiSiswa(siswa)
                _uiState.update { it.copy(pesanSukses = "Data siswa berhasil diperbarui!") }
            }

            sembunyikanForm()
        }
    }

    fun hapusSiswa(siswa: Siswa) {
        viewModelScope.launch {
            repository.hapusSiswa(siswa)
            _uiState.update { it.copy(pesanSukses = "Siswa ${siswa.nama} berhasil dihapus") }
        }
    }

    fun bersihkanPesan() {
        _uiState.update { it.copy(pesanSukses = null) }
    }
}
