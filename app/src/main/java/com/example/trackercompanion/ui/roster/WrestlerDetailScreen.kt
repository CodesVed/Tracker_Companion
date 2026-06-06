package com.example.trackercompanion.ui.roster

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.Diversity3
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChip
import androidx.compose.material3.InputChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.trackercompanion.R
import com.example.trackercompanion.data.WrestlerData
import com.example.trackercompanion.model.Wrestler
import com.example.trackercompanion.model.enums.Type
import com.example.trackercompanion.ui.theme.Green
import com.example.trackercompanion.ui.theme.Grey
import com.example.trackercompanion.ui.theme.Red
import com.example.trackercompanion.ui.theme.TrackerCompanionTheme

@Composable
fun WrestlerDetailScreen(wrestler: Wrestler?, onEditClick: () -> Unit, onBackClick: () -> Unit) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(12.dp)
    ) {
        item {
            Row {
                Icon(
                    modifier = Modifier.size(30.dp).clickable(
                        enabled = true,
                        onClick = {
                            onBackClick.invoke()
                        }
                    ),
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back Arrow"
                )

                Text(
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    textAlign = TextAlign.Center,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    text = "Wrestler Profile"
                )

                Icon(
                    modifier = Modifier.size(30.dp).clickable(
                        enabled = true,
                        onClick = {
                            onBackClick.invoke()
                        }
                    ),
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Back Arrow"
                )
            }
        }

        item { HorizontalDivider(modifier = Modifier.padding(5.dp)) }

        item {
            Image(
                modifier = Modifier.fillMaxWidth().size(300.dp),
                painter = painterResource(wrestler?.imageRes ?: R.drawable.wrestler_placeholder),
                contentDescription = wrestler?.name
            )

            Spacer(modifier = Modifier.height(12.dp))
        }

        item {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    fontSize = 24.sp,
                    maxLines = 1,
                    text = wrestler?.name ?: "Default"
                )

                BrandChip(brand = wrestler?.brand.toString(), 22.sp, modifier = Modifier.size(28.dp))
            }
        }
        
        item { Spacer(modifier = Modifier.height(18.dp)) }


        val formattedWinPercent = "%.2f".format(wrestler?.winPercentage)

        item {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                StatCell(label = "Wins", value = wrestler?.wins.toString(), fontSize = 20.sp, modifier = Modifier.weight(1f))
                StatCell(label = "Loss", value = wrestler?.loss.toString(), fontSize = 20.sp, modifier = Modifier.weight(1f))
                StatCell(label = "Win Rate", value = formattedWinPercent, fontSize = 20.sp, modifier = Modifier.weight(1f))
                StatCell(label = "Points", value = wrestler?.points.toString(), fontSize = 20.sp, modifier = Modifier.weight(1f))
            }
        }

        item { Spacer(modifier = Modifier.height(18.dp)) }

        item {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    maxLines = 1,
                    fontSize = 20.sp,
                    text = "Status"
                )

                StatusChip(wrestler?.status.toString())
            }
        }

        item { Spacer(modifier = Modifier.height(18.dp)) }

        item {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    maxLines = 1,
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
                    contentDescription =
                        when (wrestler?.type) {
                            Type.SINGLE -> "Single"
                            Type.TEAM -> "Team"
                            else -> "Both"
                        },
                    modifier = Modifier.size(28.dp)
                )
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

@Preview(showBackground = true)
@Composable
fun WrestlerDetailPreview() {
    TrackerCompanionTheme {
        WrestlerDetailScreen(wrestler = WrestlerData.getById(58), onEditClick = {}, onBackClick = {})
    }
}