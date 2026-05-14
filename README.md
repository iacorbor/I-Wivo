# I-Wivo

**I-Wivo** es una aplicación móvil nativa para Android diseñada para aprender y reforzar conocimientos de programación mediante una experiencia gamificada tipo quiz.

La aplicación permite practicar diferentes lenguajes y tecnologías como **Java, Kotlin, JavaScript, Android, SQL, HTML/CSS, Git, Spring y Firebase**, ofreciendo preguntas por dificultad y distintos modos de juego.

---

## 📱 Descripción 

I-Wivo nace como una aplicación educativa enfocada en hacer más dinámico el aprendizaje de programación. Su objetivo es que el usuario pueda practicar de forma rápida, visual e interactiva, obteniendo progreso, experiencia, monedas, rachas y logros a medida que avanza.

La app combina aprendizaje, juego y seguimiento del progreso del usuario en una interfaz moderna, oscura y minimalista.

---

## 🎯 Objetivos

- Facilitar el aprendizaje de conceptos de programación.
- Ofrecer preguntas clasificadas por temática y dificultad.
- Motivar al usuario mediante recompensas, niveles, monedas y logros.
- Permitir una experiencia multilingüe en español e inglés.
- Crear una aplicación Android moderna usando tecnologías actuales.
- Integrar autenticación, persistencia de datos y progreso del usuario.

---

## 🚀 Funcionalidades principales

- Registro e inicio de sesión de usuarios.
- Selección de temas de programación.
- Diferentes modos de juego:
  - Tipo test.
  - Verdadero o falso.
  - Completar código.
  - Ordenar código.
- Sistema de puntuación.
- Experiencia y subida de nivel.
- Monedas como recompensa.
- Rachas de actividad.
- Logros desbloqueables.
- Perfil de usuario.
- Tienda con elementos desbloqueables.
- Soporte multilenguaje:
  - Español.
  - Inglés.
- Pantalla de resultados con resumen de la partida.
- Guardado de progreso en Firebase.

---

## 🧠 Temáticas incluidas

La aplicación incluye preguntas relacionadas con:

- Java
- Kotlin
- JavaScript
- Android
- SQL
- HTML/CSS
- Git
- Spring
- Firebase

---

## 🛠️ Tecnologías utilizadas

- **Kotlin**
- **Jetpack Compose**
- **Material 3**
- **Navigation Compose**
- **Firebase Authentication**
- **Cloud Firestore**
- **Android Studio**
- **Gradle**
- **XML Resources**
- **Git / GitHub**

---

## 🏗️ Arquitectura

El proyecto sigue una estructura organizada por capas, separando la interfaz, la navegación, los modelos de datos, los repositorios y los recursos.

```text
app/
 └── src/
     └── main/
         ├── java/com/icb/iwivo/
         │   ├── data/
         │   │   ├── model/
         │   │   ├── repository/
         │   │   └── service/
         │   │
         │   ├── navigation/
         │   │
         │   ├── ui/
         │   │   ├── components/
         │   │   ├── screens/
         │   │   │   ├── login/
         │   │   │   ├── home/
         │   │   │   ├── game/
         │   │   │   ├── result/
         │   │   │   ├── profile/
         │   │   │   ├── achievements/
         │   │   │   └── shop/
         │   │   └── theme/
         │   │
         │   └── MainActivity.kt
         │
         └── res/
             ├── values/
             ├── values-en/
             ├── drawable/
             └── mipmap/
