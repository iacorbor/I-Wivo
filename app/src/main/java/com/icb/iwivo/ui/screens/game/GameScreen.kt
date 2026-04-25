package com.icb.iwivo.ui.screens.game

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.icb.iwivo.R
import com.icb.iwivo.data.model.Question
import com.icb.iwivo.data.repository.QuestionRepository
import com.icb.iwivo.ui.theme.CardDark
import com.icb.iwivo.ui.theme.GreenAccent
import com.icb.iwivo.ui.theme.PurplePrimary
import com.icb.iwivo.ui.theme.TextSecondary

@Composable
fun GameScreen(
    topic: String,
    gameType: String,
    onFinishGame: (correct: Int, total: Int) -> Unit
) {
    val repository = remember { QuestionRepository() }
    val questions = remember(topic, gameType) {
        repository.getQuestions(topic, gameType)
    }

    var currentIndex by remember { mutableIntStateOf(0) }
    var selectedIndex by remember { mutableIntStateOf(-1) }
    var correctAnswers by remember { mutableIntStateOf(0) }
    var showFeedback by remember { mutableStateOf(false) }

    if (questions.isEmpty()) {
        EmptyQuestionsState(topic, gameType)
        return
    }

    val currentQuestion = questions[currentIndex]
    val progress = (currentIndex + 1).toFloat() / questions.size.toFloat()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp)
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = stringResource(R.string.game_header, topic.uppercase()),
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(8.dp))

        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier.fillMaxWidth(),
            color = PurplePrimary,
            trackColor = MaterialTheme.colorScheme.surface
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = stringResource(
                R.string.question_counter,
                currentIndex + 1,
                questions.size
            ),
            color = TextSecondary
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = currentQuestion.questionText,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(24.dp))

        currentQuestion.options.forEachIndexed { index, option ->
            AnswerOptionCard(
                text = option,
                index = index,
                selectedIndex = selectedIndex,
                correctIndex = currentQuestion.correctOptionIndex,
                showFeedback = showFeedback,
                onClick = {
                    selectedIndex = index
                }
            )

            Spacer(modifier = Modifier.height(12.dp))
        }

        Spacer(modifier = Modifier.weight(1f))

        if (showFeedback) {
            FeedbackBlock(
                question = currentQuestion,
                selectedIndex = selectedIndex
            )

            Spacer(modifier = Modifier.height(16.dp))
        }

        Button(
            onClick = {
                if (!showFeedback) {
                    if (selectedIndex != -1) {
                        if (selectedIndex == currentQuestion.correctOptionIndex) {
                            correctAnswers++
                        }
                        showFeedback = true
                    }
                } else {
                    if (currentIndex < questions.lastIndex) {
                        currentIndex++
                        selectedIndex = -1
                        showFeedback = false
                    } else {
                        onFinishGame(correctAnswers, questions.size)
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = selectedIndex != -1 || showFeedback
        ) {
            Text(
                text = if (showFeedback) {
                    stringResource(R.string.next_question)
                } else {
                    stringResource(R.string.check_answer)
                }
            )
        }
    }
}

@Composable
private fun AnswerOptionCard(
    text: String,
    index: Int,
    selectedIndex: Int,
    correctIndex: Int,
    showFeedback: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = when {
        showFeedback && index == correctIndex -> GreenAccent
        showFeedback && index == selectedIndex && selectedIndex != correctIndex -> MaterialTheme.colorScheme.error
        selectedIndex == index -> PurplePrimary
        else -> CardDark
    }

    Card(
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        ),
        shape = RoundedCornerShape(18.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = !showFeedback) { onClick() }
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(18.dp),
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
private fun FeedbackBlock(
    question: Question,
    selectedIndex: Int
) {
    val isCorrect = selectedIndex == question.correctOptionIndex

    Card(
        colors = CardDefaults.cardColors(
            containerColor = CardDark
        ),
        shape = RoundedCornerShape(18.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Text(
                text = if (isCorrect) {
                    stringResource(R.string.correct_answer)
                } else {
                    stringResource(R.string.wrong_answer)
                },
                color = if (isCorrect) GreenAccent else MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = question.explanation,
                color = TextSecondary
            )
        }
    }
}

@Composable
private fun EmptyQuestionsState(
    topic: String,
    gameType: String
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp)
    ) {
        Text(
            text = stringResource(R.string.no_questions, topic, gameType),
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}