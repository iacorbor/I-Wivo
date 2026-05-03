package com.icb.iwivo.data.repository

import android.content.Context
import java.time.LocalDate


class UserRepository(context: Context) {

    private val prefs = context.getSharedPreferences("iwivo_user", Context.MODE_PRIVATE)

    fun getXp(): Int {
        return prefs.getInt("xp", 0)
    }

    fun saveXp(xp: Int) {
        prefs.edit().putInt("xp", xp).apply()
    }

    fun addXp(amount: Int) {
        val currentXp = getXp()
        saveXp(currentXp + amount)
    }

    fun getLevel(): Int {
        val xp = getXp()
        return (xp / 500) + 1
    }

    fun getStreak(): Int {
        return prefs.getInt("streak", 0)
    }

    fun updateStreak() {
        val today = LocalDate.now()
        val lastActivity = prefs.getString("last_activity_date", null)

        val newStreak = when {
            lastActivity == null -> 1
            LocalDate.parse(lastActivity) == today -> getStreak()
            LocalDate.parse(lastActivity).plusDays(1) == today -> getStreak() + 1
            else -> 1
        }

        prefs.edit()
            .putInt("streak", newStreak)
            .putString("last_activity_date", today.toString())
            .apply()
    }
    fun getUnlockedBadgesCount(): Int {
        val badgeRepository = BadgeRepository()

        return badgeRepository.getBadges(
            xp = getXp(),
            streak = getStreak()
        ).count { it.unlocked }
    }

    fun getCoins(): Int {
        return prefs.getInt("coins", 0)
    }

    fun addCoins(amount: Int) {
        val currentCoins = getCoins()
        prefs.edit().putInt("coins", currentCoins + amount).apply()
    }

    fun spendCoins(amount: Int): Boolean {
        val currentCoins = getCoins()

        return if (currentCoins >= amount) {
            prefs.edit().putInt("coins", currentCoins - amount).apply()
            true
        } else {
            false
        }
    }

    fun isItemUnlocked(itemId: String): Boolean {
        return prefs.getBoolean("shop_item_$itemId", false)
    }

    fun unlockItem(itemId: String) {
        prefs.edit().putBoolean("shop_item_$itemId", true).apply()
    }

    fun getShopItems(): List<com.icb.iwivo.data.model.ShopItem> {
        return listOf(
            com.icb.iwivo.data.model.ShopItem(
                id = "purple_border",
                name = "Borde Neón Morado",
                description = "Desbloquea un borde morado para tus tarjetas.",
                price = 100,
                unlocked = isItemUnlocked("purple_border")
            ),
            com.icb.iwivo.data.model.ShopItem(
                id = "blue_border",
                name = "Borde Azul Eléctrico",
                description = "Desbloquea un borde azul para tu perfil.",
                price = 150,
                unlocked = isItemUnlocked("blue_border")
            ),
            com.icb.iwivo.data.model.ShopItem(
                id = "green_glow",
                name = "Brillo Verde Wivo",
                description = "Un efecto verde para resaltar tus logros.",
                price = 200,
                unlocked = isItemUnlocked("green_glow")
            )
        )
    }

    fun buyItem(itemId: String): Boolean {
        val item = getShopItems().firstOrNull { it.id == itemId } ?: return false

        if (item.unlocked) return false

        val purchased = spendCoins(item.price)

        if (purchased) {
            unlockItem(itemId)
        }

        return purchased
    }
    fun hasItem(itemId: String): Boolean {
        return isItemUnlocked(itemId)
    }
}