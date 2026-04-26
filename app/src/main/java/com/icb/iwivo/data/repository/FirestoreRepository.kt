package com.icb.iwivo.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class FirestoreRepository {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    fun createUserIfNotExists(
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
                        "xp" to 0,
                        "coins" to 0,
                        "streak" to 0
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
    fun updateStreakSimple() {
        val uid = auth.currentUser?.uid ?: return
        val userRef = db.collection("users").document(uid)

        db.runTransaction { transaction ->
            val snapshot = transaction.get(userRef)
            val currentStreak = snapshot.getLong("streak") ?: 0
            transaction.update(userRef, "streak", currentStreak + 1)
        }
    }
}
