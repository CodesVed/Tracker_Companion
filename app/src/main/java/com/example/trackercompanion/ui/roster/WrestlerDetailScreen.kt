package com.example.trackercompanion.ui.roster

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.Diversity3
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChip
import androidx.compose.material3.InputChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.trackercompanion.R
import com.example.trackercompanion.model.MatchStats
import com.example.trackercompanion.data.ShowData
import com.example.trackercompanion.data.WrestlerData
import com.example.trackercompanion.model.Match
import com.example.trackercompanion.model.TitleReign
import com.example.trackercompanion.model.Wrestler
import com.example.trackercompanion.model.enums.Type
import com.example.trackercompanion.ui.theme.Gold
import com.example.trackercompanion.ui.theme.Green
import com.example.trackercompanion.ui.theme.Grey
import com.example.trackercompanion.ui.theme.Red
import com.example.trackercompanion.ui.theme.TrackerCompanionTheme

@Composable
fun WrestlerDetailScreen(
    wrestler: Wrestler?,
    stats: MatchStats = MatchStats(0, 0, 0, 0, 0f, 0),
    matchHistory: List<Match> = emptyList(),
    titleReigns: List<TitleReign> = emptyList(),
    onEditClick: () -> Unit,
    onBackClick: () -> Unit
) {
    val formattedWinPercent = "%.2f".format(stats.winPercent)


    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(12.dp)
    ) {
        item {
            Row {
                Icon(
                    modifier = Modifier.size(30.dp).clickable(
                        onClick = {
                            onBackClick.invoke()
                        }
                    ),
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back"
                )

                Text(
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    text = "Wrestler Profile"
                )

                Icon(
                    modifier = Modifier.size(30.dp).clickable(
                        onClick = {
                            onEditClick.invoke()
                        }
                    ),
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit"
                )
            }
        }

        item { HorizontalDivider(modifier = Modifier.padding(5.dp)) }

        item {
            Image(
                modifier = Modifier.fillMaxWidth().height(300.dp),
                painter = painterResource(wrestler?.imageRes ?: R.drawable.wrestler_placeholder),
                contentDescription = wrestler?.name
            )

            Spacer(modifier = Modifier.height(12.dp))
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    fontSize = 24.sp,
                    text = wrestler?.name ?: "Default"
                )

                BrandChip(brand = wrestler?.brand.toString(), 22.sp, modifier = Modifier.size(28.dp))
            }

            Spacer(modifier = Modifier.height(8.dp))
        }

        item {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                StatCell(label = "Wins", value = stats.wins.toString(), fontSize = 20.sp, modifier = Modifier.weight(1f))
                StatCell(label = "Loss", value = stats.losses.toString(), fontSize = 20.sp, modifier = Modifier.weight(1f))
                StatCell(label = "Win Rate", value = formattedWinPercent, fontSize = 20.sp, modifier = Modifier.weight(1f))
                StatCell(label = "Points", value = stats.points.toString(), fontSize = 20.sp, modifier = Modifier.weight(1f))
            }

            HorizontalDivider(modifier = Modifier.padding(8.dp))
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    fontSize = 20.sp,
                    text = "Status"
                )

                StatusChip(wrestler?.status.toString())
            }

            Spacer(modifier = Modifier.height(18.dp))
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    fontSize = 20.sp,
                    text = "Type"
                )

                Icon(
                    imageVector =
                        when (wrestler?.type) {
                            Type.SINGLE -> Icons.Default.Person
                            Type.TEAM -> Icons.Default.Group
                            else -> Icons.Default.Diversity3
                        },
                    contentDescription = wrestler?.type?.name,
                    modifier = Modifier.size(28.dp)
                )
            }
        }

        item {
            HorizontalDivider(modifier = Modifier.padding(8.dp))

            SectionHeader(
                title = "Match History",
                count = matchHistory.size
            )

            Spacer(modifier = Modifier.height(4.dp))
        }

        if (matchHistory.isEmpty()) {
            item {
                EmptyState(message = "No matches logged yet.")
            }
        } else {
            items(matchHistory) {match ->
                MatchHistoryRow(
                    match = match,
                    wrestlerId = wrestler?.id?:-1,
                    wrestlerName = wrestler?.name?:""
                )
            }
        }

        item {
            HorizontalDivider(modifier = Modifier.padding(8.dp))

            SectionHeader(
                title = "Championship History",
                count = titleReigns.size
            )

            Spacer(modifier = Modifier.height(4.dp))
        }

        if (titleReigns.isEmpty()) {
            item {
                EmptyState("No title reigns recorded.")
            }
        } else {
            items(titleReigns) {reign ->
                TitleReignRow(reign = reign, currentWrestlerName = wrestler?.name?:"")
            }
        }

        //── Notes ──────────────────────────
        if (!wrestler?.notes.isNullOrBlank()) {
            item {
                HorizontalDivider(modifier = Modifier.padding(8.dp))

                Text(
                    text = "Notes",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Text(
                    modifier = Modifier.padding(top = 4.dp),
                    text = wrestler.notes,
                    fontSize = 15.sp
                )

                Spacer(modifier = Modifier.height(6.dp))
            }
        }
    }
}

