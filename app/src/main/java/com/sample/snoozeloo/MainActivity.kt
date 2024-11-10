package com.sample.snoozeloo

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sample.snoozeloo.ui.theme.SnoozelooTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity()
{
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        showSplashScreen()

        setContent {

            SnoozelooTheme {

                Surface(color = Color.Blue, modifier = Modifier.fillMaxSize()) {

                    Navigation()
                }
            }
        }
    }

    private fun showSplashScreen()
    {
        val splashScreen = installSplashScreen()

        var keepSplashScreen = true

        // Set the condition to keep the splash screen on screen
        splashScreen.setKeepOnScreenCondition { keepSplashScreen }

        lifecycleScope.launch {

            delay(2000) // Keeps splash screen for 2 seconds
            keepSplashScreen = false
        }
    }
}




@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Navigation() {

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "main_screen") {

        composable("main_screen") {

            AlarmListScreen(navController)
        }
    }
}

