package com.example.trackercompanion

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.trackercompanion.navigation.App
import com.example.trackercompanion.ui.theme.Gold
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun AppEntry() {
    var showSplash by remember { mutableStateOf(true) }

    if (showSplash) {
        BrandedSplashScreen(
            onFinished = { showSplash = false }
        )
    } else {
        App()
    }
}

@Composable
fun BrandedSplashScreen(onFinished: () -> Unit) {
    val contentAlpha = remember { Animatable(0f) }
    val screenAlpha  = remember { Animatable(1f) }

    LaunchedEffect(Unit) {
        //Fade content in
        contentAlpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing)
        )

        // Hold for reading
        delay(1000.milliseconds)

        // Fade whole screen out
        screenAlpha.animateTo(
            targetValue = 0f,
            animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing)
        )

        // Hand off to main app
        onFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .alpha(screenAlpha.value)
            .background(Color(0xFF1A1A2E)),   // Dark navy — matches splash theme
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.alpha(contentAlpha.value),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.EmojiEvents,
                contentDescription = null,
                tint = Gold,
                modifier = Modifier.size(72.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            // App name
            Text(
                text = "GM Mode",
                fontSize = 38.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                letterSpacing = 2.sp
            )

            Text(
                text = "COMPANION",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Gold,
                letterSpacing = 6.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Tagline
            Text(
                text = "Your universe.\nYour booking decisions.",
                fontSize = 13.sp,
                color = Color.White.copy(alpha = 0.55f),
                textAlign = TextAlign.Center,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Small subtitle
            Text(
                text = "WWE SmackDown: Here Comes The Pain",
                fontSize = 11.sp,
                color = Color.White.copy(alpha = 0.35f),
                letterSpacing = 0.5.sp
            )
        }
    }
}