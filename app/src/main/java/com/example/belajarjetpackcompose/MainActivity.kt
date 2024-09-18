package com.example.belajarjetpackcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.belajarjetpackcompose.ui.theme.BelajarJetpackComposeTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.belajarjetpackcompose.ui.HomeScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BelajarJetpackComposeTheme {
               HomeScreen(viewModel = viewModel())
            }
        }
    }
}

