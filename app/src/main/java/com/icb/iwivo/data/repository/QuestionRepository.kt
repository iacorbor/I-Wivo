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
        ),Question(
            id = "java_code_001",
            topic = "java",
            gameType = "complete_code",
            questionText = "Completa el código:\n\n____ Persona {\n}",
            options = listOf("class", "object", "fun", "interface"),
            correctOptionIndex = 0,
            explanation = "En Java se usa 'class' para declarar una clase."
        ),
        Question(
            id = "java_code_002",
            topic = "java",
            gameType = "complete_code",
            questionText = "Completa el código:\n\nSystem.out.____(\"Hola\");",
            options = listOf("printLine", "println", "echo", "write"),
            correctOptionIndex = 1,
            explanation = "System.out.println() imprime texto y añade salto de línea."
        ),
        Question(
            id = "kotlin_code_001",
            topic = "kotlin",
            gameType = "complete_code",
            questionText = "Completa el código:\n\n____ nombre = \"Isra\"",
            options = listOf("var", "val", "let", "const"),
            correctOptionIndex = 1,
            explanation = "'val' declara una variable de solo lectura en Kotlin."
        ),
        Question(
            id = "kotlin_code_002",
            topic = "kotlin",
            gameType = "complete_code",
            questionText = "Completa el código:\n\nfun saludar() {\n    ____(\"Hola\")\n}",
            options = listOf("console", "echo", "print", "show"),
            correctOptionIndex = 2,
            explanation = "En Kotlin puedes usar print() para imprimir texto."
        ),
        Question(
            id = "sql_code_001",
            topic = "sql",
            gameType = "complete_code",
            questionText = "Completa la consulta:\n\n____ * FROM usuarios;",
            options = listOf("GET", "SELECT", "FIND", "READ"),
            correctOptionIndex = 1,
            explanation = "SELECT se utiliza para consultar datos."
        ),
        Question(
            id = "sql_code_002",
            topic = "sql",
            gameType = "complete_code",
            questionText = "Completa la consulta:\n\nDELETE FROM usuarios ____ id = 1;",
            options = listOf("WHEN", "WHERE", "IF", "FILTER"),
            correctOptionIndex = 1,
            explanation = "WHERE filtra qué registros se eliminan."
        )




    )

    fun getQuestions(topic: String, gameType: String): List<Question> {
        return questions.filter {
            it.topic == topic && it.gameType == gameType
        }
    }
}