// File: ui/MainScreen.kt
package com.example.studentapp.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.studentapp.data.Siswa
import com.example.studentapp.viewmodel.StudentUiState
import com.example.studentapp.viewmodel.StudentViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(viewModel: StudentViewModel) {
    // collectAsState() → rekomposisi otomatis saat uiState berubah
    val uiState by viewModel.uiState.collectAsState()

    // Tampilkan Snackbar untuk pesan sukses
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(uiState.pesanSukses) {
        uiState.pesanSukses?.let { pesan ->
            snackbarHostState.showSnackbar(pesan)
            viewModel.bersihkanPesan()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "StudentApp",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        },
        floatingActionButton = {
            // FAB hanya muncul saat form tersembunyi
            if (!uiState.isFormVisible) {
                FloatingActionButton(
                    onClick = { viewModel.tampilkanFormTambah() }
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Tambah Siswa")
                }
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Tampilkan form jika isFormVisible = true
            if (uiState.isFormVisible) {
                FormInput(
                    uiState = uiState,
                    onNamaChange = viewModel::onNamaChange,
                    onNimChange = viewModel::onNimChange,
                    onEmailChange = viewModel::onEmailChange,
                    onJurusanChange = viewModel::onJurusanChange,
                    onSimpan = viewModel::simpanSiswa,
                    onBatal = viewModel::sembunyikanForm
                )
                HorizontalDivider(modifier = Modifier.padding(top = 8.dp))
            }

            // Info jumlah siswa
            if (uiState.listSiswa.isNotEmpty()) {
                Text(
                    text = "Total: ${uiState.listSiswa.size} siswa terdaftar",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }

            // Daftar siswa atau pesan kosong
            if (uiState.listSiswa.isEmpty() && !uiState.isFormVisible) {
                EmptyState()
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    items(
                        items = uiState.listSiswa,
                        key = { it.id }  // Key penting untuk animasi dan performa
                    ) { siswa ->
                        StudentItem(
                            siswa = siswa,
                            onEditClick = { viewModel.tampilkanFormEdit(it) },
                            onDeleteClick = { viewModel.hapusSiswa(it) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun EmptyState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "📚",
                style = MaterialTheme.typography.displayLarge
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Belum ada siswa terdaftar",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "Tap tombol + untuk menambahkan siswa baru",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 32.dp, vertical = 4.dp)
            )
        }
    }
}
