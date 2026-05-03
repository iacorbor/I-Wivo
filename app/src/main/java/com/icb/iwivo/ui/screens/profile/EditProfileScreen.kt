package com.icb.iwivo.ui.screens.profile

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.icb.iwivo.R
import com.icb.iwivo.data.repository.FirestoreRepository
import com.icb.iwivo.ui.components.WivoButton
import com.icb.iwivo.ui.components.WivoScreen
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

    WivoScreen {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 24.dp)
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
                            }
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
    onAvatarClick: () -> Unit
) {
    val avatarBitmap = base64ToBitmap(avatarBase64)
    val fallbackInitial = username.ifBlank { "U" }.first().uppercase()

    Box(
        modifier = Modifier
            .size(126.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.22f))
            .clickable { onAvatarClick() },
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