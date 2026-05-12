package com.icb.iwivo.ui.screens.game

import android.annotation.SuppressLint
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.icb.iwivo.R
import com.icb.iwivo.data.model.Question
import com.icb.iwivo.data.model.ShopItemCategory
import com.icb.iwivo.data.repository.FirestoreRepository
import com.icb.iwivo.data.repository.QuestionRepository
import com.icb.iwivo.data.repository.SettingsRepository
import com.icb.iwivo.ui.components.CodeBlock
import com.icb.iwivo.ui.components.WivoButton
import com.icb.iwivo.ui.components.WivoCard
import com.icb.iwivo.ui.components.WivoScreen
import com.icb.iwivo.ui.theme.CardDark
import com.icb.iwivo.ui.theme.GreenAccent
import com.icb.iwivo.ui.theme.TextSecondary
import com.icb.iwivo.ui.utils.HapticUtils
import com.icb.iwivo.ui.utils.SoundUtils
import kotlinx.coroutines.delay
import androidx.compose.foundation.layout.imePadding

@Composable
fun GameScreen(
    topic: String,
    gameType: String,
    difficulty: String,
    firestoreRepository: FirestoreRepository,
    onFinishGame: (correct: Int, total: Int, bestStreak: Int) -> Unit
) {
    val context = LocalContext.current
    val settingsRepository = remember { SettingsRepository(context) }

    var equippedBackgroundId by remember { mutableStateOf<String?>(null) }
    var equippedButtonStyleId by remember { mutableStateOf<String?>(null) }
    var equippedThemeId by remember { mutableStateOf<String?>(null) }
    var equippedEffectId by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        firestoreRepository.getShopStateByCategory(
            onResult = { _, _, equippedItemsByCategory ->
                equippedBackgroundId = equippedItemsByCategory[ShopItemCategory.BACKGROUND]
                equippedButtonStyleId = equippedItemsByCategory[ShopItemCategory.BUTTON_STYLE]
                equippedThemeId = equippedItemsByCategory[ShopItemCategory.THEME]
                equippedEffectId = equippedItemsByCategory[ShopItemCategory.EFFECT]
            },
            onError = {
                // No bloqueo la partida si falla la tienda.
            }
        )
    }

    val topicPersonality = getTopicPersonality(topic)
    val repository = remember { QuestionRepository() }

    val questions = remember(topic, gameType, difficulty) {
        repository.getQuestions(
            topic = topic,
            gameType = gameType,
            difficulty = difficulty
        )
    }

    var currentIndex by remember { mutableIntStateOf(0) }
    var selectedIndex by remember { mutableIntStateOf(-1) }
    var correctAnswers by remember { mutableIntStateOf(0) }
    var showFeedback by remember { mutableStateOf(false) }


    var currentStreak by remember { mutableIntStateOf(0) }
    var bestStreak by remember { mutableIntStateOf(0) }

    val selectedOrderOriginalIndexes = remember { mutableStateListOf<Int>() }
    val selectedOrderDisplayIndexes = remember { mutableStateListOf<Int>() }

    val guessedLetters = remember { mutableStateListOf<Char>() }
    var currentHangmanLetter by remember { mutableStateOf("") }
    var hangmanAttemptsLeft by remember { mutableIntStateOf(5) }

    val questionTimeLimit = 20
    var timeLeft by remember { mutableIntStateOf(questionTimeLimit) }
    var isTimeOut by remember { mutableStateOf(false) }

    if (questions.isEmpty()) {
        EmptyQuestionsState(
            topic = topic,
            gameType = gameType,
            backgroundItemId = equippedBackgroundId,
            themeItemId = equippedThemeId
        )
        return
    }

    val currentQuestion = questions[currentIndex]
    val progress = (currentIndex + 1).toFloat() / questions.size.toFloat()

    val shuffledOrderOptions = remember(currentQuestion.id) {
        currentQuestion.optionsRes
            .mapIndexed { originalIndex, optionRes ->
                OrderCodeOption(
                    originalIndex = originalIndex,
                    textRes = optionRes
                )
            }
            .shuffled()
    }

    val currentCorrectOptionText = currentQuestion.optionsRes
        .getOrNull(currentQuestion.correctOptionIndex)
        ?.let { optionRes -> stringResource(optionRes) }
        .orEmpty()

    val hangmanTargetWord = remember(currentQuestion.id, currentCorrectOptionText) {
        currentCorrectOptionText
            .uppercase()
            .filter { it.isLetterOrDigit() }
    }

    LaunchedEffect(currentQuestion.id) {
        selectedIndex = -1
        showFeedback = false
        isTimeOut = false
        timeLeft = questionTimeLimit

        selectedOrderOriginalIndexes.clear()
        selectedOrderDisplayIndexes.clear()

        guessedLetters.clear()
        currentHangmanLetter = ""
        hangmanAttemptsLeft = 5
    }

    LaunchedEffect(currentQuestion.id, showFeedback, gameType) {
        if (gameType == "technical_wordle") {
            timeLeft = 0
            isTimeOut = false
            return@LaunchedEffect
        }

        if (!showFeedback) {
            timeLeft = questionTimeLimit
            isTimeOut = false

            while (timeLeft > 0) {
                delay(1000)
                timeLeft--
            }

            if (timeLeft == 0 && !showFeedback) {
                selectedIndex = -1
                currentStreak = 0
                isTimeOut = true
                showFeedback = true


                if (settingsRepository.isHapticsEnabled()) {
                    HapticUtils.error(context)
                }

                if (settingsRepository.isSoundEnabled()) {
                    SoundUtils.playWrong(context)
                }
            }
        }
    }

    val buttonText = when {
        gameType == "technical_wordle" && !showFeedback -> stringResource(R.string.game_try_letter)
        showFeedback && currentIndex == questions.lastIndex -> stringResource(R.string.see_result)
        showFeedback -> stringResource(R.string.next_question)
        else -> stringResource(R.string.check_answer)
    }

    WivoScreen(
        backgroundItemId = equippedBackgroundId,
        themeItemId = equippedThemeId
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .imePadding()
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            GameTopBlock(
                gameType = gameType,
                difficulty = difficulty,
                topicPersonality = topicPersonality,
                currentIndex = currentIndex,
                totalQuestions = questions.size,
                progress = progress,
                currentStreak = currentStreak,
                bestStreak = bestStreak,
                timeLeft = timeLeft,
                showTimer = gameType != "technical_wordle"
            )

            Spacer(modifier = Modifier.height(18.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
            ) {
                AnimatedContent(
                    targetState = currentIndex,
                    transitionSpec = {
                        fadeIn() togetherWith fadeOut()
                    },
                    label = "questionTransition"
                ) { animatedIndex ->

                    val animatedQuestion = questions[animatedIndex]

                    Column {
                        QuestionCard(
                            question = animatedQuestion,
                            gameType = gameType,
                            topicPersonality = topicPersonality,
                            themeItemId = equippedThemeId,
                            effectItemId = equippedEffectId
                        )

                        Spacer(modifier = Modifier.height(22.dp))

                        when (gameType) {
                            "true_false" -> {
                                TrueFalseOptions(
                                    selectedIndex = selectedIndex,
                                    correctIndex = animatedQuestion.correctOptionIndex,
                                    showFeedback = showFeedback,
                                    onSelect = { index ->
                                        selectedIndex = index
                                    }
                                )
                            }

                            "complete_code" -> {
                                animatedQuestion.optionsRes.forEachIndexed { index, optionRes ->
                                    CodeAnswerOptionCard(
                                        text = stringResource(optionRes),
                                        index = index,
                                        selectedIndex = selectedIndex,
                                        correctIndex = animatedQuestion.correctOptionIndex,
                                        showFeedback = showFeedback,
                                        topicPersonality = topicPersonality,
                                        onClick = {
                                            selectedIndex = index
                                        }
                                    )

                                    Spacer(modifier = Modifier.height(12.dp))
                                }
                            }

                            "match_concept" -> {
                                MatchConceptOptions(
                                    question = animatedQuestion,
                                    selectedIndex = selectedIndex,
                                    showFeedback = showFeedback,
                                    topicPersonality = topicPersonality,
                                    onSelect = { index ->
                                        selectedIndex = index
                                    }
                                )
                            }

                            "detect_error" -> {
                                DetectErrorOptions(
                                    question = animatedQuestion,
                                    selectedIndex = selectedIndex,
                                    showFeedback = showFeedback,
                                    topicPersonality = topicPersonality,
                                    onSelect = { index ->
                                        selectedIndex = index
                                    }
                                )
                            }

                            "console_output" -> {
                                ConsoleOutputOptions(
                                    question = animatedQuestion,
                                    selectedIndex = selectedIndex,
                                    showFeedback = showFeedback,
                                    topicPersonality = topicPersonality,
                                    onSelect = { index ->
                                        selectedIndex = index
                                    }
                                )
                            }

                            "order_code" -> {
                                OrderCodeOptions(
                                    options = shuffledOrderOptions,
                                    selectedDisplayIndexes = selectedOrderDisplayIndexes,
                                    showFeedback = showFeedback,
                                    topicPersonality = topicPersonality,
                                    onToggle = { displayIndex, originalIndex ->
                                        if (showFeedback) {
                                            return@OrderCodeOptions
                                        }

                                        val existingPosition =
                                            selectedOrderDisplayIndexes.indexOf(displayIndex)

                                        if (existingPosition != -1) {
                                            selectedOrderDisplayIndexes.removeAt(existingPosition)
                                            selectedOrderOriginalIndexes.removeAt(existingPosition)
                                        } else {
                                            selectedOrderDisplayIndexes.add(displayIndex)
                                            selectedOrderOriginalIndexes.add(originalIndex)
                                        }
                                    }
                                )
                            }

                            "technical_wordle" -> {
                                HackerHangmanOptions(
                                    question = animatedQuestion,
                                    targetWord = hangmanTargetWord,
                                    guessedLetters = guessedLetters,
                                    currentLetter = currentHangmanLetter,
                                    attemptsLeft = hangmanAttemptsLeft,
                                    maxAttempts = 5,
                                    showFeedback = showFeedback,
                                    topicPersonality = topicPersonality,
                                    themeItemId = equippedThemeId,
                                    effectItemId = equippedEffectId,
                                    onLetterChange = { value ->
                                        currentHangmanLetter = value
                                            .take(1)
                                            .uppercase()
                                            .filter { it.isLetterOrDigit() }
                                    }
                                )
                            }

                            "crossword" -> {
                                CrosswordOptions(
                                    question = animatedQuestion,
                                    selectedIndex = selectedIndex,
                                    showFeedback = showFeedback,
                                    topicPersonality = topicPersonality,
                                    onSelect = { index ->
                                        selectedIndex = index
                                    }
                                )
                            }

                            else -> {
                                animatedQuestion.optionsRes.forEachIndexed { index, optionRes ->
                                    AnswerOptionCard(
                                        text = stringResource(optionRes),
                                        index = index,
                                        selectedIndex = selectedIndex,
                                        correctIndex = animatedQuestion.correctOptionIndex,
                                        showFeedback = showFeedback,
                                        topicPersonality = topicPersonality,
                                        onClick = {
                                            selectedIndex = index
                                        }
                                    )

                                    Spacer(modifier = Modifier.height(14.dp))
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            if (showFeedback) {
                FeedbackBlock(
                    question = currentQuestion,
                    gameType = gameType,
                    selectedIndex = selectedIndex,
                    selectedOrderOriginalIndexes = selectedOrderOriginalIndexes.toList(),
                    isTimeOut = isTimeOut,
                    topicPersonality = topicPersonality,
                    themeItemId = equippedThemeId,
                    effectItemId = equippedEffectId
                )

                Spacer(modifier = Modifier.height(12.dp))
            }

            WivoButton(
                text = buttonText,
                onClick = {
                    if (!showFeedback) {
                        if (gameType == "technical_wordle") {
                            val letter = currentHangmanLetter.firstOrNull()

                            if (letter == null) {
                                return@WivoButton
                            }

                            if (letter in guessedLetters) {
                                currentHangmanLetter = ""
                                return@WivoButton
                            }

                            guessedLetters.add(letter)

                            if (letter !in hangmanTargetWord) {
                                hangmanAttemptsLeft--
                            }

                            currentHangmanLetter = ""

                            val solved = hangmanTargetWord.all { char ->
                                char in guessedLetters
                            }

                            val failed = hangmanAttemptsLeft <= 0

                            if (!solved && !failed) {
                                return@WivoButton
                            }

                            selectedIndex = if (solved) {
                                currentQuestion.correctOptionIndex
                            } else {
                                -1
                            }

                            val isCorrect = solved

                            if (isCorrect) {
                                correctAnswers++

                                val newStreak = currentStreak + 1
                                currentStreak = newStreak
                                bestStreak = maxOf(bestStreak, newStreak)

                                if (settingsRepository.isHapticsEnabled()) {
                                    HapticUtils.success(context)
                                }

                                if (settingsRepository.isSoundEnabled()) {
                                    SoundUtils.playCorrect(context)
                                }
                            } else {
                                currentStreak = 0

                                if (settingsRepository.isHapticsEnabled()) {
                                    HapticUtils.error(context)
                                }

                                if (settingsRepository.isSoundEnabled()) {
                                    SoundUtils.playWrong(context)
                                }
                            }

                            showFeedback = true
                            return@WivoButton
                        }

                        if (gameType == "order_code") {
                            if (selectedOrderOriginalIndexes.size != currentQuestion.optionsRes.size) {
                                return@WivoButton
                            }
                        } else {
                            if (selectedIndex == -1) {
                                return@WivoButton
                            }
                        }

                        isTimeOut = false

                        val isCorrect = isAnswerCorrect(
                            question = currentQuestion,
                            gameType = gameType,
                            selectedIndex = selectedIndex,
                            selectedOrderOriginalIndexes = selectedOrderOriginalIndexes.toList()
                        )

                        if (isCorrect) {
                            correctAnswers++

                            val newStreak = currentStreak + 1
                            currentStreak = newStreak
                            bestStreak = maxOf(bestStreak, newStreak)

                            if (settingsRepository.isHapticsEnabled()) {
                                HapticUtils.success(context)
                            }

                            if (settingsRepository.isSoundEnabled()) {
                                SoundUtils.playCorrect(context)
                            }
                        } else {
                            currentStreak = 0

                            if (settingsRepository.isHapticsEnabled()) {
                                HapticUtils.error(context)
                            }

                            if (settingsRepository.isSoundEnabled()) {
                                SoundUtils.playWrong(context)
                            }
                        }

                        showFeedback = true


                    } else {
                        if (currentIndex < questions.lastIndex) {
                            showFeedback = false
                            selectedIndex = -1
                            isTimeOut = false

                            selectedOrderOriginalIndexes.clear()
                            selectedOrderDisplayIndexes.clear()

                            guessedLetters.clear()
                            currentHangmanLetter = ""
                            hangmanAttemptsLeft = 5

                            currentIndex++
                        } else {
                            firestoreRepository.updateDailyMissionAfterGame(
                                questionsAnswered = questions.size,
                                onSuccess = {
                                    onFinishGame(correctAnswers, questions.size, bestStreak)
                                },
                                onError = {
                                    onFinishGame(correctAnswers, questions.size, bestStreak)
                                }
                            )
                        }
                    }
                },
                buttonStyleItemId = equippedButtonStyleId,
                themeItemId = equippedThemeId
            )

            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
private fun GameTopBlock(
    gameType: String,
    difficulty: String,
    topicPersonality: TopicPersonality,
    currentIndex: Int,
    totalQuestions: Int,
    progress: Float,
    currentStreak: Int,
    bestStreak: Int,
    timeLeft: Int,
    showTimer: Boolean
) {
    Text(
        text = "${topicPersonality.icon} ${topicPersonality.title}",
        style = MaterialTheme.typography.headlineSmall,
        fontWeight = FontWeight.Bold,
        color = topicPersonality.color
    )

    Spacer(modifier = Modifier.height(4.dp))

    Text(
        text = topicPersonality.subtitle,
        color = TextSecondary,
        style = MaterialTheme.typography.bodySmall
    )

    Spacer(modifier = Modifier.height(6.dp))

    Text(
        text = "${getReadableGameType(gameType)} · ${getReadableDifficulty(difficulty)}",
        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.78f),
        style = MaterialTheme.typography.bodySmall,
        fontWeight = FontWeight.SemiBold
    )

    Spacer(modifier = Modifier.height(10.dp))

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = stringResource(
                R.string.question_counter,
                currentIndex + 1,
                totalQuestions
            ),
            color = TextSecondary
        )

        if (showTimer) {
            Text(
                text = stringResource(R.string.time_left, timeLeft),
                color = when {
                    timeLeft <= 5 -> MaterialTheme.colorScheme.error
                    timeLeft <= 10 -> topicPersonality.color
                    else -> TextSecondary
                },
                fontWeight = FontWeight.Bold
            )
        }
    }

    Spacer(modifier = Modifier.height(6.dp))

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = stringResource(R.string.game_current_streak, currentStreak),
            color = if (currentStreak > 0) GreenAccent else TextSecondary,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = stringResource(R.string.game_best_streak, bestStreak),
            color = TextSecondary,
            style = MaterialTheme.typography.bodySmall
        )
    }

    Spacer(modifier = Modifier.height(10.dp))

    LinearProgressIndicator(
        progress = { progress },
        modifier = Modifier.fillMaxWidth(),
        color = topicPersonality.color,
        trackColor = MaterialTheme.colorScheme.surface
    )
}

@Composable
private fun QuestionCard(
    question: Question,
    gameType: String,
    topicPersonality: TopicPersonality,
    themeItemId: String?,
    effectItemId: String?
) {
    when (gameType) {
        "complete_code",
        "detect_error",
        "console_output",
        "order_code" -> {
            CodeQuestionCard(
                question = question,
                gameType = gameType,
                topicPersonality = topicPersonality,
                themeItemId = themeItemId,
                effectItemId = effectItemId
            )
        }

        "technical_wordle" -> {
            SimpleLabelQuestionCard(
                label = stringResource(R.string.game_technical_hangman_label),
                question = question,
                topicPersonality = topicPersonality,
                themeItemId = themeItemId,
                effectItemId = effectItemId
            )
        }

        "crossword" -> {
            SimpleLabelQuestionCard(
                label = stringResource(R.string.game_crossword_label),
                question = question,
                topicPersonality = topicPersonality,
                themeItemId = themeItemId,
                effectItemId = effectItemId
            )
        }

        "match_concept" -> {
            SimpleLabelQuestionCard(
                label = stringResource(R.string.game_technical_concept_label),
                question = question,
                topicPersonality = topicPersonality,
                themeItemId = themeItemId,
                effectItemId = effectItemId
            )
        }

        else -> {
            WivoCard(
                themeItemId = themeItemId,
                effectItemId = effectItemId
            ) {
                Text(
                    text = stringResource(question.questionTextRes),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Composable
private fun SimpleLabelQuestionCard(
    label: String,
    question: Question,
    topicPersonality: TopicPersonality,
    themeItemId: String?,
    effectItemId: String?
) {
    WivoCard(
        themeItemId = themeItemId,
        effectItemId = effectItemId
    ) {
        Text(
            text = label,
            color = topicPersonality.color,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(question.questionTextRes),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun AnswerOptionCard(
    text: String,
    index: Int,
    selectedIndex: Int,
    correctIndex: Int,
    showFeedback: Boolean,
    topicPersonality: TopicPersonality,
    onClick: () -> Unit
) {
    val targetColor = when {
        showFeedback && index == correctIndex -> GreenAccent
        showFeedback && index == selectedIndex && selectedIndex != correctIndex -> MaterialTheme.colorScheme.error
        selectedIndex == index -> topicPersonality.color
        else -> CardDark
    }

    val backgroundColor by animateColorAsState(
        targetValue = targetColor,
        label = "answerColor"
    )

    val targetScale = when {
        showFeedback && index == correctIndex -> 1.02f
        selectedIndex == index -> 1.01f
        else -> 1f
    }

    val scale by animateFloatAsState(
        targetValue = targetScale,
        label = "answerScale"
    )

    Card(
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        shape = RoundedCornerShape(18.dp),
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale)
            .clickable(enabled = !showFeedback) { onClick() }
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(18.dp),
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = if (selectedIndex == index) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@Composable
private fun MatchConceptOptions(
    question: Question,
    selectedIndex: Int,
    showFeedback: Boolean,
    topicPersonality: TopicPersonality,
    onSelect: (Int) -> Unit
) {
    Text(
        text = stringResource(R.string.game_match_concept_hint),
        color = TextSecondary,
        style = MaterialTheme.typography.bodySmall
    )

    Spacer(modifier = Modifier.height(12.dp))

    question.optionsRes.forEachIndexed { index, optionRes ->
        AnswerOptionCard(
            text = stringResource(optionRes),
            index = index,
            selectedIndex = selectedIndex,
            correctIndex = question.correctOptionIndex,
            showFeedback = showFeedback,
            topicPersonality = topicPersonality,
            onClick = {
                onSelect(index)
            }
        )

        Spacer(modifier = Modifier.height(12.dp))
    }
}

@Composable
private fun DetectErrorOptions(
    question: Question,
    selectedIndex: Int,
    showFeedback: Boolean,
    topicPersonality: TopicPersonality,
    onSelect: (Int) -> Unit
) {
    Text(
        text = stringResource(R.string.game_detect_error_hint),
        color = TextSecondary,
        style = MaterialTheme.typography.bodySmall
    )

    Spacer(modifier = Modifier.height(12.dp))

    question.optionsRes.forEachIndexed { index, optionRes ->
        CodeAnswerOptionCard(
            text = stringResource(optionRes),
            index = index,
            selectedIndex = selectedIndex,
            correctIndex = question.correctOptionIndex,
            showFeedback = showFeedback,
            topicPersonality = topicPersonality,
            onClick = {
                onSelect(index)
            }
        )

        Spacer(modifier = Modifier.height(12.dp))
    }
}

@Composable
private fun ConsoleOutputOptions(
    question: Question,
    selectedIndex: Int,
    showFeedback: Boolean,
    topicPersonality: TopicPersonality,
    onSelect: (Int) -> Unit
) {
    Text(
        text = stringResource(R.string.game_console_output_hint),
        color = TextSecondary,
        style = MaterialTheme.typography.bodySmall
    )

    Spacer(modifier = Modifier.height(12.dp))

    question.optionsRes.forEachIndexed { index, optionRes ->
        CodeAnswerOptionCard(
            text = stringResource(optionRes),
            index = index,
            selectedIndex = selectedIndex,
            correctIndex = question.correctOptionIndex,
            showFeedback = showFeedback,
            topicPersonality = topicPersonality,
            onClick = {
                onSelect(index)
            }
        )

        Spacer(modifier = Modifier.height(12.dp))
    }
}

@Composable
private fun CrosswordOptions(
    question: Question,
    selectedIndex: Int,
    showFeedback: Boolean,
    topicPersonality: TopicPersonality,
    onSelect: (Int) -> Unit
) {
    Text(
        text = stringResource(R.string.game_crossword_hint),
        color = TextSecondary,
        style = MaterialTheme.typography.bodySmall
    )

    Spacer(modifier = Modifier.height(12.dp))

    question.optionsRes.forEachIndexed { index, optionRes ->
        AnswerOptionCard(
            text = stringResource(optionRes),
            index = index,
            selectedIndex = selectedIndex,
            correctIndex = question.correctOptionIndex,
            showFeedback = showFeedback,
            topicPersonality = topicPersonality,
            onClick = {
                onSelect(index)
            }
        )

        Spacer(modifier = Modifier.height(12.dp))
    }
}

@Composable
private fun OrderCodeOptions(
    options: List<OrderCodeOption>,
    selectedDisplayIndexes: List<Int>,
    showFeedback: Boolean,
    topicPersonality: TopicPersonality,
    onToggle: (displayIndex: Int, originalIndex: Int) -> Unit
) {
    Text(
        text = stringResource(R.string.game_order_code_hint),
        color = TextSecondary,
        style = MaterialTheme.typography.bodySmall
    )

    Spacer(modifier = Modifier.height(12.dp))

    options.forEachIndexed { displayIndex, option ->
        val selectedOrder = selectedDisplayIndexes.indexOf(displayIndex)
        val selected = selectedOrder != -1

        OrderCodeOptionCard(
            text = stringResource(option.textRes),
            selectedOrder = selectedOrder + 1,
            selected = selected,
            showFeedback = showFeedback,
            topicPersonality = topicPersonality,
            onClick = {
                onToggle(displayIndex, option.originalIndex)
            }
        )

        Spacer(modifier = Modifier.height(12.dp))
    }
}

@Composable
private fun OrderCodeOptionCard(
    text: String,
    selectedOrder: Int,
    selected: Boolean,
    showFeedback: Boolean,
    topicPersonality: TopicPersonality,
    onClick: () -> Unit
) {
    val targetColor = when {
        selected -> topicPersonality.color.copy(alpha = 0.32f)
        else -> CardDark
    }

    val backgroundColor by animateColorAsState(
        targetValue = targetColor,
        label = "orderCodeColor"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = !showFeedback) {
                onClick()
            },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (selected) "$selectedOrder" else "•",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = if (selected) topicPersonality.color else TextSecondary
            )

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
private fun HackerHangmanOptions(
    question: Question,
    targetWord: String,
    guessedLetters: List<Char>,
    currentLetter: String,
    attemptsLeft: Int,
    maxAttempts: Int,
    showFeedback: Boolean,
    topicPersonality: TopicPersonality,
    themeItemId: String?,
    effectItemId: String?,
    onLetterChange: (String) -> Unit
) {
    val maskedWord = targetWord.map { char ->
        if (char in guessedLetters) {
            char.toString()
        } else {
            "_"
        }
    }.joinToString(" ")

    HackerHangmanStatus(
        attemptsLeft = attemptsLeft,
        maxAttempts = maxAttempts,
        topicPersonality = topicPersonality,
        themeItemId = themeItemId,
        effectItemId = effectItemId
    )

    Spacer(modifier = Modifier.height(12.dp))

    WivoCard(
        themeItemId = themeItemId,
        effectItemId = effectItemId
    ) {
        Text(
            text = stringResource(R.string.game_hangman_encrypted_word),
            color = topicPersonality.color,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = maskedWord,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(
                R.string.game_hangman_tried_letters,
                if (guessedLetters.isEmpty()) {
                    stringResource(R.string.game_hangman_none)
                } else {
                    guessedLetters.joinToString(" · ")
                }
            ),
            color = TextSecondary,
            style = MaterialTheme.typography.bodySmall
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = currentLetter,
            onValueChange = onLetterChange,
            enabled = !showFeedback,
            singleLine = true,
            label = {
                Text(stringResource(R.string.game_hangman_enter_letter))
            },
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Characters,
                keyboardType = KeyboardType.Text
            ),
            modifier = Modifier
                .fillMaxWidth()
                .imePadding()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(R.string.game_hangman_try_letter_hint),
            color = TextSecondary,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
private fun HackerHangmanStatus(
    attemptsLeft: Int,
    maxAttempts: Int,
    topicPersonality: TopicPersonality,
    themeItemId: String?,
    effectItemId: String?
) {
    val errors = maxAttempts - attemptsLeft
    val progress = errors.toFloat() / maxAttempts.toFloat()

    val failureLines = listOf(
        "> breach_attempt_01 failed",
        "> decrypt_node_02 failed",
        "> firewall_trace_detected",
        "> access_token_corrupted",
        "> root_channel_locked",
        "> system_lockdown_triggered"
    )

    WivoCard(
        themeItemId = themeItemId,
        effectItemId = effectItemId
    ) {
        Text(
            text = stringResource(R.string.game_hangman_access_terminal),
            color = topicPersonality.color,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(10.dp))

        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier.fillMaxWidth(),
            color = if (errors >= maxAttempts) {
                MaterialTheme.colorScheme.error
            } else {
                topicPersonality.color
            },
            trackColor = MaterialTheme.colorScheme.surface
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = stringResource(R.string.game_hangman_errors_detected, errors, maxAttempts),
            color = TextSecondary,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(10.dp))

        if (errors == 0) {
            Text(
                text = "> secure_session_started",
                color = GreenAccent,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.SemiBold
            )
        } else {
            failureLines.take(errors).forEach { line ->
                Text(
                    text = line,
                    color = if (errors >= maxAttempts) {
                        MaterialTheme.colorScheme.error
                    } else {
                        GreenAccent
                    },
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(4.dp))
            }
        }
    }
}

@SuppressLint("LocalContextGetResourceValueCall")
@Composable
private fun FeedbackBlock(
    question: Question,
    gameType: String,
    selectedIndex: Int,
    selectedOrderOriginalIndexes: List<Int>,
    isTimeOut: Boolean,
    topicPersonality: TopicPersonality,
    themeItemId: String?,
    effectItemId: String?
) {
    val isCorrect = !isTimeOut && isAnswerCorrect(
        question = question,
        gameType = gameType,
        selectedIndex = selectedIndex,
        selectedOrderOriginalIndexes = selectedOrderOriginalIndexes
    )

    val context = LocalContext.current

    val correctAnswer = when (gameType) {
        "order_code" -> question.optionsRes.joinToString(separator = " → ") { optionRes ->
            context.getString(optionRes)
        }

        else -> question.optionsRes
            .getOrNull(question.correctOptionIndex)
            ?.let { optionRes -> stringResource(optionRes) }
            .orEmpty()
    }

    WivoCard(
        themeItemId = themeItemId,
        effectItemId = effectItemId
    ) {
        Text(
            text = when {
                isTimeOut -> stringResource(R.string.time_out_title)
                isCorrect -> stringResource(R.string.feedback_correct_title)
                else -> stringResource(R.string.feedback_wrong_title)
            },
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = if (isCorrect) GreenAccent else MaterialTheme.colorScheme.error
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (isTimeOut) {
            Text(
                text = stringResource(R.string.time_out_message),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.82f)
            )

            Spacer(modifier = Modifier.height(8.dp))
        }

        if (!isCorrect) {
            Text(
                text = stringResource(R.string.feedback_correct_answer, correctAnswer),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(8.dp))
        }

        Text(
            text = stringResource(question.explanationRes),
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.82f)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = topicPersonality.footer,
            color = topicPersonality.color,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun TrueFalseOptions(
    selectedIndex: Int,
    correctIndex: Int,
    showFeedback: Boolean,
    onSelect: (Int) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        TrueFalseButton(
            text = stringResource(R.string.true_option),
            emoji = "✅",
            index = 0,
            selectedIndex = selectedIndex,
            correctIndex = correctIndex,
            showFeedback = showFeedback,
            baseColor = GreenAccent,
            modifier = Modifier.weight(1f),
            onClick = {
                onSelect(0)
            }
        )

        TrueFalseButton(
            text = stringResource(R.string.false_option),
            emoji = "❌",
            index = 1,
            selectedIndex = selectedIndex,
            correctIndex = correctIndex,
            showFeedback = showFeedback,
            baseColor = MaterialTheme.colorScheme.error,
            modifier = Modifier.weight(1f),
            onClick = {
                onSelect(1)
            }
        )
    }
}

@Composable
private fun TrueFalseButton(
    text: String,
    emoji: String,
    index: Int,
    selectedIndex: Int,
    correctIndex: Int,
    showFeedback: Boolean,
    baseColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val targetColor = when {
        showFeedback && index == correctIndex -> GreenAccent
        showFeedback && index == selectedIndex && selectedIndex != correctIndex -> MaterialTheme.colorScheme.error
        selectedIndex == index -> baseColor.copy(alpha = 0.85f)
        else -> baseColor.copy(alpha = 0.22f)
    }

    val backgroundColor by animateColorAsState(
        targetValue = targetColor,
        label = "trueFalseColor"
    )

    val targetScale = when {
        showFeedback && index == correctIndex -> 1.04f
        selectedIndex == index -> 1.03f
        else -> 1f
    }

    val scale by animateFloatAsState(
        targetValue = targetScale,
        label = "trueFalseScale"
    )

    Card(
        modifier = modifier
            .height(116.dp)
            .scale(scale)
            .clickable(enabled = !showFeedback) {
                onClick()
            },
        shape = RoundedCornerShape(26.dp),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = emoji,
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = text,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
private fun CodeQuestionCard(
    question: Question,
    gameType: String,
    topicPersonality: TopicPersonality,
    themeItemId: String?,
    effectItemId: String?
) {
    val questionText = stringResource(question.questionTextRes)
    val parts = splitCodeQuestionText(questionText)
    val instructionText = parts.first
    val codeText = parts.second

    WivoCard(
        themeItemId = themeItemId,
        effectItemId = effectItemId
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = getCodeModeLabel(gameType),
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = topicPersonality.color
            )

            Text(
                text = "● ● ●",
                style = MaterialTheme.typography.labelMedium,
                color = GreenAccent
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = getCodeModeHint(gameType),
            style = MaterialTheme.typography.bodySmall,
            color = TextSecondary
        )

        if (instructionText.isNotBlank()) {
            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = instructionText,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        CodeBlock(code = codeText)
    }
}

@Composable
private fun CodeAnswerOptionCard(
    text: String,
    index: Int,
    selectedIndex: Int,
    correctIndex: Int,
    showFeedback: Boolean,
    topicPersonality: TopicPersonality,
    onClick: () -> Unit
) {
    val targetColor = when {
        showFeedback && index == correctIndex -> GreenAccent.copy(alpha = 0.28f)
        showFeedback && index == selectedIndex && selectedIndex != correctIndex -> MaterialTheme.colorScheme.error.copy(alpha = 0.26f)
        selectedIndex == index -> topicPersonality.color.copy(alpha = 0.30f)
        else -> CardDark
    }

    val backgroundColor by animateColorAsState(
        targetValue = targetColor,
        label = "codeAnswerColor"
    )

    val targetScale = when {
        showFeedback && index == correctIndex -> 1.02f
        selectedIndex == index -> 1.01f
        else -> 1f
    }

    val scale by animateFloatAsState(
        targetValue = targetScale,
        label = "codeAnswerScale"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale)
            .clickable(enabled = !showFeedback) { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = ">",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = topicPersonality.color
            )

            Spacer(modifier = Modifier.width(10.dp))

            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
private fun EmptyQuestionsState(
    topic: String,
    gameType: String,
    backgroundItemId: String?,
    themeItemId: String?
) {
    WivoScreen(
        backgroundItemId = backgroundItemId,
        themeItemId = themeItemId
    ) {
        Text(
            text = stringResource(R.string.no_questions, topic, gameType),
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

private fun isAnswerCorrect(
    question: Question,
    gameType: String,
    selectedIndex: Int,
    selectedOrderOriginalIndexes: List<Int>
): Boolean {
    return if (gameType == "order_code") {
        selectedOrderOriginalIndexes == question.optionsRes.indices.toList()
    } else {
        selectedIndex == question.correctOptionIndex
    }
}

private fun splitCodeQuestionText(text: String): Pair<String, String> {
    val normalizedText = text.trim()

    val parts = normalizedText.split(
        delimiters = arrayOf("\n\n"),
        limit = 2
    )

    return if (parts.size == 2) {
        parts[0].trim() to parts[1].trim()
    } else {
        "" to normalizedText
    }
}

@Composable
private fun getReadableDifficulty(difficulty: String): String {
    return when (difficulty) {
        "easy" -> stringResource(R.string.difficulty_easy)
        "medium" -> stringResource(R.string.difficulty_medium)
        "hard" -> stringResource(R.string.difficulty_hard)
        else -> difficulty
    }
}

@Composable
private fun getReadableGameType(gameType: String): String {
    return when (gameType) {
        "test" -> stringResource(R.string.game_type_test)
        "true_false" -> stringResource(R.string.game_type_true_false)
        "complete_code" -> stringResource(R.string.game_type_complete_code)
        "match_concept" -> stringResource(R.string.game_type_match_concept)
        "detect_error" -> stringResource(R.string.game_type_detect_error)
        "console_output" -> stringResource(R.string.game_type_console_output)
        "order_code" -> stringResource(R.string.game_type_order_code)
        "technical_wordle" -> stringResource(R.string.game_type_technical_wordle)
        "crossword" -> stringResource(R.string.game_type_crossword)
        else -> gameType
    }
}

@Composable
private fun getCodeModeLabel(gameType: String): String {
    return when (gameType) {
        "detect_error" -> stringResource(R.string.game_code_label_debugger)
        "console_output" -> stringResource(R.string.game_code_label_console)
        "order_code" -> stringResource(R.string.game_code_label_order)
        else -> stringResource(R.string.game_code_label_console)
    }
}

@Composable
private fun getCodeModeHint(gameType: String): String {
    return when (gameType) {
        "detect_error" -> stringResource(R.string.game_detect_error_hint)
        "console_output" -> stringResource(R.string.game_console_output_hint)
        "order_code" -> stringResource(R.string.game_code_order_hint_short)
        else -> stringResource(R.string.game_complete_code_hint)
    }
}

private data class OrderCodeOption(
    val originalIndex: Int,
    @StringRes val textRes: Int
)