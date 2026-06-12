package com.example.trackercompanion.ui.roster

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.trackercompanion.model.Wrestler
import com.example.trackercompanion.model.enums.Brand
import com.example.trackercompanion.model.enums.Status
import com.example.trackercompanion.model.enums.Type
import com.example.trackercompanion.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditWrestlerScreen(existing: Wrestler? = null, onSave: (Wrestler)->Unit, onBack: ()->Unit) {
    val isEditMode = existing != null

    var name by remember { mutableStateOf(existing?.name ?: "") }
    var brand by remember { mutableStateOf(existing?.brand?: Brand.RAW) }
    var type by remember { mutableStateOf(existing?.type?: Type.SINGLE) }
    var status by remember { mutableStateOf(existing?.status?: Status.ACTIVE) }
    var notes by remember { mutableStateOf(existing?.notes?: "") }

    var nameError by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (isEditMode) "Edit Wrestler" else "Add Wrestler",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) {innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(modifier = Modifier.height(4.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = name,
                onValueChange = {
                    name = it
                    nameError = false
                },
                label = { Text(text = "Wrestler Name") },
                placeholder = { Text(text = "e.g. Steve Austin") },
                isError = nameError,
                supportingText = {
                    if (nameError) {
                        Text(
                            text = "Name cannot be empty",
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                },
                singleLine = true
            )

            DropdownField(
                label = "Brand",
                selected = brand.name,
                options = Brand.entries.map { it.name },
                onOptionsSelected = {selected ->
                    brand = Brand.valueOf(selected)
                }
            )

            DropdownField(
                label = "Type",
                selected = type.name,
                options = Type.entries.map { it.name },
                onOptionsSelected = {selected ->
                    type = Type.valueOf(selected)
                }
            )

            DropdownField(
                label = "Status",
                selected = status.name,
                options = Status.entries.map { it.name },
                onOptionsSelected = {selected ->
                    status = Status.valueOf(selected)
                }
            )

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = notes,
                onValueChange = { notes = it },
                label = { Text(text = "Notes") },
                minLines = 3,
                maxLines = 5,
            )

            Text(
                text = "Wins, Loss, and Points are calculated automatically from match history.",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(4.dp))

            Button(
                modifier = Modifier.fillMaxWidth().height(52.dp),
                onClick = {
                    if (name.isBlank()) {
                        nameError = true
                        return@Button
                    }

                    val saved = Wrestler(
                        id = existing?.id ?: System.currentTimeMillis().toInt(),
                        name = name.trim(),
                        brand = brand,
                        type = type,
                        status = status,
                        notes = notes.trim(),
                        imageRes = existing?.imageRes ?: R.drawable.wrestler_placeholder
                    )
                    onSave(saved)
                }
            ) {
                Text(
                    text = if (isEditMode) "Saved Changes" else "Add Wrestler",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownField(label: String, selected: String, options: List<String>, onOptionsSelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {expanded = !expanded}
    ) {
        OutlinedTextField(
            value = selected,
            onValueChange = {},
            readOnly = true,
            label = { Text(text = label) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            modifier = Modifier.fillMaxWidth().menuAnchor()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(text = option) },
                    onClick = {
                        onOptionsSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddWrestlerPreview() {
    AddEditWrestlerScreen(
        existing = null,
        onSave = {},
        onBack = {}
    )
}

@Preview(showBackground = true)
@Composable
fun EditWrestlerPreview() {
    AddEditWrestlerScreen(
        existing = Wrestler(
            id = 51,
            name = "Steve Austin",
            brand = Brand.RAW,
            type = Type.SINGLE,
            status = Status.ACTIVE,
            imageRes = R.drawable.wrestler_placeholder
        ),
        onSave = {},
        onBack = {}
    )
}