package com.raystatic.incentivetimer.rewardlist

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.raystatic.incentivetimer.FullScreenDestinations
import com.raystatic.incentivetimer.R
import com.raystatic.incentivetimer.data.Reward
import com.raystatic.incentivetimer.ui.IconKeys
import com.raystatic.incentivetimer.ui.ListBottomPadding
import com.raystatic.incentivetimer.ui.defaultIcon
import com.raystatic.incentivetimer.ui.rewardIcons
import kotlinx.coroutines.launch

@Composable
fun RewardListScreen(
    navController: NavController,
    viewModel: RewardListViewModel = hiltViewModel()
) {
    val dummyRecords by viewModel.rewards.observeAsState(listOf())
    ScreenContent(
        rewards = dummyRecords,
        onAddRewardClicked = {
            navController.navigate(FullScreenDestinations.AddEditRewardScreen.route)
        }
    )
}

@Composable
private fun ScreenContent(
    rewards:List<Reward>,
    onAddRewardClicked: () -> Unit
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
        }
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
                    RewardItem(reward = reward)
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
    modifier: Modifier = Modifier
) {
    Card(
        onClick ={

        },
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = rewardIcons[reward.icon] ?: defaultIcon,
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
                Reward(icon = IconKeys.CAKE, title = "Reward 1",5),
                Reward(icon = IconKeys.BATH_TUB, title = "Reward 2",5),
                Reward(icon = IconKeys.TV, title = "Reward 3",5)
            ),
            onAddRewardClicked = {}
        )
    }
}