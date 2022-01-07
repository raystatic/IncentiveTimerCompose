package com.raystatic.incentivetimer.addeditreward

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.raystatic.incentivetimer.R
import kotlinx.coroutines.flow.collect

@Composable
fun AddEditRewardScreen(
    navController: NavController
) {
    val viewModel: AddEditRewardViewModel = hiltViewModel()
    val rewardNameInput by viewModel.rewardNameInput.observeAsState()
    val isEditMode = viewModel.isEditMode
    val chanceInPercentInput by viewModel.chanceInPercentInput.observeAsState(10)

    LaunchedEffect(Unit){
       viewModel.events.collect {event->

           when(event){
               AddEditRewardViewModel.AddEditRewardEvent.RewardCreated -> {
                   navController.popBackStack()
               }
           }

       }
    }

    ScreenContent(
        isEditMode = isEditMode,
        rewardNameInput = rewardNameInput,
        onRewardNameInputChange = viewModel::onRewardNameInputChanged,
        chanceInPercentInput = chanceInPercentInput,
        onChangeInPercentInputChange = viewModel::onChanceInPercentInputChanged,
        onSaveClicked = viewModel::onSaveClicked,
        onCloseClicked = { navController.popBackStack() },
    )

}


@Composable
private fun ScreenContent(
    isEditMode:Boolean,
    rewardNameInput:String?,
    onRewardNameInputChange:(String) -> Unit,
    chanceInPercentInput:Int,
    onChangeInPercentInputChange:(Int) -> Unit,
    onSaveClicked:() -> Unit,
    onCloseClicked:() -> Unit
) {
    Scaffold(
        topBar = {
            val appBarTitle = if (isEditMode) stringResource(id = R.string.edit_reward) else stringResource(id = R.string.add_reward)
            TopAppBar(
                title ={
                    Text(text = appBarTitle)
                },
                navigationIcon = {
                    IconButton(onClick = onCloseClicked) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = stringResource(id = R.string.close)
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onSaveClicked,
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(Icons.Default.Check, contentDescription = stringResource(id = R.string.save_reward))
            }
        }
    ) {
        Column(Modifier.padding(16.dp)) {
            TextField(
                value = rewardNameInput ?: "",
                onValueChange = onRewardNameInputChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = stringResource(id = R.string.reward_name)) },
                singleLine = true
            )

            Spacer(modifier = Modifier.padding(16.dp))
            Text(text = "${stringResource(id = R.string.chance)}: $chanceInPercentInput%")
            Slider(
                value = chanceInPercentInput.toFloat() / 100,
                onValueChange = {
                    onChangeInPercentInputChange((it * 100).toInt())
                }
            )
//            Text(
//                text = "$chanceInPercentInput%",
//                modifier = Modifier.fillMaxWidth(),
//                textAlign = TextAlign.Center
//            )
        }
    }
}

@Preview
@Composable
private fun ScreenContentPreview() {
    Surface {
        ScreenContent(
            isEditMode = false,
            rewardNameInput = "",
            onRewardNameInputChange = {},
            chanceInPercentInput = 10,
            onChangeInPercentInputChange = {},
            onSaveClicked = {},
            onCloseClicked = {}
        )
    }
}