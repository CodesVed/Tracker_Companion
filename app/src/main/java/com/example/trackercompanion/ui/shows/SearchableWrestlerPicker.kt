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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.trackercompanion.model.Wrestler
import com.example.trackercompanion.model.enums.Brand
import com.example.trackercompanion.ui.theme.Blue
import com.example.trackercompanion.ui.theme.Gold
import com.example.trackercompanion.ui.theme.Red

@Composable
fun SearchableWrestlerPicker(
    label: String,
    selected: Wrestler?,
    wrestlers: List<Wrestler>,
    onWrestlerSelected: (Wrestler) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded  by remember { mutableStateOf(false) }
    var query     by remember { mutableStateOf("") }

    val filtered = wrestlers.filter { wrestler ->
        query.isBlank() || wrestler.name.contains(query.trim(), ignoreCase = true)
    }

    Column(modifier = modifier.fillMaxWidth()) {

        // ── Trigger row ────────────────────────────────────
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    expanded = !expanded
                    if (!expanded) query = "" // clear search on close
                },
            shape = MaterialTheme.shapes.small,
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
            tonalElevation = 1.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (selected != null) {
                    WrestlerPickerAvatar(name = selected.name, brand = selected.brand)
                    Spacer(modifier = Modifier.width(10.dp))
                } else {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier.size(32.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = label,
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = selected?.name ?: "Tap to select",
                        fontSize = 15.sp,
                        fontWeight = if (selected != null) FontWeight.Medium
                        else FontWeight.Normal,
                        color = if (selected != null)
                            MaterialTheme.colorScheme.onSurface
                        else
                            MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp
                    else Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // ── Expanded search + list ─────────────────────────
        AnimatedVisibility(visible = expanded) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outlineVariant,
                        shape = MaterialTheme.shapes.small
                    )
                    .background(
                        color = MaterialTheme.colorScheme.surface,
                        shape = MaterialTheme.shapes.small
                    )
            ) {
                OutlinedTextField(
                    value = query,
                    onValueChange = { query = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    placeholder = { Text("Search by name...") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                    },
                    trailingIcon = {
                        if (query.isNotBlank()) {
                            IconButton(onClick = { query = "" }) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "Clear",
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    },
                    singleLine = true,
                    shape = MaterialTheme.shapes.medium
                )

                HorizontalDivider()

                Text(
                    text = "${filtered.size} wrestler${if (filtered.size != 1) "s" else ""}",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                )

                if (filtered.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No wrestlers match \"$query\"",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.heightIn(max = 220.dp)
                    ) {
                        items(filtered, key = { it.id }) { wrestler ->
                            WrestlerPickerRow(
                                wrestler = wrestler,
                                isSelected = wrestler.id == selected?.id,
                                onClick = {
                                    onWrestlerSelected(wrestler)
                                    expanded = false
                                    query = ""
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun WrestlerPickerRow(
    wrestler: Wrestler,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .background(
                if (isSelected)
                    MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
                else
                    MaterialTheme.colorScheme.surface
            )
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        WrestlerPickerAvatar(name = wrestler.name, brand = wrestler.brand)

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = wrestler.name,
                fontSize = 14.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
            )
            Text(
                text = wrestler.brand.name,
                fontSize = 11.sp,
                color = when (wrestler.brand) {
                    Brand.RAW  -> Red
                    Brand.SD   -> Blue
                    else       -> MaterialTheme.colorScheme.onSurfaceVariant
                }
            )
        }

        // Checkmark for currently selected wrestler
        if (isSelected) {
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(20.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = "✓",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
    }
    HorizontalDivider(thickness = 0.5.dp)
}

@Composable
fun WrestlerPickerAvatar(name: String, brand: Brand) {
    val initials = name.split(" ")
        .take(2)
        .joinToString("") { it.first().uppercase() }

    val brandColor = when (brand) {
        Brand.RAW  -> Red
        Brand.SD   -> Blue
        Brand.DIVA -> Gold
        else       -> MaterialTheme.colorScheme.primary
    }

    Box(
        modifier = Modifier
            .size(32.dp)
            .clip(CircleShape)
            .background(brandColor.copy(alpha = 0.2f)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = initials,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = brandColor
        )
    }
}
