package com.sample.snoozeloo.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.sample.snoozeloo.ui.theme.DIMEN_10_dp



@Composable
fun CommonSpacer(height: Dp = DIMEN_10_dp)
{
    Spacer(Modifier.height(height))
}