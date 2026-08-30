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
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
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

sealed class WeekSaveResult {
    data class NoLink(val week: CalendarWeek): WeekSaveResult()
    data class LinkExisting(val week: CalendarWeek): WeekSaveResult()
    data class NewEpisode(val week: CalendarWeek, val episode: ShowEpisode): WeekSaveResult()
    data class NewPPV(val week: CalendarWeek, val ppv: PPVEvent): WeekSaveResult()
}

private enum class LinkMode{
    NONE, EXISTING, NEW
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditWeekBottomSheet(
    existing: CalendarWeek? = null,
    episodes: List<ShowEpisode>,
    ppvEvents: List<PPVEvent>,
    onSave: (WeekSaveResult) -> Unit,
    onDelete: ((CalendarWeek) -> Unit)? = null,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val isEditMode = existing != null

    var weekNumStr  by rememberSaveable { mutableStateOf(existing?.weekNumber?.toString() ?: "") }
    var notes       by rememberSaveable { mutableStateOf(existing?.notes ?: "") }

    var linkMode by rememberSaveable {
        mutableStateOf(
            when {
                existing?.linkedShowId != null || existing?.linkedPPVId != null -> LinkMode.EXISTING
                else -> LinkMode.NONE
            }
        )
    }

    var isPPVLink by rememberSaveable { mutableStateOf(existing?.linkedPPVId != null) }
    var selectedEpisode by rememberSaveable { mutableStateOf(episodes.find { it.id == existing?.linkedShowId }) }
    var selectedPPV by rememberSaveable { mutableStateOf(ppvEvents.find { it.id == existing?.linkedPPVId }) }

    var newIsPPV by rememberSaveable { mutableStateOf(false) }
    var newBrand by rememberSaveable { mutableStateOf(Brand.RAW) }
    var newEpisodeNumStr by rememberSaveable { mutableStateOf("") }
    var newPPVName by rememberSaveable { mutableStateOf("") }
    var newPPVNumStr by rememberSaveable { mutableStateOf("") }

    var manualLabel by rememberSaveable {
        mutableStateOf(
            if (existing?.linkedShowId == null && existing?.linkedPPVId == null) existing?.showLabel ?: ""
            else ""
        )
    }

    var linkToShow  by rememberSaveable { mutableStateOf(existing?.linkedShowId != null || existing?.linkedPPVId != null) }

    var weekNumError by rememberSaveable { mutableStateOf(false) }
    var labelError    by rememberSaveable { mutableStateOf(false) }
    var showDeleteConfirm by rememberSaveable { mutableStateOf(false) }

    val computedLabel = when (linkMode) {
        LinkMode.EXISTING -> when {
            isPPVLink && selectedPPV != null -> selectedPPV!!.name
            !isPPVLink && selectedEpisode != null ->  {
                val brandLabel = when (selectedEpisode!!.brand) {
                    Brand.RAW -> "RAW"
                    Brand.SD -> "Smackdown"
                    else -> selectedEpisode!!.brand.name
                }
                "$brandLabel ${selectedEpisode!!.episodeNumber}"
            }
            else -> ""
        }
        LinkMode.NEW -> when {
            newIsPPV -> newPPVName
            else -> {
                val brandLabel = when (newBrand) {
                    Brand.RAW -> "RAW"
                    Brand.SD -> "Smackdown"
                    else -> newBrand.name
                }
                if (newEpisodeNumStr.isNotBlank()) "$brandLabel $newEpisodeNumStr" else ""
            }
        }
        LinkMode.NONE -> manualLabel
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
                    val filtered = it.filter { c -> c.isDigit() }
                    weekNumStr = filtered
                    weekNumError = false
                },
                label = { Text("Week Number") },
                isError = weekNumError,
                supportingText = {
                    if (weekNumError) {
                        val errorText = if (weekNumStr.isBlank()) "Week number required" else "Invalid week number"
                        Text(errorText, color = MaterialTheme.colorScheme.error)
                    }
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                enabled = !isEditMode   // week number is the PK — don't allow changing it once created
            )

            HorizontalDivider()

            // ── Mode selector ────────────────────────────
            Text("This Week's Show", fontSize = 16.sp, fontWeight = FontWeight.Medium)
            SingleChoiceSegmentedButtonRow (modifier = Modifier.fillMaxWidth()) {
                val modes = listOf(LinkMode.NONE to "No Link", LinkMode.EXISTING to "Existing", LinkMode.NEW to "New Show")
                modes.forEachIndexed { index, (mode, label) ->
                    SegmentedButton(
                        selected = linkMode == mode,
                        shape = SegmentedButtonDefaults.itemShape(index = index, count = modes.size),
                        onClick = { linkMode = mode; labelError = false }
                    ) { Text(label) }
                }
            }

            when (linkMode) {
                LinkMode.EXISTING -> {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Is this a PPV?", fontSize = 14.sp)
                        Switch(
                            checked = isPPVLink,
                            onCheckedChange = { isPPVLink = it; selectedEpisode = null; selectedPPV = null; labelError = false }
                        )
                    }

                    if (isPPVLink) {
                        DropdownField(
                            label = "Select PPV",
                            selected = selectedPPV?.name ?: "Select a PPV",
                            options = ppvEvents.map { it.name },
                            onOptionsSelected = { name -> selectedPPV = ppvEvents.find { it.name == name }; labelError = false }
                        )
                    } else {
                        val episodeOptions = episodes.map { ep ->
                            val brandLabel = when (ep.brand) { Brand.RAW -> "RAW"; Brand.SD -> "SmackDown"; else -> ep.brand.name }
                            "$brandLabel ${ep.episodeNumber}"
                        }
                        DropdownField(
                            label = "Select Episode",
                            selected = selectedEpisode?.let { ep ->
                                val brandLabel = when (ep.brand) { Brand.RAW -> "RAW"; Brand.SD -> "SmackDown"; else -> ep.brand.name }
                                "$brandLabel ${ep.episodeNumber}"
                            } ?: "Select an episode",
                            options = episodeOptions,
                            onOptionsSelected = { label ->
                                selectedEpisode = episodes.find { ep ->
                                    val brandLabel = when (ep.brand) { Brand.RAW -> "RAW"; Brand.SD -> "SmackDown"; else -> ep.brand.name }
                                    "$brandLabel ${ep.episodeNumber}" == label
                                }
                                labelError = false
                            }
                        )
                    }
                }

                LinkMode.NEW -> {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Is this a PPV?", fontSize = 14.sp)
                        Switch(checked = newIsPPV, onCheckedChange = { newIsPPV = it; labelError = false })
                    }

                    if (newIsPPV) {
                        OutlinedTextField(
                            value = newPPVName,
                            onValueChange = { newPPVName = it; labelError = false },
                            label = { Text("PPV Name") },
                            placeholder = { Text("e.g. Royal Rumble") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = newPPVNumStr,
                            onValueChange = { newPPVNumStr = it.filter { c -> c.isDigit() } },
                            label = { Text("PPV Number") },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth()
                        )
                    } else {
                        DropdownField(
                            label = "Brand",
                            selected = newBrand.name,
                            options = listOf(Brand.RAW.name, Brand.SD.name),
                            onOptionsSelected = { selected -> newBrand = Brand.valueOf(selected); labelError = false }
                        )
                        OutlinedTextField(
                            value = newEpisodeNumStr,
                            onValueChange = { newEpisodeNumStr = it.filter { c -> c.isDigit() }; labelError = false },
                            label = { Text("Episode Number") },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    Text(
                        text = "This creates a new, empty show — add match details later from the Shows tab.",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                LinkMode.NONE -> {
                    OutlinedTextField(
                        value = manualLabel,
                        onValueChange = { manualLabel = it; labelError = false },
                        label = { Text("Show Label") },
                        placeholder = { Text("e.g. Off Week") },
                        isError = labelError,
                        supportingText = { if (labelError) Text("Show label required", color = MaterialTheme.colorScheme.error) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            OutlinedTextField(
                value = notes,
                onValueChange = { notes = it },
                label = { Text("Theme / Notes (optional)") },
                placeholder = { Text("e.g. IC Tournament continues") },
                minLines = 2,
                maxLines = 4,
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                modifier = Modifier.fillMaxWidth().height(52.dp),
                onClick = {
                    var valid = true
                    val weekNum = weekNumStr.trim().toIntOrNull()
                    if (weekNum == null) { weekNumError = true; valid = false }
                    if (computedLabel.isBlank()) { labelError = true; valid = false }
                    if (linkMode == LinkMode.NEW && !newIsPPV && newEpisodeNumStr.toIntOrNull() == null) { labelError = true; valid = false }
                    if (linkMode == LinkMode.NEW && newIsPPV && newPPVNumStr.toIntOrNull() == null) { labelError = true; valid = false }
                    if (!valid) return@Button

                    when (linkMode) {
                        LinkMode.EXISTING -> {
                            val week = CalendarWeek(
                                id = existing?.id ?: 0,
                                weekNumber = weekNum!!,
                                showLabel = computedLabel,
                                linkedShowId = if (!isPPVLink) selectedEpisode?.id else null,
                                linkedPPVId = if (isPPVLink) selectedPPV?.id else null,
                                notes = notes.trim()
                            )
                            onSave(WeekSaveResult.LinkExisting(week))
                        }
                        LinkMode.NEW -> {
                            val week = CalendarWeek(
                                id = existing?.id ?: 0,
                                weekNumber = weekNum!!,
                                showLabel = computedLabel,
                                linkedShowId = null, // filled in by the caller once the new show's id is known
                                linkedPPVId = null,
                                notes = notes.trim()
                            )
                            if (newIsPPV) {
                                val ppv = PPVEvent(id = 0, ppvNumber = newPPVNumStr.toInt(), name = newPPVName.trim(), notes = "")
                                onSave(WeekSaveResult.NewPPV(week, ppv))
                            } else {
                                val episode = ShowEpisode(
                                    id = 0,
                                    episodeNumber = newEpisodeNumStr.toInt(),
                                    brand = newBrand,
                                    weekNumber = weekNum,
                                    notes = ""
                                )
                                onSave(WeekSaveResult.NewEpisode(week, episode))
                            }
                        }
                        LinkMode.NONE -> {
                            val week = CalendarWeek(
                                id = existing?.id ?: 0,
                                weekNumber = weekNum!!,
                                showLabel = computedLabel,
                                linkedShowId = null,
                                linkedPPVId = null,
                                notes = notes.trim()
                            )
                            onSave(WeekSaveResult.NoLink(week))
                        }
                    }
                }
            ) {
                Text(text = if (isEditMode) "Save Changes" else "Add Week", fontSize = 16.sp, fontWeight = FontWeight.Bold)
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