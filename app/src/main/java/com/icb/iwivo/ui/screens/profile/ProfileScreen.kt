package com.icb.iwivo.ui.screens.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.google.firebase.auth.FirebaseAuth
import com.icb.iwivo.R
import com.icb.iwivo.data.model.ShopItemCategory
import com.icb.iwivo.data.model.UserProfile
import com.icb.iwivo.data.repository.AuthRepository
import com.icb.iwivo.data.repository.BadgeRepository
import com.icb.iwivo.data.repository.FirestoreRepository
import com.icb.iwivo.ui.components.WivoButton
import com.icb.iwivo.ui.components.WivoCard
import com.icb.iwivo.ui.components.WivoScreen
import com.icb.iwivo.ui.theme.GreenAccent
import com.icb.iwivo.ui.theme.PurplePrimary
import com.icb.iwivo.utils.base64ToBitmap

@Composable
fun ProfileScreen(
    authRepository: AuthRepository,
    firestoreRepository: FirestoreRepository,
    onEditProfileClick: () -> Unit,
    onAchievementsClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onLogout: () -> Unit
) {
    val currentUser = FirebaseAuth.getInstance().currentUser
    val lifecycleOwner = LocalLifecycleOwner.current

    val noUsernameText = stringResource(R.string.profile_no_username)
    val noEmailText = stringResource(R.string.profile_no_email)
    val notAuthenticatedText = stringResource(R.string.profile_not_authenticated)

    var profile by remember { mutableStateOf<UserProfile?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }

    var xp by remember { mutableIntStateOf(0) }
    var coins by remember { mutableIntStateOf(0) }
    var streak by remember { mutableIntStateOf(0) }

    var equippedAvatarBorderId by remember { mutableStateOf<String?>(null) }
    var equippedBackgroundId by remember { mutableStateOf<String?>(null) }
    var equippedButtonStyleId by remember { mutableStateOf<String?>(null) }
    var equippedThemeId by remember { mutableStateOf<String?>(null) }
    var equippedEffectId by remember { mutableStateOf<String?>(null) }

    fun loadProfileData() {
        val uid = currentUser?.uid

        if (uid == null) {
            error = notAuthenticatedText
            isLoading = false
            return
        }

        firestoreRepository.getCurrentUserData(
            onResult = { remoteXp, remoteCoins, remoteStreak ->
                xp = remoteXp
                coins = remoteCoins
                streak = remoteStreak
            }
        )

        firestoreRepository.getShopStateByCategory(
            onResult = { _, _, equippedItemsByCategory ->
                equippedAvatarBorderId = equippedItemsByCategory[ShopItemCategory.AVATAR_BORDER]
                equippedBackgroundId = equippedItemsByCategory[ShopItemCategory.BACKGROUND]
                equippedButtonStyleId = equippedItemsByCategory[ShopItemCategory.BUTTON_STYLE]
                equippedThemeId = equippedItemsByCategory[ShopItemCategory.THEME]
                equippedEffectId = equippedItemsByCategory[ShopItemCategory.EFFECT]
            },
            onError = {
                // No bloqueamos el perfil si falla la tienda.
            }
        )

        firestoreRepository.getUserProfile(
            uid = uid,
            onSuccess = { userProfile ->
                profile = userProfile
                error = null
                isLoading = false
            },
            onError = { errorMessage ->
                error = errorMessage
                isLoading = false
            }
        )
    }

    LaunchedEffect(currentUser?.uid) {
        loadProfileData()
    }

    DisposableEffect(lifecycleOwner, currentUser?.uid) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                loadProfileData()
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    WivoScreen(
        backgroundItemId = equippedBackgroundId,
        themeItemId = equippedThemeId
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
        ) {
            SettingsButton(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 4.dp, end = 16.dp),
                onClick = onSettingsClick
            )

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

                    val level = (xp / 500) + 1
                    val currentLevelXp = xp % 500
                    val levelProgress = currentLevelXp / 500f

                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        HeaderBlock(
                            username = username,
                            email = email,
                            avatarBase64 = userProfile.avatarBase64,
                            equippedAvatarBorderId = equippedAvatarBorderId
                        )

                        MiddleBlock(
                            xp = xp,
                            coins = coins,
                            streak = streak,
                            level = level,
                            currentLevelXp = currentLevelXp,
                            levelProgress = levelProgress,
                            themeItemId = equippedThemeId,
                            effectItemId = equippedEffectId,
                            buttonStyleItemId = equippedButtonStyleId,
                            onEditProfileClick = onEditProfileClick,
                            onAchievementsClick = onAchievementsClick
                        )

                        OutlinedButton(
                            onClick = {
                                authRepository.logout()
                                onLogout()
                            },
                            modifier = Modifier
                                .fillMaxWidth(0.72f)
                                .height(48.dp),
                            shape = RoundedCornerShape(18.dp)
                        ) {
                            Text(
                                text = stringResource(R.string.profile_logout_button),
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun HeaderBlock(
    username: String,
    email: String,
    avatarBase64: String,
    equippedAvatarBorderId: String?
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 18.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.profile_title),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(18.dp))

        ProfileAvatar(
            username = username,
            avatarBase64 = avatarBase64,
            equippedAvatarBorderId = equippedAvatarBorderId
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = username,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = email,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.72f)
        )
    }
}

