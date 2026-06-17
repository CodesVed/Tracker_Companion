package com.example.trackercompanion.ui.championships

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.trackercompanion.data.ChampionshipData
import com.example.trackercompanion.data.WrestlerData
import com.example.trackercompanion.model.Championship
import com.example.trackercompanion.model.Contendership
import com.example.trackercompanion.model.TitleReign
import com.example.trackercompanion.model.Wrestler
import com.example.trackercompanion.model.enums.Brand
import com.example.trackercompanion.ui.roster.ScrollableSection
import com.example.trackercompanion.ui.roster.TitleReignRow
import com.example.trackercompanion.ui.shows.SearchableWrestlerPicker
import com.example.trackercompanion.ui.theme.Gold
import com.example.trackercompanion.ui.theme.Grey

@Composable
fun TitleDetailScreen(
    title: Championship,
    allReignsForTitle: List<TitleReign>,
    contenders: List<Contendership>,
    onAddContenderClick: () -> Unit,
    onSuggestContenderClick: () -> Unit,
    onMoveUp: (Contendership) -> Unit,
    onMoveDown: (Contendership) -> Unit,
    onRemove: (Contendership) -> Unit,
    onLogTitleChangeClick: () -> Unit,
    onBackClick: () -> Unit
) {
     val currentReign = allReignsForTitle.find { it.lostAtEvent == null }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                modifier = Modifier
                    .size(30.dp)
                    .clickable(onClick = onBackClick),
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back"
            )

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                textAlign = TextAlign.Center,
                maxLines = 2,
                text = title.title,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = if (currentReign?.holderNames != null) Gold.copy(alpha = 0.08f)
                else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
            ),
            border = if (currentReign?.holderNames != null) BorderStroke(1.dp, Gold.copy(alpha = 0.3f))
            else BorderStroke(1.dp, MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier.size(32.dp),
                    imageVector = Icons.Default.EmojiEvents,
                    contentDescription = null,
                    tint = if (currentReign != null) Gold else Grey
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    TitleStatCell(
                        label = "",
                        value = currentReign?.holderNames?.joinToString(" & ") ?: "Vacant",
                        fontSize = 22.sp,
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    TitleStatCell(
                        label = "Reign: ",
                        value = if (currentReign?.reignNumber?.toString() != null) "#${currentReign.reignNumber}" else "N/A",
                        fontSize = 16.sp,
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    TitleStatCell(
                        label = "Defenses: ",
                        value = if (currentReign?.defenses != null) currentReign.defenses.toString() else "N/A",
                        fontSize = 16.sp
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    TitleStatCell(
                        label = "Champion Since: ",
                        value = if (currentReign?.wonAtEvent != null) currentReign.wonAtEvent else "N/A",
                        fontSize = 16.sp
                    )
                }
            }
        }

        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

        FloatingActionButton(
            modifier = Modifier.fillMaxWidth().padding(start = 15.dp, end = 15.dp, top = 5.dp),
            onClick = onLogTitleChangeClick
        ) {
            Text(
                text = "Log Title Change",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))

        ScrollableSection(
            title = "Reign History",
            count = allReignsForTitle.size,
            emptyMessage = "No title reigns yet."
        ) {
            items(allReignsForTitle.sortedByDescending { it.reignNumber }) { reign ->
                TitleReignRow(reign = reign)
            }
        }

        HorizontalDivider(modifier = Modifier.padding(10.dp))

        Spacer(modifier = Modifier.height(4.dp))

        ContendershipSection(
            contenders = contenders,
            wrestlers = WrestlerData.roster,
            onMoveUp = onMoveUp,
            onMoveDown = onMoveDown,
            onRemove = onRemove
        )

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedButton(
                onClick = onAddContenderClick,
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(4.dp))
                Text("Add")
            }
            OutlinedButton(
                onClick = onSuggestContenderClick,
                modifier = Modifier.weight(1f),
                enabled = contenders.size < 12
            ) {
                Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(4.dp))
                Text("Suggest")
            }
        }
    }
}

@Composable
fun ContendershipSection(
    contenders: List<Contendership>,
    wrestlers: List<Wrestler>,
    onMoveUp: (Contendership) -> Unit,
    onMoveDown: (Contendership) -> Unit,
    onRemove: (Contendership) -> Unit
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Contenders",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "${contenders.size} ranked",
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (contenders.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No contenders ranked yet.",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            val sortedContenders = contenders.sortedBy { it.rank }
            sortedContenders.forEachIndexed { index, contender ->
                val brand = wrestlers.find { it.id == contender.wrestlerIds.firstOrNull() }?.brand
                ContenderRow(
                    rank = contender.rank,
                    wrestlerNames = contender.wrestlerNames,
                    brand = brand,
                    isFirst = index == 0,
                    isLast = index == sortedContenders.size - 1,
                    onMoveUp = { onMoveUp(contender) },
                    onMoveDown = { onMoveDown(contender) },
                    onRemove = { onRemove(contender) }
                )
            }
        }
    }
}

