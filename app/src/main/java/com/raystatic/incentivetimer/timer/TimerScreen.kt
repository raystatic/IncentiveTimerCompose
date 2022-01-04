package com.raystatic.incentivetimer.timer

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.raystatic.incentivetimer.R
import com.raystatic.incentivetimer.ui.theme.IncentiveTimerTheme

@Composable
fun TimerScreen() {
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