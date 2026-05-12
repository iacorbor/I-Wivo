package com.icb.iwivo.ui.screens.settings

import android.app.Activity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.icb.iwivo.R
import com.icb.iwivo.data.model.ShopItemCategory
import com.icb.iwivo.data.repository.FirestoreRepository
import com.icb.iwivo.data.repository.SettingsRepository
import com.icb.iwivo.ui.components.WivoButton
import com.icb.iwivo.ui.components.WivoCard
import com.icb.iwivo.ui.components.WivoScreen

@Composable
fun SettingsScreen(
    firestoreRepository: FirestoreRepository,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val settingsRepository = remember { SettingsRepository(context) }

    var soundEnabled by remember {
        mutableStateOf(settingsRepository.isSoundEnabled())
    }

    var hapticsEnabled by remember {
        mutableStateOf(settingsRepository.isHapticsEnabled())
    }

    var selectedLanguage by remember {
        mutableStateOf(settingsRepository.getSelectedLanguage())
    }

    var equippedBackgroundId by remember { mutableStateOf<String?>(null) }
    var equippedButtonStyleId by remember { mutableStateOf<String?>(null) }
    var equippedThemeId by remember { mutableStateOf<String?>(null) }
    var equippedEffectId by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        firestoreRepository.getShopStateByCategory(
            onResult = { _, _, equippedItemsByCategory ->
                equippedBackgroundId = equippedItemsByCategory[ShopItemCategory.BACKGROUND]
                equippedButtonStyleId = equippedItemsByCategory[ShopItemCategory.BUTTON_STYLE]
                equippedThemeId = equippedItemsByCategory[ShopItemCategory.THEME]
                equippedEffectId = equippedItemsByCategory[ShopItemCategory.EFFECT]
            },
            onError = {
                // No bloqueo Ajustes si falla la tienda.
            }
        )
    }

    WivoScreen(
        backgroundItemId = equippedBackgroundId,
        themeItemId = equippedThemeId
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(26.dp))

            Text(
                text = stringResource(R.string.settings_title),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = stringResource(R.string.settings_subtitle),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.72f)
            )

            Spacer(modifier = Modifier.height(24.dp))

            SettingsOptionCard(
                title = stringResource(R.string.settings_sound_title),
                description = stringResource(R.string.settings_sound_description),
                checked = soundEnabled,
                themeItemId = equippedThemeId,
                effectItemId = equippedEffectId,
                onCheckedChange = { enabled ->
                    soundEnabled = enabled
                    settingsRepository.setSoundEnabled(enabled)
                }
            )

            Spacer(modifier = Modifier.height(14.dp))

            SettingsOptionCard(
                title = stringResource(R.string.settings_haptics_title),
                description = stringResource(R.string.settings_haptics_description),
                checked = hapticsEnabled,
                themeItemId = equippedThemeId,
                effectItemId = equippedEffectId,
                onCheckedChange = { enabled ->
                    hapticsEnabled = enabled
                    settingsRepository.setHapticsEnabled(enabled)
                }
            )

            Spacer(modifier = Modifier.height(14.dp))

            LanguageSettingsCard(
                selectedLanguage = selectedLanguage,
                themeItemId = equippedThemeId,
                effectItemId = equippedEffectId,
                buttonStyleItemId = equippedButtonStyleId,
                onLanguageSelected = { language ->
                    selectedLanguage = language
                    settingsRepository.setSelectedLanguage(language)

                    val activity = context as? Activity
                    activity?.recreate()
                }
            )

            Spacer(modifier = Modifier.height(14.dp))

            AboutAppCard(
                themeItemId = equippedThemeId,
                effectItemId = equippedEffectId
            )

            Spacer(modifier = Modifier.height(24.dp))

            TextButton(
                onClick = onBackClick,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(text = stringResource(R.string.settings_back))
            }

            Spacer(modifier = Modifier.height(18.dp))
        }
    }
}

@Composable
private fun SettingsOptionCard(
    title: String,
    description: String,
    checked: Boolean,
    themeItemId: String?,
    effectItemId: String?,
    onCheckedChange: (Boolean) -> Unit
) {
    WivoCard(
        themeItemId = themeItemId,
        effectItemId = effectItemId
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.72f)
                )
            }

            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange
            )
        }
    }
}

@Composable
private fun LanguageSettingsCard(
    selectedLanguage: String,
    themeItemId: String?,
    effectItemId: String?,
    buttonStyleItemId: String?,
    onLanguageSelected: (String) -> Unit
) {
    WivoCard(
        themeItemId = themeItemId,
        effectItemId = effectItemId
    ) {
        Text(
            text = stringResource(R.string.settings_language_title),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = stringResource(R.string.settings_language_description),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.72f)
        )

        Spacer(modifier = Modifier.height(14.dp))

        WivoButton(
            text = if (selectedLanguage == SettingsRepository.LANGUAGE_SPANISH) {
                "🇪🇸 ${stringResource(R.string.settings_language_spanish_selected)}"
            } else {
                "🇪🇸 ${stringResource(R.string.settings_language_spanish)}"
            },
            onClick = {
                onLanguageSelected(SettingsRepository.LANGUAGE_SPANISH)
            },
            buttonStyleItemId = buttonStyleItemId,
            themeItemId = themeItemId
        )

        Spacer(modifier = Modifier.height(10.dp))

        WivoButton(
            text = if (selectedLanguage == SettingsRepository.LANGUAGE_ENGLISH) {
                "🇬🇧 ${stringResource(R.string.settings_language_english_selected)}"
            } else {
                "🇬🇧 ${stringResource(R.string.settings_language_english)}"
            },
            onClick = {
                onLanguageSelected(SettingsRepository.LANGUAGE_ENGLISH)
            },
            buttonStyleItemId = buttonStyleItemId,
            themeItemId = themeItemId
        )
    }
}

@Composable
private fun AboutAppCard(
    themeItemId: String?,
    effectItemId: String?
) {
    WivoCard(
        themeItemId = themeItemId,
        effectItemId = effectItemId
    ) {
        Text(
            text = stringResource(R.string.settings_about_title),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = stringResource(R.string.settings_about_description),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.72f)
        )

        Spacer(modifier = Modifier.height(16.dp))

        AboutRow(
            label = stringResource(R.string.settings_about_app_name_label),
            value = stringResource(R.string.settings_about_app_name_value)
        )

        Spacer(modifier = Modifier.height(8.dp))

        AboutRow(
            label = stringResource(R.string.settings_about_version_label),
            value = stringResource(R.string.settings_about_version_value)
        )

        Spacer(modifier = Modifier.height(8.dp))

        AboutRow(
            label = stringResource(R.string.settings_about_creator_label),
            value = stringResource(R.string.settings_about_creator_value)
        )

        Spacer(modifier = Modifier.height(8.dp))

        AboutRow(
            label = stringResource(R.string.settings_about_project_label),
            value = stringResource(R.string.settings_about_project_value)
        )

        Spacer(modifier = Modifier.height(8.dp))

        AboutRow(
            label = stringResource(R.string.settings_about_status_label),
            value = stringResource(R.string.settings_about_status_value)
        )
    }
}

@Composable
private fun AboutRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.74f)
        )
    }
}