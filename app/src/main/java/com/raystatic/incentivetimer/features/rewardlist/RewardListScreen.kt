package com.raystatic.incentivetimer.features.rewardlist

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.raystatic.incentivetimer.FullScreenDestinations
import com.raystatic.incentivetimer.R
import com.raystatic.incentivetimer.data.Reward
import com.raystatic.incentivetimer.core.ui.IconKey
import com.raystatic.incentivetimer.core.ui.ListBottomPadding
import com.raystatic.incentivetimer.features.addeditreward.*
import kotlinx.coroutines.launch

@Composable
fun RewardListScreen(
    navController: NavController,
    viewModel: RewardListViewModel = hiltViewModel()
) {
    val rewards by viewModel.rewards.observeAsState(listOf())

    val addEditRewardResult = navController.currentBackStackEntry
        ?.savedStateHandle?.getLiveData<String>(ADD_EDIT_REWARD_RESULT)
        ?.observeAsState()


    val scaffoldState = rememberScaffoldState()

    val context = LocalContext.current

    LaunchedEffect(key1 = addEditRewardResult){
        navController.currentBackStackEntry?.savedStateHandle?.remove<String>(
            ADD_EDIT_REWARD_RESULT
        )
        addEditRewardResult?.value?.let {
            when(it){
                RESULT_REWARD_ADDED -> {
                    scaffoldState.snackbarHostState.showSnackbar(context.getString(R.string.reward_added))
                }

                RESULT_REWARD_UPDATED -> {
                    scaffoldState.snackbarHostState.showSnackbar(context.getString(R.string.reward_updated))
                }

                RESULT_REWARD_DELETED -> {
                    scaffoldState.snackbarHostState.showSnackbar(context.getString(R.string.reward_deleted))
                }
            }

        }
    }

    ScreenContent(
        rewards = rewards,
        onAddRewardClicked = {
            navController.navigate(FullScreenDestinations.AddEditRewardScreen.route)
        },
        onRewardItemClicked = {id->
            navController.navigate(FullScreenDestinations.AddEditRewardScreen.route+"?$ARG_REWARD_ID=${id}")
        },
        scaffoldState = scaffoldState
    )
}

@Composable
private fun ScreenContent(
    rewards:List<Reward>,
    onAddRewardClicked: () -> Unit,
    onRewardItemClicked: (Long) -> Unit,
    scaffoldState: ScaffoldState = rememberScaffoldState()
) {

    Scaffold(
        topBar = {
            TopAppBar(title ={
                Text(text = stringResource(id = R.string.reward_list))
            })
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddRewardClicked,
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = stringResource(id = R.string.add_new_reward))
            }
        },
        scaffoldState = scaffoldState
    ) {

        val listState = rememberLazyListState()
        val coroutineScope = rememberCoroutineScope()

        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            LazyColumn(
                contentPadding = PaddingValues(
                    top = 8.dp,
                    start = 8.dp,
                    end = 8.dp,
                    bottom = ListBottomPadding
                ),
                state = listState,
                modifier = Modifier.fillMaxSize()
            ) {
                items(rewards) { reward ->
                    RewardItem(
                        reward = reward,
                        onRewardItemClicked = onRewardItemClicked
                    )
                }
            }
            AnimatedVisibility (
                visible = listState.firstVisibleItemIndex > 5,
                enter = fadeIn(),
                exit = fadeOut(),
                modifier = Modifier
                    .align(Alignment.BottomCenter)

            ){
                FloatingActionButton(
                    onClick = {
                        coroutineScope.launch {
                            listState.animateScrollToItem(0)
                        }
                    },
                    backgroundColor = Color.LightGray,
                    contentColor = Color.Black,
                    modifier = Modifier.padding(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ExpandLess,
                        contentDescription = stringResource(id = R.string.scroll_to_top),
                    )
                }
            }
        }
    }
}

@Composable
private fun RewardItem(
    reward: Reward,
    onRewardItemClicked:(Long) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick ={
            onRewardItemClicked(reward.id)
        },
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = reward.icon.rewardIcon,
                contentDescription = null, modifier = Modifier
                    .padding(8.dp)
                    .size(64.dp)
                    .fillMaxWidth())
            Column {
                Text(
                    text = reward.title,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = "${stringResource(id = R.string.chance)}:${reward.chanceInPercent}%",
                    style = MaterialTheme.typography.body2,
                    modifier = Modifier.fillMaxWidth(),
                    color = Color.Gray
                )
            }

        }
    }
}

@Preview
@Composable
private fun ScreenContentPreview() {
    Surface {
        ScreenContent(
            listOf(
                Reward(icon = IconKey.CAKE, title = "Reward 1",5),
                Reward(icon = IconKey.BATH_TUB, title = "Reward 2",5),
                Reward(icon = IconKey.TV, title = "Reward 3",5)
            ),
            onAddRewardClicked = {},
            onRewardItemClicked = {},
        )
    }
}