@Composable
private fun MiddleBlock(
    xp: Int,
    coins: Int,
    streak: Int,
    level: Int,
    currentLevelXp: Int,
    levelProgress: Float,
    themeItemId: String?,
    effectItemId: String?,
    buttonStyleItemId: String?,
    onEditProfileClick: () -> Unit,
    onAchievementsClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(0.78f)
        ) {
            WivoButton(
                text = stringResource(R.string.profile_edit_button),
                onClick = onEditProfileClick,
                buttonStyleItemId = buttonStyleItemId,
                themeItemId = themeItemId
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        AchievementsPreviewCard(
            xp = xp,
            coins = coins,
            streak = streak,
            themeItemId = themeItemId,
            effectItemId = effectItemId,
            onClick = onAchievementsClick
        )

        Spacer(modifier = Modifier.height(12.dp))

        ProfileProgressCard(
            level = level,
            xp = xp,
            coins = coins,
            streak = streak,
            currentLevelXp = currentLevelXp,
            levelProgress = levelProgress,
            themeItemId = themeItemId,
            effectItemId = effectItemId
        )
    }
}

@Composable
private fun SettingsButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .size(34.dp)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "⚙️",
            style = MaterialTheme.typography.titleLarge
        )
    }
}

@Composable
private fun ProfileAvatar(
    username: String,
    avatarBase64: String,
    equippedAvatarBorderId: String?
) {
    val fallbackInitial = username.ifBlank { "U" }.first().uppercase()
    val avatarBitmap = base64ToBitmap(avatarBase64)

    val outerBrush = getAvatarDecorationBrush(equippedAvatarBorderId)
    val borderPadding = getAvatarBorderPadding(equippedAvatarBorderId)

    Box(
        modifier = Modifier
            .size(108.dp)
            .clip(CircleShape)
            .background(outerBrush)
            .padding(borderPadding),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surface),
            contentAlignment = Alignment.Center
        ) {
            if (avatarBitmap != null) {
                Image(
                    bitmap = avatarBitmap.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape),
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
private fun getAvatarDecorationBrush(
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

private fun getAvatarBorderPadding(
    equippedAvatarBorderId: String?
): Dp {
    return when (equippedAvatarBorderId) {
        "green_glow" -> 5.dp
        "purple_border" -> 4.dp
        "blue_border" -> 4.dp
        "cyber_pink_border" -> 4.dp
        "gold_rank_border" -> 4.dp
        "ice_blue_border" -> 4.dp
        "fire_orange_border" -> 4.dp
        "rainbow_dev_border" -> 5.dp
        else -> 3.dp
    }
}

@Composable
private fun AchievementsPreviewCard(
    xp: Int,
    coins: Int,
    streak: Int,
    themeItemId: String?,
    effectItemId: String?,
    onClick: () -> Unit
) {
    val badgeRepository = remember { BadgeRepository() }

    val badges = remember(xp, coins, streak) {
        badgeRepository.getBadges(
            xp = xp,
            coins = coins,
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

    WivoCard(
        modifier = Modifier.clickable { onClick() },
        themeItemId = themeItemId,
        effectItemId = effectItemId
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

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = stringResource(
                R.string.profile_achievements_real_description,
                unlockedBadges.size,
                lockedBadgesCount
            ),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.78f)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            previewBadges.forEach { badge ->
                AchievementIconPreview(
                    modifier = Modifier.weight(1f),
                    emoji = if (badge.unlocked) badge.icon else "🔒",
                    label = badge.name.take(8),
                    unlocked = badge.unlocked
                )
            }
        }
    }
}

@Composable
private fun ProfileProgressCard(
    level: Int,
    xp: Int,
    coins: Int,
    streak: Int,
    currentLevelXp: Int,
    levelProgress: Float,
    themeItemId: String?,
    effectItemId: String?
) {
    WivoCard(
        themeItemId = themeItemId,
        effectItemId = effectItemId
    ) {
        Text(
            text = stringResource(R.string.profile_progress_title),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            ProfileProgressStat(
                modifier = Modifier.weight(1f),
                value = level.toString(),
                label = stringResource(R.string.profile_progress_level)
            )

            ProfileProgressStat(
                modifier = Modifier.weight(1f),
                value = xp.toString(),
                label = stringResource(R.string.profile_progress_xp)
            )

            ProfileProgressStat(
                modifier = Modifier.weight(1f),
                value = coins.toString(),
                label = stringResource(R.string.profile_progress_coins)
            )

            ProfileProgressStat(
                modifier = Modifier.weight(1f),
                value = streak.toString(),
                label = stringResource(R.string.profile_progress_streak)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = stringResource(
                R.string.profile_progress_next_level,
                currentLevelXp,
                500
            ),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.78f)
        )

        Spacer(modifier = Modifier.height(6.dp))

        LinearProgressIndicator(
            progress = { levelProgress.coerceIn(0f, 1f) },
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f)
        )
    }
}

@Composable
private fun ProfileProgressStat(
    modifier: Modifier = Modifier,
    value: String,
    label: String
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(2.dp))

        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.72f)
        )
    }
}

@Composable
private fun AchievementIconPreview(
    modifier: Modifier = Modifier,
    emoji: String,
    label: String,
    unlocked: Boolean
) {
    val alpha = if (unlocked) 1f else 0.55f

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
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
                style = MaterialTheme.typography.titleMedium
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.88f * alpha)
        )
    }
}