package com.icb.iwivo.data.model

import androidx.annotation.StringRes

data class ShopItem(
    val id: String,
    @StringRes val nameRes: Int,
    @StringRes val descriptionRes: Int,
    val price: Int,
    val previewType: String,
    val category: ShopItemCategory,
    val requiredLevel: Int = 1,
    val unlocked: Boolean = false,
    val equipped: Boolean = false
)

enum class ShopItemCategory {
    AVATAR_BORDER,
    BACKGROUND,
    BUTTON_STYLE,
    THEME,
    EFFECT
}