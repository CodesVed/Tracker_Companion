package com.example.trackercompanion.ui.calendar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.trackercompanion.model.CalendarWeek
import com.example.trackercompanion.model.PPVEvent
import com.example.trackercompanion.model.ShowEpisode
import com.example.trackercompanion.model.enums.Brand
import com.example.trackercompanion.ui.roster.DropdownField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditWeekBottomSheet(
    existing: CalendarWeek? = null,
    episodes: List<ShowEpisode>,
    ppvEvents: List<PPVEvent>,
    onSave: (CalendarWeek) -> Unit,
    onDelete: ((CalendarWeek) -> Unit)? = null,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val isEditMode = existing != null

    var weekNumStr  by remember { mutableStateOf(existing?.weekNumber?.toString() ?: "") }
    var notes       by remember { mutableStateOf(existing?.notes ?: "") }
    var linkToShow  by remember { mutableStateOf(existing?.linkedShowId != null || existing?.linkedPPVId != null) }
    var isPPVLink   by remember { mutableStateOf(existing?.linkedPPVId != null) }

    var selectedEpisode by remember { mutableStateOf(episodes.find { it.id == existing?.linkedShowId }) }
    var selectedPPV by remember { mutableStateOf(ppvEvents.find { it.id == existing?.linkedPPVId }) }
    var manualLabel by remember { mutableStateOf(
        if (existing?.linkedShowId == null && existing?.linkedPPVId == null) existing?.showLabel?:""
        else ""
    ) }

    var weekNumError by remember { mutableStateOf(false) }
    var labelError    by remember { mutableStateOf(false) }
    var showDeleteConfirm by remember { mutableStateOf(false) }

    val computedLabel = when {
        linkToShow && isPPVLink && selectedPPV != null -> selectedPPV!!.name
        linkToShow && !isPPVLink && selectedEpisode != null -> {
            val brandLabel = when (selectedEpisode!!.brand) {
                Brand.RAW -> "RAW"
                Brand.SD -> "SmackDown"
                else -> selectedEpisode!!.brand.name
            }
            "$brandLabel ${selectedEpisode!!.episodeNumber}"
        }
        else -> manualLabel
    }

    if (showDeleteConfirm) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = false },
            icon = {
                Icon(
                    imageVector = Icons.Default.DeleteForever,
                    contentDescription = "Delete",
                    tint = MaterialTheme.colorScheme.error
                )
            },
            title = {
                Text(text = "Delete Week ${existing?.weekNumber}?")
            },
            text = {
                Text(
                    text = "This calendar entry will be permanently removed. " +
                            "The linked show or PPV itself will not be deleted.",
                    textAlign = TextAlign.Center
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        existing?.let { onDelete?.invoke(it) }
                        showDeleteConfirm = false
                        onDismiss()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) { Text("Delete") }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirm = false }) { Text("Cancel") }
            }
        )
    }

    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = onDismiss
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // ── Header ─────────────────────────────────────
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (isEditMode) "Edit Week" else "Add Week",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
                if (isEditMode && onDelete != null) {
                    IconButton(onClick = { showDeleteConfirm = true }) {
                        Icon(
                            imageVector = Icons.Default.DeleteForever,
                            contentDescription = "Delete week",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }

            HorizontalDivider()

            // ── Week number ────────────────────────────────
            OutlinedTextField(
                value = weekNumStr,
                onValueChange = {
                    if (it.all { c -> c.isDigit() }) {
                        weekNumStr = it
                        weekNumError = false
                    }
                },
                label = { Text("Week Number") },
                isError = weekNumError,
                supportingText = {
                    if (weekNumError) Text("Week number required",
                        color = MaterialTheme.colorScheme.error)
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                enabled = !isEditMode   // week number is the PK — don't allow changing it once created
            )

            HorizontalDivider()

            // ── Link to existing show toggle ───────────────
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Link to a Show", fontSize = 16.sp, fontWeight = FontWeight.Medium)
                    Text(
                        text = "Tapping this week will jump straight to that episode/PPV",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Switch(
                    checked = linkToShow,
                    onCheckedChange = {
                        linkToShow = it
                        labelError = false
                    }
                )
            }

            if (linkToShow) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Is this a PPV?", fontSize = 14.sp)
                    Switch(
                        checked = isPPVLink,
                        onCheckedChange = {
                            isPPVLink = it
                            selectedEpisode = null
                            selectedPPV = null
                            labelError = false
                        }
                    )
                }

                if (isPPVLink) {
                    DropdownField(
                        label = "Select PPV",
                        selected = selectedPPV?.name ?: "Select a PPV",
                        options = ppvEvents.map { it.name },
                        onOptionsSelected = { name ->
                            selectedPPV = ppvEvents.find { it.name == name }
                            labelError = false
                        }
                    )
                } else {
                    val episodeOptions = episodes.map { ep ->
                        val brandLabel = when (ep.brand) {
                            Brand.RAW -> "RAW"; Brand.SD -> "SmackDown"; else -> ep.brand.name
                        }
                        "$brandLabel ${ep.episodeNumber}"
                    }

                    DropdownField(
                        label = "Select Episode",
                        selected = selectedEpisode?.let { ep ->
                            val brandLabel = when (ep.brand) {
                                Brand.RAW -> "RAW"; Brand.SD -> "SmackDown"; else -> ep.brand.name
                            }
                            "$brandLabel ${ep.episodeNumber}"
                        } ?: "Select an episode",
                        options = episodeOptions,
                        onOptionsSelected = { label ->
                            selectedEpisode = episodes.find { ep ->
                                val brandLabel = when (ep.brand) {
                                    Brand.RAW -> "RAW"; Brand.SD -> "SmackDown"; else -> ep.brand.name
                                }
                                "$brandLabel ${ep.episodeNumber}" == label
                            }
                            labelError = false
                        }
                    )
                }
            } else {
                OutlinedTextField(
                    value = manualLabel,
                    onValueChange = {
                        manualLabel = it
                        labelError = false
                    },
                    label = { Text("Show Label") },
                    placeholder = { Text("e.g. RAW 12, Off Week, No Way Out") },
                    isError = labelError,
                    supportingText = {
                        if (labelError) Text("Show label required",
                            color = MaterialTheme.colorScheme.error)
                    },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // ── Theme notes ─────────────────────────────────
            OutlinedTextField(
                value = notes,
                onValueChange = { notes = it },
                label = { Text("Theme / Notes (optional)") },
                placeholder = { Text("e.g. IC Tournament continues") },
                minLines = 2,
                maxLines = 4,
                modifier = Modifier.fillMaxWidth()
            )

            // ── Save button ─────────────────────────────────
            Button(
                modifier = Modifier.fillMaxWidth().height(52.dp),
                onClick = {
                    var valid = true

                    val weekNum = weekNumStr.toIntOrNull()
                    if (weekNum == null) {
                        weekNumError = true
                        valid = false
                    }

                    if (computedLabel.isBlank()) {
                        labelError = true
                        valid = false
                    }

                    if (!valid) return@Button

                    val saved = CalendarWeek(
                        weekNumber = weekNum!!,
                        showLabel = computedLabel,
                        linkedShowId = if (linkToShow && !isPPVLink) selectedEpisode?.id else null,
                        linkedPPVId = if (linkToShow && isPPVLink) selectedPPV?.id else null,
                        notes = notes.trim()
                    )
                    onSave(saved)
                }
            ) {
                Text(
                    text = if (isEditMode) "Save Changes" else "Add Week",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun AddEditWeekPreview() {
    AddEditWeekBottomSheet(
        existing = null,
        episodes = listOf(),
        ppvEvents = listOf(),
        onSave = {},
        onDismiss = {}
    )
}