package com.darius.android_app

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.darius.android_app.auth.ui.LoginScreen
import com.darius.android_app.core.data.remote.Api
import com.darius.android_app.core.ui.UserPreferencesViewModel
import com.darius.android_app.item.ui.item.ItemScreen
import com.darius.android_app.item.ui.items.ItemsScreen

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
}