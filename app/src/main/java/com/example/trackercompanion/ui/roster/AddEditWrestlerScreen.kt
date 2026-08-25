package com.example.trackercompanion.ui.roster

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.trackercompanion.model.Wrestler
import com.example.trackercompanion.model.enums.Brand
import com.example.trackercompanion.model.enums.Status
import com.example.trackercompanion.model.enums.Type
import com.example.trackercompanion.R
import com.example.trackercompanion.util.processAndSaveWrestlerImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditWrestlerScreen(existing: Wrestler? = null, onSave: (Wrestler)->Unit, onBack: ()->Unit) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val isEditMode = existing != null

    var name by rememberSaveable { mutableStateOf(existing?.name ?: "") }
    var brand by rememberSaveable { mutableStateOf(existing?.brand?: Brand.RAW) }
    var type by rememberSaveable { mutableStateOf(existing?.type?: Type.SINGLE) }
    var status by rememberSaveable { mutableStateOf(existing?.status?: Status.ACTIVE) }
    var imageUrl by rememberSaveable { mutableStateOf(existing?.imageUrl?: "file:///android_asset/images/wrestler/wrestler_placeholder.webp") }
    var notes by rememberSaveable { mutableStateOf(existing?.notes?: "") }

    var isProcessingImage by rememberSaveable { mutableStateOf(false) }
    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            isProcessingImage = true
            coroutineScope.launch {
                val savedPath = withContext(Dispatchers.IO) {
                    processAndSaveWrestlerImage(context, uri)
                }

                if (savedPath != null) {
                    val previousUrl = imageUrl
                    if (previousUrl.startsWith("file://${context.filesDir.path}")) {
                        val previousFile = File(previousUrl.removePrefix("file://"))
                        if (previousFile.exists()) previousFile.delete()
                    }
                    imageUrl = savedPath
                }

                isProcessingImage = false
            }
        }
    }

    var nameError by rememberSaveable { mutableStateOf(false) }

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

            // ── Image picker ─────────────────────────────
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .clickable(enabled = !isProcessingImage) {
                            pickImageLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        },
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        modifier = Modifier.size(120.dp).clip(CircleShape),
                        model = ImageRequest.Builder(context)
                            .data(imageUrl)
                            .crossfade(true)
                            .build(),
                        contentDescription = "Wrestler Image",
                        contentScale = ContentScale.Crop,
                        placeholder = painterResource(R.drawable.wrestler_placeholder),
                        error = painterResource(R.drawable.wrestler_placeholder),
                    )

                    if (isProcessingImage) {
                        Box(
                            modifier = Modifier
                                .size(120.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.scrim.copy(alpha = 0.5f)),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = MaterialTheme.colorScheme.onSurface)
                        }
                    } else {
                        Icon(
                            imageVector = Icons.Default.CameraAlt,
                            contentDescription = "Change photo",
                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                        )
                    }
                }
            }

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
                        imageUrl = imageUrl
                    )
                    onSave(saved)
                }
            ) {
                Text(
                    text = if (isEditMode) "Save Changes" else "Add Wrestler",
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
            modifier = Modifier.fillMaxWidth().menuAnchor(
                type = ExposedDropdownMenuAnchorType.PrimaryNotEditable
            )
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
            imageUrl = "file:///android_asset/images/wrestler_placeholder.webp"
        ),
        onSave = {},
        onBack = {}
    )
}