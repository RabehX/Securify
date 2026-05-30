package io.github.rabehx.securify

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.motionScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.unit.IntOffset
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import io.github.rabehx.securify.ui.screens.home.Home
import io.github.rabehx.securify.ui.screens.home.settings.Settings
import kotlinx.serialization.Serializable

@Serializable
sealed interface Route : NavKey {
    @Serializable
    data object Home : Route

    @Serializable
    data object Settings : Route
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun MainNavigation() {
    val startDestination = Route.Home
    val backStack = rememberNavBackStack(startDestination)

    val spatialSpec = motionScheme.defaultSpatialSpec<IntOffset>()
    val spatialFloatSpec = motionScheme.defaultSpatialSpec<Float>()
    val effectsSpec = motionScheme.defaultEffectsSpec<Float>()

    val forwardTransitionSpec: AnimatedContentTransitionScope<*>.() -> ContentTransform = {
        ContentTransform(
            targetContentEnter = slideInHorizontally(
                animationSpec = spatialSpec,
                initialOffsetX = { it }
            ),
            initialContentExit = slideOutHorizontally(
                animationSpec = spatialSpec,
                targetOffsetX = { -it / 3 }
            ) + fadeOut(animationSpec = effectsSpec),
            sizeTransform = null
        )
    }

    val popTransitionSpec: AnimatedContentTransitionScope<*>.() -> ContentTransform = {
        ContentTransform(
            targetContentEnter = slideInHorizontally(
                animationSpec = spatialSpec,
                initialOffsetX = { -it / 3 }
            ) + scaleIn(
                animationSpec = spatialFloatSpec,
                initialScale = 0.9f
            ),
            initialContentExit = slideOutHorizontally(
                animationSpec = spatialSpec,
                targetOffsetX = { it }
            ) + scaleOut(
                animationSpec = spatialFloatSpec,
                targetScale = 0.75f,
                transformOrigin = TransformOrigin(0.5f, 0.5f)
            ),
            sizeTransform = null
        )
    }

    val predictivePopTransitionSpec: AnimatedContentTransitionScope<*>.(Int) -> ContentTransform = {
        ContentTransform(
            targetContentEnter = slideInHorizontally(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioNoBouncy,
                    stiffness = Spring.StiffnessMediumLow
                ),
                initialOffsetX = { -it / 3 }
            ) + scaleIn(
                initialScale = 0.9f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioNoBouncy,
                    stiffness = Spring.StiffnessMediumLow
                )
            ),
            initialContentExit = slideOutHorizontally(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioNoBouncy,
                    stiffness = Spring.StiffnessMediumLow
                ),
                targetOffsetX = { it }
            ) + scaleOut(
                targetScale = 0.75f,
                transformOrigin = TransformOrigin(0.5f, 0.5f),
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioNoBouncy,
                    stiffness = Spring.StiffnessMediumLow
                )
            ),
            sizeTransform = null
        )
    }

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        transitionSpec = forwardTransitionSpec,
        popTransitionSpec = popTransitionSpec,
        predictivePopTransitionSpec = predictivePopTransitionSpec,
        entryDecorators = listOf(rememberSaveableStateHolderNavEntryDecorator()),
        entryProvider = { route ->
            when (route) {
                Route.Home -> NavEntry(route) {
                    Home(
                        onNavigateTo = { backStack.add(Route.Settings) },
                    )
                }

                Route.Settings -> NavEntry(route) {
                    Settings(
                        onBack = { backStack.removeLastOrNull() },
                    )
                }

                else -> NavEntry(route) { Text("Unknown route: $route") }
            }
        },
    )
}
