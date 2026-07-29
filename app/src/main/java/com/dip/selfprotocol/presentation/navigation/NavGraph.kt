package com.dip.selfprotocol.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.dip.selfprotocol.domain.model.CategoryType
import com.dip.selfprotocol.presentation.categories.CategoriesScreen
import com.dip.selfprotocol.presentation.home.HomeScreen
import com.dip.selfprotocol.presentation.lessons.LessonDetailScreen
import com.dip.selfprotocol.presentation.lessons.LessonsListScreen
import com.dip.selfprotocol.presentation.rules.RuleDetailScreen
import com.dip.selfprotocol.presentation.rules.RulesListScreen

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally

@Composable
fun AppNavGraph(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Route.Home.route,
        enterTransition = {
            slideInHorizontally(
                initialOffsetX = { it },
                animationSpec = tween(300)
            ) + fadeIn(animationSpec = tween(300))
        },
        exitTransition = {
            slideOutHorizontally(
                targetOffsetX = { -it / 3 },
                animationSpec = tween(300)
            ) + fadeOut(animationSpec = tween(300))
        },
        popEnterTransition = {
            slideInHorizontally(
                initialOffsetX = { -it / 3 },
                animationSpec = tween(300)
            ) + fadeIn(animationSpec = tween(300))
        },
        popExitTransition = {
            slideOutHorizontally(
                targetOffsetX = { it },
                animationSpec = tween(300)
            ) + fadeOut(animationSpec = tween(300))
        }
    ) {
        composable(route = Route.Home.route) {
            HomeScreen(
                onNavigateToRules = { navController.navigate(Route.Categories.createRoute(CategoryType.RULE.name)) },
                onNavigateToLessons = { navController.navigate(Route.Categories.createRoute(CategoryType.LESSON.name)) },
                onNavigateToSettings = { navController.navigate(Route.Settings.route) }
            )
        }
        
        composable(route = Route.Categories.route) { backStackEntry ->
            val type = backStackEntry.arguments?.getString("type") ?: CategoryType.RULE.name
            CategoriesScreen(
                onNavigateBack = { navController.popBackStack() },
                onCategoryClick = { categoryId ->
                    if (type == CategoryType.RULE.name) {
                        navController.navigate(Route.RulesList.createRoute(categoryId))
                    } else {
                        navController.navigate(Route.LessonsList.createRoute(categoryId))
                    }
                }
            )
        }
        
        composable(route = Route.RulesList.route) { backStackEntry ->
            val categoryId = backStackEntry.arguments?.getString("categoryId")?.toIntOrNull() ?: 0
            RulesListScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToDetail = { ruleId ->
                    navController.navigate(Route.RuleDetail.createRoute(categoryId, ruleId))
                }
            )
        }

        composable(route = Route.LessonsList.route) { backStackEntry ->
            val categoryId = backStackEntry.arguments?.getString("categoryId")?.toIntOrNull() ?: 0
            LessonsListScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToDetail = { lessonId ->
                    navController.navigate(Route.LessonDetail.createRoute(categoryId, lessonId))
                }
            )
        }

        composable(route = Route.RuleDetail.route) {
            RuleDetailScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(route = Route.LessonDetail.route) {
            LessonDetailScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        composable(route = Route.Settings.route) {
            com.dip.selfprotocol.presentation.settings.SettingsScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
