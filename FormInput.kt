// File: ui/FormInput.kt
package com.example.studentapp.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.studentapp.viewmodel.StudentUiState

@Composable
fun FormInput(
    uiState: StudentUiState,
    onNamaChange: (String) -> Unit,
    onNimChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onJurusanChange: (String) -> Unit,
    onSimpan: () -> Unit,
    onBatal: () -> Unit
) {
    val isEditMode = uiState.siswaYangDiedit != null
    val judul = if (isEditMode) "Edit Data Siswa" else "Tambah Siswa Baru"

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = judul,
            style = MaterialTheme.typography.headlineSmall
        )

        // Field Nama
        OutlinedTextField(
            value = uiState.nama,
            onValueChange = onNamaChange,
            label = { Text("Nama Lengkap *") },
            isError = uiState.namaError != null,
            supportingText = {
                uiState.namaError?.let { Text(it, color = MaterialTheme.colorScheme.error) }
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        // Field NIM
        OutlinedTextField(
            value = uiState.nim,
            onValueChange = onNimChange,
            label = { Text("NIM *") },
            isError = uiState.nimError != null,
            supportingText = {
                uiState.nimError?.let { Text(it, color = MaterialTheme.colorScheme.error) }
            },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true
        )

        // Field Email
        OutlinedTextField(
            value = uiState.email,
            onValueChange = onEmailChange,
            label = { Text("Email *") },
            isError = uiState.emailError != null,
            supportingText = {
                uiState.emailError?.let { Text(it, color = MaterialTheme.colorScheme.error) }
            },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            singleLine = true
        )

        // Field Jurusan
        OutlinedTextField(
            value = uiState.jurusan,
            onValueChange = onJurusanChange,
            label = { Text("Jurusan *") },
            isError = uiState.jurusanError != null,
            supportingText = {
                uiState.jurusanError?.let { Text(it, color = MaterialTheme.colorScheme.error) }
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Tombol Simpan dan Batal
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = onBatal,
                modifier = Modifier.weight(1f)
            ) {
                Text("Batal")
            }

            Button(
                onClick = onSimpan,
                modifier = Modifier.weight(1f)
            ) {
                Text(if (isEditMode) "Perbarui" else "Simpan")
            }
        }
    }
}
