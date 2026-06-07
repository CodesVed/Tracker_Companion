package com.example.trackercompanion.ui.roster

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.trackercompanion.R
import com.example.trackercompanion.data.ChampionshipData
import com.example.trackercompanion.data.ShowData
import com.example.trackercompanion.data.WrestlerData
import com.example.trackercompanion.model.Championship
import com.example.trackercompanion.model.Match
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
    matchHistory: List<Match> = emptyList(),
    titleHistory: List<Championship> = emptyList(),
    onEditClick: () -> Unit,
    onBackClick: () -> Unit
) {
    val formattedWinPercent = "%.2f".format(wrestler?.winPercentage?:0f)


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
                StatCell(label = "Wins", value = wrestler?.wins.toString(), fontSize = 20.sp, modifier = Modifier.weight(1f))
                StatCell(label = "Loss", value = wrestler?.loss.toString(), fontSize = 20.sp, modifier = Modifier.weight(1f))
                StatCell(label = "Win Rate", value = formattedWinPercent, fontSize = 20.sp, modifier = Modifier.weight(1f))
                StatCell(label = "Points", value = wrestler?.points.toString(), fontSize = 20.sp, modifier = Modifier.weight(1f))
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

        // resolving champion history properly.
        // Right now the history is not being displayed correctly or as intended
        item {
            HorizontalDivider(modifier = Modifier.padding(8.dp))

            SectionHeader(
                title = "Championship History",
                count = titleHistory.size
            )

            Spacer(modifier = Modifier.height(4.dp))
        }

        if (titleHistory.isEmpty()) {
            item {
                EmptyState("No title reigns recorded.")
            }
        } else {
            items(titleHistory) {title ->
                TitleHistoryRow(title = title)
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
    wrestlerId in match.participantIds || match.participants.contains(wrestlerName, ignoreCase = true)

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
                modifier = Modifier.size(width = 32.dp, height = 32.dp),
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
                }

                Text(
                    text = "${match.stipulation}  ·  ${match.slot}",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                match.winnerLabel?.let {
                    Text(
                        text = "Winner: $it",
                        fontSize = 12.sp,
                        color = if (isWinner) Green else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Text(
                text = "Show #${match.showId}",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun TitleHistoryRow(title: Championship) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.size(24.dp),
                imageVector = Icons.Default.EmojiEvents,
                contentDescription = null,
                tint = Gold
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title.title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )

                Text(
                    text = "Reign #${title.reignNumber}  ·  Won: ${title.reignStartEpisode}",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            if (title.championId != null) {
                Surface(
                    shape = MaterialTheme.shapes.small,
                    color = Green.copy(alpha = 0.15f)
                ) {
                    Text(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        text = "Current",
                        fontSize = 11.sp,
                        color = Green
                    )
                }
            } else {
                Text(
                    text = "${title.defenses} def.",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
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
            titleHistory = emptyList()
        )
    }
}