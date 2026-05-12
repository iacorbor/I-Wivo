package com.icb.iwivo.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.icb.iwivo.data.model.ShopItem
import com.icb.iwivo.data.model.ShopItemCategory
import com.icb.iwivo.data.model.UserProfile
import java.time.LocalDate

class FirestoreRepository {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    fun createUserIfNotExists(
        username: String = "",
        onSuccess: () -> Unit = {},
        onError: (Exception?) -> Unit = {}
    ) {
        val uid = auth.currentUser?.uid

        if (uid == null) {
            onError(null)
            return
        }

        val userRef = db.collection("users").document(uid)

        userRef.get()
            .addOnSuccessListener { document ->
                if (!document.exists()) {
                    val data = mapOf(
                        "username" to username,
                        "xp" to 0,
                        "coins" to 0,
                        "streak" to 0,
                        "lastActivityDate" to null,
                        "avatarBase64" to "",
                        "unlockedShopItems" to emptyList<String>(),
                        "equippedShopItemId" to null,
                        "equippedAvatarBorderId" to null,
                        "equippedBackgroundId" to null,
                        "equippedButtonStyleId" to null,
                        "equippedThemeId" to null,
                        "equippedEffectId" to null,
                        "dailyMissionDate" to null,
                        "dailyQuestionsAnswered" to 0,
                        "dailyMissionClaimed" to false,
                        "lastPlayedTopic" to null,
                        "lastPlayedGameType" to null
                    )

                    userRef.set(data)
                        .addOnSuccessListener {
                            onSuccess()
                        }
                        .addOnFailureListener { exception ->
                            onError(exception)
                        }
                } else {
                    onSuccess()
                }
            }
            .addOnFailureListener { exception ->
                onError(exception)
            }
    }

    fun addXp(amount: Int) {
        val uid = auth.currentUser?.uid ?: return
        val userRef = db.collection("users").document(uid)

        db.runTransaction { transaction ->
            val snapshot = transaction.get(userRef)
            val currentXp = snapshot.getLong("xp") ?: 0
            transaction.update(userRef, "xp", currentXp + amount)
        }
    }

    fun addCoins(amount: Int) {
        val uid = auth.currentUser?.uid ?: return
        val userRef = db.collection("users").document(uid)

        db.runTransaction { transaction ->
            val snapshot = transaction.get(userRef)
            val currentCoins = snapshot.getLong("coins") ?: 0
            transaction.update(userRef, "coins", currentCoins + amount)
        }
    }

    fun getUserData(
        onResult: (xp: Int, coins: Int, streak: Int) -> Unit
    ) {
        val uid = auth.currentUser?.uid ?: return

        db.collection("users")
            .document(uid)
            .get()
            .addOnSuccessListener { doc ->
                val xp = doc.getLong("xp")?.toInt() ?: 0
                val coins = doc.getLong("coins")?.toInt() ?: 0
                val streak = doc.getLong("streak")?.toInt() ?: 0

                onResult(xp, coins, streak)
            }
    }

    fun getCurrentUserData(
        onResult: (xp: Int, coins: Int, streak: Int) -> Unit,
        onError: () -> Unit = {}
    ) {
        val uid = auth.currentUser?.uid

        if (uid == null) {
            onError()
            return
        }

        db.collection("users")
            .document(uid)
            .get()
            .addOnSuccessListener { doc ->
                val xp = doc.getLong("xp")?.toInt() ?: 0
                val coins = doc.getLong("coins")?.toInt() ?: 0
                val streak = doc.getLong("streak")?.toInt() ?: 0

                onResult(xp, coins, streak)
            }
            .addOnFailureListener {
                onError()
            }
    }

    fun getCurrentUserProgress(
        onResult: (xp: Int, coins: Int, streak: Int, lastActivityDate: String?) -> Unit,
        onError: () -> Unit = {}
    ) {
        val uid = auth.currentUser?.uid

        if (uid == null) {
            onError()
            return
        }

        db.collection("users")
            .document(uid)
            .get()
            .addOnSuccessListener { doc ->
                val xp = doc.getLong("xp")?.toInt() ?: 0
                val coins = doc.getLong("coins")?.toInt() ?: 0
                val streak = doc.getLong("streak")?.toInt() ?: 0
                val lastActivityDate = doc.getString("lastActivityDate")

                onResult(xp, coins, streak, lastActivityDate)
            }
            .addOnFailureListener {
                onError()
            }
    }

