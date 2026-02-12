package com.collectoslot

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.collectoslot.engine.Screen
import com.collectoslot.engine.SlotViewModel
import com.collectoslot.ui.CollectoSlotScreen
import com.collectoslot.ui.CollectionScreen
import com.collectoslot.ui.theme.CollectoSlotTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CollectoSlotTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val viewModel: SlotViewModel = viewModel()
                    val currentScreen by viewModel.currentScreen.collectAsState()
                    val state by viewModel.state.collectAsState()

                    when (currentScreen) {
                        Screen.SLOT -> CollectoSlotScreen(
                            viewModel = viewModel,
                            modifier = Modifier.padding(innerPadding)
                        )
                        Screen.COLLECTION -> CollectionScreen(
                            collection = state.collection,
                            onBack = { viewModel.navigateTo(Screen.SLOT) },
                            modifier = Modifier.padding(innerPadding)
                        )
                    }
                }
            }
        }
    }
}
