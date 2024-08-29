package com.example.lokalapp.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.lokalapp.LokalApp
import com.example.lokalapp.screens.jobdetails.JobDetailScreen
import com.example.lokalapp.screens.splash.SplashScreen

@Composable
fun LokalNavigation(modifier: Modifier = Modifier) {

    val navController = rememberNavController()

    NavHost(
        modifier = Modifier.fillMaxSize(),
        navController = navController,
        startDestination = LokalScreens.SplashScreen.name,
    ) {


        composable(route = LokalScreens.SplashScreen.name) {
            SplashScreen(navController)
        }

        composable(
            route = LokalScreens.JobScreen.name,
            enterTransition = { fadeIn(tween(500)) },
        ) {
            LokalApp(navController)
        }

        composable(
            route = LokalScreens.JobDetailScreen.name + "/{jobId}",
            arguments = listOf(navArgument("jobId") {
                type = NavType.StringType
            }),
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start,
                    tween(500)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.End,
                    tween(500)
                )
            },
        ) { backStackEntry ->
            backStackEntry.arguments?.getString("jobId").let {
                JobDetailScreen(navController = navController, jobId = it.toString())
            }
        }
    }
}

