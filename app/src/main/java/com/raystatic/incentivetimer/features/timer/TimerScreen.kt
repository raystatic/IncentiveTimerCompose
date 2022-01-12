package com.raystatic.incentivetimer.features.timer

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.raystatic.incentivetimer.R
import com.raystatic.incentivetimer.core.ui.theme.IncentiveTimerTheme

@Composable
fun TimerScreen(
    navController: NavController
) {
    ScreenContent()
}

@Composable
private fun ScreenContent() {
    Scaffold(
        topBar = {
            TopAppBar(title ={
                Text(text = stringResource(id = R.string.timer))
            })
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(id = R.string.timer)
            )
        }
    }
}

@Composable
private fun ScreenContentPreview() {
   IncentiveTimerTheme {
       Surface {
           ScreenContent()
       }
   }
}