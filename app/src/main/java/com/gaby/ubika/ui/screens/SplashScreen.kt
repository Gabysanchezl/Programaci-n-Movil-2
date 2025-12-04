package com.gaby.ubika.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.gaby.ubika.theme.CafeUbika
import com.gaby.ubika.viewmodels.SplashViewModel
import androidx.compose.runtime.livedata.observeAsState
import kotlin.math.hypot
@Composable
fun SplashScreen(navController: NavHostController, splashViewModel: SplashViewModel) {
    val transitionProgress = remember { Animatable(0f) }

    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val screenDiagonal = with(LocalDensity.current) {
        hypot(screenWidth.toPx(), screenHeight.toPx()) * 1.2f
    }
    val isLoggedIn by splashViewModel.isLoggedIn.observeAsState()

    LaunchedEffect(Unit) {
        splashViewModel.checkSession()
        transitionProgress.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 1000,
                easing = FastOutSlowInEasing
            )
        )
        if (isLoggedIn == true) {
            navController.navigate("admin_home") {
                popUpTo("splash") { inclusive = true }
            }
        } else {
            navController.navigate("main_menu") {
                popUpTo("splash") { inclusive = true }
            }
        }
    }
    Box(modifier = Modifier.fillMaxSize().background(Color.White)) {
//        MainMenuScreen(navController) // Lo revela gradualmente  TEMP CHANGE OJO OJO OJO OJO OJO ojo ojo ojo ojo
        Canvas(modifier = Modifier.fillMaxSize()) {
            val radius = screenDiagonal * (1f - transitionProgress.value)
            drawCircle(
                color = CafeUbika,
                radius = radius,
                center = Offset(size.width / 2, size.height / 2)
            )
        }
    }
}