@Composable
fun ContenderRow(
    rank: Int,
    wrestlerNames: List<String>,
    brand: Brand?,
    isFirst: Boolean,
    isLast: Boolean,
    onMoveUp: () -> Unit,
    onMoveDown: () -> Unit,
    onRemove: () -> Unit
) {
    val displayName = wrestlerNames.joinToString(" & ")
    val isTeam = wrestlerNames.size > 1

    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 3.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(28.dp),
                shape = CircleShape,
                color = if (rank == 1) Gold.copy(alpha = 0.2f)
                else MaterialTheme.colorScheme.surfaceVariant
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "#$rank",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (rank == 1) Gold
                        else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = displayName,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                    if (isTeam) {
                        Spacer(modifier = Modifier.width(6.dp))

                        Icon(
                            imageVector = Icons.Default.Group,
                            contentDescription = "Tag Team",
                            modifier = Modifier.size(14.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                if (brand != null) {
                    Text(
                        text = brand.name,
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            IconButton(
                onClick = onMoveUp,
                modifier = Modifier.size(32.dp),
                enabled = !isFirst
            ) {
                Icon(
                    modifier = Modifier.size(18.dp),
                    imageVector = Icons.Default.KeyboardArrowUp,
                    contentDescription = "Move up"
                )
            }
            IconButton(
                onClick = onMoveDown,
                modifier = Modifier.size(32.dp),
                enabled = !isLast
            ) {
                Icon(
                    modifier = Modifier.size(18.dp),
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = "Move down"
                )
            }
            IconButton(
                onClick = onRemove,
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    modifier = Modifier.size(16.dp),
                    imageVector = Icons.Default.Close,
                    contentDescription = "Remove",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddContenderBottomSheet(
    isTagTitle: Boolean,
    eligibleWrestlers: List<Wrestler>,
    currentContenderCount: Int,
    maxContenders: Int = 12,
    onSave: (List<Wrestler>) -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var wrestler1 by remember { mutableStateOf<Wrestler?>(null) }
    var wrestler2 by remember { mutableStateOf<Wrestler?>(null) }
    var error by remember { mutableStateOf(false) }

    val isFull = currentContenderCount >= maxContenders

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
            Text(
                text = if (isTagTitle) "Add Contending Team" else "Add Contender",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "$currentContenderCount / $maxContenders ranked",
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            HorizontalDivider()

            if (isFull) {
                Text(
                    text = "Contender list is full. Remove someone first to add a new contender.",
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 13.sp
                )
            } else {
                SearchableWrestlerPicker(
                    label = if (isTagTitle) "Wrestler 1" else "Wrestler",
                    selected = wrestler1,
                    wrestlers = eligibleWrestlers,
                    onWrestlerSelected = {
                        wrestler1 = it
                        error = false
                    }
                )

                if (isTagTitle) {
                    SearchableWrestlerPicker(
                        label = "Wrestler 2",
                        selected = wrestler2,
                        wrestlers = eligibleWrestlers.filter { it.id != wrestler1?.id },
                        onWrestlerSelected = {
                            wrestler2 = it
                            error = false
                        }
                    )
                }

                if (error) {
                    Text(
                        text = if (isTagTitle)"Please select both team members" else "Please select a wrestler",
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 12.sp
                    )
                }

                Text(
                    text = "New contender is added at the bottom of the ranking. " +
                            "Use the up/down arrows afterward to reorder.",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Button(
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    onClick = {
                        val valid = if (isTagTitle) {
                            wrestler1 != null && wrestler2 != null
                        } else wrestler1 != null

                        if (!valid) { error = true; return@Button }
                        onSave(listOfNotNull(wrestler1, wrestler2))
                    }
                ) {
                    Text(
                        text = if (isTagTitle)"Add Team" else "Add Contender",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TitleDetailPreview() {
    TitleDetailScreen(
        title = ChampionshipData.titles[0],
        allReignsForTitle = ChampionshipData.getReignsForTitle(0),
        contenders = ChampionshipData.getContendersForTitle(0),
        onAddContenderClick = {},
        onSuggestContenderClick = {},
        onMoveUp = {},
        onMoveDown = {},
        onRemove = {},
        onLogTitleChangeClick = {},
        onBackClick = {}
    )
}