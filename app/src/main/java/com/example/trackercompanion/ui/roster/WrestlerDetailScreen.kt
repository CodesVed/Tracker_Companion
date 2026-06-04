package com.example.trackercompanion.ui.roster

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.trackercompanion.model.Wrestler

@Composable
fun WrestlerDetailScreen(wrestler: Wrestler, onEditClick: ()->Unit, onBackClick: ()->Unit) {
    Text("Details")
}