package com.darius.android_app.screens.login

import android.app.Application
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Circle
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.darius.android_app.R
import com.darius.android_app.widgets.MyAppBar

@Composable
fun LoginScreen(onClose: () -> Unit) {

    val loginViewModel =
        viewModel<LoginViewModel>(factory = LoginViewModel.Factory(LocalContext.current.applicationContext as Application))
    val loginUIState = loginViewModel.uiState

    Scaffold(
        topBar = {
            MyAppBar(
                isConnected = loginViewModel.baseUIState,
                title = stringResource(id = R.string.login)
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
        ) {
            var username by remember { mutableStateOf("") }
            TextField(
                value = username, onValueChange = { username = it },
                label = {
                    Text(
                        text = stringResource(
                            id = R.string.username
                        )
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 15.dp),
            )

            var password by remember { mutableStateOf("") }
            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text(text = stringResource(id = R.string.password)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 15.dp),
                visualTransformation = PasswordVisualTransformation()
            )

            Button(onClick = {
                loginViewModel.login(username, password)
            }) {
                Text(text = "Login")
            }

            if (loginUIState.isAuthenticating) {
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(15.dp)
                )
            }

            if (loginUIState.authenticationError != null) {
                Text(text = "Login failed ${loginUIState.authenticationError.message}")
            }
        }
    }

    LaunchedEffect(loginUIState.authenticationCompleted) {
        if (loginUIState.authenticationCompleted) {
            onClose()
        }
    }

}

@Preview
@Composable
fun LoginScreenPreview() {
    LoginScreen({})
}