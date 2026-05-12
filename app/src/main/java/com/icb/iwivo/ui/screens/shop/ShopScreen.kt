package com.icb.iwivo.ui.screens.shop

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.icb.iwivo.R
import com.icb.iwivo.data.model.ShopItem
import com.icb.iwivo.data.model.ShopItemCategory
import com.icb.iwivo.data.repository.FirestoreRepository
import com.icb.iwivo.data.repository.ShopRepository
import com.icb.iwivo.ui.components.WivoButton
import com.icb.iwivo.ui.components.WivoCard
import com.icb.iwivo.ui.components.WivoScreen
import com.icb.iwivo.ui.theme.CardDark
import com.icb.iwivo.ui.theme.GreenAccent
import com.icb.iwivo.ui.theme.PurplePrimary
import com.icb.iwivo.ui.theme.TextSecondary

@SuppressLint("LocalContextGetResourceValueCall")
@Composable
fun ShopScreen(
    firestoreRepository: FirestoreRepository
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var coins by remember { mutableIntStateOf(0) }
    var xp by remember { mutableIntStateOf(0) }
    var streak by remember { mutableIntStateOf(0) }

    var unlockedIds by remember { mutableStateOf<List<String>>(emptyList()) }

    var equippedItemsByCategory by remember {
        mutableStateOf<Map<ShopItemCategory, String?>>(emptyMap())
    }

    var selectedCategory by remember {
        mutableStateOf<ShopItemCategory?>(null)
    }

    var isLoading by remember { mutableStateOf(true) }
    var message by remember { mutableStateOf<String?>(null) }

    val shopRepository = remember { ShopRepository() }
    val baseItems = remember { shopRepository.getBaseShopItems() }

    val level = (xp / 500) + 1

    fun loadShop() {
        isLoading = true

        firestoreRepository.getCurrentUserData(
            onResult = { remoteXp, remoteCoins, remoteStreak ->
                xp = remoteXp
                coins = remoteCoins
                streak = remoteStreak
            },
            onError = {
                // No bloqueo la tienda si falla el progreso.
            }
        )

        firestoreRepository.getShopStateByCategory(
            onResult = { remoteCoins, remoteUnlockedIds, remoteEquippedItemsByCategory ->
                coins = remoteCoins
                unlockedIds = remoteUnlockedIds
                equippedItemsByCategory = remoteEquippedItemsByCategory
                isLoading = false
            },
            onError = { errorMessage ->
                message = errorMessage
                isLoading = false
            }
        )
    }

    LaunchedEffect(Unit) {
        loadShop()
    }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                loadShop()
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    val shopItems = baseItems
        .filter { item ->
            selectedCategory == null || item.category == selectedCategory
        }
        .map { item ->
            item.copy(
                unlocked = item.id in unlockedIds,
                equipped = equippedItemsByCategory[item.category] == item.id
            )
        }

    val equippedBackgroundId = equippedItemsByCategory[ShopItemCategory.BACKGROUND]
    val equippedButtonStyleId = equippedItemsByCategory[ShopItemCategory.BUTTON_STYLE]
    val equippedThemeId = equippedItemsByCategory[ShopItemCategory.THEME]
    val equippedEffectId = equippedItemsByCategory[ShopItemCategory.EFFECT]

    WivoScreen(
        backgroundItemId = equippedBackgroundId,
        themeItemId = equippedThemeId
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = stringResource(R.string.shop_title),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = stringResource(R.string.shop_subtitle),
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(20.dp))

            ShopCoinsCard(
                coins = coins,
                level = level,
                themeItemId = equippedThemeId,
                effectItemId = equippedEffectId
            )

            message?.let { currentMessage ->
                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = currentMessage,
                    color = TextSecondary
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            ShopCategorySelector(
                selectedCategory = selectedCategory,
                onCategorySelected = { category ->
                    selectedCategory = category
                }
            )

            Spacer(modifier = Modifier.height(20.dp))

            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                shopItems.forEach { item ->
                    ShopItemCard(
                        item = item,
                        userLevel = level,
                        themeItemId = equippedThemeId,
                        effectItemId = equippedEffectId,
                        buttonStyleItemId = equippedButtonStyleId,
                        onBuyClick = {
                            firestoreRepository.buyShopItem(
                                item = item,
                                onSuccess = {
                                    message = context.getString(
                                        R.string.shop_message_unlocked,
                                        context.getString(item.nameRes)
                                    )
                                    loadShop()
                                },
                                onError = { errorMessage ->
                                    message = errorMessage
                                }
                            )
                        },
                        onEquipClick = {
                            firestoreRepository.equipShopItemByCategory(
                                item = item,
                                onSuccess = {
                                    message = context.getString(
                                        R.string.shop_message_equipped,
                                        context.getString(item.nameRes)
                                    )
                                    loadShop()
                                },
                                onError = { errorMessage ->
                                    message = errorMessage
                                }
                            )
                        },
                        onUnequipClick = {
                            firestoreRepository.unequipShopItemByCategory(
                                category = item.category,
                                onSuccess = {
                                    message = context.getString(
                                        R.string.shop_message_unequipped
                                    )
                                    loadShop()
                                },
                                onError = { errorMessage ->
                                    message = errorMessage
                                }
                            )
                        }
                    )

                    Spacer(modifier = Modifier.height(14.dp))
                }

                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@Composable
private fun ShopCoinsCard(
    coins: Int,
    level: Int,
    themeItemId: String?,
    effectItemId: String?
) {
    WivoCard(
        themeItemId = themeItemId,
        effectItemId = effectItemId
    ) {
        Text(
            text = stringResource(R.string.wivo_coins),
            color = TextSecondary
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = stringResource(R.string.coins_amount, coins),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = GreenAccent
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = stringResource(R.string.shop_user_level, level),
            color = PurplePrimary,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun ShopCategorySelector(
    selectedCategory: ShopItemCategory?,
    onCategorySelected: (ShopItemCategory?) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ShopCategoryChip(
            text = stringResource(R.string.shop_category_all),
            selected = selectedCategory == null,
            onClick = {
                onCategorySelected(null)
            }
        )

        ShopItemCategory.entries.forEach { category ->
            ShopCategoryChip(
                text = getCategoryText(category),
                selected = selectedCategory == category,
                onClick = {
                    onCategorySelected(category)
                }
            )
        }
    }
}

@Composable
private fun ShopCategoryChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = {
            Text(
                text = text,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
            )
        },
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = PurplePrimary.copy(alpha = 0.28f),
            selectedLabelColor = MaterialTheme.colorScheme.onSurface,
            labelColor = TextSecondary
        )
    )
}

@Composable
private fun ShopItemCard(
    item: ShopItem,
    userLevel: Int,
    themeItemId: String?,
    effectItemId: String?,
    buttonStyleItemId: String?,
    onBuyClick: () -> Unit,
    onEquipClick: () -> Unit,
    onUnequipClick: () -> Unit
) {
    val levelLocked = userLevel < item.requiredLevel

    WivoCard(
        themeItemId = themeItemId,
        effectItemId = if (item.equipped) effectItemId else null
    ) {
        ShopPreview(item = item)

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = stringResource(item.nameRes),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = when {
                        item.equipped -> GreenAccent
                        item.unlocked -> GreenAccent
                        levelLocked -> TextSecondary
                        else -> MaterialTheme.colorScheme.onSurface
                    }
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = stringResource(item.descriptionRes),
                    color = TextSecondary,
                    style = MaterialTheme.typography.bodySmall
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = getCategoryText(item.category),
                    color = PurplePrimary,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Text(
                text = when {
                    item.equipped -> stringResource(R.string.shop_status_equipped)
                    item.unlocked -> stringResource(R.string.shop_status_unlocked)
                    levelLocked -> stringResource(
                        R.string.shop_required_level_short,
                        item.requiredLevel
                    )
                    else -> stringResource(R.string.shop_price, item.price)
                },
                color = when {
                    item.equipped -> GreenAccent
                    item.unlocked -> GreenAccent
                    levelLocked -> TextSecondary
                    else -> PurplePrimary
                },
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        when {
            item.equipped -> {
                WivoButton(
                    text = stringResource(R.string.shop_unequip),
                    onClick = onUnequipClick,
                    buttonStyleItemId = buttonStyleItemId,
                    themeItemId = themeItemId
                )
            }

            item.unlocked -> {
                WivoButton(
                    text = stringResource(R.string.shop_equip),
                    onClick = onEquipClick,
                    buttonStyleItemId = buttonStyleItemId,
                    themeItemId = themeItemId
                )
            }

            levelLocked -> {
                OutlinedButton(
                    onClick = {},
                    enabled = false,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(
                            R.string.shop_required_level,
                            item.requiredLevel
                        )
                    )
                }
            }

            else -> {
                WivoButton(
                    text = stringResource(R.string.buy_for_coins, item.price),
                    onClick = onBuyClick,
                    buttonStyleItemId = buttonStyleItemId,
                    themeItemId = themeItemId
                )
            }
        }
    }
}

@Composable
private fun ShopPreview(
    item: ShopItem
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.36f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            PreviewIcon(previewType = item.previewType)

            Spacer(modifier = Modifier.size(16.dp))

            Column {
                Text(
                    text = stringResource(R.string.shop_preview_label),
                    style = MaterialTheme.typography.labelMedium,
                    color = TextSecondary
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = getPreviewText(item.previewType),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Composable
private fun PreviewIcon(
    previewType: String
) {
    val borderBrush = getPreviewBrush(previewType)

    Box(
        modifier = Modifier
            .size(74.dp)
            .clip(CircleShape)
            .background(borderBrush)
            .padding(4.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = getPreviewInitials(previewType),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Black,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

@Composable
private fun getPreviewBrush(previewType: String): Brush {
    return when (previewType) {
        "purple_border",
        "button_purple_core",
        "theme_purple_neon",
        "background_purple_void" -> Brush.linearGradient(
            listOf(
                PurplePrimary,
                MaterialTheme.colorScheme.primary
            )
        )

        "blue_border",
        "button_blue_flash",
        "theme_blue_terminal",
        "background_blue_horizon" -> Brush.linearGradient(
            listOf(
                MaterialTheme.colorScheme.secondary,
                MaterialTheme.colorScheme.primary
            )
        )

        "ice_blue_border" -> Brush.linearGradient(
            listOf(
                androidx.compose.ui.graphics.Color(0xFF80D8FF),
                androidx.compose.ui.graphics.Color(0xFF2979FF)
            )
        )

        "green_glow",
        "button_green_pulse",
        "button_terminal_green",
        "theme_matrix_wivo",
        "background_matrix_flow",
        "background_green_terminal" -> Brush.linearGradient(
            listOf(
                GreenAccent,
                androidx.compose.ui.graphics.Color(0xFF00C853)
            )
        )

        "cyber_pink_border",
        "background_neon_grid",
        "theme_cyber_academy",
        "button_neon_outline" -> Brush.linearGradient(
            listOf(
                androidx.compose.ui.graphics.Color(0xFFFF2DFF),
                androidx.compose.ui.graphics.Color(0xFF7C4DFF)
            )
        )

        "gold_rank_border",
        "button_gold_focus",
        "effect_coin_flash" -> Brush.linearGradient(
            listOf(
                androidx.compose.ui.graphics.Color(0xFFFFD54F),
                androidx.compose.ui.graphics.Color(0xFFFFA000)
            )
        )

        "fire_orange_border",
        "effect_streak_fire" -> Brush.linearGradient(
            listOf(
                androidx.compose.ui.graphics.Color(0xFFFF7043),
                androidx.compose.ui.graphics.Color(0xFFFF1744)
            )
        )

        "rainbow_dev_border" -> Brush.linearGradient(
            listOf(
                androidx.compose.ui.graphics.Color(0xFFFF2DFF),
                androidx.compose.ui.graphics.Color(0xFF2979FF),
                androidx.compose.ui.graphics.Color(0xFF00E676),
                androidx.compose.ui.graphics.Color(0xFFFFD54F)
            )
        )

        "background_deep_space",
        "background_midnight_code",
        "background_pro_black",
        "theme_dark_minimal",
        "theme_full_stack",
        "effect_code_scan",
        "effect_soft_glow",
        "effect_xp_spark" -> Brush.linearGradient(
            listOf(
                CardDark,
                PurplePrimary
            )
        )

        else -> Brush.linearGradient(
            listOf(
                MaterialTheme.colorScheme.primary,
                MaterialTheme.colorScheme.secondary
            )
        )
    }
}

private fun getPreviewInitials(previewType: String): String {
    return when {
        previewType.contains("background") -> "BG"
        previewType.contains("button") -> "BT"
        previewType.contains("theme") -> "TH"
        previewType.contains("effect") -> "FX"
        else -> "IW"
    }
}

@Composable
private fun getCategoryText(category: ShopItemCategory): String {
    return when (category) {
        ShopItemCategory.AVATAR_BORDER -> stringResource(R.string.shop_category_avatar_border)
        ShopItemCategory.BACKGROUND -> stringResource(R.string.shop_category_background)
        ShopItemCategory.BUTTON_STYLE -> stringResource(R.string.shop_category_button_style)
        ShopItemCategory.THEME -> stringResource(R.string.shop_category_theme)
        ShopItemCategory.EFFECT -> stringResource(R.string.shop_category_effect)
    }
}

@Composable
private fun getPreviewText(previewType: String): String {
    return when (previewType) {
        "purple_border" -> stringResource(R.string.shop_preview_purple_border)
        "blue_border" -> stringResource(R.string.shop_preview_blue_border)
        "green_glow" -> stringResource(R.string.shop_preview_green_glow)
        "cyber_pink_border" -> stringResource(R.string.shop_preview_cyber_pink_border)
        "gold_rank_border" -> stringResource(R.string.shop_preview_gold_rank_border)
        "ice_blue_border" -> stringResource(R.string.shop_preview_ice_blue_border)
        "fire_orange_border" -> stringResource(R.string.shop_preview_fire_orange_border)
        "rainbow_dev_border" -> stringResource(R.string.shop_preview_rainbow_dev_border)

        "background_deep_space" -> stringResource(R.string.shop_preview_background_deep_space)
        "background_neon_grid" -> stringResource(R.string.shop_preview_background_neon_grid)
        "background_midnight_code" -> stringResource(R.string.shop_preview_background_midnight_code)
        "background_matrix_flow" -> stringResource(R.string.shop_preview_background_matrix_flow)
        "background_blue_horizon" -> stringResource(R.string.shop_preview_background_blue_horizon)
        "background_purple_void" -> stringResource(R.string.shop_preview_background_purple_void)
        "background_green_terminal" -> stringResource(R.string.shop_preview_background_green_terminal)
        "background_pro_black" -> stringResource(R.string.shop_preview_background_pro_black)

        "button_green_pulse" -> stringResource(R.string.shop_preview_button_green_pulse)
        "button_purple_core" -> stringResource(R.string.shop_preview_button_purple_core)
        "button_blue_flash" -> stringResource(R.string.shop_preview_button_blue_flash)
        "button_neon_outline" -> stringResource(R.string.shop_preview_button_neon_outline)
        "button_terminal_green" -> stringResource(R.string.shop_preview_button_terminal_green)
        "button_gold_focus" -> stringResource(R.string.shop_preview_button_gold_focus)

        "theme_blue_terminal" -> stringResource(R.string.shop_preview_theme_blue_terminal)
        "theme_matrix_wivo" -> stringResource(R.string.shop_preview_theme_matrix_wivo)
        "theme_purple_neon" -> stringResource(R.string.shop_preview_theme_purple_neon)
        "theme_cyber_academy" -> stringResource(R.string.shop_preview_theme_cyber_academy)
        "theme_dark_minimal" -> stringResource(R.string.shop_preview_theme_dark_minimal)
        "theme_full_stack" -> stringResource(R.string.shop_preview_theme_full_stack)

        "effect_soft_glow" -> stringResource(R.string.shop_preview_effect_soft_glow)
        "effect_xp_spark" -> stringResource(R.string.shop_preview_effect_xp_spark)
        "effect_coin_flash" -> stringResource(R.string.shop_preview_effect_coin_flash)
        "effect_streak_fire" -> stringResource(R.string.shop_preview_effect_streak_fire)
        "effect_code_scan" -> stringResource(R.string.shop_preview_effect_code_scan)

        else -> stringResource(R.string.shop_preview_default)
    }
}