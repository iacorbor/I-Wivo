package com.icb.iwivo.ui.screens.game

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.icb.iwivo.R
import com.icb.iwivo.data.model.Question
import com.icb.iwivo.data.repository.QuestionRepository
import com.icb.iwivo.ui.components.WivoButton
import com.icb.iwivo.ui.components.WivoCard
import com.icb.iwivo.ui.components.WivoScreen
import com.icb.iwivo.ui.theme.CardDark
import com.icb.iwivo.ui.theme.GreenAccent
import com.icb.iwivo.ui.theme.PurplePrimary
import com.icb.iwivo.ui.theme.TextSecondary
import androidx.compose.runtime.LaunchedEffect
import com.icb.iwivo.data.model.Badge
import com.icb.iwivo.data.repository.BadgeRepository
import com.icb.iwivo.ui.components.BadgeUnlockedOverlay
import com.icb.iwivo.ui.components.CodeBlock
import kotlinx.coroutines.delay
import androidx.compose.ui.platform.LocalContext
import com.icb.iwivo.ui.utils.HapticUtils
import com.icb.iwivo.ui.utils.SoundUtils

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
    val badgeRepository = remember { BadgeRepository() }
    var previousBadges by remember { mutableStateOf<List<Badge>>(emptyList()) }
    var newBadge by remember { mutableStateOf<Badge?>(null) }
    val context = LocalContext.current

    LaunchedEffect(showFeedback) {
        if (showFeedback) {
            kotlinx.coroutines.delay(2300)

            if (currentIndex < questions.lastIndex) {
                currentIndex++
                selectedIndex = -1
                showFeedback = false
            } else {
                onFinishGame(correctAnswers, questions.size)
            }
        }
    }

    if (questions.isEmpty()) {
        EmptyQuestionsState(topic, gameType)
        return
    }

    val currentQuestion = questions[currentIndex]
    val progress = (currentIndex + 1).toFloat() / questions.size.toFloat()

    WivoScreen {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Spacer(modifier = Modifier.height(32.dp))
            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = stringResource(R.string.game_header, topic.uppercase()),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(
                    R.string.question_counter,
                    currentIndex + 1,
                    questions.size
                ),
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(8.dp))

            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier.fillMaxWidth(),
                color = PurplePrimary,
                trackColor = MaterialTheme.colorScheme.surface
            )

            Spacer(modifier = Modifier.height(24.dp))

            AnimatedContent(
                targetState = currentIndex,
                transitionSpec = {
                    fadeIn() togetherWith fadeOut()
                },
                label = "questionTransition"
            ) { animatedIndex ->

                val animatedQuestion = questions[animatedIndex]

                Column {
                    WivoCard {
                        if (gameType == "complete_code") {
                            CodeBlock(code = animatedQuestion.questionText)
                        } else {
                            Text(
                                text = animatedQuestion.questionText,
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    animatedQuestion.options.forEachIndexed { index, option ->
                        AnswerOptionCard(
                            text = option,
                            index = index,
                            selectedIndex = selectedIndex,
                            correctIndex = animatedQuestion.correctOptionIndex,
                            showFeedback = showFeedback,
                            onClick = {
                                selectedIndex = index
                            }
                        )

                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            if (showFeedback) {
                FeedbackBlock(
                    question = currentQuestion,
                    selectedIndex = selectedIndex
                )

                Spacer(modifier = Modifier.height(16.dp))
            }

            WivoButton(
                text = stringResource(R.string.check_answer),
                onClick = {
                    if (selectedIndex != -1 && !showFeedback) {
                        if (selectedIndex == currentQuestion.correctOptionIndex) {
                            correctAnswers++
                            HapticUtils.success(context)
                            SoundUtils.playCorrect(context)
                            val currentXp = (correctAnswers * 50)
                            val badges = badgeRepository.getBadges(currentXp, 0)

                            val unlockedNow = badges.filter { it.unlocked }
                            val newlyUnlocked = unlockedNow.firstOrNull { new ->
                                previousBadges.none { it.id == new.id }
                            }

                            if (newlyUnlocked != null) {
                                newBadge = newlyUnlocked
                            }

                            previousBadges = unlockedNow
                        }else{
                            HapticUtils.error(context)
                            SoundUtils.playWrong(context)
                        }
                        showFeedback = true
                    }
                }
            )
            newBadge?.let { badge ->
                BadgeUnlockedOverlay(
                    badge = badge,
                    onDismiss = {
                        newBadge = null
                    }
                )
            }
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
    val targetColor = when {
        showFeedback && index == correctIndex -> GreenAccent
        showFeedback && index == selectedIndex && selectedIndex != correctIndex -> MaterialTheme.colorScheme.error
        selectedIndex == index -> PurplePrimary
        else -> CardDark
    }

    val backgroundColor by animateColorAsState(
        targetValue = targetColor,
        label = "answerColor"
    )

    Card(
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
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

    WivoCard {
        Text(
            text = if (isCorrect) {
                stringResource(R.string.feedback_correct)
            } else {
                stringResource(R.string.feedback_wrong)
            },
            style = MaterialTheme.typography.titleLarge,
            color = if (isCorrect) GreenAccent else MaterialTheme.colorScheme.error
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = question.explanation,
            color = TextSecondary
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = if (isCorrect) {
                stringResource(R.string.feedback_correct_hint)
            } else {
                stringResource(R.string.feedback_wrong_hint)
            },
            color = TextSecondary
        )
    }
}

@Composable
private fun EmptyQuestionsState(
    topic: String,
    gameType: String
) {
    WivoScreen {
        Text(
            text = stringResource(R.string.no_questions, topic, gameType),
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}