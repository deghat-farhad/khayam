package com.vuxur.khayyam.pages.splashScreen.view

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.vuxur.khayyam.R
import com.vuxur.khayyam.pages.splashScreen.SplashScreenViewModel

@Composable
fun SplashScreenRoute(
    viewModel: SplashScreenViewModel,
    navigateToSetting: () -> Unit,
    navigateToPoemList: () -> Unit,
    popBack: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()
    var circleAnimationTrigger by remember { mutableStateOf(false) }
    var animTrigger by remember { mutableStateOf(false) }
    val size: Dp by animateDpAsState(
        animationSpec = tween(
            durationMillis = 200,
            easing = FastOutSlowInEasing
        ),
        targetValue = if (circleAnimationTrigger) 0.dp else 192.dp, label = ""
    ) {
        animTrigger = true
    }
    val alpha: Float by animateFloatAsState(
        animationSpec = tween(
            durationMillis = 1500,
        ),
        targetValue = if (animTrigger) 0f else 1f, label = ""
    )

    LaunchedEffect(key1 = true) {
        viewModel.viewIsReady()
    }

    DisposableEffect(uiState) {
        uiState.let { uiStateSnapshot ->
            (uiStateSnapshot as? SplashScreenViewModel.UiState.Initialized)?.let {
                uiStateSnapshot.events.forEach { event ->
                    when (event) {
                        SplashScreenViewModel.Event.NavigateToLanguageSetting -> navigateToSetting()
                        SplashScreenViewModel.Event.NavigateToPoemList -> navigateToPoemList()
                    }
                    viewModel.onEventConsumed(event)
                }
            }
        }
        onDispose { }
    }

    LaunchedEffect(key1 = true) {
        circleAnimationTrigger = true
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary)
                .size(192.dp)
        )
        Image(
            modifier = Modifier.size(256.dp),
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = "",
        )
    }
}