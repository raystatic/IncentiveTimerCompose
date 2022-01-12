package com.raystatic.incentivetimer.features.addeditreward

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.flowlayout.MainAxisAlignment
import com.raystatic.incentivetimer.R
import com.raystatic.incentivetimer.core.ui.IconKey
import com.raystatic.incentivetimer.core.ui.composables.ITIconButton
import com.raystatic.incentivetimer.core.ui.defaultRrewardIcon
import com.raystatic.incentivetimer.core.utils.exhaustive
import kotlinx.coroutines.flow.collect

interface AddEditRewardActions{
    fun onRewardNameInputChanged(input:String)
    fun onChanceInPercentInputChanged(input:Int)
    fun onRewardIconButtonClicked()
    fun onRewardIconSelected(iconKey: IconKey)
    fun onRewardIconDialogDismissRequest()
    fun onSaveClicked()
}

@Composable
fun AddEditRewardScreen(
    navController: NavController
) {
    val viewModel: AddEditRewardViewModel = hiltViewModel()
    val rewardNameInput by viewModel.rewardNameInput.observeAsState()
    val isEditMode = viewModel.isEditMode
    val chanceInPercentInput by viewModel.chanceInPercentInput.observeAsState(10)
    val showRewardIconSelectionDialog by viewModel.showRewardIconSelectionDialog.observeAsState(false)
    val rewardIconKey by viewModel.rewardIconSelection.observeAsState(defaultRrewardIcon)
    val rewardNameInputIsError by viewModel.rewardNameInputIsError.observeAsState(false)

    LaunchedEffect(Unit){
       viewModel.events.collect {event->

           when(event){
               AddEditRewardViewModel.AddEditRewardEvent.RewardCreated -> {
                   navController.popBackStack()
               }
               AddEditRewardViewModel.AddEditRewardEvent.RewardUpdated -> {
                   navController.popBackStack()  
               }
           }.exhaustive

       }
    }

    ScreenContent(
        isEditMode = isEditMode,
        rewardNameInput = rewardNameInput,
        chanceInPercentInput = chanceInPercentInput,
        onCloseClicked = { navController.popBackStack() },
        showRewardIconSelectionDialog = showRewardIconSelectionDialog,
        rewardIconKey = rewardIconKey,
        actions = viewModel,
        rewardNameInputIsError = rewardNameInputIsError
    )

}


@Composable
private fun ScreenContent(
    isEditMode:Boolean,
    rewardNameInput:String?,
    rewardNameInputIsError:Boolean,
    chanceInPercentInput:Int,
    rewardIconKey: IconKey,
    showRewardIconSelectionDialog:Boolean,
    onCloseClicked:() -> Unit,
    actions:AddEditRewardActions
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
                onClick = actions::onSaveClicked,
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(Icons.Default.Check, contentDescription = stringResource(id = R.string.save_reward))
            }
        }
    ) {
        Column(Modifier.padding(16.dp)) {
            TextField(
                value = rewardNameInput ?: "",
                onValueChange = actions::onRewardNameInputChanged,
                modifier = Modifier.fillMaxWidth(),
                label = { Text(stringResource(id = R.string.reward_name)) },
                singleLine = true,
                isError = rewardNameInputIsError
            )
            
            if (rewardNameInputIsError){
                Text(
                    text = stringResource(id = R.string.field_cant_be_blank),
                    style = MaterialTheme.typography.body2,
                    color = MaterialTheme.colors.error
                )
            }

            Spacer(modifier = Modifier.padding(16.dp))
            Text(text = "${stringResource(id = R.string.chance)}: $chanceInPercentInput%")
            Slider(
                value = chanceInPercentInput.toFloat() / 100,
                onValueChange = {
                    actions.onChanceInPercentInputChanged((it * 100).toInt())
                }
            )

            Spacer(modifier = Modifier.padding(16.dp))

            ITIconButton(
                onClick = actions::onRewardIconButtonClicked,
                modifier = Modifier.size(64.dp)
            ) {
                Icon(
                    imageVector = rewardIconKey.rewardIcon,
                    contentDescription = stringResource(id = R.string.select_icon),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                )
            }

        }
    }

    if (showRewardIconSelectionDialog){
        RewardIconSelectionDialog(
            onDismissRequest = actions::onRewardIconDialogDismissRequest,
            onIconKeySelected = actions::onRewardIconSelected
        )
    }
}

@Composable
private fun RewardIconSelectionDialog(
    onDismissRequest:() -> Unit,
    onIconKeySelected:(iconKey:IconKey) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        text = {
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                mainAxisAlignment = MainAxisAlignment.Center
            ) {
                IconKey.values().forEach { iconKey ->
                    IconButton(onClick = {
                        onIconKeySelected(iconKey)
                        onDismissRequest()
                    }) {
                        Icon(
                            imageVector = iconKey.rewardIcon,
                            contentDescription = null,
                            modifier = Modifier
                                .size(48.dp)
                                .padding(8.dp)
                        )
                    }
                }
            }
        },
        buttons = {
            TextButton(
                onClick = onDismissRequest,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, end = 8.dp, bottom = 8.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.cancel),
                )
            }
        }
    )
}

@Preview
@Composable
private fun ScreenContentPreview() {
    Surface {
        ScreenContent(
            isEditMode = false,
            rewardNameInput = "",
            chanceInPercentInput = 10,
            onCloseClicked = {},
            showRewardIconSelectionDialog = false,
            rewardIconKey = defaultRrewardIcon,
            actions = object : AddEditRewardActions {
                override fun onRewardNameInputChanged(input: String) {}

                override fun onChanceInPercentInputChanged(input: Int) {}

                override fun onRewardIconButtonClicked() {}

                override fun onRewardIconSelected(iconKey: IconKey) {}

                override fun onRewardIconDialogDismissRequest() {}

                override fun onSaveClicked() {}
            },
            rewardNameInputIsError = false
        )
    }
}