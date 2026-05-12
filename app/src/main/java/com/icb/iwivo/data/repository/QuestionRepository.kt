package com.icb.iwivo.data.repository

import com.icb.iwivo.data.model.Question
import com.icb.iwivo.data.repository.questions.AndroidQuestions
import com.icb.iwivo.data.repository.questions.FirebaseQuestions
import com.icb.iwivo.data.repository.questions.GitQuestions
import com.icb.iwivo.data.repository.questions.HtmlCssQuestions
import com.icb.iwivo.data.repository.questions.JavaQuestions
import com.icb.iwivo.data.repository.questions.JavaScriptQuestions
import com.icb.iwivo.data.repository.questions.KotlinQuestions
import com.icb.iwivo.data.repository.questions.SpringQuestions
import com.icb.iwivo.data.repository.questions.SqlQuestions

class QuestionRepository {

    private val questions: List<Question> =
        JavaQuestions.questions +
                KotlinQuestions.questions +
                SqlQuestions.questions +
                JavaScriptQuestions.questions +
                HtmlCssQuestions.questions +
                GitQuestions.questions +
                AndroidQuestions.questions +
                FirebaseQuestions.questions +
                SpringQuestions.questions

    fun getQuestions(
        topic: String,
        gameType: String,
        difficulty: String = "easy",
        limit: Int = 10
    ): List<Question> {
        val compatibleGameTypes = getCompatibleGameTypes(gameType)

        val exactDifficultyQuestions = questions.filter { question ->
            question.topic == topic &&
                    question.gameType in compatibleGameTypes &&
                    question.difficulty == difficulty
        }

        if (exactDifficultyQuestions.isNotEmpty()) {
            return exactDifficultyQuestions
                .shuffled()
                .take(limit)
        }

        val fallbackQuestions = questions.filter { question ->
            question.topic == topic &&
                    question.gameType in compatibleGameTypes
        }

        return fallbackQuestions
            .shuffled()
            .take(limit)
    }

    fun hasQuestions(
        topic: String,
        gameType: String,
        difficulty: String = "easy"
    ): Boolean {
        val compatibleGameTypes = getCompatibleGameTypes(gameType)

        val hasExactDifficultyQuestions = questions.any { question ->
            question.topic == topic &&
                    question.gameType in compatibleGameTypes &&
                    question.difficulty == difficulty
        }

        if (hasExactDifficultyQuestions) {
            return true
        }

        return questions.any { question ->
            question.topic == topic &&
                    question.gameType in compatibleGameTypes
        }
    }

    fun getAvailableTopics(): List<String> {
        return questions
            .map { question -> question.topic }
            .distinct()
    }

    fun getAvailableGameTypesByTopic(topic: String): List<String> {
        return questions
            .filter { question -> question.topic == topic }
            .map { question -> normalizeGameType(question.gameType) }
            .distinct()
    }

    fun getAvailableDifficulties(
        topic: String,
        gameType: String
    ): List<String> {
        val compatibleGameTypes = getCompatibleGameTypes(gameType)

        return questions
            .filter { question ->
                question.topic == topic &&
                        question.gameType in compatibleGameTypes
            }
            .map { question -> question.difficulty }
            .distinct()
    }

    private fun getCompatibleGameTypes(gameType: String): List<String> {
        return when (gameType) {
            "match_concept", "concept_match" -> listOf("match_concept", "concept_match")
            else -> listOf(gameType)
        }
    }

    private fun normalizeGameType(gameType: String): String {
        return when (gameType) {
            "concept_match" -> "match_concept"
            else -> gameType
        }
    }
}