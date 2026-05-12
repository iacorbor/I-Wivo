package com.icb.iwivo.ui.screens.login

import android.app.Activity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.icb.iwivo.R
import com.icb.iwivo.data.repository.AuthRepository
import com.icb.iwivo.data.repository.SettingsRepository
import com.icb.iwivo.ui.components.WivoButton
import com.icb.iwivo.ui.components.WivoLogo
import com.icb.iwivo.ui.components.WivoScreen
import com.icb.iwivo.ui.components.WivoTextField

@Composable
fun LoginScreen(
    onLoginClick: () -> Unit,
    onGoToRegister: () -> Unit
) {
    val context = LocalContext.current
    val authRepository = remember { AuthRepository() }
    val settingsRepository = remember { SettingsRepository(context) }

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorResId by remember { mutableStateOf<Int?>(null) }

    var selectedLanguage by remember {
        mutableStateOf(settingsRepository.getSelectedLanguage())
    }

    WivoScreen {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
        ) {
            LanguageDropdown(
                selectedLanguage = selectedLanguage,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 4.dp, end = 16.dp),
                onLanguageSelected = { language ->
                    selectedLanguage = language
                    settingsRepository.setSelectedLanguage(language)

                    val activity = context as? Activity
                    activity?.recreate()
                }
            )

            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Spacer(modifier = Modifier.height(150.dp))

                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    WivoLogo()
                }

                Spacer(modifier = Modifier.height(2.dp))

                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.app_name),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                WivoTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = stringResource(R.string.email),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                WivoTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = stringResource(R.string.password),
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = PasswordVisualTransformation()
                )

                errorResId?.let { messageResId ->
                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = stringResource(messageResId),
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
                    Text(
                        text = stringResource(R.string.create_account)
                    )
                }
            }
        }
    }
}

@Composable
private fun LanguageDropdown(
    selectedLanguage: String,
    modifier: Modifier = Modifier,
    onLanguageSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    val selectedFlag = when (selectedLanguage) {
        SettingsRepository.LANGUAGE_ENGLISH -> "🇬🇧"
        else -> "🇪🇸"
    }

    Box(
        modifier = modifier
    ) {
        Text(
            text = selectedFlag,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier
                .clickable {
                    expanded = true
                }
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            }
        ) {
            DropdownMenuItem(
                text = {
                    Text(text = "🇪🇸 Español")
                },
                onClick = {
                    expanded = false
                    onLanguageSelected(SettingsRepository.LANGUAGE_SPANISH)
                }
            )

            DropdownMenuItem(
                text = {
                    Text(text = "🇬🇧 English")
                },
                onClick = {
                    expanded = false
                    onLanguageSelected(SettingsRepository.LANGUAGE_ENGLISH)
                }
            )
        }
    }
}