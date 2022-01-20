package com.raystatic.incentivetimer.core.ui.screenSpecs

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.navArgument
import com.raystatic.incentivetimer.ARG_HIDE_BOTTOM_BAR
import com.raystatic.incentivetimer.R
import com.raystatic.incentivetimer.features.addeditreward.*
import com.raystatic.incentivetimer.features.rewardlist.RewardListScreen
import com.raystatic.incentivetimer.features.timer.TimerScreen

object AddEditRewardScreenSpec: ScreenSpec {

    override val navHostRoute: String
        get() = "add_edit_reward?$ARG_REWARD_ID={$ARG_REWARD_ID}"

    override val arguments: List<NamedNavArgument>
        get() = listOf(
            navArgument(ARG_REWARD_ID){
                defaultValue = NO_REWARD_ID
            },
            navArgument(ARG_HIDE_BOTTOM_BAR){
                defaultValue = true
            }
        )

//    override fun getScreenTitle(navBackStackEntry: NavBackStackEntry?): Int {
//        val rewardId = navBackStackEntry?.arguments?.getLong(ARG_REWARD_ID)
//        return if (isEditMode(rewardId)) R.string.edit_reward else R.string.add_reward
//    }


    @Composable
    override fun TopBar(navController: NavController, navBackStackEntry: NavBackStackEntry) {
        val viewModel:AddEditRewardViewModel = hiltViewModel(navBackStackEntry)
        val rewardId = navBackStackEntry.arguments?.getLong(ARG_REWARD_ID)
        AddEditRewardTopAppBar(
            isEditMode = isEditMode(rewardId),
            onCloseClicked = {
                navController.popBackStack()
            },
            actions = viewModel
        )
    }

    fun isEditMode(rewardId: Long?) = rewardId != NO_REWARD_ID

    fun buildRoute(rewardId:Long = NO_REWARD_ID) = "add_edit_reward?$ARG_REWARD_ID=$rewardId"

    fun getRewardIdFromSavedStateHandle(savedStateHandle: SavedStateHandle) =
        savedStateHandle.get<Long>(ARG_REWARD_ID)

    @Composable
    override fun Content(navController: NavController, navBackStackEntry: NavBackStackEntry) {
        AddEditRewardScreen(navController = navController)
    }
}