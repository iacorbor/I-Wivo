package com.icb.iwivo.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.icb.iwivo.R
import com.icb.iwivo.data.model.UserProfile
import com.icb.iwivo.data.repository.AuthRepository
import com.icb.iwivo.data.repository.FirestoreRepository
import com.icb.iwivo.ui.components.WivoButton
import com.icb.iwivo.ui.components.WivoScreen

@Composable
fun ProfileScreen(
    authRepository: AuthRepository,
    firestoreRepository: FirestoreRepository,
    onEditProfileClick: () -> Unit,
    onAchievementsClick: () -> Unit,
    onLogout: () -> Unit
) {
    val currentUser = FirebaseAuth.getInstance().currentUser

    val noUsernameText = stringResource(R.string.profile_no_username)
    val noEmailText = stringResource(R.string.profile_no_email)
    val notAuthenticatedText = stringResource(R.string.profile_not_authenticated)

    var profile by remember { mutableStateOf<UserProfile?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(currentUser?.uid) {
        val uid = currentUser?.uid

        if (uid == null) {
            error = notAuthenticatedText
            isLoading = false
            return@LaunchedEffect
        }

        firestoreRepository.getUserProfile(
            uid = uid,
            onSuccess = { userProfile ->
                profile = userProfile
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
                .padding(horizontal = 28.dp, vertical = 24.dp)
        ) {
            when {
                isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                error != null -> {
                    Text(
                        text = error ?: stringResource(R.string.profile_unknown_error),
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                profile != null -> {
                    val userProfile = profile!!

                    val username = userProfile.username.ifBlank { noUsernameText }
                    val email = userProfile.email.ifBlank {
                        currentUser?.email ?: noEmailText
                    }

                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(modifier = Modifier.height(24.dp))

                        Text(
                            text = stringResource(R.string.profile_title),
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )

                        Spacer(modifier = Modifier.height(26.dp))

                        Box(
                            modifier = Modifier
                                .size(112.dp)
                                .clip(CircleShape)
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(
                                            MaterialTheme.colorScheme.primary,
                                            MaterialTheme.colorScheme.secondary
                                        )
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = username.first().uppercase(),
                                style = MaterialTheme.typography.headlineLarge,
                                fontWeight = FontWeight.Black,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        Text(
                            text = username,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = email,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.72f)
                        )

                        Spacer(modifier = Modifier.height(28.dp))

                        WivoButton(
                            text = stringResource(R.string.profile_edit_button),
                            onClick = onEditProfileClick
                        )

                        Spacer(modifier = Modifier.height(18.dp))

                        AchievementsPreviewCard(
                            onClick = onAchievementsClick
                        )

                        Spacer(modifier = Modifier.weight(1f))

                        OutlinedButton(
                            onClick = {
                                authRepository.logout()
                                onLogout()
                            },
                            modifier = Modifier
                                .fillMaxWidth(0.78f)
                                .height(52.dp),
                            shape = RoundedCornerShape(18.dp)
                        ) {
                            Text(
                                text = stringResource(R.string.profile_logout_button),
                                color = MaterialTheme.colorScheme.onBackground
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
private fun AchievementsPreviewCard(
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.55f)
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = stringResource(R.string.profile_achievements_title),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = stringResource(R.string.profile_achievements_description),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.78f)
            )

            Spacer(modifier = Modifier.height(18.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                AchievementIconPreview(
                    emoji = "🔥",
                    label = stringResource(R.string.profile_achievement_streak)
                )

                AchievementIconPreview(
                    emoji = "⚡",
                    label = stringResource(R.string.profile_achievement_xp)
                )

                AchievementIconPreview(
                    emoji = "🏆",
                    label = stringResource(R.string.profile_achievement_quiz)
                )

                AchievementIconPreview(
                    emoji = "🔒",
                    label = stringResource(R.string.profile_achievement_locked)
                )
            }
        }
    }
}

@Composable
private fun AchievementIconPreview(
    emoji: String,
    label: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(52.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.22f)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = emoji,
                style = MaterialTheme.typography.titleLarge
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.88f)
        )
    }
}