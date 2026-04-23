package com.icb.iwivo.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.icb.iwivo.ui.screens.home.HomeScreen
import com.icb.iwivo.ui.screens.login.LoginScreen
import com.icb.iwivo.ui.screens.topic.TopicSelectionScreen
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.icb.iwivo.ui.screens.game.GameTypeScreen

@Composable
fun NavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginClick = {
                    navController.navigate(Screen.Home.route)
                }
            )
        }

        composable(Screen.Home.route) {
            HomeScreen(
                onStartClick = {
                    navController.navigate(Screen.TopicSelection.route)
                }
            )
        }

        composable(Screen.TopicSelection.route) {
            TopicSelectionScreen(
                onTopicClick = { topic ->
                    navController.navigate(Screen.GameType.createRoute(topic))
                }
            )
        }

        composable(
            route = Screen.GameType.route,
            arguments = listOf(
                navArgument("topic") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val topic = backStackEntry.arguments?.getString("topic").orEmpty()

            GameTypeScreen(
                topic = topic,
                onGameTypeClick = { gameType ->
                    // aquí luego iremos a la pantalla de juego
                }
            )
        }
    }

}