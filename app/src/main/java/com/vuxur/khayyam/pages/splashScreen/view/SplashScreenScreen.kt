package com.vuxur.khayyam.pages.splashScreen.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.vuxur.khayyam.R

@Composable
fun SplashScreenScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Image(
            modifier = Modifier.size(290.dp),
            painter = painterResource(id = R.drawable.ic_splash),
            contentDescription = "",
        )
    }
}