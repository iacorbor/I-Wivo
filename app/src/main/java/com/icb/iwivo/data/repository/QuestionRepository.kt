package com.icb.iwivo.data.repository

import com.icb.iwivo.data.model.Question

class QuestionRepository {

    private val questions = listOf(
        Question(
            id = "java_001",
            topic = "java",
            gameType = "test",
            questionText = "¿Qué palabra clave se usa en Java para crear una clase?",
            options = listOf("class", "object", "fun", "struct"),
            correctOptionIndex = 0,
            explanation = "En Java se usa la palabra clave 'class' para declarar una clase."
        ),
        Question(
            id = "java_002",
            topic = "java",
            gameType = "test",
            questionText = "¿Qué método es el punto de entrada de una aplicación Java?",
            options = listOf("start()", "run()", "main()", "init()"),
            correctOptionIndex = 2,
            explanation = "El método main() es el punto de entrada principal en Java."
        ),
        Question(
            id = "kotlin_001",
            topic = "kotlin",
            gameType = "test",
            questionText = "¿Qué palabra clave declara una variable inmutable en Kotlin?",
            options = listOf("var", "val", "let", "const"),
            correctOptionIndex = 1,
            explanation = "val declara una variable de solo lectura en Kotlin."
        ),
        Question(
            id = "sql_001",
            topic = "sql",
            gameType = "test",
            questionText = "¿Qué sentencia se usa para consultar datos en SQL?",
            options = listOf("GET", "SELECT", "READ", "FIND"),
            correctOptionIndex = 1,
            explanation = "SELECT se utiliza para consultar datos en una base de datos."
        ),Question(
            id = "java_tf_001",
            topic = "java",
            gameType = "true_false",
            questionText = "En Java, una clase puede heredar de varias clases al mismo tiempo.",
            options = listOf("Verdadero", "Falso"),
            correctOptionIndex = 1,
            explanation = "Java no permite herencia múltiple de clases. Solo permite implementar varias interfaces."
        ),
        Question(
            id = "kotlin_tf_001",
            topic = "kotlin",
            gameType = "true_false",
            questionText = "En Kotlin, val se usa para declarar una variable de solo lectura.",
            options = listOf("Verdadero", "Falso"),
            correctOptionIndex = 0,
            explanation = "Correcto. val declara una referencia de solo lectura."
        ),
        Question(
            id = "sql_tf_001",
            topic = "sql",
            gameType = "true_false",
            questionText = "La sentencia SELECT se utiliza para eliminar registros de una tabla.",
            options = listOf("Verdadero", "Falso"),
            correctOptionIndex = 1,
            explanation = "SELECT consulta datos. Para eliminar registros se usa DELETE."
        )



    )

    fun getQuestions(topic: String, gameType: String): List<Question> {
        return questions.filter {
            it.topic == topic && it.gameType == gameType
        }
    }
}