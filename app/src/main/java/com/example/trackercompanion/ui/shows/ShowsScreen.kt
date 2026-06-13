package com.example.trackercompanion.ui.shows

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LiveTv
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.PrimaryScrollableTabRow
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.SecondaryScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.trackercompanion.data.ShowData
import com.example.trackercompanion.model.Match
import com.example.trackercompanion.model.PPVEvent
import com.example.trackercompanion.model.ShowEpisode
import com.example.trackercompanion.model.enums.Brand
import com.example.trackercompanion.model.enums.Show
import com.example.trackercompanion.navigation.Routes.*
import com.example.trackercompanion.ui.theme.Blue
import com.example.trackercompanion.ui.theme.Gold
import com.example.trackercompanion.ui.theme.Red

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowScreen(
    episodes: List<ShowEpisode>,
    ppvEvents: List<PPVEvent>,
    matches: List<Match>,
    onEpisodeClick: (ShowEpisode) -> Unit,
    onPPVClick: (PPVEvent) -> Unit,
    onAddEpisodeClick: () -> Unit,
    onEpisodeEdited: (ShowEpisode) -> Unit,
    onEpisodeDeleted: (ShowEpisode) -> Unit
){
    var selectedTab by remember { mutableIntStateOf(0) }
    var episodeOptionsTarget by remember { mutableStateOf<ShowEpisode?>(null) }
    var showEpisodeOptions   by remember { mutableStateOf(false) }
    var showEditEpisode      by remember { mutableStateOf(false) }
    var showDeleteEpisodeConfirm by remember { mutableStateOf(false) }

    val tabs = listOf("RAW", "Smackdown", "PPV")

    val rawEpisodes = episodes
        .filter { it.brand == Brand.RAW }
        .sortedBy { it.episodeNumber }

    val sdEpisodes = episodes
        .filter { it.brand == Brand.SD }
        .sortedBy { it.episodeNumber }

    val sortedPPVs = ppvEvents.sortedBy { it.ppvNumber }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                text = "Shows",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            HorizontalDivider(modifier = Modifier.padding(5.dp))

            SecondaryScrollableTabRow (
                selectedTabIndex = selectedTab,
                edgePadding = 0.dp,
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = {
                            Text(
                                text = title,
                                fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    )
                }
            }

            when (selectedTab) {
                0 -> EpisodeList(
                    episodes = rawEpisodes,
                    matches = matches,
                    onEpisodeClick = onEpisodeClick,
                    onEpisodeLongPress = {episode ->
                        episodeOptionsTarget = episode
                        showEpisodeOptions = true
                    }
                )
                1 -> EpisodeList(
                    episodes = sdEpisodes,
                    matches = matches,
                    onEpisodeClick = onEpisodeClick,
                    onEpisodeLongPress = { episode ->
                        episodeOptionsTarget = episode
                        showEpisodeOptions = true
                    }
                )
                2 -> PPVList(
                    ppvEvents = sortedPPVs,
                    matches = matches,
                    onPPVClick = onPPVClick
                )
            }
        }

        FloatingActionButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            onClick = onAddEpisodeClick
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
                Text(text = "Add Episode", fontSize = 16.sp)
            }
        }

        if (showEpisodeOptions && episodeOptionsTarget != null) {
            val target = episodeOptionsTarget!!

            ModalBottomSheet(
                onDismissRequest = {
                    showEpisodeOptions = false
                    episodeOptionsTarget = null
                }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    // Header showing which episode was long-pressed
                    val brandLabel = when (target.brand) {
                        Brand.RAW -> "RAW"
                        Brand.SD  -> "SmackDown"
                        else      -> target.brand.name
                    }
                    Text(
                        text = "$brandLabel ${target.episodeNumber}",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Week ${target.weekNumber}",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    HorizontalDivider()
                    Spacer(modifier = Modifier.height(8.dp))

                    // Edit option
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                showEpisodeOptions = false
                                showEditEpisode = true
                            }
                            .padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Column {
                            Text("Edit Episode", fontSize = 16.sp, fontWeight = FontWeight.Medium)
                            Text(
                                text = "Change week number or theme notes",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    HorizontalDivider()

                    // Delete option
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showDeleteEpisodeConfirm = true }
                            .padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.DeleteForever,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.error
                        )
                        Column {
                            Text(
                                "Delete Episode",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.error
                            )
                            Text(
                                text = "Also deletes all matches on this card",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.error.copy(alpha = 0.7f)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            if (showEditEpisode && episodeOptionsTarget != null) {
                val target = episodeOptionsTarget!!

                EditEpisodeBottomSheet(
                    episode = target,
                    onSave = { edited ->
                        onEpisodeEdited(edited)
                        showEditEpisode = false
                        episodeOptionsTarget = null
                    },
                    onDismiss = {
                        showEditEpisode = false
                        episodeOptionsTarget = null
                    }
                )
            }
        }

        // Delete confirmation dialog
        if (showDeleteEpisodeConfirm && episodeOptionsTarget != null) {
            val target = episodeOptionsTarget!!
            val brandLabel = when (target.brand) {
                Brand.RAW -> "RAW"; Brand.SD -> "SmackDown"; else -> target.brand.name
            }
            AlertDialog(
                onDismissRequest = { showDeleteEpisodeConfirm = false },
                icon = {
                    Icon(
                        Icons.Default.DeleteForever,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error
                    )
                },
                title = { Text("Delete $brandLabel ${target.episodeNumber}?") },
                text = {
                    Text(
                        text = "This will permanently delete the episode and all " +
                                "matches logged on its card. This cannot be undone.",
                        textAlign = TextAlign.Center
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            onEpisodeDeleted(target)
                            showDeleteEpisodeConfirm = false
                            showEpisodeOptions = false
                            episodeOptionsTarget = null
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error
                        )
                    ) { Text("Delete") }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteEpisodeConfirm = false }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}

@Composable
fun EpisodeList(episodes: List<ShowEpisode>, matches: List<Match>, onEpisodeClick: (ShowEpisode) -> Unit, onEpisodeLongPress: (ShowEpisode) -> Unit) {
    if (episodes.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "No episodes found. Tap + to add an episode.",
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = 12.dp, end = 12.dp, top = 8.dp, bottom = 80.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(episodes, key = { it.id }) {episode ->
                val matchCount = matches.count {
                    it.showId == episode.id &&
                    it.showType == Show.SHOW
                }

                EpisodeCard(
                    episode = episode,
                    matchCount = matchCount,
                    onClick = { onEpisodeClick(episode) },
                    onLongPress = { onEpisodeLongPress(episode) }
                )
            }
        }
    }
}

@Composable
fun PPVList(ppvEvents: List<PPVEvent>, matches: List<Match>, onPPVClick: (PPVEvent) -> Unit) {
    if (ppvEvents.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "No PPV events found.",
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = 12.dp, end = 12.dp, top = 8.dp, bottom = 80.dp),
        ) {
            items(ppvEvents, key = { it.id }) {ppv ->
                val matchCount = matches.count {
                    it.showId == ppv.id &&
                    it.showType == Show.PPV
                }

                PPVCard(
                    ppv = ppv,
                    matchCount = matchCount,
                    onClick = { onPPVClick(ppv) }
                )
            }
        }
    }
}

