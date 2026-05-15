package com.example.manekelsa

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.manekelsa.ui.theme.ManekelsaTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            ManekelsaTheme {

                Surface(
                    color = MaterialTheme.colorScheme.background
                ) {

                    val navController = rememberNavController()

                    NavHost(
                        navController = navController,
                        startDestination = "home"
                    ) {

                        composable("home") {
                            HomeScreen(navController)
                        }

                        composable("workerList") {
                            WorkerListScreen()
                        }

                        composable("addWorker") {
                            AddWorkerScreen(navController)
                        }
                    }
                }
            }
        }
    }
}