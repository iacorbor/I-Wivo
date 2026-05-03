package com.icb.iwivo.ui.screens.profile

import androidx.compose.foundation.Image
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.icb.iwivo.R
import com.icb.iwivo.data.model.UserProfile
import com.icb.iwivo.data.repository.AuthRepository
import com.icb.iwivo.data.repository.BadgeRepository
import com.icb.iwivo.data.repository.FirestoreRepository
import com.icb.iwivo.ui.components.WivoButton
import com.icb.iwivo.ui.components.WivoScreen
import com.icb.iwivo.utils.base64ToBitmap

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

    var xp by remember { mutableIntStateOf(0) }
    var streak by remember { mutableIntStateOf(0) }

    LaunchedEffect(currentUser?.uid) {
        val uid = currentUser?.uid

        if (uid == null) {
            error = notAuthenticatedText
            isLoading = false
            return@LaunchedEffect
        }

        firestoreRepository.getCurrentUserData(
            onResult = { remoteXp, _, remoteStreak ->
                xp = remoteXp
                streak = remoteStreak
            }
        )

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

                        ProfileAvatar(
                            username = username,
                            avatarBase64 = userProfile.avatarBase64
                        )

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
                            xp = xp,
                            streak = streak,
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
private fun ProfileAvatar(
    username: String,
    avatarBase64: String
) {
    val avatarBitmap = base64ToBitmap(avatarBase64)
    val fallbackInitial = username.ifBlank { "U" }.first().uppercase()

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
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

@Composable
private fun AchievementsPreviewCard(
    xp: Int,
    streak: Int,
    onClick: () -> Unit
) {
    val badgeRepository = remember { BadgeRepository() }

    val badges = remember(xp, streak) {
        badgeRepository.getBadges(
            xp = xp,
            streak = streak
        )
    }

    val unlockedBadges = badges.filter { it.unlocked }
    val lockedBadgesCount = badges.count { !it.unlocked }

    val previewBadges = buildList {
        addAll(unlockedBadges.takeLast(3))

        if (size < 4) {
            addAll(
                badges
                    .filter { !it.unlocked }
                    .take(4 - size)
            )
        }
    }

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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.profile_achievements_title),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Text(
                    text = "${unlockedBadges.size}/${badges.size}",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = stringResource(
                    R.string.profile_achievements_real_description,
                    unlockedBadges.size,
                    lockedBadgesCount
                ),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.78f)
            )

            Spacer(modifier = Modifier.height(18.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                previewBadges.forEach { badge ->
                    AchievementIconPreview(
                        emoji = if (badge.unlocked) getProfileBadgeIcon(badge.id) else "🔒",
                        label = badge.name.take(8),
                        unlocked = badge.unlocked
                    )
                }
            }
        }
    }
}

@Composable
private fun AchievementIconPreview(
    emoji: String,
    label: String,
    unlocked: Boolean
) {
    val alpha = if (unlocked) 1f else 0.55f

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(52.dp)
                .clip(CircleShape)
                .background(
                    if (unlocked) {
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.22f)
                    } else {
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.10f)
                    }
                ),
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
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.88f * alpha)
        )
    }
}

private fun getProfileBadgeIcon(badgeId: String): String {
    return when {
        badgeId.startsWith("xp_") -> "⚡"
        badgeId.startsWith("level_") -> "🎓"
        badgeId.startsWith("streak_") -> "🔥"
        badgeId.startsWith("coins_") -> "🪙"
        badgeId == "first_steps" -> "🚀"
        badgeId == "warm_up" -> "🔥"
        badgeId == "rookie_dev" -> "💻"
        badgeId == "junior_dev" -> "🧠"
        badgeId == "daily_player" -> "📅"
        badgeId == "week_warrior" -> "🛡️"
        badgeId == "month_grinder" -> "🏋️"
        badgeId == "xp_machine" -> "⚙️"
        badgeId == "serious_student" -> "📚"
        badgeId == "discipline_mode" -> "🎯"
        badgeId == "legend" -> "👑"
        else -> "🏅"
    }
}