package com.lilcode.examples.jetpack_compose_material3.lottery

import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.lilcode.examples.jetpack_compose_material3.MainActivity

fun NavHostController.onNavigateToHome() {
    navigate(
        MainActivity.mainHomeRouteName
    ) {
        launchSingleTop = true
        popUpTo(graph.findStartDestination().id) {
            saveState = true
        }
    }
}

fun NavHostController.onNavigateTo645() {
    navigate(MainActivity.lottery645RouteName) {
        launchSingleTop = true
    }
}

fun NavHostController.onNavigateTo720() {
    navigate(MainActivity.lottery720RouteName) {
        launchSingleTop = true
    }
}