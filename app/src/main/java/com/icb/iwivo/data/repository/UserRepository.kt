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
        return getBadges().count { it.unlocked }
    }

    fun getBadges(): List<com.icb.iwivo.data.model.Badge> {
        val xp = getXp()
        val level = getLevel()
        val streak = getStreak()

        return listOf(
            com.icb.iwivo.data.model.Badge(
                id = "first_steps",
                name = "Primeros pasos",
                description = "Consigue tus primeros 50 XP",
                unlocked = xp >= 50
            ),
            com.icb.iwivo.data.model.Badge(
                id = "level_2",
                name = "Subida de nivel",
                description = "Alcanza el nivel 2",
                unlocked = level >= 2
            ),
            com.icb.iwivo.data.model.Badge(
                id = "streak_3",
                name = "Constancia",
                description = "Consigue una racha de 3 días",
                unlocked = streak >= 3
            )
        )
    }
}