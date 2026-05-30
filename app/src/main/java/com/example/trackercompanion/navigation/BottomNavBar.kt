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
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.TheaterComedy
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Dashboard
import androidx.compose.material.icons.outlined.EmojiEvents
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.TheaterComedy
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
import androidx.compose.ui.tooling.preview.Preview
import com.example.bottombar.AnimatedBottomBar
import com.example.bottombar.components.BottomBarItem
import com.example.bottombar.model.IndicatorDirection
import com.example.bottombar.model.IndicatorStyle

data class BottomNavigationItem(val name: String, val icon: ImageVector, val unselectedIcon: ImageVector)

@Composable
fun BottomNavigationBar(){
    var selectedItem by remember { mutableIntStateOf(0) }
    val bottomNavItem = listOf(
        BottomNavigationItem("Dashboard", Icons.Default.Dashboard, Icons.Outlined.Dashboard),
        BottomNavigationItem("Roster", Icons.Default.Groups, Icons.Outlined.Groups),
        BottomNavigationItem("Shows", Icons.Default.TheaterComedy, Icons.Outlined.TheaterComedy),
        BottomNavigationItem("Championships", Icons.Default.EmojiEvents, Icons.Outlined.EmojiEvents),
        BottomNavigationItem("Calendar", Icons.Default.CalendarMonth, Icons.Outlined.CalendarMonth),
    )

    Scaffold(
        bottomBar = {
            Surface(
                modifier = Modifier.fillMaxWidth().padding(
                    bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
                )
            ) {
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
                                when(index){

                                }
                            },
                            imageVector = item.icon,
                            label = item.name,
                            contentColor = Color.Transparent
                        )
                    }
                }
            }
        }
    ) {innerPadding ->

    }
}

//@Preview(showSystemUi = true)
//@Composable
//fun BottomNavBarPreview(){
//    BottomNavigationBar()
//}