package com.raystatic.incentivetimer.core.ui.screenSpecs

import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.raystatic.incentivetimer.R
import com.raystatic.incentivetimer.features.rewardlist.RewardListScreen
import com.raystatic.incentivetimer.features.rewardlist.RewardListTopAppBar
import com.raystatic.incentivetimer.features.timer.TimerScreen

object RewardListScreenSpec: ScreenSpec {

    override val navHostRoute: String
        get() = "reward_list"

    @Composable
    override fun TopBar(navController: NavController, navBackStackEntry: NavBackStackEntry) {
        RewardListTopAppBar()
    }

    @Composable
    override fun Content(navController: NavController, navBackStackEntry: NavBackStackEntry) {
        RewardListScreen(navController = navController)
    }
}