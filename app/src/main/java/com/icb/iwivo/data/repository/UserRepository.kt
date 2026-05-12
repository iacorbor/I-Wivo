package com.icb.iwivo.data.repository

import android.content.Context
import com.icb.iwivo.R
import com.icb.iwivo.data.model.ShopItem
import com.icb.iwivo.data.model.ShopItemCategory
import java.time.LocalDate

class UserRepository(context: Context) {

    private val prefs = context.getSharedPreferences("iwivo_user", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_XP = "xp"
        private const val KEY_COINS = "coins"
        private const val KEY_STREAK = "streak"
        private const val KEY_LAST_ACTIVITY_DATE = "last_activity_date"

        private const val KEY_DAILY_QUESTIONS_COMPLETED = "daily_questions_completed"
        private const val KEY_DAILY_QUESTIONS_DATE = "daily_questions_date"
        private const val KEY_DAILY_MISSION_CLAIMED_DATE = "daily_mission_claimed_date"

        private const val DAILY_MISSION_REQUIRED_QUESTIONS = 10
        private const val DAILY_MISSION_XP_REWARD = 100
        private const val DAILY_MISSION_COINS_REWARD = 25
    }

    fun getXp(): Int {
        return prefs.getInt(KEY_XP, 0)
    }

    fun saveXp(xp: Int) {
        prefs.edit().putInt(KEY_XP, xp).apply()
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
        return prefs.getInt(KEY_STREAK, 0)
    }

    fun updateStreak() {
        val today = LocalDate.now()
        val lastActivity = prefs.getString(KEY_LAST_ACTIVITY_DATE, null)

        val newStreak = when {
            lastActivity == null -> 1
            LocalDate.parse(lastActivity) == today -> getStreak()
            LocalDate.parse(lastActivity).plusDays(1) == today -> getStreak() + 1
            else -> 1
        }

        prefs.edit()
            .putInt(KEY_STREAK, newStreak)
            .putString(KEY_LAST_ACTIVITY_DATE, today.toString())
            .apply()
    }

    fun getCoins(): Int {
        return prefs.getInt(KEY_COINS, 0)
    }

    fun addCoins(amount: Int) {
        val currentCoins = getCoins()
        prefs.edit().putInt(KEY_COINS, currentCoins + amount).apply()
    }

    fun spendCoins(amount: Int): Boolean {
        val currentCoins = getCoins()

        return if (currentCoins >= amount) {
            prefs.edit().putInt(KEY_COINS, currentCoins - amount).apply()
            true
        } else {
            false
        }
    }

    // -----------------------------
    // Daily Mission
    // -----------------------------

    private fun getTodayString(): String {
        return LocalDate.now().toString()
    }

    private fun resetDailyQuestionsIfNeeded() {
        val today = getTodayString()
        val savedDate = prefs.getString(KEY_DAILY_QUESTIONS_DATE, null)

        if (savedDate != today) {
            prefs.edit()
                .putString(KEY_DAILY_QUESTIONS_DATE, today)
                .putInt(KEY_DAILY_QUESTIONS_COMPLETED, 0)
                .apply()
        }
    }

    fun getDailyQuestionsCompleted(): Int {
        resetDailyQuestionsIfNeeded()
        return prefs.getInt(KEY_DAILY_QUESTIONS_COMPLETED, 0)
    }

    fun incrementDailyQuestionsCompleted(amount: Int = 1) {
        resetDailyQuestionsIfNeeded()

        val currentQuestions = prefs.getInt(KEY_DAILY_QUESTIONS_COMPLETED, 0)

        prefs.edit()
            .putInt(KEY_DAILY_QUESTIONS_COMPLETED, currentQuestions + amount)
            .putString(KEY_DAILY_QUESTIONS_DATE, getTodayString())
            .apply()
    }

    fun getDailyMissionRequiredQuestions(): Int {
        return DAILY_MISSION_REQUIRED_QUESTIONS
    }

    fun getDailyMissionClaimedDate(): String {
        return prefs.getString(KEY_DAILY_MISSION_CLAIMED_DATE, "") ?: ""
    }

    fun hasClaimedDailyMissionToday(): Boolean {
        return getDailyMissionClaimedDate() == getTodayString()
    }

    fun canClaimDailyMission(): Boolean {
        return getDailyQuestionsCompleted() >= DAILY_MISSION_REQUIRED_QUESTIONS &&
                !hasClaimedDailyMissionToday()
    }

    fun claimDailyMission(): Boolean {
        if (!canClaimDailyMission()) {
            return false
        }

        addXp(DAILY_MISSION_XP_REWARD)
        addCoins(DAILY_MISSION_COINS_REWARD)

        prefs.edit()
            .putString(KEY_DAILY_MISSION_CLAIMED_DATE, getTodayString())
            .apply()

        return true
    }

    fun getDailyMissionXpReward(): Int {
        return DAILY_MISSION_XP_REWARD
    }

    fun getDailyMissionCoinsReward(): Int {
        return DAILY_MISSION_COINS_REWARD
    }

    // -----------------------------
    // Shop
    // -----------------------------

    fun isItemUnlocked(itemId: String): Boolean {
        return prefs.getBoolean("shop_item_$itemId", false)
    }

    fun unlockItem(itemId: String) {
        prefs.edit().putBoolean("shop_item_$itemId", true).apply()
    }

    fun getShopItems(): List<ShopItem> {
        return listOf(
            ShopItem(
                id = "purple_border",
                nameRes = R.string.shop_item_purple_border_name,
                descriptionRes = R.string.shop_item_purple_border_description,
                price = 100,
                previewType = "purple_border",
                category = ShopItemCategory.AVATAR_BORDER,
                unlocked = isItemUnlocked("purple_border")
            ),
            ShopItem(
                id = "blue_border",
                nameRes = R.string.shop_item_blue_border_name,
                descriptionRes = R.string.shop_item_blue_border_description,
                price = 150,
                previewType = "blue_border",
                category = ShopItemCategory.AVATAR_BORDER,
                unlocked = isItemUnlocked("blue_border")
            ),
            ShopItem(
                id = "green_glow",
                nameRes = R.string.shop_item_green_glow_name,
                descriptionRes = R.string.shop_item_green_glow_description,
                price = 200,
                previewType = "green_glow",
                category = ShopItemCategory.AVATAR_BORDER,
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