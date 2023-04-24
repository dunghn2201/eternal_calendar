package com.dunghn2201.eternalcalendar.util.extension

import android.app.Activity
import android.os.SystemClock
import android.view.Window
import android.view.WindowManager
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun HideStatusBar(window: Window) {
    val systemUiController = rememberSystemUiController()
    SideEffect {
        with(window) {
            addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        }
        systemUiController.setStatusBarColor(Color.Transparent, darkIcons = true)
    }
}

inline fun Modifier.clickableWithoutRipple(
    enabled: Boolean = true,
    onClickLabel: String? = null,
    role: Role? = null,
    crossinline onClick: () -> Unit,
): Modifier = composed {
    clickable(
        enabled = enabled,
        indication = null,
        onClickLabel = onClickLabel,
        role = role,
        interactionSource = remember { MutableInteractionSource() },
    ) {
        onClick()
    }
}

@Composable
fun ScaleAnimatedClickable(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    content: @Composable () -> Unit,
) {
    val scale = remember { Animatable(1f) }
    val coroutineScope: CoroutineScope = rememberCoroutineScope()
    val clickableModifier = modifier
        .scale(scale.value)
        .clickableWithoutRipple {
            coroutineScope.launch {
                scale.animateTo(1.2f, animationSpec = spring())
                scale.animateTo(1f, animationSpec = spring())
            }
            onClick()
        }
    Box(modifier = clickableModifier) {
        content()
    }
}

@Composable
fun SafeScaleAnimatedClickable(
    modifier: Modifier = Modifier,
    onClickBeforeInterval: () -> Unit = {},
    onClick: suspend () -> Unit,
    defaultInterval: Long = 150L,
    content: @Composable () -> Unit
) {
    val scale = remember { Animatable(1f) }
    val coroutineScope: CoroutineScope = rememberCoroutineScope()
    var lastTimeClicked by rememberSaveable { mutableStateOf(0L) }
    val onClickWithInterval = {
        coroutineScope.launch {
            scale.animateTo(1.2f, animationSpec = spring())
            scale.animateTo(1f, animationSpec = spring())
            onClickBeforeInterval()
            val currentTime = SystemClock.elapsedRealtime()
            if (currentTime - lastTimeClicked > defaultInterval) {
                lastTimeClicked = currentTime
                onClick()
            }
        }
    }
    val clickableModifier = modifier
        .scale(scale.value)
        .clickableWithoutRipple { onClickWithInterval() }
    Box(modifier = clickableModifier) { content() }
}

@Composable
fun SafeClickable(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    defaultInterval: Long = 500L,
    content: @Composable () -> Unit
) {
    var lastTimeClicked by rememberSaveable { mutableStateOf(0L) }
    val onClickWithInterval = {
        val currentTime = SystemClock.elapsedRealtime()
        if (currentTime - lastTimeClicked > defaultInterval) {
            lastTimeClicked = currentTime
            onClick()
        }
    }
    Box(modifier = modifier.clickable(onClick = onClickWithInterval)) { content() }
}
