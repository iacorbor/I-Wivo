package com.icb.iwivo.ui.screens.register

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.icb.iwivo.R
import com.icb.iwivo.data.repository.AuthRepository
import com.icb.iwivo.data.repository.FirestoreRepository
import com.icb.iwivo.ui.components.WivoButton
import com.icb.iwivo.ui.components.WivoLogo
import com.icb.iwivo.ui.components.WivoScreen
import com.icb.iwivo.ui.components.WivoTextField
import com.icb.iwivo.ui.theme.TextSecondary

@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    onBackToLogin: () -> Unit
) {
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var errorResId by remember { mutableStateOf<Int?>(null) }

    val authRepository = remember { AuthRepository() }

    WivoScreen {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Spacer(modifier = Modifier.height(90.dp))

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                WivoLogo()
            }

            Spacer(modifier = Modifier.height(32.dp))

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = stringResource(R.string.create_profile),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            WivoTextField(
                value = username,
                onValueChange = { username = it },
                label = stringResource(R.string.username),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            WivoTextField(
                value = email,
                onValueChange = { email = it },
                label = stringResource(R.string.email),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            WivoTextField(
                value = password,
                onValueChange = { password = it },
                label = stringResource(R.string.password),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            WivoTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = stringResource(R.string.confirm_password),
                modifier = Modifier.fillMaxWidth()
            )

            errorResId?.let {
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = stringResource(it),
                    color = MaterialTheme.colorScheme.error
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                WivoButton(
                    text = stringResource(R.string.register),
                    onClick = {
                        when {
                            username.isBlank() || email.isBlank() || password.isBlank() || confirmPassword.isBlank() -> {
                                errorResId = R.string.error_fill_all_fields
                            }

                            password != confirmPassword -> {
                                errorResId = R.string.error_passwords_do_not_match
                            }

                            password.length < 6 -> {
                                errorResId = R.string.error_password_too_short
                            }

                            else -> {
                                errorResId = null

                                authRepository.register(
                                    email = email,
                                    password = password
                                ) { success, _ ->
                                    if (success) {
                                        FirestoreRepository().createUserIfNotExists(
                                            username = username,
                                            onSuccess = {
                                                onRegisterSuccess()
                                            },
                                            onError = {
                                                errorResId = R.string.error_register_failed
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    },
                    modifier = Modifier
                        .width(260.dp)
                        .height(56.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            TextButton(
                onClick = onBackToLogin,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.back))
            }
        }
    }
}