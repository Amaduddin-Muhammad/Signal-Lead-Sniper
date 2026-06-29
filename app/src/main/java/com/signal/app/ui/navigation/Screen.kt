package com.signal.app.ui.navigation

import kotlinx.serialization.Serializable

sealed class Screen {
    @Serializable
    object Feed

    @Serializable
    object Settings
}
