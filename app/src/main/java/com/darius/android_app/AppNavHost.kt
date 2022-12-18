package com.darius.android_app

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.darius.android_app.screens.login.LoginScreen
import com.darius.android_app.networking.Api
import com.darius.android_app.screens.UserPreferencesViewModel
import com.darius.android_app.screens.item.ItemScreen
import com.darius.android_app.screens.items.ItemsScreen

const val authRoute = "auth"
const val hotelRoute = "hotels"


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavHost(context: Context) {
    val navController = rememberNavController()
    val onCloseItem = {
        navController.popBackStack()
    }

    var userPreferencesViewModel =
        viewModel<UserPreferencesViewModel>(factory = UserPreferencesViewModel.Factory)
    val userPreferencesUIState = userPreferencesViewModel.uiState
    val androidAppViewModel = viewModel<AndroidAppViewModel>(factory = AndroidAppViewModel.Factory)

    NavHost(navController = navController, startDestination = authRoute) {
        composable(hotelRoute) {
            ItemsScreen(onItemClick = { itemId ->
                navController.navigate("$hotelRoute/$itemId")
            }, onAddItem = { navController.navigate("$hotelRoute-new") },
                onLogout = {
                    androidAppViewModel.logout()
                    Api.tokenInterceptor.token = null
                    navController.navigate(authRoute) {
                        popUpTo(0)
                    }
                })
        }

        composable(route = "$hotelRoute/{id}", arguments = listOf(navArgument("id") {
            type = NavType.StringType
        })) {
            ItemScreen(
                context = context,
                itemId = it.arguments?.getString("id"),
                onClose = { onCloseItem() })
        }

        composable(route = "$hotelRoute-new") {
            ItemScreen(context = context, itemId = null, onClose = { onCloseItem() })
        }

        composable(route = authRoute) {
            LoginScreen(onClose = {
                navController.navigate(hotelRoute)
            })
        }
    }

    LaunchedEffect(userPreferencesUIState.token) {
        if (userPreferencesUIState.token.isNotEmpty()) {
            Api.tokenInterceptor.token = userPreferencesUIState.token
            navController.navigate(hotelRoute) {
                popUpTo(0)
            }
        }
    }
}