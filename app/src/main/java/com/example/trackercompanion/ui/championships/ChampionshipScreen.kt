package com.example.trackercompanion.ui.championships

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.trackercompanion.data.ChampionshipData
import com.example.trackercompanion.model.Championship
import com.example.trackercompanion.model.TitleReign
import com.example.trackercompanion.model.Wrestler

@Composable
fun ChampionshipScreen(
    championships: List<Championship>,
){

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {
        Column {
            Text(
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                text = "Glory Hall"
            )

            HorizontalDivider(modifier = Modifier.padding(5.dp))

            LazyColumn {
                items(championships, key = { it.id }) {championship ->
                    TitleCard(
                        championship = championship,
                        currentChampion = championship.currentChampion
                    )
                }
            }
        }
    }
}

@Composable
fun TitleCard(championship: Championship, currentChampion: TitleReign?) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        onClick = {}
    ) {
        Column(
            modifier = Modifier.padding(4.dp)
        ) {
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .size(80.dp),
                painter = painterResource(id = championship.titleImage),
                contentDescription = null
            )
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = championship.title,
                textAlign = TextAlign.Center,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(6.dp)
            ) {
                TitleStatCell(
                    label = "Current Champion: ",
                    value = currentChampion?.holderNames?.joinToString(" & ") ?: "Vacant",
                    fontSize = 16.sp
                )
                TitleStatCell(
                    label = "Reign ",
                    value = "#${currentChampion?.reignNumber?.toString() ?: "N/A"}",
                    fontSize = 16.sp
                )
                TitleStatCell(
                    label = "Defenses: ",
                    value = currentChampion?.defenses.toString(),
                    fontSize = 16.sp
                )
            }
        }
    }
}

@Composable
fun TitleStatCell(label: String, value: String, fontSize: TextUnit ,modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, fontSize = fontSize, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(text = value, fontSize = fontSize, color = MaterialTheme.colorScheme.onTertiaryContainer)
    }
}

@Preview(showBackground = true)
@Composable
fun ChampionshipScreenPreview(){
    ChampionshipScreen(
        championships = ChampionshipData.titles
    )
}