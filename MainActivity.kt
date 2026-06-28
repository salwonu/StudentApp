// File: MainActivity.kt
package com.example.studentapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.studentapp.ui.MainScreen
import com.example.studentapp.ui.theme.StudentAppTheme
import com.example.studentapp.viewmodel.StudentViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StudentAppTheme {
                // viewModel() secara otomatis membuat dan menyimpan ViewModel
                // ViewModel tidak akan dihancurkan saat rotate layar
                val viewModel: StudentViewModel = viewModel()
                MainScreen(viewModel = viewModel)
            }
        }
    }
}
