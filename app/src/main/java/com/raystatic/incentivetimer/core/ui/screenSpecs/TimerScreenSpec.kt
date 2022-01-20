package com.raystatic.incentivetimer.core.ui.screenSpecs

import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.raystatic.incentivetimer.R
import com.raystatic.incentivetimer.features.timer.TimerScreen
import com.raystatic.incentivetimer.features.timer.TimerScreenTopAppBar

object TimerScreenSpec: ScreenSpec {

    override val navHostRoute: String
        get() = "timer"

    @Composable
    override fun TopBar(navController: NavController, navBackStackEntry: NavBackStackEntry) {
       TimerScreenTopAppBar()
    }

    @Composable
    override fun Content(navController: NavController, navBackStackEntry: NavBackStackEntry) {
        TimerScreen(navController = navController)
    }
}