@Composable
fun StatusChip(status: String, modifier: Modifier = Modifier) {
    val statusColor = when(status) {
        "ACTIVE" -> Green
        "INACTIVE" -> Grey
        "INJURED" -> Red
        else -> MaterialTheme.colorScheme.inverseOnSurface
    }

    InputChip(
        modifier = modifier,
        selected = true,
        onClick = {},
        label = {
            Text(text = status, maxLines = 1, fontSize = 20.sp)
        },
        colors = InputChipDefaults.inputChipColors(
            leadingIconColor = statusColor,
            selectedContainerColor = statusColor.copy(alpha = 0.15f)
        ),
        leadingIcon = {
            Image(imageVector = Icons.Default.Circle, contentDescription = "Status Badge", modifier = Modifier.size(12.dp))
        },
    )
}

@Composable
fun SectionHeader(title: String, count: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "$count entries",
            fontSize = 15.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun EmptyState(message: String) {
    Box(
        modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = message,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun MatchHistoryRow(match: Match, wrestlerId: Int, wrestlerName: String) {
    val isWinner = if (match.winnerIds.isNotEmpty()) {
        wrestlerId in match.winnerIds
    } else {
        match.winnerLabel?.contains(wrestlerName, ignoreCase = true) == true
    }


    val resultColor = if (isWinner) Green else Red
    val resultLabel = if (isWinner) "W" else "L"

    Card(
        modifier = Modifier.fillMaxWidth().padding(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(32.dp),
                shape = MaterialTheme.shapes.small,
                color = resultColor.copy(alpha = 0.15f),
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = resultLabel,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = resultColor
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
                    if (match.isTagMatch) {
                        Icon(
                            modifier = Modifier.size(14.dp),
                            imageVector = Icons.Default.Group,
                            contentDescription = "Tag Match",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.width(4.dp))
                    }

                    Text(
                        maxLines = 1,
                        text = match.participants,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Text(
                    text = "${match.stipulation}  ·  ${match.slot}",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Text(
                text = ShowData.getShowLabel(match.showId, match.showType),
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun TitleReignRow(reign: TitleReign, currentWrestlerName: String? = null) {
    val isCurrentChampion = reign.lostAtEvent == null
    val isTagReign = reign.holderIds.size > 1

    val holderDisplay = if (isTagReign) {
        reign.holderNames.joinToString(" & ")
    } else {
        reign.holderNames.firstOrNull()?:""
    }

    val partners = reign.holderNames.filter { it != currentWrestlerName }
    val partnerDisplay = if (currentWrestlerName != null && isTagReign) {
        if (partners.isNotEmpty()) "w/ ${partners.joinToString(" & ")}" else null
    } else null

    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isCurrentChampion) {
                Gold.copy(alpha = 0.08f)
            } else {
                MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
            }
        ),
        border = if (isCurrentChampion) {
            BorderStroke(1.dp, Gold.copy(alpha = 0.4f))
        } else null
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.size(28.dp),
                imageVector = Icons.Default.EmojiEvents,
                contentDescription = null,
                tint = if (isCurrentChampion) Gold
                       else MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = reign.titleName,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )

                if (currentWrestlerName == null) {
                    Text(
                        text = holderDisplay,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = if (isCurrentChampion) Gold
                        else MaterialTheme.colorScheme.onSurface
                    )
                }

                Text(
                    text = "Reign #${reign.reignNumber}",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Text(
                    text = "Won: ${reign.wonAtEvent}",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                if (!isCurrentChampion) {
                    Text(
                        text = "Lost: ${reign.lostAtEvent}",
                        fontSize = 12.sp,
                        color = Red.copy(alpha = 0.8f)
                    )
                }

                if (partnerDisplay != null) {
                    Text(
                        text = partnerDisplay,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                if (reign.defenses > 0) {
                    Text(
                        text = "${reign.defenses} defenses${if (reign.defenses > 1) "s" else ""}",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                if (reign.notes.isNotBlank()) {
                    Text(
                        text = reign.notes,
                        fontSize = 11.sp,
                        fontStyle = FontStyle.Italic,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Surface(
                shape = MaterialTheme.shapes.small,
                color = if (isCurrentChampion) Gold.copy(alpha = 0.2f)
                        else MaterialTheme.colorScheme.onSurfaceVariant
            ) {
                Text(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    text = if (isCurrentChampion) "Champion" else "Former",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isCurrentChampion) Gold
                            else MaterialTheme.colorScheme.inverseOnSurface
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WrestlerDetailPreview() {
    TrackerCompanionTheme {
        WrestlerDetailScreen(
            wrestler = WrestlerData.getById(51),
            onEditClick = {},
            onBackClick = {},
            matchHistory = emptyList(),
            titleReigns = emptyList()
        )
    }
}