@Composable
fun EpisodeCard(episode: ShowEpisode, matchCount: Int, onClick: () -> Unit, onLongPress: () -> Unit = {}) {
    val brandColor = when (episode.brand) {
        Brand.RAW -> Red
        Brand.SD -> Blue
        else -> Gold
    }

    val brandLabel = when (episode.brand) {
        Brand.RAW -> "RAW"
        Brand.SD -> "Smackdown"
        else -> episode.brand.name
    }

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongPress
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(width = 4.dp, height = 56.dp)
                    .padding(end = 0.dp),
                contentAlignment = Alignment.Center
            ) {
                Canvas(
                    modifier = Modifier.fillMaxSize()
                ) {
                    drawRect(color = brandColor)
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Icon(
                modifier = Modifier.size(28.dp),
                imageVector = Icons.Default.LiveTv,
                contentDescription = null,
                tint = brandColor
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "$brandLabel ${episode.episodeNumber}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                if (episode.notes.isNotBlank()) {
                    Text(
                        text = episode.notes,
                        maxLines = 1,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = matchCount.toString(),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Text(
                    text = if (matchCount == 1) "match" else "matches",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun PPVCard(ppv: PPVEvent, matchCount: Int, onClick: () -> Unit) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(width = 4.dp, height = 56.dp)
            ) {
                Canvas(
                    modifier = Modifier.fillMaxSize()
                ) {
                    drawRect(color = Gold)
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Icon(
                modifier = Modifier.size(28.dp),
                imageVector = Icons.Default.Bolt,
                contentDescription = null,
                tint = Gold
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = ppv.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                )

                Text(
                    text = "PPV #${ppv.ppvNumber}",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                if (ppv.notes.isNotBlank()) {
                    Text(
                        text = ppv.notes,
                        maxLines = 1,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = matchCount.toString(),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Gold
                )

                Text(
                    text = if (matchCount == 1) "match" else "matches",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditEpisodeBottomSheet(
    episode: ShowEpisode,
    onSave: (ShowEpisode) -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var weekNumStr  by remember { mutableStateOf(episode.weekNumber.toString()) }
    var themeNotes  by remember { mutableStateOf(episode.notes) }
    var weekError   by remember { mutableStateOf(false) }

    val brandLabel = when (episode.brand) {
        Brand.RAW -> "RAW"; Brand.SD -> "SmackDown"; else -> episode.brand.name
    }

    ModalBottomSheet(onDismissRequest = onDismiss, sheetState = sheetState) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(
                text = "Edit $brandLabel ${episode.episodeNumber}",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
            HorizontalDivider()

            OutlinedTextField(
                value = weekNumStr,
                onValueChange = {
                    if (it.all { c -> c.isDigit() }) {
                        weekNumStr = it
                        weekError = false
                    }
                },
                label = { Text("Week Number") },
                isError = weekError,
                supportingText = {
                    if (weekError) Text("Week number required",
                        color = MaterialTheme.colorScheme.error)
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = themeNotes,
                onValueChange = { themeNotes = it },
                label = { Text("Theme / Notes") },
                minLines = 2,
                maxLines = 4,
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                modifier = Modifier.fillMaxWidth().height(52.dp),
                onClick = {
                    val weekNum = weekNumStr.toIntOrNull()
                    if (weekNum == null) { weekError = true; return@Button }
                    onSave(episode.copy(weekNumber = weekNum, notes = themeNotes.trim()))
                }
            ) {
                Text("Save Changes", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ShowsScreenPreview() {
    ShowScreen(
        episodes = ShowData.episodes,
        ppvEvents = ShowData.ppvEvents,
        matches = ShowData.matches,
        onEpisodeClick = {},
        onPPVClick = {},
        onAddEpisodeClick = {},
        onEpisodeEdited = {},
        onEpisodeDeleted = {}
    )
}