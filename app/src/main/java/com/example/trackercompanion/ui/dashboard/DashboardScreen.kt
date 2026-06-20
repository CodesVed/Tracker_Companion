package com.example.trackercompanion.ui.dashboard

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.LiveTv
import androidx.compose.material.icons.filled.SportsMartialArts
import androidx.compose.material.icons.filled.SportsMma
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.trackercompanion.data.CalendarData
import com.example.trackercompanion.data.ChampionshipData
import com.example.trackercompanion.data.ShowData
import com.example.trackercompanion.data.WrestlerData
import com.example.trackercompanion.model.CalendarWeek
import com.example.trackercompanion.model.Championship
import com.example.trackercompanion.model.Match
import com.example.trackercompanion.model.MatchStats
import com.example.trackercompanion.model.PPVEvent
import com.example.trackercompanion.model.ShowEpisode
import com.example.trackercompanion.model.TitleReign
import com.example.trackercompanion.model.Wrestler
import com.example.trackercompanion.model.computeStatsForWrestler
import com.example.trackercompanion.model.enums.Brand
import com.example.trackercompanion.model.enums.Show
import com.example.trackercompanion.ui.theme.Blue
import com.example.trackercompanion.ui.theme.Gold
import com.example.trackercompanion.ui.theme.Grey
import com.example.trackercompanion.ui.theme.Red

