package com.example.trackercompanion.ui.roster

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Grid
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilterChip
import androidx.compose.material3.InputChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.trackercompanion.R
import com.example.trackercompanion.data.WrestlerData
import com.example.trackercompanion.model.Wrestler
import com.example.trackercompanion.ui.theme.Accent
import com.example.trackercompanion.ui.theme.Dark


@Composable
fun RosterScreen() {
    Box(
        modifier = Modifier.fillMaxSize().systemBarsPadding().padding(10.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Column {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "Locker Room",
                textAlign = TextAlign.Center,
                fontSize = 30.sp,

            )

            LazyColumn {
                items(WrestlerData.roster.size) {
                    WrestlerCard()
                }
            }
        }
    }
}

@Composable
fun WrestlerCard() {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth().padding(10.dp),
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier.size(100.dp).padding(10.dp).clip(CircleShape),
                painter = painterResource(R.drawable.ic_launcher_background),
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
            
            Column {
                Row {
                    Text(
                        modifier = Modifier.padding(10.dp),
                        text = "Rob Van Dam",
                        fontSize = 25.sp
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    InputChip(
                        modifier = Modifier.padding(5.dp),
                        selected = true,
                        onClick = {

                        },
                        label = {Text(text = "Label")},
                        leadingIcon = {Image(imageVector = Icons.Default.Person, contentDescription = null)}
                    )
                }

                Row {
                    Text(
                        modifier = Modifier.padding(10.dp),
                        text = "Wins: 4",
                        fontSize = 18.sp
                    )

                    Text(
                        modifier = Modifier.padding(10.dp),
                        text = "Loss: 9",
                        fontSize = 18.sp
                    )

                    Text(
                        modifier = Modifier.padding(10.dp),
                        text = "Points: 45",
                        fontSize = 18.sp
                    )
                }
            }

        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun RosterPreview() {
    RosterScreen()
}