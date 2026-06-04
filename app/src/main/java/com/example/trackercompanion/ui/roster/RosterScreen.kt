package com.example.trackercompanion.ui.roster

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChip
import androidx.compose.material3.InputChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.trackercompanion.data.WrestlerData
import com.example.trackercompanion.model.Wrestler
import com.example.trackercompanion.navigation.Routes
import com.example.trackercompanion.ui.theme.Blue
import com.example.trackercompanion.ui.theme.Grey
import com.example.trackercompanion.ui.theme.Red


@Composable
fun RosterScreen(wrestlers: List<Wrestler>, onWrestlerClick: (Int)->Unit, onAddWrestlerClick: ()->Unit) {
    Box(
        modifier = Modifier.fillMaxSize().padding(10.dp)
    ) {
        Column {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "Locker Room",
                textAlign = TextAlign.Center,
                fontSize = 30.sp,

            )

            LazyColumn {
                items(wrestlers) {wrestler ->
                    WrestlerCard(
                        wrestler = wrestler,
                        onClick = { onWrestlerClick(wrestler.id) }
                    )
                }
            }
        }

        FloatingActionButton(
            modifier = Modifier.align(Alignment.BottomEnd).width(140.dp).height(70.dp),
            onClick = {onAddWrestlerClick}
        ) {
            Row {
                Icon(modifier = Modifier.size(28.dp), imageVector = Icons.Default.Add, contentDescription = null)

                Text(
                    fontSize = 20.sp,
                    text = "Add\nWrestler"
                )
            }
        }
    }
}

@Composable
fun WrestlerCard(wrestler: Wrestler, onClick: () -> Unit) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth().padding(10.dp),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            WrestlerAvatar(name = wrestler.name)

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

                    BrandChip(wrestler.brand.toString())
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    StatCell(label = "W", value = wrestler.wins.toString(), modifier = Modifier.weight(1f))
                    StatCell(label = "L", value = wrestler.loss.toString(), modifier = Modifier.weight(1f))
                    StatCell(label = "Pts", value = wrestler.points.toString(), modifier = Modifier.weight(1f))
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
        modifier = modifier.size(80.dp).clip(CircleShape).background(MaterialTheme.colorScheme.primary),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = initials,
            color = MaterialTheme.colorScheme.onPrimary,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
    }
}

@Composable
fun BrandChip(brand: String) {
    val brandColor = when (brand) {
        "RAW" -> Red
        "SD" -> Blue
        else -> Grey
    }

    InputChip(
        selected = true,
        onClick = {},
        label = {
            Text(text = brand, maxLines = 1, fontSize = 15.sp)
        },
        colors = InputChipDefaults.inputChipColors(
            selectedContainerColor = brandColor.copy(alpha = 0.15f),
            selectedLabelColor = brandColor
        ),
        leadingIcon = {
            Image(imageVector = Icons.Default.Badge, contentDescription = null)
        }
    )
}

@Composable
fun StatCell(label: String, value: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = label, fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(text = value, fontSize = 16.sp)
    }
}

@Preview(showSystemUi = true)
@Composable
fun RosterPreview() {
    RosterScreen(WrestlerData.roster, onWrestlerClick = {}, onAddWrestlerClick = {})
}