package com.sample.snoozeloo.ui.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.sample.snoozeloo.ui.theme.DIMEN_10_dp



@Composable
fun CommonVerticalSpacer(height: Dp = DIMEN_10_dp)
{
    Spacer(Modifier.height(height))
}

@Composable
fun CommonHorizontalSpacer(width: Dp = DIMEN_10_dp)
{
    Spacer(Modifier.width(width))
}