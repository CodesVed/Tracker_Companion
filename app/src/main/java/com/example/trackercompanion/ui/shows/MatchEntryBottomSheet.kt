package com.example.trackercompanion.ui.shows

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.material3.Surface
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.trackercompanion.model.Match
import com.example.trackercompanion.model.Wrestler
import com.example.trackercompanion.model.enums.Brand
import com.example.trackercompanion.model.enums.CardSlot
import com.example.trackercompanion.model.enums.Show
import com.example.trackercompanion.ui.roster.DropdownField
import com.example.trackercompanion.ui.theme.Blue
import com.example.trackercompanion.ui.theme.Gold
import com.example.trackercompanion.ui.theme.Red

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MatchEntryBottomSheet(
    showId: Int,
    showType: Show,
    wrestlers: List<Wrestler>,
    existingMatchCount: Int,
    editingMatch: Match? = null,
    onSave: (Match) -> Unit,
    onDismiss: () -> Unit,
    onDelete: ((Match) -> Unit)? = null
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val isEditMode = editingMatch != null

    val suggestedSlot = when {
        existingMatchCount == 0 -> CardSlot.OPEN
        existingMatchCount <= 2 -> CardSlot.MID
        existingMatchCount <= 4 -> CardSlot.UPPER
        else                    -> CardSlot.MAIN
    }

    var isTagMatch by remember { mutableStateOf(editingMatch?.isTagMatch ?: false) }
    var slot by remember { mutableStateOf(editingMatch?.slot ?: suggestedSlot) }
    var stipulation by remember { mutableStateOf(editingMatch?.stipulation ?: "Normal") }
    var notes by remember { mutableStateOf(editingMatch?.notes ?: "") }

    // Singles
    var participant1 by remember {
        mutableStateOf(
            editingMatch?.let {
                if (!it.isTagMatch) wrestlers.find { w -> w.id == it.participantIds.getOrNull(0) }
                else null
            }
        )
    }
    var participant2 by remember {
        mutableStateOf(
            editingMatch?.let {
                if (!it.isTagMatch) wrestlers.find { w -> w.id == it.participantIds.getOrNull(1) }
                else null
            }
        )
    }

    // Tag
    var team1Wrestler1 by remember {
        mutableStateOf(
            editingMatch?.let {
                if (it.isTagMatch) wrestlers.find { w -> w.id == it.participantIds.getOrNull(0) }
                else null
            }
        )
    }
    var team1Wrestler2 by remember {
        mutableStateOf(
            editingMatch?.let {
                if (it.isTagMatch) wrestlers.find { w -> w.id == it.participantIds.getOrNull(1) }
                else null
            }
        )
    }
    var team2Wrestler1 by remember {
        mutableStateOf(
            editingMatch?.let {
                if (it.isTagMatch) wrestlers.find { w -> w.id == it.participantIds.getOrNull(2) }
                else null
            }
        )
    }
    var team2Wrestler2 by remember {
        mutableStateOf(
            editingMatch?.let {
                if (it.isTagMatch) wrestlers.find { w -> w.id == it.participantIds.getOrNull(3) }
                else null
            }
        )
    }

    // Winner
    var winnerId    by remember { mutableStateOf(editingMatch?.winnerId) }
    var winnerLabel by remember { mutableStateOf(editingMatch?.winnerLabel) }

    var participantError by remember { mutableStateOf(false) }
    var winnerError      by remember { mutableStateOf(false) }

    var showDeleteConfirm by remember { mutableStateOf(false) }

    val participantIds: List<Int>
    val participantDisplay: String
    val winnerOptions: List<Pair<String, List<Int>>>    //label to list of IDs

    if (isTagMatch) {
        participantIds = listOfNotNull(
            team1Wrestler1?.id, team1Wrestler2?.id,
            team2Wrestler1?.id, team2Wrestler2?.id
        )
        val team1Label = buildTeamLabel(team1Wrestler1, team1Wrestler2)
        val team2Label = buildTeamLabel(team2Wrestler1, team2Wrestler2)
        participantDisplay = if (team1Label.isNotBlank() && team2Label.isNotBlank())
            "$team1Label vs $team2Label" else ""
        winnerOptions = buildList {
            if (team1Label.isNotBlank())
                add(team1Label to listOfNotNull(team1Wrestler1?.id, team1Wrestler2?.id))
            if (team2Label.isNotBlank())
                add(team2Label to listOfNotNull(team2Wrestler1?.id, team2Wrestler2?.id))
        }
    } else {
        participantIds = listOfNotNull(participant1?.id, participant2?.id)
        val p1Name = participant1?.name ?: ""
        val p2Name = participant2?.name ?: ""
        participantDisplay = if (p1Name.isNotBlank() && p2Name.isNotBlank())
            "$p1Name vs $p2Name" else ""
        winnerOptions = buildList {
            if (p1Name.isNotBlank())
                add(p1Name to listOfNotNull(participant1?.id))
            if (p2Name.isNotBlank())
                add(p2Name to listOfNotNull(participant2?.id))
        }
    }

    val stipulations = listOf(
        "Normal", "Steel Cage", "Hell in a Cell", "Ladder",
        "TLC", "Tables", "Last Man Standing", "Royal Rumble",
        "Iron Man", "Submission", "Falls Count Anywhere",
        "No DQ", "First Blood"
    )

    val slotOptions = CardSlot.entries.map { it.name }

    // ── Delete confirmation dialog ─────────────────────────
    if (showDeleteConfirm) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = false },
            icon = {
                Icon(
                    imageVector = Icons.Default.DeleteForever,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error
                )
            },
            title = { Text("Delete Match?") },
            text = {
                Text(
                    text = "\"${editingMatch?.participants}\" will be permanently removed " +
                            "from this card. This cannot be undone.",
                    textAlign = TextAlign.Center
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        editingMatch?.let { onDelete?.invoke(it) }
                        showDeleteConfirm = false
                        onDismiss()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirm = false }) {
                    Text("Cancel")
                }
            }
        )
    }

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
            // ── Header row ─────────────────────────────────
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (isEditMode) "Edit Match" else "Add Match",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )

                // Delete button — only in edit mode
                if (isEditMode && onDelete != null) {
                    IconButton(onClick = { showDeleteConfirm = true }) {
                        Icon(
                            imageVector = Icons.Default.DeleteForever,
                            contentDescription = "Delete match",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }

            HorizontalDivider()

            // ── Slot picker ────────────────────────────────
            DropdownField(
                label = "Card Slot",
                selected = slot.name,
                options = slotOptions,
                onOptionsSelected = { slot = CardSlot.valueOf(it) }
            )

            if (!isEditMode) {
                Text(
                    text = "Suggested based on $existingMatchCount existing match" +
                            if (existingMatchCount == 1) "" else "es",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // ── Stipulation picker ─────────────────────────
            DropdownField(
                label = "Stipulation",
                selected = stipulation,
                options = stipulations,
                onOptionsSelected = { stipulation = it }
            )

            // ── Tag match toggle ───────────────────────────
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Tag Team Match", fontSize = 16.sp, fontWeight = FontWeight.Medium)
                    Text(
                        text = "Two wrestlers per team",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Switch(
                    checked = isTagMatch,
                    onCheckedChange = {
                        isTagMatch = it
                        participant1 = null; participant2 = null
                        team1Wrestler1 = null; team1Wrestler2 = null
                        team2Wrestler1 = null; team2Wrestler2 = null
                        winnerId = null; winnerLabel = null
                        participantError = false
                    }
                )
            }

            HorizontalDivider()

            // ── Participant pickers ────────────────────────
            if (isTagMatch) {
                Text(
                    text = "Team 1",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                SearchableWrestlerPicker(
                    label = "Team 1 — Wrestler 1",
                    selected = team1Wrestler1,
                    wrestlers = wrestlers,
                    onWrestlerSelected = { w ->
                        team1Wrestler1 = w
                        winnerId = null; winnerLabel = null
                        participantError = false
                    }
                )
                SearchableWrestlerPicker(
                    label = "Team 1 — Wrestler 2",
                    selected = team1Wrestler2,
                    wrestlers = wrestlers,
                    onWrestlerSelected = { w ->
                        team1Wrestler2 = w
                        winnerId = null; winnerLabel = null
                    }
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Team 2",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                SearchableWrestlerPicker(
                    label = "Team 2 — Wrestler 1",
                    selected = team2Wrestler1,
                    wrestlers = wrestlers,
                    onWrestlerSelected = { w ->
                        team2Wrestler1 = w
                        winnerId = null; winnerLabel = null
                        participantError = false
                    }
                )
                SearchableWrestlerPicker(
                    label = "Team 2 — Wrestler 2",
                    selected = team2Wrestler2,
                    wrestlers = wrestlers,
                    onWrestlerSelected = { w ->
                        team2Wrestler2 = w
                        winnerId = null; winnerLabel = null
                    }
                )
            } else {
                SearchableWrestlerPicker(
                    label = "Participant 1",
                    selected = participant1,
                    wrestlers = wrestlers,
                    onWrestlerSelected = { w ->
                        participant1 = w
                        winnerId = null; winnerLabel = null
                        participantError = false
                    }
                )
                SearchableWrestlerPicker(
                    label = "Participant 2",
                    selected = participant2,
                    wrestlers = wrestlers,
                    onWrestlerSelected = { w ->
                        participant2 = w
                        winnerId = null; winnerLabel = null
                        participantError = false
                    }
                )
            }

            if (participantError) {
                Text(
                    text = "Please select all participants",
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 12.sp
                )
            }

            // ── Winner picker ──────────────────────────────
            if (winnerOptions.size == 2) {
                HorizontalDivider()
                Text(
                    text = "Winner",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                DropdownField(
                    label = "Select winner",
                    selected = winnerLabel ?: "Select winner",
                    options = winnerOptions.map { it.first },
                    onOptionsSelected = { selectedLabel ->
                        val selected = winnerOptions.find { it.first == selectedLabel }
                        winnerLabel = selected?.first
                        winnerId = selected?.second?.firstOrNull()
                        winnerError = false
                    }
                )
                if (winnerError) {
                    Text(
                        text = "Please select a winner",
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 12.sp
                    )
                }
            }

            // ── Notes ──────────────────────────────────────
            OutlinedTextField(
                value = notes,
                onValueChange = { notes = it },
                label = { Text("Notes (optional)") },
                placeholder = { Text("e.g. Tournament final, title match") },
                minLines = 2,
                maxLines = 3,
                modifier = Modifier.fillMaxWidth()
            )

            if (participantDisplay.isNotBlank()) {
                Text(
                    text = "Preview: $participantDisplay",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Medium
                )
            }

            // ── Save button ────────────────────────────────
            Button(
                modifier = Modifier.fillMaxWidth().height(52.dp),
                onClick = {
                    val participantsValid = if (isTagMatch) {
                        team1Wrestler1 != null && team1Wrestler2 != null &&
                                team2Wrestler1 != null && team2Wrestler2 != null
                    } else {
                        participant1 != null && participant2 != null
                    }

                    if (!participantsValid) { participantError = true; return@Button }
                    if (winnerLabel == null) { winnerError = true; return@Button }

                    val winnerIds = winnerOptions
                        .find { it.first == winnerLabel }?.second ?: emptyList()

                    val savedMatch = Match(
                        id = editingMatch?.id ?: System.currentTimeMillis().toInt(),
                        showId = showId,
                        showType = showType,
                        slot = slot,
                        participants = participantDisplay,
                        stipulation = stipulation,
                        winnerId = winnerId,
                        winnerLabel = winnerLabel,
                        isTagMatch = isTagMatch,
                        participantIds = participantIds,
                        winnerIds = winnerIds,
                        notes = notes.trim()
                    )
                    onSave(savedMatch)
                }
            ) {
                Text(
                    text = if (isEditMode) "Save Changes" else "Save Match",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

fun buildTeamLabel(wrestler1: Wrestler?, wrestler2: Wrestler?): String {
    return when {
        wrestler1 != null && wrestler2 != null -> "${wrestler1.name} & ${wrestler2.name}"
        wrestler1 != null -> wrestler1.name
        wrestler2 != null -> wrestler2.name
        else -> ""
    }
}