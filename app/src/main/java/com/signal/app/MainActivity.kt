package com.signal.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Radar
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Radar
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.signal.app.ui.feed.FeedViewModel
import com.signal.app.ui.navigation.NavGraph
import com.signal.app.ui.navigation.Screen
import com.signal.app.ui.settings.SettingsViewModel
import com.signal.app.ui.theme.SignalTheme
import com.signal.app.workers.LeadSyncWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        LeadSyncWorker.schedule(applicationContext)

        val repository = (application as SignalApplication).repository
        val api = (application as SignalApplication).leadApi

        setContent {
            SignalTheme {
                val navController = rememberNavController()
                
                val feedViewModel: FeedViewModel by viewModels { FeedViewModel.Factory(repository) }
                val settingsViewModel: SettingsViewModel by viewModels { SettingsViewModel.Factory(repository) }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        val navBackStackEntry by navController.currentBackStackEntryAsState()
                        val currentDestination = navBackStackEntry?.destination?.route

                        NavigationBar(
                            modifier = Modifier.testTag("bottom_nav"),
                            containerColor = androidx.compose.material3.MaterialTheme.colorScheme.surface
                        ) {
                            NavigationBarItem(
                                selected = currentDestination == Screen.Feed::class.qualifiedName,
                                onClick = {
                                    navController.navigate(Screen.Feed) {
                                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                },
                                icon = {
                                    Icon(
                                        imageVector = if (currentDestination == Screen.Feed::class.qualifiedName) Icons.Default.Radar else Icons.Outlined.Radar,
                                        contentDescription = "Feed"
                                    )
                                },
                                label = { Text("Feed") },
                                modifier = Modifier.testTag("nav_feed"),
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = androidx.compose.material3.MaterialTheme.colorScheme.primary,
                                    selectedTextColor = androidx.compose.material3.MaterialTheme.colorScheme.primary
                                )
                            )

                            NavigationBarItem(
                                selected = currentDestination == Screen.Settings::class.qualifiedName,
                                onClick = {
                                    navController.navigate(Screen.Settings) {
                                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                },
                                icon = {
                                    Icon(
                                        imageVector = if (currentDestination == Screen.Settings::class.qualifiedName) Icons.Default.Settings else Icons.Outlined.Settings,
                                        contentDescription = "Settings"
                                    )
                                },
                                label = { Text("Settings") },
                                modifier = Modifier.testTag("nav_settings"),
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = androidx.compose.material3.MaterialTheme.colorScheme.primary,
                                    selectedTextColor = androidx.compose.material3.MaterialTheme.colorScheme.primary
                                )
                            )
                        }
                    }
                ) { innerPadding ->
                    NavGraph(
                        navController = navController,
                        feedViewModel = feedViewModel,
                        settingsViewModel = settingsViewModel,
                        onRefresh = {
                            CoroutineScope(Dispatchers.IO).launch {
                                val profile = repository.getUserProfile().first()
                                val newLeads = api.fetchLeads(profile.serviceCategory, profile.radius)
                                repository.insertLeads(newLeads)
                            }
                        },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}
