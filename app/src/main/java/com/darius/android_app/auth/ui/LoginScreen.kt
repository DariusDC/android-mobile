package com.darius.android_app.auth.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.darius.android_app.R

@Composable
fun LoginScreen(onClose: () -> Unit) {

    val loginViewModel = viewModel<LoginViewModel>(factory = LoginViewModel.Factory)
    val loginUIState = loginViewModel.uiState

    Scaffold(topBar = {
        TopAppBar(title = { Text(text = stringResource(id = R.string.login)) })
    }) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
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