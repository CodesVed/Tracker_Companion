package com.example.trackercompanion.ui.championships

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.trackercompanion.model.Championship
import com.example.trackercompanion.model.TitleReign
import com.example.trackercompanion.model.Wrestler
import com.example.trackercompanion.ui.shows.SearchableWrestlerPicker

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogTitleChangeBottomSheet(
    title: Championship,
    currentReign: TitleReign?,
    nextReignNumber: Int,
    isTagTitle: Boolean,
    wrestlers: List<Wrestler>,
    onSave: (closedReign: TitleReign?, newReign: TitleReign?) -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var newChampion1 by remember { mutableStateOf<Wrestler?>(null) }
    var newChampion2 by remember { mutableStateOf<Wrestler?>(null) }
    var wonAtEvent    by remember { mutableStateOf("") }
    var notes         by remember { mutableStateOf("") }
    var vacateOnly    by remember { mutableStateOf(false) }

    var champion1Error by remember { mutableStateOf(false) }
    var champion2Error by remember { mutableStateOf(false) }
    var eventError      by remember { mutableStateOf(false) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // ── Header ─────────────────────────────────────
            Text(
                text = "Log Title Change",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = title.title,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            HorizontalDivider()

            // ── Current champion context (read-only info) ──
            if (currentReign != null) {
                Text(
                    text = "Currently held by: ${currentReign.holderNames.joinToString(" & ")}",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                Text(
                    text = "Title is currently VACANT",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            HorizontalDivider()

            // ── Vacate-only toggle ───────────────────────────
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Vacate Only", fontSize = 16.sp, fontWeight = FontWeight.Medium)
                    Text(
                        text = "Close current reign without naming a new champion yet",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Switch(
                    checked = vacateOnly,
                    onCheckedChange = {
                        vacateOnly = it
                        if (it) { newChampion1 = null; newChampion2 = null }
                    },
                    enabled = currentReign != null
                )
            }

            HorizontalDivider()

            // ── New champion pickers — hidden if vacating only ──
            if (!vacateOnly) {
                Text(
                    text = if (isTagTitle) "New Champions" else "New Champion",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                SearchableWrestlerPicker(
                    label = if (isTagTitle) "Champion 1" else "New Champion",
                    selected = newChampion1,
                    wrestlers = wrestlers,
                    onWrestlerSelected = {
                        newChampion1 = it
                        champion1Error = false
                    }
                )

                if (champion1Error) {
                    Text(
                        text = "Please select a wrestler",
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 12.sp
                    )
                }

                if (isTagTitle) {
                    SearchableWrestlerPicker(
                        label = "Champion 2",
                        selected = newChampion2,
                        wrestlers = wrestlers,
                        onWrestlerSelected = {
                            newChampion2 = it
                            champion2Error = false
                        }
                    )
                    if (champion2Error) {
                        Text(
                            text = "Please select a second wrestler",
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 12.sp
                        )
                    }
                }
            }

            // ── Event reference ─────────────────────────────
            OutlinedTextField(
                value = wonAtEvent,
                onValueChange = {
                    wonAtEvent = it
                    eventError = false
                },
                label = { Text(if (vacateOnly) "Vacated At" else "Won At") },
                placeholder = { Text("e.g. RAW 9, Royal Rumble") },
                isError = eventError,
                supportingText = {
                    if (eventError) {
                        Text(
                            text = "Event reference is required",
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            // ── Notes ────────────────────────────────────────
            OutlinedTextField(
                value = notes,
                onValueChange = { notes = it },
                label = { Text("Notes (optional)") },
                placeholder = { Text("e.g. Won by pinfall after interference") },
                minLines = 2,
                maxLines = 3,
                modifier = Modifier.fillMaxWidth()
            )

            // ── Save button ──────────────────────────────────
            Button(
                modifier = Modifier.fillMaxWidth().height(52.dp),
                onClick = {
                    var valid = true

                    if (wonAtEvent.isBlank()) {
                        eventError = true
                        valid = false
                    }

                    if (!vacateOnly) {
                        if (newChampion1 == null) {
                            champion1Error = true
                            valid = false
                        }
                        if (isTagTitle && newChampion2 == null) {
                            champion2Error = true
                            valid = false
                        }
                    }

                    if (!valid) return@Button

                    val closedReign = currentReign?.copy(lostAtEvent = wonAtEvent.trim())

                    if (vacateOnly) {
                        onSave(closedReign, null)
                    } else {
                        val newReign = TitleReign(
                            id = System.currentTimeMillis().toInt(),
                            titleId = title.id,
                            titleName = title.title,
                            reignNumber = nextReignNumber,
                            holderIds = listOfNotNull(newChampion1?.id, newChampion2?.id),
                            holderNames = listOfNotNull(newChampion1?.name, newChampion2?.name),
                            wonAtEvent = wonAtEvent.trim(),
                            lostAtEvent = null,
                            defenses = 0,
                            notes = notes.trim()
                        )
                        onSave(closedReign, newReign)
                    }
                }
            ) {
                Text(
                    text = if (vacateOnly) "Vacate Title" else "Save Title Change",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}