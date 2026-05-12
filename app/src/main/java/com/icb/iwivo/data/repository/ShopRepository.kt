package com.icb.iwivo.data.repository

import com.icb.iwivo.R
import com.icb.iwivo.data.model.ShopItem
import com.icb.iwivo.data.model.ShopItemCategory

class ShopRepository {

    fun getBaseShopItems(): List<ShopItem> {
        return listOf(

            // =========================
            // AVATAR BORDER
            // =========================

            ShopItem(
                id = "purple_border",
                nameRes = R.string.shop_item_purple_border_name,
                descriptionRes = R.string.shop_item_purple_border_description,
                price = 100,
                previewType = "purple_border",
                category = ShopItemCategory.AVATAR_BORDER,
                requiredLevel = 1
            ),
            ShopItem(
                id = "blue_border",
                nameRes = R.string.shop_item_blue_border_name,
                descriptionRes = R.string.shop_item_blue_border_description,
                price = 150,
                previewType = "blue_border",
                category = ShopItemCategory.AVATAR_BORDER,
                requiredLevel = 1
            ),
            ShopItem(
                id = "green_glow",
                nameRes = R.string.shop_item_green_glow_name,
                descriptionRes = R.string.shop_item_green_glow_description,
                price = 200,
                previewType = "green_glow",
                category = ShopItemCategory.AVATAR_BORDER,
                requiredLevel = 1
            ),
            ShopItem(
                id = "cyber_pink_border",
                nameRes = R.string.shop_item_cyber_pink_border_name,
                descriptionRes = R.string.shop_item_cyber_pink_border_description,
                price = 220,
                previewType = "cyber_pink_border",
                category = ShopItemCategory.AVATAR_BORDER,
                requiredLevel = 2
            ),
            ShopItem(
                id = "gold_rank_border",
                nameRes = R.string.shop_item_gold_rank_border_name,
                descriptionRes = R.string.shop_item_gold_rank_border_description,
                price = 300,
                previewType = "gold_rank_border",
                category = ShopItemCategory.AVATAR_BORDER,
                requiredLevel = 3
            ),
            ShopItem(
                id = "ice_blue_border",
                nameRes = R.string.shop_item_ice_blue_border_name,
                descriptionRes = R.string.shop_item_ice_blue_border_description,
                price = 260,
                previewType = "ice_blue_border",
                category = ShopItemCategory.AVATAR_BORDER,
                requiredLevel = 3
            ),
            ShopItem(
                id = "fire_orange_border",
                nameRes = R.string.shop_item_fire_orange_border_name,
                descriptionRes = R.string.shop_item_fire_orange_border_description,
                price = 280,
                previewType = "fire_orange_border",
                category = ShopItemCategory.AVATAR_BORDER,
                requiredLevel = 4
            ),
            ShopItem(
                id = "rainbow_dev_border",
                nameRes = R.string.shop_item_rainbow_dev_border_name,
                descriptionRes = R.string.shop_item_rainbow_dev_border_description,
                price = 450,
                previewType = "rainbow_dev_border",
                category = ShopItemCategory.AVATAR_BORDER,
                requiredLevel = 5
            ),

            // =========================
            // BACKGROUNDS
            // =========================

            ShopItem(
                id = "background_deep_space",
                nameRes = R.string.shop_item_background_deep_space_name,
                descriptionRes = R.string.shop_item_background_deep_space_description,
                price = 180,
                previewType = "background_deep_space",
                category = ShopItemCategory.BACKGROUND,
                requiredLevel = 1
            ),
            ShopItem(
                id = "background_neon_grid",
                nameRes = R.string.shop_item_background_neon_grid_name,
                descriptionRes = R.string.shop_item_background_neon_grid_description,
                price = 220,
                previewType = "background_neon_grid",
                category = ShopItemCategory.BACKGROUND,
                requiredLevel = 2
            ),
            ShopItem(
                id = "background_midnight_code",
                nameRes = R.string.shop_item_background_midnight_code_name,
                descriptionRes = R.string.shop_item_background_midnight_code_description,
                price = 240,
                previewType = "background_midnight_code",
                category = ShopItemCategory.BACKGROUND,
                requiredLevel = 2
            ),
            ShopItem(
                id = "background_matrix_flow",
                nameRes = R.string.shop_item_background_matrix_flow_name,
                descriptionRes = R.string.shop_item_background_matrix_flow_description,
                price = 280,
                previewType = "background_matrix_flow",
                category = ShopItemCategory.BACKGROUND,
                requiredLevel = 3
            ),
            ShopItem(
                id = "background_blue_horizon",
                nameRes = R.string.shop_item_background_blue_horizon_name,
                descriptionRes = R.string.shop_item_background_blue_horizon_description,
                price = 300,
                previewType = "background_blue_horizon",
                category = ShopItemCategory.BACKGROUND,
                requiredLevel = 3
            ),
            ShopItem(
                id = "background_purple_void",
                nameRes = R.string.shop_item_background_purple_void_name,
                descriptionRes = R.string.shop_item_background_purple_void_description,
                price = 340,
                previewType = "background_purple_void",
                category = ShopItemCategory.BACKGROUND,
                requiredLevel = 4
            ),
            ShopItem(
                id = "background_green_terminal",
                nameRes = R.string.shop_item_background_green_terminal_name,
                descriptionRes = R.string.shop_item_background_green_terminal_description,
                price = 360,
                previewType = "background_green_terminal",
                category = ShopItemCategory.BACKGROUND,
                requiredLevel = 4
            ),
            ShopItem(
                id = "background_pro_black",
                nameRes = R.string.shop_item_background_pro_black_name,
                descriptionRes = R.string.shop_item_background_pro_black_description,
                price = 400,
                previewType = "background_pro_black",
                category = ShopItemCategory.BACKGROUND,
                requiredLevel = 5
            ),

            // =========================
            // BUTTON STYLES
            // =========================

            ShopItem(
                id = "button_green_pulse",
                nameRes = R.string.shop_item_button_green_pulse_name,
                descriptionRes = R.string.shop_item_button_green_pulse_description,
                price = 160,
                previewType = "button_green_pulse",
                category = ShopItemCategory.BUTTON_STYLE,
                requiredLevel = 1
            ),
            ShopItem(
                id = "button_purple_core",
                nameRes = R.string.shop_item_button_purple_core_name,
                descriptionRes = R.string.shop_item_button_purple_core_description,
                price = 160,
                previewType = "button_purple_core",
                category = ShopItemCategory.BUTTON_STYLE,
                requiredLevel = 1
            ),
            ShopItem(
                id = "button_blue_flash",
                nameRes = R.string.shop_item_button_blue_flash_name,
                descriptionRes = R.string.shop_item_button_blue_flash_description,
                price = 190,
                previewType = "button_blue_flash",
                category = ShopItemCategory.BUTTON_STYLE,
                requiredLevel = 2
            ),
            ShopItem(
                id = "button_neon_outline",
                nameRes = R.string.shop_item_button_neon_outline_name,
                descriptionRes = R.string.shop_item_button_neon_outline_description,
                price = 220,
                previewType = "button_neon_outline",
                category = ShopItemCategory.BUTTON_STYLE,
                requiredLevel = 2
            ),
            ShopItem(
                id = "button_terminal_green",
                nameRes = R.string.shop_item_button_terminal_green_name,
                descriptionRes = R.string.shop_item_button_terminal_green_description,
                price = 260,
                previewType = "button_terminal_green",
                category = ShopItemCategory.BUTTON_STYLE,
                requiredLevel = 3
            ),
            ShopItem(
                id = "button_gold_focus",
                nameRes = R.string.shop_item_button_gold_focus_name,
                descriptionRes = R.string.shop_item_button_gold_focus_description,
                price = 320,
                previewType = "button_gold_focus",
                category = ShopItemCategory.BUTTON_STYLE,
                requiredLevel = 4
            ),

            // =========================
            // THEMES
            // =========================

            ShopItem(
                id = "theme_blue_terminal",
                nameRes = R.string.shop_item_theme_blue_terminal_name,
                descriptionRes = R.string.shop_item_theme_blue_terminal_description,
                price = 300,
                previewType = "theme_blue_terminal",
                category = ShopItemCategory.THEME,
                requiredLevel = 3
            ),
            ShopItem(
                id = "theme_matrix_wivo",
                nameRes = R.string.shop_item_theme_matrix_wivo_name,
                descriptionRes = R.string.shop_item_theme_matrix_wivo_description,
                price = 350,
                previewType = "theme_matrix_wivo",
                category = ShopItemCategory.THEME,
                requiredLevel = 4
            ),
            ShopItem(
                id = "theme_purple_neon",
                nameRes = R.string.shop_item_theme_purple_neon_name,
                descriptionRes = R.string.shop_item_theme_purple_neon_description,
                price = 380,
                previewType = "theme_purple_neon",
                category = ShopItemCategory.THEME,
                requiredLevel = 4
            ),
            ShopItem(
                id = "theme_cyber_academy",
                nameRes = R.string.shop_item_theme_cyber_academy_name,
                descriptionRes = R.string.shop_item_theme_cyber_academy_description,
                price = 420,
                previewType = "theme_cyber_academy",
                category = ShopItemCategory.THEME,
                requiredLevel = 5
            ),
            ShopItem(
                id = "theme_dark_minimal",
                nameRes = R.string.shop_item_theme_dark_minimal_name,
                descriptionRes = R.string.shop_item_theme_dark_minimal_description,
                price = 450,
                previewType = "theme_dark_minimal",
                category = ShopItemCategory.THEME,
                requiredLevel = 5
            ),
            ShopItem(
                id = "theme_full_stack",
                nameRes = R.string.shop_item_theme_full_stack_name,
                descriptionRes = R.string.shop_item_theme_full_stack_description,
                price = 600,
                previewType = "theme_full_stack",
                category = ShopItemCategory.THEME,
                requiredLevel = 7
            ),

            // =========================
            // EFFECTS
            // =========================

            ShopItem(
                id = "effect_soft_glow",
                nameRes = R.string.shop_item_effect_soft_glow_name,
                descriptionRes = R.string.shop_item_effect_soft_glow_description,
                price = 180,
                previewType = "effect_soft_glow",
                category = ShopItemCategory.EFFECT,
                requiredLevel = 2
            ),
            ShopItem(
                id = "effect_xp_spark",
                nameRes = R.string.shop_item_effect_xp_spark_name,
                descriptionRes = R.string.shop_item_effect_xp_spark_description,
                price = 240,
                previewType = "effect_xp_spark",
                category = ShopItemCategory.EFFECT,
                requiredLevel = 3
            ),
            ShopItem(
                id = "effect_coin_flash",
                nameRes = R.string.shop_item_effect_coin_flash_name,
                descriptionRes = R.string.shop_item_effect_coin_flash_description,
                price = 260,
                previewType = "effect_coin_flash",
                category = ShopItemCategory.EFFECT,
                requiredLevel = 3
            ),
            ShopItem(
                id = "effect_streak_fire",
                nameRes = R.string.shop_item_effect_streak_fire_name,
                descriptionRes = R.string.shop_item_effect_streak_fire_description,
                price = 320,
                previewType = "effect_streak_fire",
                category = ShopItemCategory.EFFECT,
                requiredLevel = 4
            ),
            ShopItem(
                id = "effect_code_scan",
                nameRes = R.string.shop_item_effect_code_scan_name,
                descriptionRes = R.string.shop_item_effect_code_scan_description,
                price = 380,
                previewType = "effect_code_scan",
                category = ShopItemCategory.EFFECT,
                requiredLevel = 5
            )
        )
    }
}