package com.icb.iwivo.ui.screens.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.icb.iwivo.R
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Brush
import com.icb.iwivo.data.repository.AuthRepository
import com.icb.iwivo.ui.components.WivoButton
import com.icb.iwivo.ui.components.WivoLogo
import com.icb.iwivo.ui.components.WivoScreen
import com.icb.iwivo.ui.components.WivoTextField
import com.icb.iwivo.ui.theme.GreenAccent
import com.icb.iwivo.ui.theme.PurplePrimary


@Composable
fun LoginScreen(
    onLoginClick: () -> Unit,
    onGoToRegister: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorResId by remember { mutableStateOf<Int?>(null) }
    val authRepository = remember { AuthRepository() }

    WivoScreen {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Spacer(modifier = Modifier.height(150.dp))
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ){
                WivoLogo()
            }
            Spacer(modifier = Modifier.height(2.dp))

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.app_name),
                    style = MaterialTheme.typography.titleLarge, // 👈 más pequeño
                    color = MaterialTheme.colorScheme.onBackground // 👈 blanco automático en dark mode
                )
            }

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
                label = stringResource(R.string.password),
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

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                WivoButton(
                    onClick = {
                        if (email.isBlank() || password.isBlank()) {
                            errorResId = R.string.error_fill_all_fields
                        } else {
                            errorResId = null

                            authRepository.login(
                                email = email,
                                password = password
                            ) { success, _ ->
                                if (success) {
                                    onLoginClick()
                                } else {
                                    errorResId = R.string.error_login_failed
                                }
                            }
                        }
                    },
                    text = stringResource(R.string.login),
                    modifier = Modifier
                        .width(260.dp)
                        .height(56.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            TextButton(
                onClick = onGoToRegister,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.create_account))
            }
        }
    }
}