    fun calculateStreakAfterActivity(
        currentStreak: Int,
        lastActivityDate: String?
    ): Int {
        val today = LocalDate.now()

        val lastActivity = lastActivityDate?.let { date ->
            runCatching { LocalDate.parse(date) }.getOrNull()
        }

        return when {
            lastActivity == null -> 1
            lastActivity == today -> currentStreak
            lastActivity.plusDays(1) == today -> currentStreak + 1
            else -> 1
        }
    }

    fun updateStreak() {
        val uid = auth.currentUser?.uid ?: return
        val userRef = db.collection("users").document(uid)
        val today = LocalDate.now().toString()

        db.runTransaction { transaction ->
            val snapshot = transaction.get(userRef)

            val currentStreak = snapshot.getLong("streak") ?: 0
            val lastActivityDate = snapshot.getString("lastActivityDate")

            val newStreak = when {
                lastActivityDate == null -> 1
                lastActivityDate == today -> currentStreak
                runCatching { LocalDate.parse(lastActivityDate).plusDays(1).toString() == today }
                    .getOrDefault(false) -> currentStreak + 1
                else -> 1
            }

            transaction.update(
                userRef,
                mapOf(
                    "streak" to newStreak,
                    "lastActivityDate" to today
                )
            )
        }
    }

    fun updateUserProfile(
        uid: String,
        username: String,
        avatarBase64: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val updates = mapOf(
            "username" to username,
            "avatarBase64" to avatarBase64
        )

        db.collection("users")
            .document(uid)
            .update(updates)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { exception ->
                onError(exception.message ?: "Error al actualizar perfil")
            }
    }

