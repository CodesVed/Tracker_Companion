package com.example.trackercompanion.navigation

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.LiveTv
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Dashboard
import androidx.compose.material.icons.outlined.EmojiEvents
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.LiveTv
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.bottombar.AnimatedBottomBar
import com.example.bottombar.components.BottomBarItem
import com.example.bottombar.model.IndicatorDirection
import com.example.bottombar.model.IndicatorStyle
import com.example.trackercompanion.ui.dashboard.DashboardScreen
import com.example.trackercompanion.ui.roster.RosterScreen

data class BottomNavigationItem(val name: String, val icon: ImageVector, val unselectedIcon: ImageVector)

@Composable
fun BottomNavigationBar(navController: NavHostController){
    var selectedItem by remember { mutableIntStateOf(0) }
    val bottomNavItem = listOf(
        BottomNavigationItem("Championships", Icons.Default.EmojiEvents, Icons.Outlined.EmojiEvents),
        BottomNavigationItem("Roster", Icons.Default.Groups, Icons.Outlined.Groups),
        BottomNavigationItem("Dashboard", Icons.Default.Dashboard, Icons.Outlined.Dashboard),
        BottomNavigationItem("Shows", Icons.Default.LiveTv, Icons.Outlined.LiveTv),
        BottomNavigationItem("Calendar", Icons.Default.CalendarMonth, Icons.Outlined.CalendarMonth),
    )

    AnimatedBottomBar(
        selectedItem = selectedItem,
        itemSize = bottomNavItem.size,
        containerColor = Color.Transparent,
        indicatorColor = MaterialTheme.colorScheme.primary,
        indicatorDirection = IndicatorDirection.TOP,
        indicatorStyle = IndicatorStyle.LINE
    ) {
        bottomNavItem.forEachIndexed { index, item ->
            BottomBarItem(
                modifier = Modifier.align(alignment = Alignment.Top),
                selected = selectedItem == index,
                onClick = {
                    selectedItem = index
                    when(selectedItem){
                        0 -> navController.navigate(Routes.Championships)
                        1 -> navController.navigate(Routes.Roster)
                        2 -> navController.navigate(Routes.Dashboard)
                        3 -> navController.navigate(Routes.Shows)
                        4 -> navController.navigate(Routes.Calendar)
                    }
                },
                imageVector = item.icon,
                label = item.name,
                contentColor = Color.Transparent
            )
        }
    }
}

//@Preview(showSystemUi = true)
//@Composable
//fun BottomNavBarPreview(){
//    BottomNavigationBar()
//}