package com.example.trackercompanion.ui.roster

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.trackercompanion.model.Wrestler

@Composable
fun AddEditWrestlerScreen(onSave: (Wrestler)->Unit, onBack: ()->Unit) {
    Text(text = "Add-Edit")
}