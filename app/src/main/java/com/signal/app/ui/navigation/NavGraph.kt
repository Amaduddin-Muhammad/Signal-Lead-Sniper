package com.signal.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.signal.app.ui.feed.FeedScreen
import com.signal.app.ui.feed.FeedViewModel
import com.signal.app.ui.settings.SettingsScreen
import com.signal.app.ui.settings.SettingsViewModel

@Composable
fun NavGraph(
    navController: NavHostController,
    feedViewModel: FeedViewModel,
    settingsViewModel: SettingsViewModel,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Feed,
        modifier = modifier
    ) {
        composable<Screen.Feed> {
            FeedScreen(
                viewModel = feedViewModel,
                onRefresh = onRefresh
            )
        }

        composable<Screen.Settings> {
            SettingsScreen(
                viewModel = settingsViewModel
            )
        }
    }
}
