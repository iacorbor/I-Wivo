package com.icb.iwivo.ui.screens.register

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.icb.iwivo.R
import androidx.compose.runtime.remember
import com.icb.iwivo.data.repository.AuthRepository
import com.icb.iwivo.data.repository.FirestoreRepository
import com.icb.iwivo.ui.components.WivoButton
import com.icb.iwivo.ui.components.WivoScreen
import com.icb.iwivo.ui.components.WivoTextField

@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    onBackToLogin: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var errorResId by remember { mutableStateOf<Int?>(null) }
    val authRepository = remember { AuthRepository() }

    WivoScreen{
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Spacer(modifier = Modifier.height(64.dp))

            Text(
                text = stringResource(R.string.create_account),
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(32.dp))

            WivoTextField(
                value = email,
                onValueChange = { email = it },
                label =stringResource(R.string.email),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            WivoTextField(
                value = password,
                onValueChange = { password = it },
                label =stringResource(R.string.password),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            WivoTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label =stringResource(R.string.confirm_password),
                modifier = Modifier.fillMaxWidth()
            )

            errorResId?.let {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(it),
                    color = MaterialTheme.colorScheme.error
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            WivoButton(
                onClick = {
                    when {
                        email.isBlank() || password.isBlank() || confirmPassword.isBlank() -> {
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
                                    FirestoreRepository().createUserIfNotExists()
                                    onRegisterSuccess()

                                } else {
                                    errorResId = R.string.error_register_failed
                                }
                            }
                        }
                    }
                },
                text = stringResource(R.string.register)
            )

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