@Composable
fun DashboardScreen(
    wrestlers: List<Wrestler>,
    matches: List<Match>,
    episodes: List<ShowEpisode>,
    ppvEvents: List<PPVEvent>,
    championships: List<Championship>,
    reigns: List<TitleReign>,
    calendarWeeks: List<CalendarWeek>,
    onShowClick: (CalendarWeek) -> Unit
){
    val currentWeek = calendarWeeks.maxByOrNull { it.weekNumber }

    val lastLoggedWeek = calendarWeeks
        .filter {week ->
            (week.linkedShowId != null && episodes.any { it.id == week.linkedShowId}) ||
            (week.linkedPPVId != null && episodes.any { it.id == week.linkedPPVId })
        }
        .maxByOrNull { it.weekNumber }

    val nextEvent = calendarWeeks
        .filter { it.weekNumber > (lastLoggedWeek?.weekNumber?:0) }
        .minByOrNull { it.weekNumber }

    val recentShows: List<Any> = buildList {
        addAll(episodes)
        addAll(ppvEvents)
    }.sortedByDescending {
        when (it) {
            is ShowEpisode -> it.id
            is PPVEvent -> it.id
            else -> 0
        }
    }. take(3)

    val topWrestlers = remember (wrestlers, matches) {
        wrestlers
            .asSequence()
            .filter { it.brand == Brand.RAW || it.brand == Brand.SD }
            .map { w -> w to computeStatsForWrestler(w.id, matches) }
            .filter { (_, stats) -> stats.totalMatches > 0 }
            .sortedWith(
                compareByDescending<Pair<Wrestler, MatchStats>> { it.second.points }
                    .thenByDescending { it.second.winPercent }
            )
            .take(5)
            .toList()
    }

    val totalMatches  = matches.size
    val totalEpisodes = episodes.size + ppvEvents.size
    val rawEpisodes   = episodes.count { it.brand == Brand.RAW }
    val sdEpisodes    = episodes.count { it.brand == Brand.SD }
    val ppvEpisodes = ppvEvents.size

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        item {
            DashboardHeader(currentWeek = currentWeek)
        }

        // ── Quick Stats Row ──────────────────────────────────
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                QuickStatCell(label = "Matches", value = totalMatches.toString())
                VerticalDivider()
                QuickStatCell(label = "Shows", value = totalEpisodes.toString())
                VerticalDivider()
                QuickStatCell(label = "RAW", value = rawEpisodes.toString())
                VerticalDivider()
                QuickStatCell(label = "SD", value = sdEpisodes.toString())
                VerticalDivider()
                QuickStatCell(label = "PPV", value = ppvEpisodes.toString())
            }
            HorizontalDivider()
        }

        // ── Next event ─────────────────────────────────────
        item {
            nextEvent?.let {
                DashboardSection(title = "Next Event") {
                    NextEventCard(week = it, onClick = { onShowClick(it) })
                }
            }
        }

        // ── Championships ──────────────────────────────────
        item {
            DashboardSection(title = "Championships") {
                ChampionshipScrollRow(
                    championships = championships,
                    reigns = reigns
                )
            }
        }

        // ── Recent shows ───────────────────────────────────
        item {
            DashboardSection(title = "Recent Shows") {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    if (recentShows.isEmpty()) {
                        Text(
                            text = "No shows logged yet.",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    } else {
                        recentShows.forEach { show ->
                            when (show) {
                                is ShowEpisode -> RecentShowCard(
                                    episode = show,
                                    matchCount = matches.count {
                                        it.showId == show.id && it.showType == Show.SHOW
                                    }
                                )
                                is PPVEvent -> RecentPPVCard(
                                    ppv = show,
                                    matchCount = matches.count {
                                        it.showId == show.id && it.showType == Show.PPV
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }

        // ── Top 5 wrestlers ────────────────────────────────
        item {
            DashboardSection(title = "Top Wrestlers") {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    if (topWrestlers.isEmpty()) {
                        Text(
                            text = "No match data yet.",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    } else {
                        topWrestlers.forEachIndexed { index, (wrestler, stats) ->
                            TopWrestlerRow(
                                rank = index + 1,
                                wrestler = wrestler,
                                points = stats.points,
                                wins = stats.wins,
                                losses = stats.losses,
                                winPercent = stats.winPercent
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DashboardHeader(currentWeek: CalendarWeek?) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.Stars,
                contentDescription = null,
                tint = Gold,
                modifier = Modifier.size(36.dp)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "WWE SmackDown: HCTP",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                letterSpacing = 3.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "GM Office",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Surface(
                shape = MaterialTheme.shapes.small,
                color = Gold.copy(alpha = 0.15f)
            ) {
                Text(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                    text = if (currentWeek != null)
                        "Current: Week ${currentWeek.weekNumber} — ${currentWeek.showLabel}"
                    else
                        "No weeks scheduled yet",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = Gold
                )
            }
        }
    }
    HorizontalDivider()
}

@Composable
fun QuickStatCell(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = label,
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun VerticalDivider() {
    Box(
        modifier = Modifier
            .height(32.dp)
            .width(1.dp)
            .background(MaterialTheme.colorScheme.outlineVariant)
    )
}

// ── Reusable section wrapper ───────────────────────────────
@Composable
fun DashboardSection(
    title: String,
    content: @Composable () -> Unit
) {
    Column(modifier = Modifier.padding(top = 16.dp)) {
        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = title,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.5.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(8.dp))
        content()
        Spacer(modifier = Modifier.height(4.dp))
        HorizontalDivider(modifier = Modifier.padding(top = 16.dp))
    }
}

@Composable
fun NextEventCard(week: CalendarWeek, onClick: () -> Unit) {
    val isPPV = week.linkedPPVId != null
    val accentColor = if (isPPV) Gold else MaterialTheme.colorScheme.primary

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(
            containerColor = accentColor.copy(alpha = 0.08f)
        ),
        border = BorderStroke(1.dp, accentColor.copy(alpha = 0.3f)),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = if (isPPV) Icons.Default.Bolt else Icons.Default.LiveTv,
                contentDescription = null,
                tint = accentColor,
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = week.showLabel,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = accentColor
                )
                Text(
                    text = "Week ${week.weekNumber}",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (week.notes.isNotBlank()) {
                    Text(
                        text = week.notes,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            if (isPPV) {
                Surface(
                    shape = MaterialTheme.shapes.small,
                    color = Gold.copy(alpha = 0.2f)
                ) {
                    Text(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        text = "PPV",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Gold
                    )
                }
            }
        }
    }
}

@Composable
fun ChampionshipScrollRow(
    championships: List<Championship>,
    reigns: List<TitleReign>
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        championships.forEach { championship ->
            val currentReign = reigns.find {
                it.titleId == championship.id && it.lostAtEvent == null
            }
            ChampionshipMiniCard(
                championship = championship,
                currentReign = currentReign
            )
        }
    }
}

@Composable
fun ChampionshipMiniCard(
    championship: Championship,
    currentReign: TitleReign?
) {
    val isHeld = currentReign != null
    val holderDisplay = currentReign?.holderNames?.joinToString(" & ") ?: "Vacant"

    Card(
        modifier = Modifier.width(160.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isHeld) Gold.copy(alpha = 0.08f)
            else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        ),
        border = BorderStroke(
            1.dp,
            if (isHeld) Gold.copy(alpha = 0.35f)
            else MaterialTheme.colorScheme.outlineVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.EmojiEvents,
                contentDescription = null,
                tint = if (isHeld) Gold else Grey,
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = championship.title,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = holderDisplay,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                color = if (isHeld) MaterialTheme.colorScheme.onSurface
                else MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            if (currentReign != null) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Reign #${currentReign.reignNumber}",
                    fontSize = 11.sp,
                    color = Gold
                )
                if (currentReign.defenses > 0) {
                    Text(
                        text = "${currentReign.defenses} def.",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                Spacer(modifier = Modifier.height(4.dp))
                Surface(
                    shape = MaterialTheme.shapes.extraSmall,
                    color = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.4f)
                ) {
                    Text(
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                        text = "VACANT",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            }
        }
    }
}

@Composable
fun RecentShowCard(episode: ShowEpisode, matchCount: Int) {
    val brandColor = when (episode.brand) {
        Brand.RAW -> Red
        Brand.SD  -> Blue
        else      -> MaterialTheme.colorScheme.primary
    }
    val brandLabel = when (episode.brand) {
        Brand.RAW -> "RAW"; Brand.SD -> "SmackDown"; else -> episode.brand.name
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.LiveTv,
                contentDescription = null,
                tint = brandColor,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "$brandLabel ${episode.episodeNumber}",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Week ${episode.weekNumber}",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (episode.notes.isNotBlank()) {
                    Text(
                        text = episode.notes,
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = matchCount.toString(),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = brandColor
                )
                Text(
                    text = if (matchCount == 1) "match" else "matches",
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun RecentPPVCard(ppv: PPVEvent, matchCount: Int) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Gold.copy(alpha = 0.08f)
        ),
        border = BorderStroke(1.dp, Gold.copy(alpha = 0.25f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Bolt,
                contentDescription = null,
                tint = Gold,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = ppv.name,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "PPV #${ppv.ppvNumber}",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (ppv.notes.isNotBlank()) {
                    Text(
                        text = ppv.notes,
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = matchCount.toString(),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Gold
                )
                Text(
                    text = if (matchCount == 1) "match" else "matches",
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun TopWrestlerRow(
    rank: Int,
    wrestler: Wrestler,
    points: Int,
    wins: Int,
    losses: Int,
    winPercent: Float
) {
    val brandColor = when (wrestler.brand) {
        Brand.RAW -> Red
        Brand.SD  -> Blue
        else      -> Grey
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (rank == 1)
                Gold.copy(alpha = 0.06f)
            else
                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
        ),
        border = if (rank == 1)
            BorderStroke(1.dp, Gold.copy(alpha = 0.3f))
        else null
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Rank badge
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(
                        when (rank) {
                            1    -> Gold.copy(alpha = 0.2f)
                            2, 3 -> MaterialTheme.colorScheme.surfaceVariant
                            else -> MaterialTheme.colorScheme.surface
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "#$rank",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = when (rank) {
                        1    -> Gold
                        else -> MaterialTheme.colorScheme.onSurfaceVariant
                    }
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            // Avatar initials
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(brandColor.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = wrestler.name.split(" ")
                        .take(2)
                        .joinToString("") { it.first().uppercase() },
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = brandColor
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = wrestler.name,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = wrestler.brand.name,
                    fontSize = 11.sp,
                    color = brandColor
                )
            }

            // Stats
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                MiniStat(label = "W-L", value = "$wins-$losses")
                MiniStat(label = "Win%", value = "%.0f%%".format(winPercent))
                MiniStat(label = "Pts", value = points.toString(), highlight = rank == 1)
            }
        }
    }
}

@Composable
fun MiniStat(label: String, value: String, highlight: Boolean = false) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = if (highlight) Gold else MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = label,
            fontSize = 10.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DashboardPreview() {
    DashboardScreen(
        wrestlers = WrestlerData.roster,
        matches = ShowData.matches,
        episodes = ShowData.episodes,
        ppvEvents = ShowData.ppvEvents,
        championships = ChampionshipData.titles,
        reigns = ChampionshipData.reigns,
        calendarWeeks = CalendarData.weeks,
        onShowClick = {}
    )
}