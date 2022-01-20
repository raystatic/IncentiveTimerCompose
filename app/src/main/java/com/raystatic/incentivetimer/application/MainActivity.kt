package com.raystatic.incentivetimer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.raystatic.incentivetimer.core.ui.screenSpecs.AddEditRewardScreenSpec
import com.raystatic.incentivetimer.core.ui.screenSpecs.RewardListScreenSpec
import com.raystatic.incentivetimer.core.ui.screenSpecs.ScreenSpec
import com.raystatic.incentivetimer.core.ui.screenSpecs.TimerScreenSpec
import com.raystatic.incentivetimer.core.ui.theme.IncentiveTimerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            IncentiveTimerTheme {
                ScreenContent()
            }
        }
    }
}

@Composable
private fun ScreenContent() {
    val navController = rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val hideBottomBar = navBackStackEntry?.arguments?.getBoolean(ARG_HIDE_BOTTOM_BAR)

    val screenSpec =  ScreenSpec.allScreens[currentDestination?.route]

    Scaffold(
        topBar ={
            val navBackStackEntry = navBackStackEntry
            if (navBackStackEntry!=null){
                screenSpec?.TopBar(
                    navController = navController,
                    navBackStackEntry = navBackStackEntry
                )
            }
        },
        bottomBar = {

            if (hideBottomBar == null || !hideBottomBar){
                BottomNavigation {
                    bottomNavDestinations.forEach { bottomNavDestination ->
                        BottomNavigationItem(
                            icon = {
                                Icon(
                                    bottomNavDestination.icon,
                                    contentDescription = null
                                )
                            },
                            label = {
                                Text(text = stringResource(id = bottomNavDestination.label))
                            },
                            selected = currentDestination?.hierarchy?.any { it.route == bottomNavDestination.screenSpec.navHostRoute } == true,
                            onClick = {
                                navController.navigate(bottomNavDestination.screenSpec.navHostRoute){
                                    popUpTo(navController.graph.findStartDestination().id){
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            alwaysShowLabel = false
                        )
                    }
                }
            }
        }
    ) {innerPadding->
        NavHost(
            navController= navController,
            startDestination = BottomNavDestination.TimerScreen.screenSpec.navHostRoute,
            modifier = Modifier.padding(innerPadding
        )){
            ScreenSpec.allScreens.values .forEach { screen->
                composable(
                    route = screen.navHostRoute,
                    arguments = screen.arguments,
                    deepLinks = screen.deepLinks
                ){ navBackStackEntry ->
                    screen.Content(
                        navController = navController,
                        navBackStackEntry = navBackStackEntry
                    )
                }
            }
        }
    }
}

val bottomNavDestinations = listOf(
    BottomNavDestination.TimerScreen,
    BottomNavDestination.RewardListScreen
)

sealed class BottomNavDestination(
    val screenSpec: ScreenSpec,
    val icon:ImageVector,
    @StringRes val label:Int
){
    object TimerScreen:BottomNavDestination(screenSpec = TimerScreenSpec,icon = Icons.Outlined.Timer,label =R.string.timer)
    object RewardListScreen:BottomNavDestination(screenSpec = RewardListScreenSpec,icon = Icons.Outlined.List,label =R.string.reward_list)

}

const val ARG_HIDE_BOTTOM_BAR = "ARG_HIDE_BOTTOM_BAR"

//@Preview(
//    name = "Dark mode",
//    uiMode = Configuration.UI_MODE_NIGHT_YES
//)
//@Composable
//fun DefaultPreview() {
//    Surface {
//        ScreenContent()
//    }
//}