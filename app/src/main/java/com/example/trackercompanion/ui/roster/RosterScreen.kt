package com.example.trackercompanion.ui.roster

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.InputChip
import androidx.compose.material3.InputChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.trackercompanion.data.ShowData
import com.example.trackercompanion.data.WrestlerData
import com.example.trackercompanion.model.computeStatsForWrestler
import com.example.trackercompanion.model.Match
import com.example.trackercompanion.model.Wrestler
import com.example.trackercompanion.ui.theme.Blue
import com.example.trackercompanion.ui.theme.Grey
import com.example.trackercompanion.ui.theme.Pink
import com.example.trackercompanion.ui.theme.Red


@Composable
fun RosterScreen(
    wrestlers: List<Wrestler>,
    selectedBrand: String,
    selectedSort: String,
    searchQuery: String,
    onSearchQueryChanged: (String) -> Unit,
    onBrandSelected: (String) -> Unit,
    onSortSelected: (String) -> Unit,
    onWrestlerClick: (Int) -> Unit,
    onAddWrestlerClick: () -> Unit,
    matcheSources: List<Match>
) {
    val brandTypes = listOf("ALL", "RAW", "SD", "DIVA", "FREE")
    val sorts = listOf("Name", "Points", "Win Rate")
    var sortMenuExpanded by remember { mutableStateOf(false) }


    Box(
        modifier = Modifier.fillMaxSize().padding(12.dp)
    ) {
        Column {
            Text(
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                text = "Locker Room",

            )

            // ── Search bar ─────────────────────────────────
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchQueryChanged,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Search wrestlers...") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search"
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotBlank()) {
                        IconButton(onClick = { onSearchQueryChanged("") }) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Clear search"
                            )
                        }
                    }
                },
                singleLine = true,
                shape = MaterialTheme.shapes.large
            )

            Spacer(modifier = Modifier.height(8.dp))

            // ── Brand Filter Chips ────────────────────────────────────
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(brandTypes) {brand ->
                    FilterChip(
                        selected = selectedBrand == brand,
                        onClick = { onBrandSelected(brand) },
                        label = { Text(brand) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // ── Sort row ────────────────────────────────────
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${wrestlers.size} wrestler${if (wrestlers.size != 1) "s" else ""}",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Sort: ",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Box {
                        TextButton(
                            onClick = { sortMenuExpanded = true}
                        ) {
                            Text(text = selectedSort, fontSize = 13.sp)
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        DropdownMenu(
                            expanded = sortMenuExpanded,
                            onDismissRequest = { sortMenuExpanded = false}
                        ) {
                            sorts.forEach { sort ->
                                DropdownMenuItem(
                                    text = { Text(sort) },
                                    onClick = {
                                        onSortSelected(sort)
                                        sortMenuExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }
            }

            // ── Empty state for search ─────────────────────
            if (wrestlers.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            modifier = Modifier.size(48.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = if (searchQuery.isNotBlank())
                                "No wrestlers match \"$searchQuery\""
                            else
                                "No wrestlers in this brand.",
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 14.sp
                        )
                    }
                }
            } else {
                // ── List ────────────────────────────────────
                LazyColumn {
                    items(wrestlers, key = { it.id }) {wrestler ->
                        WrestlerCard(
                            wrestler = wrestler,
                            onClick = { onWrestlerClick(wrestler.id) },
                            matches = matcheSources
                        )
                    }
                }
            }
        }

        FloatingActionButton(
            modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp),
            onClick = { onAddWrestlerClick.invoke() }
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
                Text(text = "Add Wrestler", fontSize = 16.sp)
            }
        }
    }
}

@Composable
fun WrestlerCard(wrestler: Wrestler, matches: List<Match>, onClick: () -> Unit) {
    val stats = remember(matches) {
        computeStatsForWrestler(wrestler.id, matches)
    }

    ElevatedCard(
        modifier = Modifier.fillMaxWidth().padding(10.dp),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            WrestlerAvatar(name = wrestler.name, modifier = Modifier.clip(CircleShape))

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier.weight(1f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontSize = 22.sp,
                        text = wrestler.name
                    )

                    BrandChip(brand = wrestler.brand.toString(), fontSize = 15.sp)
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    StatCell(label = "W", value = stats.wins.toString(), fontSize = 16.sp, modifier = Modifier.weight(1f))
                    StatCell(label = "L", value = stats.losses.toString(), fontSize = 16.sp, modifier = Modifier.weight(1f))
                    StatCell(label = "Pts", value = stats.points.toString(), fontSize = 16.sp, modifier = Modifier.weight(1f))
                }
            }

        }
    }
}

@Composable
fun WrestlerAvatar(name: String, modifier: Modifier = Modifier) {
    val initials = name.split(" ")
        .take(2)
        .joinToString("") { it.first().uppercase() }

    Box(
        modifier = modifier.size(80.dp).background(MaterialTheme.colorScheme.primary),
        contentAlignment = Alignment.Center
    ) {
        Text(
            color = MaterialTheme.colorScheme.onPrimary,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            text = initials
        )
    }
}

@Composable
fun BrandChip(brand: String, fontSize: TextUnit, modifier: Modifier = Modifier) {
    val brandColor = when (brand) {
        "RAW" -> Red
        "SD" -> Blue
        "DIVA" -> Pink
        else -> Grey
    }

    InputChip(
        selected = true,
        onClick = {},
        label = {
            Text(text = brand, maxLines = 1, fontSize = fontSize)
        },
        colors = InputChipDefaults.inputChipColors(
            selectedContainerColor = brandColor.copy(alpha = 0.15f),
            selectedLabelColor = brandColor
        ),
        leadingIcon = {
            Image(imageVector = Icons.Default.Badge, contentDescription = "Brand Badge", modifier = modifier)
        }
    )
}

@Composable
fun StatCell(label: String, value: String, fontSize: TextUnit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = label, fontSize = fontSize, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(text = value, fontSize = fontSize, color = MaterialTheme.colorScheme.onTertiaryContainer)
    }
}


@Preview(showBackground = true)
@Composable
fun RosterPreview() {
    RosterScreen(
        wrestlers = WrestlerData.roster,
        matcheSources = ShowData.matches,
        selectedBrand = "ALL",
        selectedSort = "Name",
        searchQuery = "",
        onSearchQueryChanged = {},
        onBrandSelected = {},
        onSortSelected = {},
        onWrestlerClick = {},
        onAddWrestlerClick = {}
    )
}