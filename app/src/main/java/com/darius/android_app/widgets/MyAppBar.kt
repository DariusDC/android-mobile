package com.darius.android_app.widgets

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.darius.android_app.R

@Composable
fun MyAppBar(isConnected: Boolean, title: String, moreActions: (@Composable RowScope.() -> Unit) = {}) {
    TopAppBar(title = { Text(text = title) },
        actions = {
            Icon(
                Icons.Filled.Circle,
                contentDescription = "circle",
                tint = if (isConnected) Color.Green else Color.Gray,
            )
            moreActions(this)
        })
}