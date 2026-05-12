package com.icb.iwivo.ui.screens.game

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.icb.iwivo.R
import com.icb.iwivo.ui.theme.BluePrimary
import com.icb.iwivo.ui.theme.GreenAccent
import com.icb.iwivo.ui.theme.PurplePrimary

data class TopicPersonality(
    val title: String,
    val subtitle: String,
    val footer: String,
    val icon: String,
    val color: Color
)

@Composable
fun getTopicPersonality(topic: String): TopicPersonality {
    return when (topic) {
        "java" -> TopicPersonality(
            title = stringResource(R.string.topic_java_title),
            subtitle = stringResource(R.string.topic_java_subtitle),
            footer = stringResource(R.string.topic_java_footer),
            icon = "☕",
            color = PurplePrimary
        )

        "kotlin" -> TopicPersonality(
            title = stringResource(R.string.topic_kotlin_title),
            subtitle = stringResource(R.string.topic_kotlin_subtitle),
            footer = stringResource(R.string.topic_kotlin_footer),
            icon = "🟦",
            color = BluePrimary
        )

        "sql" -> TopicPersonality(
            title = stringResource(R.string.topic_sql_title),
            subtitle = stringResource(R.string.topic_sql_subtitle),
            footer = stringResource(R.string.topic_sql_footer),
            icon = "🗄️",
            color = GreenAccent
        )

        "javascript" -> TopicPersonality(
            title = stringResource(R.string.topic_javascript_title),
            subtitle = stringResource(R.string.topic_javascript_subtitle),
            footer = stringResource(R.string.topic_javascript_footer),
            icon = "🟨",
            color = Color(0xFFFFD54F)
        )

        "html_css" -> TopicPersonality(
            title = stringResource(R.string.topic_html_css_title),
            subtitle = stringResource(R.string.topic_html_css_subtitle),
            footer = stringResource(R.string.topic_html_css_footer),
            icon = "🎨",
            color = Color(0xFFFF8A3D)
        )

        "git" -> TopicPersonality(
            title = stringResource(R.string.topic_git_title),
            subtitle = stringResource(R.string.topic_git_subtitle),
            footer = stringResource(R.string.topic_git_footer),
            icon = "🐱",
            color = Color(0xFFFF5A5F)
        )

        "android" -> TopicPersonality(
            title = stringResource(R.string.topic_android_title),
            subtitle = stringResource(R.string.topic_android_subtitle),
            footer = stringResource(R.string.topic_android_footer),
            icon = "🤖",
            color = GreenAccent
        )

        "firebase" -> TopicPersonality(
            title = stringResource(R.string.topic_firebase_title),
            subtitle = stringResource(R.string.topic_firebase_subtitle),
            footer = stringResource(R.string.topic_firebase_footer),
            icon = "🔥",
            color = Color(0xFFFFB300)
        )

        "spring" -> TopicPersonality(
            title = stringResource(R.string.topic_spring_title),
            subtitle = stringResource(R.string.topic_spring_subtitle),
            footer = stringResource(R.string.topic_spring_footer),
            icon = "🍃",
            color = Color(0xFF7CB342)
        )

        else -> TopicPersonality(
            title = topic.uppercase(),
            subtitle = stringResource(R.string.topic_default_subtitle),
            footer = stringResource(R.string.topic_default_footer),
            icon = "⚡",
            color = PurplePrimary
        )
    }
}