    fun getUserProfile(
        uid: String,
        onSuccess: (UserProfile) -> Unit,
        onError: (String) -> Unit
    ) {
        db.collection("users")
            .document(uid)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val profile = document.toObject(UserProfile::class.java)

                    if (profile != null) {
                        onSuccess(profile)
                    } else {
                        onError("No se pudo leer el perfil")
                    }
                } else {
                    onError("El perfil no existe")
                }
            }
            .addOnFailureListener { exception ->
                onError(exception.message ?: "Error al cargar perfil")
            }
    }

    fun buyShopItem(
        item: ShopItem,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val uid = auth.currentUser?.uid

        if (uid == null) {
            onError("Usuario no autenticado")
            return
        }

        val userRef = db.collection("users").document(uid)

        db.runTransaction { transaction ->
            val snapshot = transaction.get(userRef)

            val currentXp = snapshot.getLong("xp")?.toInt() ?: 0
            val currentLevel = (currentXp / 500) + 1

            if (currentLevel < item.requiredLevel) {
                throw Exception("Necesitas nivel ${item.requiredLevel} para comprar este objeto")
            }

            val currentCoins = snapshot.getLong("coins")?.toInt() ?: 0

            val unlockedShopItems = snapshot.get("unlockedShopItems") as? List<*>
            val unlockedIds = unlockedShopItems
                ?.filterIsInstance<String>()
                ?: emptyList()

            if (item.id in unlockedIds) {
                throw Exception("Este objeto ya está desbloqueado")
            }

            if (currentCoins < item.price) {
                throw Exception("No tienes monedas suficientes")
            }

            transaction.update(
                userRef,
                mapOf(
                    "coins" to currentCoins - item.price,
                    "unlockedShopItems" to FieldValue.arrayUnion(item.id)
                )
            )
        }
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { exception ->
                onError(exception.message ?: "Error al comprar objeto")
            }
    }

    private fun getEquippedFieldByCategory(category: ShopItemCategory): String {
        return when (category) {
            ShopItemCategory.AVATAR_BORDER -> "equippedAvatarBorderId"
            ShopItemCategory.BACKGROUND -> "equippedBackgroundId"
            ShopItemCategory.BUTTON_STYLE -> "equippedButtonStyleId"
            ShopItemCategory.THEME -> "equippedThemeId"
            ShopItemCategory.EFFECT -> "equippedEffectId"
        }
    }

    fun getShopStateByCategory(
        onResult: (
            coins: Int,
            unlockedShopItems: List<String>,
            equippedItemsByCategory: Map<ShopItemCategory, String?>
        ) -> Unit,
        onError: (String) -> Unit = {}
    ) {
        val uid = auth.currentUser?.uid

        if (uid == null) {
            onError("Usuario no autenticado")
            return
        }

        db.collection("users")
            .document(uid)
            .get()
            .addOnSuccessListener { doc ->
                val coins = doc.getLong("coins")?.toInt() ?: 0

                val unlockedShopItems = doc.get("unlockedShopItems") as? List<*>
                val unlockedIds = unlockedShopItems
                    ?.filterIsInstance<String>()
                    ?: emptyList()

                val equippedItemsByCategory = mapOf(
                    ShopItemCategory.AVATAR_BORDER to doc.getString("equippedAvatarBorderId"),
                    ShopItemCategory.BACKGROUND to doc.getString("equippedBackgroundId"),
                    ShopItemCategory.BUTTON_STYLE to doc.getString("equippedButtonStyleId"),
                    ShopItemCategory.THEME to doc.getString("equippedThemeId"),
                    ShopItemCategory.EFFECT to doc.getString("equippedEffectId")
                )

                onResult(
                    coins,
                    unlockedIds,
                    equippedItemsByCategory
                )
            }
            .addOnFailureListener { exception ->
                onError(exception.message ?: "Error al cargar tienda")
            }
    }

    fun equipShopItemByCategory(
        item: ShopItem,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val uid = auth.currentUser?.uid

        if (uid == null) {
            onError("Usuario no autenticado")
            return
        }

        val userRef = db.collection("users").document(uid)
        val fieldName = getEquippedFieldByCategory(item.category)

        db.runTransaction { transaction ->
            val snapshot = transaction.get(userRef)

            val unlockedShopItems = snapshot.get("unlockedShopItems") as? List<*>
            val unlockedIds = unlockedShopItems
                ?.filterIsInstance<String>()
                ?: emptyList()

            if (item.id !in unlockedIds) {
                throw Exception("Primero debes comprar este objeto")
            }

            transaction.update(userRef, fieldName, item.id)
        }
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { exception ->
                onError(exception.message ?: "Error al equipar objeto")
            }
    }

    fun unequipShopItemByCategory(
        category: ShopItemCategory,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val uid = auth.currentUser?.uid

        if (uid == null) {
            onError("Usuario no autenticado")
            return
        }

        val userRef = db.collection("users").document(uid)
        val fieldName = getEquippedFieldByCategory(category)

        userRef.update(fieldName, null)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { exception ->
                onError(exception.message ?: "Error al desequipar objeto")
            }
    }

    fun getShopState(
        onResult: (
            coins: Int,
            unlockedShopItems: List<String>,
            equippedShopItemId: String?
        ) -> Unit,
        onError: (String) -> Unit = {}
    ) {
        val uid = auth.currentUser?.uid

        if (uid == null) {
            onError("Usuario no autenticado")
            return
        }

        db.collection("users")
            .document(uid)
            .get()
            .addOnSuccessListener { doc ->
                val coins = doc.getLong("coins")?.toInt() ?: 0

                val unlockedShopItems = doc.get("unlockedShopItems") as? List<*>
                val unlockedIds = unlockedShopItems
                    ?.filterIsInstance<String>()
                    ?: emptyList()

                val equippedShopItemId =
                    doc.getString("equippedAvatarBorderId")
                        ?: doc.getString("equippedShopItemId")

                onResult(
                    coins,
                    unlockedIds,
                    equippedShopItemId
                )
            }
            .addOnFailureListener { exception ->
                onError(exception.message ?: "Error al cargar tienda")
            }
    }

    fun equipShopItem(
        itemId: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val uid = auth.currentUser?.uid

        if (uid == null) {
            onError("Usuario no autenticado")
            return
        }

        db.collection("users")
            .document(uid)
            .update("equippedAvatarBorderId", itemId)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { exception ->
                onError(exception.message ?: "Error al equipar objeto")
            }
    }

    fun unequipShopItem(
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val uid = auth.currentUser?.uid

        if (uid == null) {
            onError("Usuario no autenticado")
            return
        }

        db.collection("users")
            .document(uid)
            .update("equippedAvatarBorderId", null)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { exception ->
                onError(exception.message ?: "Error al desequipar objeto")
            }
    }

    fun getDailyMissionProgress(
        onResult: (
            answeredQuestions: Int,
            goalQuestions: Int,
            claimed: Boolean
        ) -> Unit,
        onError: () -> Unit = {}
    ) {
        val uid = auth.currentUser?.uid

        if (uid == null) {
            onError()
            return
        }

        val today = LocalDate.now().toString()

        db.collection("users")
            .document(uid)
            .get()
            .addOnSuccessListener { doc ->
                val dailyMissionDate = doc.getString("dailyMissionDate")
                val claimed = doc.getBoolean("dailyMissionClaimed") ?: false

                val answeredQuestions = if (dailyMissionDate == today) {
                    doc.getLong("dailyQuestionsAnswered")?.toInt() ?: 0
                } else {
                    0
                }

                val isClaimedToday = if (dailyMissionDate == today) {
                    claimed
                } else {
                    false
                }

                onResult(
                    answeredQuestions.coerceAtMost(10),
                    10,
                    isClaimedToday
                )
            }
            .addOnFailureListener {
                onError()
            }
    }

    fun updateDailyMissionAfterGame(
        questionsAnswered: Int,
        onSuccess: () -> Unit = {},
        onError: () -> Unit = {}
    ) {
        val uid = auth.currentUser?.uid

        if (uid == null) {
            onError()
            return
        }

        val userRef = db.collection("users").document(uid)
        val today = LocalDate.now().toString()

        db.runTransaction { transaction ->
            val snapshot = transaction.get(userRef)

            val dailyMissionDate = snapshot.getString("dailyMissionDate")
            val currentAnswered = snapshot.getLong("dailyQuestionsAnswered")?.toInt() ?: 0
            val currentClaimed = snapshot.getBoolean("dailyMissionClaimed") ?: false

            val isSameDay = dailyMissionDate == today

            val newAnswered = if (isSameDay) {
                currentAnswered + questionsAnswered
            } else {
                questionsAnswered
            }

            val newClaimed = if (isSameDay) {
                currentClaimed
            } else {
                false
            }

            transaction.update(
                userRef,
                mapOf(
                    "dailyMissionDate" to today,
                    "dailyQuestionsAnswered" to newAnswered.coerceAtMost(10),
                    "dailyMissionClaimed" to newClaimed
                )
            )
        }
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener {
                onError()
            }
    }

    fun claimDailyMissionReward(
        xpReward: Int = 50,
        coinsReward: Int = 20,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val uid = auth.currentUser?.uid

        if (uid == null) {
            onError("Usuario no autenticado")
            return
        }

        val userRef = db.collection("users").document(uid)
        val today = LocalDate.now().toString()

        db.runTransaction { transaction ->
            val snapshot = transaction.get(userRef)

            val dailyMissionDate = snapshot.getString("dailyMissionDate")
            val answeredQuestions = snapshot.getLong("dailyQuestionsAnswered")?.toInt() ?: 0
            val claimed = snapshot.getBoolean("dailyMissionClaimed") ?: false

            if (dailyMissionDate != today) {
                throw Exception("La misión diaria todavía no está completada")
            }

            if (answeredQuestions < 10) {
                throw Exception("La misión diaria todavía no está completada")
            }

            if (claimed) {
                throw Exception("La recompensa ya ha sido reclamada")
            }

            val currentXp = snapshot.getLong("xp") ?: 0
            val currentCoins = snapshot.getLong("coins") ?: 0

            transaction.update(
                userRef,
                mapOf(
                    "xp" to currentXp + xpReward,
                    "coins" to currentCoins + coinsReward,
                    "dailyMissionClaimed" to true
                )
            )
        }
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { exception ->
                onError(exception.message ?: "No se pudo reclamar la recompensa")
            }
    }

    fun saveLastPlayedMode(
        topic: String,
        gameType: String,
        onSuccess: () -> Unit = {},
        onError: () -> Unit = {}
    ) {
        val uid = auth.currentUser?.uid

        if (uid == null) {
            onError()
            return
        }

        db.collection("users")
            .document(uid)
            .update(
                mapOf(
                    "lastPlayedTopic" to topic,
                    "lastPlayedGameType" to gameType
                )
            )
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener {
                onError()
            }
    }

    fun getLastPlayedMode(
        onResult: (topic: String?, gameType: String?) -> Unit,
        onError: () -> Unit = {}
    ) {
        val uid = auth.currentUser?.uid

        if (uid == null) {
            onError()
            return
        }

        db.collection("users")
            .document(uid)
            .get()
            .addOnSuccessListener { doc ->
                val topic = doc.getString("lastPlayedTopic")
                val gameType = doc.getString("lastPlayedGameType")

                onResult(topic, gameType)
            }
            .addOnFailureListener {
                onError()
            }
    }
}