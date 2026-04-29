package com.icb.iwivo.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.icb.iwivo.data.model.UserProfile

class FirestoreRepository {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    fun createUserIfNotExists(
        username:String ="",
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
                        "lastActivityDate" to null
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

    fun getUserData(onResult: (xp: Int, coins: Int, streak: Int) -> Unit) {
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
    fun addCoins(amount: Int) {
        val uid = auth.currentUser?.uid ?: return
        val userRef = db.collection("users").document(uid)

        db.runTransaction { transaction ->
            val snapshot = transaction.get(userRef)
            val currentCoins = snapshot.getLong("coins") ?: 0
            transaction.update(userRef, "coins", currentCoins + amount)
        }
    }
    fun updateStreak() {
        val uid = auth.currentUser?.uid ?: return
        val userRef = db.collection("users").document(uid)

        val today = java.time.LocalDate.now().toString()

        db.runTransaction { transaction ->
            val snapshot = transaction.get(userRef)

            val currentStreak = snapshot.getLong("streak") ?: 0
            val lastActivityDate = snapshot.getString("lastActivityDate")

            val newStreak = when {
                lastActivityDate == null -> 1
                lastActivityDate == today -> currentStreak
                java.time.LocalDate.parse(lastActivityDate).plusDays(1).toString() == today -> currentStreak + 1
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
            .addOnFailureListener {
                onError(it.message ?: "Error al cargar perfil")
            }
    }

}
