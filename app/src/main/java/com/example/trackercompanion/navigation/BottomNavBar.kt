package com.example.trackercompanion.navigation

import android.util.Log
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.bottombar.AnimatedBottomBar
import com.example.bottombar.components.BottomBarItem
import com.example.bottombar.model.IndicatorDirection
import com.example.bottombar.model.IndicatorStyle
import com.example.trackercompanion.navigation.Routes.*

data class BottomNavigationItem(val name: String, val icon: ImageVector, val unselectedIcon: ImageVector, val route: String?, val routeObject: Any)

@Composable
fun BottomNavigationBar(navController: NavHostController){
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    Log.d("NAV_DEBUG", "currentRoute = $currentRoute")

    val activeRoute = when {
        currentRoute == WrestlerDetail::class.qualifiedName -> Roster::class.qualifiedName
        currentRoute == AddEditWrestler::class.qualifiedName -> Roster::class.qualifiedName
        else -> currentRoute
    }

    val bottomNavItems = listOf(
        BottomNavigationItem("Championships", Icons.Default.EmojiEvents, Icons.Outlined.EmojiEvents, Championships::class.qualifiedName, Championships),
        BottomNavigationItem("Roster", Icons.Default.Groups, Icons.Outlined.Groups, Roster::class.qualifiedName, Roster),
        BottomNavigationItem("Dashboard", Icons.Default.Dashboard, Icons.Outlined.Dashboard, Dashboard::class.qualifiedName, Dashboard),
        BottomNavigationItem("Shows", Icons.Default.LiveTv, Icons.Outlined.LiveTv, Shows::class.qualifiedName, Shows),
        BottomNavigationItem("Calendar", Icons.Default.CalendarMonth, Icons.Outlined.CalendarMonth, Calendar::class.qualifiedName, Calendar),
    )

    val selectedItem = bottomNavItems.indexOfFirst { it.route == activeRoute}
        .takeIf { it >= 0 } ?: 2

    AnimatedBottomBar(
        selectedItem = selectedItem,
        itemSize = bottomNavItems.size,
        containerColor = Color.Transparent,
        indicatorColor = MaterialTheme.colorScheme.primary,
        indicatorDirection = IndicatorDirection.TOP,
        indicatorStyle = IndicatorStyle.LINE
    ) {
        bottomNavItems.forEachIndexed { index, item ->
            BottomBarItem(
                modifier = Modifier.align(alignment = Alignment.Top),
                selected = selectedItem == index,
                onClick = {
                    navController.navigate(item.routeObject) {
                        popUpTo<Dashboard> {inclusive = false}
                        launchSingleTop = true
                    }
                },
                imageVector = item.icon,
                label = item.name,
                contentColor = Color.Transparent
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BottomNavBarPreview(){
    BottomNavigationBar(navController = rememberNavController())
}