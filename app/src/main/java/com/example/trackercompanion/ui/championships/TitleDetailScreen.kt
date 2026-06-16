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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.trackercompanion.ui.roster.EmptyState
import com.example.trackercompanion.ui.roster.SectionHeader
import com.example.trackercompanion.ui.roster.TitleReignRow
import com.example.trackercompanion.ui.theme.Gold
import com.example.trackercompanion.ui.theme.Grey

@Composable
fun TitleDetailScreen(
    title: Championship,
    allReignsForTitle: List<TitleReign>,
    contenders: List<Contendership>,
    onMoveUp: (Contendership) -> Unit,
    onMoveDown: (Contendership) -> Unit,
    onRemove: (Contendership) -> Unit,
    onLogTitleChangeClick: () -> Unit,
    onBackClick: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {
        item {
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
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = if (title.currentChampion?.holderNames != null) Gold.copy(alpha = 0.08f)
                                     else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                ),
                border = if (title.currentChampion?.holderNames != null) BorderStroke(1.dp, Gold.copy(alpha = 0.3f))
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
                        tint = if (title.currentChampion != null) Gold else Grey
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        TitleStatCell(
                            label = "",
                            value = title.currentChampion?.holderNames?.joinToString(" & ") ?: "Vacant",
                            fontSize = 22.sp,
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        TitleStatCell(
                            label = "Reign: ",
                            value = if (title.currentChampion?.reignNumber?.toString() != null) "#${title.currentChampion.reignNumber}" else "N/A",
                            fontSize = 16.sp,
                        )

                        Spacer(modifier = Modifier.height(2.dp))

                        TitleStatCell(
                            label = "Defenses: ",
                            value = if (title.currentChampion?.defenses != null) title.currentChampion.defenses.toString() else "N/A",
                            fontSize = 16.sp
                        )

                        Spacer(modifier = Modifier.height(2.dp))

                        TitleStatCell(
                            label = "Champion Since: ",
                            value = if (title.currentChampion?.wonAtEvent != null) title.currentChampion.wonAtEvent else "N/A",
                            fontSize = 16.sp
                        )
                    }
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))
        }

        item {
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
        }

        item {
            HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))

            SectionHeader(
                title = "Reign History",
                count = allReignsForTitle.size
            )

            Spacer(modifier = Modifier.height(4.dp))
        }

        if (allReignsForTitle.isEmpty()) {
            item {
                EmptyState(message = "No title reigns yet.")
            }
        } else {
            items(allReignsForTitle.sortedByDescending { it.reignNumber }) { reign ->
                TitleReignRow(reign = reign)
            }
        }

        item { HorizontalDivider(modifier = Modifier.padding(10.dp)) }

        item {
            Spacer(modifier = Modifier.height(4.dp))

            ContendershipSection(
                contenders = contenders,
                wrestlers = WrestlerData.roster,
                onMoveUp = onMoveUp,
                onMoveDown = onMoveDown,
                onRemove = onRemove
            )
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
                val wrestler = wrestlers.find { it.id == contender.wrestlerId }
                ContenderRow(
                    rank = contender.rank,
                    wrestlerName = wrestler?.name ?: "Unknown",
                    brand = wrestler?.brand,
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
    wrestlerName: String,
    brand: Brand?,
    isFirst: Boolean,
    isLast: Boolean,
    onMoveUp: () -> Unit,
    onMoveDown: () -> Unit,
    onRemove: () -> Unit
) {
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
                Text(
                    text = wrestlerName,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
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

@Preview(showBackground = true)
@Composable
fun TitleDetailPreview() {
    TitleDetailScreen(
        title = ChampionshipData.titles[0],
        allReignsForTitle = ChampionshipData.getReignsForTitle(0),
        contenders = ChampionshipData.getContendersForTitle(0),
        onMoveUp = {},
        onMoveDown = {},
        onRemove = {},
        onLogTitleChangeClick = {},
        onBackClick = {}
    )
}