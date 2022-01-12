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
import androidx.navigation.navArgument
import com.raystatic.incentivetimer.features.addeditreward.AddEditRewardScreen
import com.raystatic.incentivetimer.features.rewardlist.RewardListScreen
import com.raystatic.incentivetimer.features.timer.TimerScreen
import com.raystatic.incentivetimer.core.ui.theme.IncentiveTimerTheme
import com.raystatic.incentivetimer.features.addeditreward.ARG_REWARD_ID
import com.raystatic.incentivetimer.features.addeditreward.NO_REWARD_ID
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
    Scaffold(
        bottomBar = {
            BottomNavigation {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
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
                        selected = currentDestination?.hierarchy?.any { it.route == bottomNavDestination.route } == true,
                        onClick = {
                            navController.navigate(bottomNavDestination.route){
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
    ) {innerPadding->
        NavHost(navController, startDestination = BottomNavDestination.TimerScreen.route, Modifier.padding(innerPadding)){
            composable(route = BottomNavDestination.TimerScreen.route){
                TimerScreen(navController)
            }
            composable(route = BottomNavDestination.RewardListScreen.route){
                RewardListScreen(
                    navController = navController
                )
            }
            composable(
                route = FullScreenDestinations.AddEditRewardScreen.route + "?$ARG_REWARD_ID={$ARG_REWARD_ID}",
                arguments = listOf(navArgument(ARG_REWARD_ID){
                    defaultValue = NO_REWARD_ID
                })
            ){
                AddEditRewardScreen(navController)
            }
        }
    }
}

val bottomNavDestinations = listOf(
    BottomNavDestination.TimerScreen,
    BottomNavDestination.RewardListScreen
)

sealed class BottomNavDestination(
    val route:String,
    val icon:ImageVector,
    @StringRes val label:Int
){
    object TimerScreen:BottomNavDestination(route = "timer",icon = Icons.Outlined.Timer,label =R.string.timer)
    object RewardListScreen:BottomNavDestination(route = "reward_list",icon = Icons.Outlined.List,label =R.string.reward_list)

}

sealed class FullScreenDestinations(
    val route:String
){

    object AddEditRewardScreen: FullScreenDestinations(route = "add_edit_reward")

}

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