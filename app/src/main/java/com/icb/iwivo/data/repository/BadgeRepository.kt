package com.icb.iwivo.data.repository

import com.icb.iwivo.data.model.Badge

class BadgeRepository {

    fun getBadges(xp: Int, coins: Int, streak: Int): List<Badge> {
        val badges = mutableListOf<Badge>()

        addXpBadges(badges, xp)
        addLevelBadges(badges, xp)
        addStreakBadges(badges, streak)
        addCoinLikeBadges(badges, coins)
        addSpecialBadges(badges, xp, streak)

        return badges
    }

    fun getBadgeById(id: String): Badge? {
        return getBadges(
            xp = Int.MAX_VALUE,
            coins = Int.MAX_VALUE,
            streak = Int.MAX_VALUE
        ).firstOrNull { it.id == id }
    }

    private fun addXpBadges(
        badges: MutableList<Badge>,
        xp: Int
    ) {
        val xpMilestones = listOf(
            50, 100, 250, 500, 750,
            1000, 1500, 2000, 3000, 4000,
            5000, 7500, 10000, 15000, 20000,
            30000, 50000, 75000, 100000
        )

        xpMilestones.forEach { milestone ->
            badges.add(
                Badge(
                    id = "xp_$milestone",
                    name = "XP $milestone",
                    description = "Alcanza $milestone XP acumulada",
                    icon = "⚡",
                    unlocked = xp >= milestone
                )
            )
        }
    }

    private fun addLevelBadges(
        badges: MutableList<Badge>,
        xp: Int
    ) {
        val level = (xp / 500) + 1

        val levelMilestones = listOf(
            2, 3, 5, 10, 15,
            20, 25, 30, 40, 50,
            75, 100
        )

        levelMilestones.forEach { milestone ->
            badges.add(
                Badge(
                    id = "level_$milestone",
                    name = "Nivel $milestone",
                    description = "Alcanza el nivel $milestone",
                    icon = "🎓",
                    unlocked = level >= milestone
                )
            )
        }
    }

    private fun addStreakBadges(
        badges: MutableList<Badge>,
        streak: Int
    ) {
        val streakMilestones = listOf(
            1, 2, 3, 5, 7,
            10, 14, 21, 30, 45,
            60, 90, 120, 180, 365
        )

        streakMilestones.forEach { milestone ->
            badges.add(
                Badge(
                    id = "streak_$milestone",
                    name = "Racha $milestone",
                    description = "Mantén una racha de $milestone días",
                    icon = "🔥",
                    unlocked = streak >= milestone
                )
            )
        }
    }

    private fun addCoinLikeBadges(
        badges: MutableList<Badge>,
        coins: Int
    ) {
        val coinMilestones = listOf(
            10, 50, 100, 250, 500,
            1000, 2500, 5000, 10000
        )

        coinMilestones.forEach { milestone ->
            badges.add(
                Badge(
                    id = "coins_$milestone",
                    name = "Wivo Coins $milestone",
                    description = "Acumula $milestone monedas",
                    icon = "🪙",
                    unlocked = coins >= milestone
                )
            )
        }
    }

    private fun addSpecialBadges(
        badges: MutableList<Badge>,
        xp: Int,
        streak: Int
    ) {
        val level = (xp / 500) + 1

        badges.addAll(
            listOf(
                Badge(
                    id = "first_steps",
                    name = "Primeros pasos",
                    description = "Consigue tus primeros 50 XP",
                    icon = "🚀",
                    unlocked = xp >= 50
                ),
                Badge(
                    id = "warm_up",
                    name = "Calentando motores",
                    description = "Supera los 100 XP",
                    icon = "🔥",
                    unlocked = xp >= 100
                ),
                Badge(
                    id = "rookie_dev",
                    name = "Dev Novato",
                    description = "Llega al nivel 2",
                    icon = "💻",
                    unlocked = level >= 2
                ),
                Badge(
                    id = "junior_dev",
                    name = "Junior en marcha",
                    description = "Llega al nivel 5",
                    icon = "🧠",
                    unlocked = level >= 5
                ),
                Badge(
                    id = "daily_player",
                    name = "Primer día activo",
                    description = "Completa actividad en tu primer día",
                    icon = "📅",
                    unlocked = streak >= 1
                ),
                Badge(
                    id = "week_warrior",
                    name = "Guerrero semanal",
                    description = "Mantén una racha de 7 días",
                    icon = "🛡️",
                    unlocked = streak >= 7
                ),
                Badge(
                    id = "month_grinder",
                    name = "Modo constancia",
                    description = "Mantén una racha de 30 días",
                    icon = "🏋️",
                    unlocked = streak >= 30
                ),
                Badge(
                    id = "xp_machine",
                    name = "Máquina de XP",
                    description = "Supera los 5000 XP",
                    icon = "⚙️",
                    unlocked = xp >= 5000
                ),
                Badge(
                    id = "serious_student",
                    name = "Estudiante serio",
                    description = "Alcanza nivel 10 y racha de 7 días",
                    icon = "📚",
                    unlocked = level >= 10 && streak >= 7
                ),
                Badge(
                    id = "discipline_mode",
                    name = "Disciplina total",
                    description = "Alcanza nivel 20 y racha de 30 días",
                    icon = "🎯",
                    unlocked = level >= 20 && streak >= 30
                ),
                Badge(
                    id = "legend",
                    name = "Leyenda I-Wivo",
                    description = "Alcanza 100000 XP o una racha de 365 días",
                    icon = "👑",
                    unlocked = xp >= 100000 || streak >= 365
                )
            )
        )
    }
}
