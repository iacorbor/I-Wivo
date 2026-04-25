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
import com.icb.iwivo.ui.screens.game.GameScreen
import com.icb.iwivo.ui.screens.result.ResultScreen
import com.icb.iwivo.data.repository.UserRepository
import androidx.compose.ui.platform.LocalContext
import com.icb.iwivo.ui.screens.profile.ProfileScreen

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
                },
                onProfileClick = {
                    navController.navigate(Screen.Profile.route)
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
                    navController.navigate(Screen.Game.createRoute(topic, gameType))
                }
            )
        }
        composable(
            route = Screen.Game.route,
            arguments = listOf(
                navArgument("topic") {
                    type = NavType.StringType
                },
                navArgument("gameType") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->

            val context = LocalContext.current
            val userRepository = androidx.compose.runtime.remember { UserRepository(context) }

            val topic = backStackEntry.arguments?.getString("topic").orEmpty()
            val gameType = backStackEntry.arguments?.getString("gameType").orEmpty()

            GameScreen(
                topic = topic,
                gameType = gameType,
                onFinishGame = { correct, total ->
                    val xpEarned = correct * 50

                    userRepository.addXp(xpEarned)
                    userRepository.updateStreak()

                    navController.navigate(
                        Screen.Result.createRoute(correct, total)
                    )
                }
            )
        }
        composable(
            route = Screen.Result.route,
            arguments = listOf(
                navArgument("correct") { type = NavType.IntType },
                navArgument("total") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val correct = backStackEntry.arguments?.getInt("correct") ?: 0
            val total = backStackEntry.arguments?.getInt("total") ?: 0

            ResultScreen(
                correct = correct,
                total = total,
                onBackHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }
        composable(Screen.Profile.route) {
            ProfileScreen()
        }
    }

}