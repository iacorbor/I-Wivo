package com.icb.iwivo.ui.screens.profile

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.icb.iwivo.R
import com.icb.iwivo.data.model.ShopItemCategory
import com.icb.iwivo.data.repository.FirestoreRepository
import com.icb.iwivo.ui.components.WivoButton
import com.icb.iwivo.ui.components.WivoScreen
import com.icb.iwivo.ui.theme.GreenAccent
import com.icb.iwivo.ui.theme.PurplePrimary
import com.icb.iwivo.utils.base64ToBitmap
import com.icb.iwivo.utils.uriToBase64

@Composable
fun EditProfileScreen(
    firestoreRepository: FirestoreRepository,
    onBackClick: () -> Unit,
    onProfileUpdated: () -> Unit
) {
    val context = LocalContext.current
    val currentUser = FirebaseAuth.getInstance().currentUser

    val notAuthenticatedText = stringResource(R.string.profile_not_authenticated)
    val unknownErrorText = stringResource(R.string.profile_unknown_error)
    val emptyUsernameText = stringResource(R.string.edit_profile_username_empty_error)
    val shortUsernameText = stringResource(R.string.edit_profile_username_short_error)

    var username by remember { mutableStateOf("") }
    var avatarBase64 by remember { mutableStateOf("") }

    var equippedAvatarBorderId by remember { mutableStateOf<String?>(null) }
    var equippedBackgroundId by remember { mutableStateOf<String?>(null) }
    var equippedButtonStyleId by remember { mutableStateOf<String?>(null) }
    var equippedThemeId by remember { mutableStateOf<String?>(null) }

    var isLoading by remember { mutableStateOf(true) }
    var isSaving by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            val base64 = uriToBase64(
                context = context,
                uri = uri,
                maxSize = 320,
                quality = 60
            )

            if (base64.isNotBlank()) {
                avatarBase64 = base64
                error = null
            }
        }
    }

    LaunchedEffect(currentUser?.uid) {
        firestoreRepository.getShopStateByCategory(
            onResult = { _, _, equippedItemsByCategory ->
                equippedAvatarBorderId = equippedItemsByCategory[ShopItemCategory.AVATAR_BORDER]
                equippedBackgroundId = equippedItemsByCategory[ShopItemCategory.BACKGROUND]
                equippedButtonStyleId = equippedItemsByCategory[ShopItemCategory.BUTTON_STYLE]
                equippedThemeId = equippedItemsByCategory[ShopItemCategory.THEME]
            },
            onError = {
                // No bloqueo edición de perfil si falla la tienda.
            }
        )

        val uid = currentUser?.uid

        if (uid == null) {
            error = notAuthenticatedText
            isLoading = false
            return@LaunchedEffect
        }

        firestoreRepository.getUserProfile(
            uid = uid,
            onSuccess = { profile ->
                username = profile.username
                avatarBase64 = profile.avatarBase64
                isLoading = false
            },
            onError = { errorMessage ->
                error = errorMessage
                isLoading = false
            }
        )
    }

    WivoScreen(
        backgroundItemId = equippedBackgroundId,
        themeItemId = equippedThemeId
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            when {
                isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                else -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(modifier = Modifier.height(20.dp))

                        Text(
                            text = stringResource(R.string.edit_profile_title),
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )

                        Spacer(modifier = Modifier.height(28.dp))

                        AvatarEditor(
                            username = username,
                            avatarBase64 = avatarBase64,
                            equippedAvatarBorderId = equippedAvatarBorderId,
                            onAvatarClick = {
                                imagePickerLauncher.launch("image/*")
                            }
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = stringResource(R.string.edit_profile_change_photo),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.clickable {
                                imagePickerLauncher.launch("image/*")
                            }
                        )

                        Spacer(modifier = Modifier.height(32.dp))

                        OutlinedTextField(
                            value = username,
                            onValueChange = {
                                username = it
                                error = null
                            },
                            label = {
                                Text(stringResource(R.string.edit_profile_username_label))
                            },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(18.dp)
                        )

                        if (error != null) {
                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = error ?: unknownErrorText,
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }

                        Spacer(modifier = Modifier.weight(1f))

                        WivoButton(
                            text = if (isSaving) {
                                stringResource(R.string.edit_profile_saving)
                            } else {
                                stringResource(R.string.edit_profile_save_button)
                            },
                            onClick = {
                                val uid = currentUser?.uid
                                val cleanUsername = username.trim()

                                when {
                                    uid == null -> {
                                        error = notAuthenticatedText
                                    }

                                    cleanUsername.isBlank() -> {
                                        error = emptyUsernameText
                                    }

                                    cleanUsername.length < 3 -> {
                                        error = shortUsernameText
                                    }

                                    else -> {
                                        isSaving = true
                                        error = null

                                        firestoreRepository.updateUserProfile(
                                            uid = uid,
                                            username = cleanUsername,
                                            avatarBase64 = avatarBase64,
                                            onSuccess = {
                                                isSaving = false
                                                onProfileUpdated()
                                            },
                                            onError = { errorMessage ->
                                                isSaving = false
                                                error = errorMessage
                                            }
                                        )
                                    }
                                }
                            },
                            buttonStyleItemId = equippedButtonStyleId,
                            themeItemId = equippedThemeId
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        TextButton(
                            onClick = onBackClick
                        ) {
                            Text(
                                text = stringResource(R.string.edit_profile_cancel_button)
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun AvatarEditor(
    username: String,
    avatarBase64: String,
    equippedAvatarBorderId: String?,
    onAvatarClick: () -> Unit
) {
    val avatarBitmap = base64ToBitmap(avatarBase64)
    val fallbackInitial = username.ifBlank { "U" }.first().uppercase()

    val hasDecoration = equippedAvatarBorderId != null
    val outerSize = if (hasDecoration) 134.dp else 126.dp
    val innerSize = 126.dp

    Box(
        modifier = Modifier
            .size(outerSize)
            .clip(CircleShape)
            .background(
                brush = if (hasDecoration) {
                    getAvatarEditorDecorationBrush(equippedAvatarBorderId)
                } else {
                    Brush.linearGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.22f),
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.22f)
                        )
                    )
                }
            )
            .padding(
                if (hasDecoration) {
                    getAvatarEditorBorderPadding(equippedAvatarBorderId)
                } else {
                    0.dp
                }
            )
            .clickable { onAvatarClick() },
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(innerSize)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            if (avatarBitmap != null) {
                Image(
                    bitmap = avatarBitmap.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                Text(
                    text = fallbackInitial,
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Black,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
private fun getAvatarEditorDecorationBrush(
    equippedAvatarBorderId: String?
): Brush {
    return when (equippedAvatarBorderId) {
        "purple_border" -> Brush.linearGradient(
            colors = listOf(
                PurplePrimary,
                MaterialTheme.colorScheme.primary
            )
        )

        "blue_border" -> Brush.linearGradient(
            colors = listOf(
                MaterialTheme.colorScheme.secondary,
                MaterialTheme.colorScheme.primary
            )
        )

        "green_glow" -> Brush.linearGradient(
            colors = listOf(
                GreenAccent,
                MaterialTheme.colorScheme.primary
            )
        )

        "cyber_pink_border" -> Brush.linearGradient(
            colors = listOf(
                PurplePrimary,
                GreenAccent
            )
        )

        "gold_rank_border" -> Brush.linearGradient(
            colors = listOf(
                Color(0xFFFFD54F),
                Color(0xFFFFA000)
            )
        )

        "ice_blue_border" -> Brush.linearGradient(
            colors = listOf(
                Color(0xFF80D8FF),
                MaterialTheme.colorScheme.secondary
            )
        )

        "fire_orange_border" -> Brush.linearGradient(
            colors = listOf(
                Color(0xFFFF7043),
                Color(0xFFFF1744)
            )
        )

        "rainbow_dev_border" -> Brush.linearGradient(
            colors = listOf(
                PurplePrimary,
                MaterialTheme.colorScheme.secondary,
                GreenAccent
            )
        )

        else -> Brush.verticalGradient(
            colors = listOf(
                MaterialTheme.colorScheme.primary,
                MaterialTheme.colorScheme.secondary
            )
        )
    }
}

private fun getAvatarEditorBorderPadding(
    equippedAvatarBorderId: String?
): Dp {
    return when (equippedAvatarBorderId) {
        "green_glow" -> 6.dp
        "purple_border" -> 5.dp
        "blue_border" -> 5.dp
        "cyber_pink_border" -> 5.dp
        "gold_rank_border" -> 5.dp
        "ice_blue_border" -> 5.dp
        "fire_orange_border" -> 5.dp
        "rainbow_dev_border" -> 6.dp
        else -> 0.dp
    }
}