package com.dunghn2201.eternalcalendar.util.extension

import android.os.SystemClock
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.scale
import androidx.compose.ui.semantics.Role
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun HideStatusBar() {
    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.isStatusBarVisible = false
        systemUiController.isNavigationBarVisible = false
        systemUiController.isSystemBarsVisible = false
        systemUiController.navigationBarDarkContentEnabled = false
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
