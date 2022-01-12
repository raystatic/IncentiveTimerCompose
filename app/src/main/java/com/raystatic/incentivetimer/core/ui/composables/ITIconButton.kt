package com.raystatic.incentivetimer.core.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.raystatic.incentivetimer.R
import com.raystatic.incentivetimer.core.ui.IconButtonRoundedCornerSize

@Composable
fun ITIconButton(
    onClick:() -> Unit,
    modifier:Modifier = Modifier,
    enabled:Boolean = true,
    content: @Composable () -> Unit
) {

    val iconButtonBackground = if (isSystemInDarkTheme()) Color.Gray else Color.LightGray

    IconButton(
        onClick = onClick,
        modifier = modifier
            .background(iconButtonBackground)
            .clip(RoundedCornerShape(IconButtonRoundedCornerSize))
    ) {
        content()
    }
}