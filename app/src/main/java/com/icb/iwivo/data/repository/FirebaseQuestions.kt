package com.icb.iwivo.data.repository.questions

import androidx.annotation.StringRes
import com.icb.iwivo.R
import com.icb.iwivo.data.model.Question

object FirebaseQuestions {

    private fun q(
        id: String,
        gameType: String,
        @StringRes questionTextRes: Int,
        optionsRes: List<Int>,
        correctOptionIndex: Int,
        @StringRes explanationRes: Int,
        difficulty: String = "easy"
    ): Question {
        return Question(
            id = id,
            topic = "firebase",
            gameType = gameType,
            questionTextRes = questionTextRes,
            optionsRes = optionsRes,
            correctOptionIndex = correctOptionIndex,
            explanationRes = explanationRes,
            difficulty = difficulty
        )
    }

    private val trueFalseOptionsRes = listOf(
        R.string.true_option,
        R.string.false_option
    )

    private val firebaseTestQuestions = listOf(
        q("firebase_test_001", "test", R.string.firebase_test_001_question, listOf(R.string.firebase_test_001_option_1, R.string.firebase_test_001_option_2, R.string.firebase_test_001_option_3, R.string.firebase_test_001_option_4), 1, R.string.firebase_test_001_explanation),
        q("firebase_test_002", "test", R.string.firebase_test_002_question, listOf(R.string.firebase_test_002_option_1, R.string.firebase_test_002_option_2, R.string.firebase_test_002_option_3, R.string.firebase_test_002_option_4), 2, R.string.firebase_test_002_explanation),
        q("firebase_test_003", "test", R.string.firebase_test_003_question, listOf(R.string.firebase_test_003_option_1, R.string.firebase_test_003_option_2, R.string.firebase_test_003_option_3, R.string.firebase_test_003_option_4), 0, R.string.firebase_test_003_explanation, "medium"),
        q("firebase_test_004", "test", R.string.firebase_test_004_question, listOf(R.string.firebase_test_004_option_1, R.string.firebase_test_004_option_2, R.string.firebase_test_004_option_3, R.string.firebase_test_004_option_4), 1, R.string.firebase_test_004_explanation),
        q("firebase_test_005", "test", R.string.firebase_test_005_question, listOf(R.string.firebase_test_005_option_1, R.string.firebase_test_005_option_2, R.string.firebase_test_005_option_3, R.string.firebase_test_005_option_4), 0, R.string.firebase_test_005_explanation),
        q("firebase_test_006", "test", R.string.firebase_test_006_question, listOf(R.string.firebase_test_006_option_1, R.string.firebase_test_006_option_2, R.string.firebase_test_006_option_3, R.string.firebase_test_006_option_4), 0, R.string.firebase_test_006_explanation),
        q("firebase_test_007", "test", R.string.firebase_test_007_question, listOf(R.string.firebase_test_007_option_1, R.string.firebase_test_007_option_2, R.string.firebase_test_007_option_3, R.string.firebase_test_007_option_4), 0, R.string.firebase_test_007_explanation),
        q("firebase_test_008", "test", R.string.firebase_test_008_question, listOf(R.string.firebase_test_008_option_1, R.string.firebase_test_008_option_2, R.string.firebase_test_008_option_3, R.string.firebase_test_008_option_4), 0, R.string.firebase_test_008_explanation),
        q("firebase_test_009", "test", R.string.firebase_test_009_question, listOf(R.string.firebase_test_009_option_1, R.string.firebase_test_009_option_2, R.string.firebase_test_009_option_3, R.string.firebase_test_009_option_4), 0, R.string.firebase_test_009_explanation),
        q("firebase_test_010", "test", R.string.firebase_test_010_question, listOf(R.string.firebase_test_010_option_1, R.string.firebase_test_010_option_2, R.string.firebase_test_010_option_3, R.string.firebase_test_010_option_4), 0, R.string.firebase_test_010_explanation),

        q("firebase_test_011", "test", R.string.firebase_test_011_question, listOf(R.string.firebase_test_011_option_1, R.string.firebase_test_011_option_2, R.string.firebase_test_011_option_3, R.string.firebase_test_011_option_4), 0, R.string.firebase_test_011_explanation),
        q("firebase_test_012", "test", R.string.firebase_test_012_question, listOf(R.string.firebase_test_012_option_1, R.string.firebase_test_012_option_2, R.string.firebase_test_012_option_3, R.string.firebase_test_012_option_4), 0, R.string.firebase_test_012_explanation),
        q("firebase_test_013", "test", R.string.firebase_test_013_question, listOf(R.string.firebase_test_013_option_1, R.string.firebase_test_013_option_2, R.string.firebase_test_013_option_3, R.string.firebase_test_013_option_4), 0, R.string.firebase_test_013_explanation, "medium"),
        q("firebase_test_014", "test", R.string.firebase_test_014_question, listOf(R.string.firebase_test_014_option_1, R.string.firebase_test_014_option_2, R.string.firebase_test_014_option_3, R.string.firebase_test_014_option_4), 0, R.string.firebase_test_014_explanation, "medium"),
        q("firebase_test_015", "test", R.string.firebase_test_015_question, listOf(R.string.firebase_test_015_option_1, R.string.firebase_test_015_option_2, R.string.firebase_test_015_option_3, R.string.firebase_test_015_option_4), 0, R.string.firebase_test_015_explanation, "medium"),
        q("firebase_test_016", "test", R.string.firebase_test_016_question, listOf(R.string.firebase_test_016_option_1, R.string.firebase_test_016_option_2, R.string.firebase_test_016_option_3, R.string.firebase_test_016_option_4), 0, R.string.firebase_test_016_explanation, "medium"),
        q("firebase_test_017", "test", R.string.firebase_test_017_question, listOf(R.string.firebase_test_017_option_1, R.string.firebase_test_017_option_2, R.string.firebase_test_017_option_3, R.string.firebase_test_017_option_4), 0, R.string.firebase_test_017_explanation, "medium"),
        q("firebase_test_018", "test", R.string.firebase_test_018_question, listOf(R.string.firebase_test_018_option_1, R.string.firebase_test_018_option_2, R.string.firebase_test_018_option_3, R.string.firebase_test_018_option_4), 0, R.string.firebase_test_018_explanation),
        q("firebase_test_019", "test", R.string.firebase_test_019_question, listOf(R.string.firebase_test_019_option_1, R.string.firebase_test_019_option_2, R.string.firebase_test_019_option_3, R.string.firebase_test_019_option_4), 0, R.string.firebase_test_019_explanation),
        q("firebase_test_020", "test", R.string.firebase_test_020_question, listOf(R.string.firebase_test_020_option_1, R.string.firebase_test_020_option_2, R.string.firebase_test_020_option_3, R.string.firebase_test_020_option_4), 0, R.string.firebase_test_020_explanation, "medium"),

        q("firebase_test_021", "test", R.string.firebase_test_021_question, listOf(R.string.firebase_test_021_option_1, R.string.firebase_test_021_option_2, R.string.firebase_test_021_option_3, R.string.firebase_test_021_option_4), 0, R.string.firebase_test_021_explanation, "medium"),
        q("firebase_test_022", "test", R.string.firebase_test_022_question, listOf(R.string.firebase_test_022_option_1, R.string.firebase_test_022_option_2, R.string.firebase_test_022_option_3, R.string.firebase_test_022_option_4), 0, R.string.firebase_test_022_explanation, "hard"),
        q("firebase_test_023", "test", R.string.firebase_test_023_question, listOf(R.string.firebase_test_023_option_1, R.string.firebase_test_023_option_2, R.string.firebase_test_023_option_3, R.string.firebase_test_023_option_4), 0, R.string.firebase_test_023_explanation),
        q("firebase_test_024", "test", R.string.firebase_test_024_question, listOf(R.string.firebase_test_024_option_1, R.string.firebase_test_024_option_2, R.string.firebase_test_024_option_3, R.string.firebase_test_024_option_4), 0, R.string.firebase_test_024_explanation),
        q("firebase_test_025", "test", R.string.firebase_test_025_question, listOf(R.string.firebase_test_025_option_1, R.string.firebase_test_025_option_2, R.string.firebase_test_025_option_3, R.string.firebase_test_025_option_4), 0, R.string.firebase_test_025_explanation),
        q("firebase_test_026", "test", R.string.firebase_test_026_question, listOf(R.string.firebase_test_026_option_1, R.string.firebase_test_026_option_2, R.string.firebase_test_026_option_3, R.string.firebase_test_026_option_4), 0, R.string.firebase_test_026_explanation),
        q("firebase_test_027", "test", R.string.firebase_test_027_question, listOf(R.string.firebase_test_027_option_1, R.string.firebase_test_027_option_2, R.string.firebase_test_027_option_3, R.string.firebase_test_027_option_4), 0, R.string.firebase_test_027_explanation, "medium"),
        q("firebase_test_028", "test", R.string.firebase_test_028_question, listOf(R.string.firebase_test_028_option_1, R.string.firebase_test_028_option_2, R.string.firebase_test_028_option_3, R.string.firebase_test_028_option_4), 0, R.string.firebase_test_028_explanation),
        q("firebase_test_029", "test", R.string.firebase_test_029_question, listOf(R.string.firebase_test_029_option_1, R.string.firebase_test_029_option_2, R.string.firebase_test_029_option_3, R.string.firebase_test_029_option_4), 0, R.string.firebase_test_029_explanation),
        q("firebase_test_030", "test", R.string.firebase_test_030_question, listOf(R.string.firebase_test_030_option_1, R.string.firebase_test_030_option_2, R.string.firebase_test_030_option_3, R.string.firebase_test_030_option_4), 0, R.string.firebase_test_030_explanation),

        q("firebase_test_031", "test", R.string.firebase_test_031_question, listOf(R.string.firebase_test_031_option_1, R.string.firebase_test_031_option_2, R.string.firebase_test_031_option_3, R.string.firebase_test_031_option_4), 0, R.string.firebase_test_031_explanation),
        q("firebase_test_032", "test", R.string.firebase_test_032_question, listOf(R.string.firebase_test_032_option_1, R.string.firebase_test_032_option_2, R.string.firebase_test_032_option_3, R.string.firebase_test_032_option_4), 0, R.string.firebase_test_032_explanation),
        q("firebase_test_033", "test", R.string.firebase_test_033_question, listOf(R.string.firebase_test_033_option_1, R.string.firebase_test_033_option_2, R.string.firebase_test_033_option_3, R.string.firebase_test_033_option_4), 0, R.string.firebase_test_033_explanation),
        q("firebase_test_034", "test", R.string.firebase_test_034_question, listOf(R.string.firebase_test_034_option_1, R.string.firebase_test_034_option_2, R.string.firebase_test_034_option_3, R.string.firebase_test_034_option_4), 0, R.string.firebase_test_034_explanation),
        q("firebase_test_035", "test", R.string.firebase_test_035_question, listOf(R.string.firebase_test_035_option_1, R.string.firebase_test_035_option_2, R.string.firebase_test_035_option_3, R.string.firebase_test_035_option_4), 0, R.string.firebase_test_035_explanation),
        q("firebase_test_036", "test", R.string.firebase_test_036_question, listOf(R.string.firebase_test_036_option_1, R.string.firebase_test_036_option_2, R.string.firebase_test_036_option_3, R.string.firebase_test_036_option_4), 0, R.string.firebase_test_036_explanation),
        q("firebase_test_037", "test", R.string.firebase_test_037_question, listOf(R.string.firebase_test_037_option_1, R.string.firebase_test_037_option_2, R.string.firebase_test_037_option_3, R.string.firebase_test_037_option_4), 0, R.string.firebase_test_037_explanation),
        q("firebase_test_038", "test", R.string.firebase_test_038_question, listOf(R.string.firebase_test_038_option_1, R.string.firebase_test_038_option_2, R.string.firebase_test_038_option_3, R.string.firebase_test_038_option_4), 0, R.string.firebase_test_038_explanation),
        q("firebase_test_039", "test", R.string.firebase_test_039_question, listOf(R.string.firebase_test_039_option_1, R.string.firebase_test_039_option_2, R.string.firebase_test_039_option_3, R.string.firebase_test_039_option_4), 0, R.string.firebase_test_039_explanation),
        q("firebase_test_040", "test", R.string.firebase_test_040_question, listOf(R.string.firebase_test_040_option_1, R.string.firebase_test_040_option_2, R.string.firebase_test_040_option_3, R.string.firebase_test_040_option_4), 0, R.string.firebase_test_040_explanation),

        q("firebase_test_041", "test", R.string.firebase_test_041_question, listOf(R.string.firebase_test_041_option_1, R.string.firebase_test_041_option_2, R.string.firebase_test_041_option_3, R.string.firebase_test_041_option_4), 0, R.string.firebase_test_041_explanation),
        q("firebase_test_042", "test", R.string.firebase_test_042_question, listOf(R.string.firebase_test_042_option_1, R.string.firebase_test_042_option_2, R.string.firebase_test_042_option_3, R.string.firebase_test_042_option_4), 0, R.string.firebase_test_042_explanation),
        q("firebase_test_043", "test", R.string.firebase_test_043_question, listOf(R.string.firebase_test_043_option_1, R.string.firebase_test_043_option_2, R.string.firebase_test_043_option_3, R.string.firebase_test_043_option_4), 0, R.string.firebase_test_043_explanation, "medium"),
        q("firebase_test_044", "test", R.string.firebase_test_044_question, listOf(R.string.firebase_test_044_option_1, R.string.firebase_test_044_option_2, R.string.firebase_test_044_option_3, R.string.firebase_test_044_option_4), 0, R.string.firebase_test_044_explanation),
        q("firebase_test_045", "test", R.string.firebase_test_045_question, listOf(R.string.firebase_test_045_option_1, R.string.firebase_test_045_option_2, R.string.firebase_test_045_option_3, R.string.firebase_test_045_option_4), 0, R.string.firebase_test_045_explanation, "medium"),
        q("firebase_test_046", "test", R.string.firebase_test_046_question, listOf(R.string.firebase_test_046_option_1, R.string.firebase_test_046_option_2, R.string.firebase_test_046_option_3, R.string.firebase_test_046_option_4), 0, R.string.firebase_test_046_explanation, "hard"),
        q("firebase_test_047", "test", R.string.firebase_test_047_question, listOf(R.string.firebase_test_047_option_1, R.string.firebase_test_047_option_2, R.string.firebase_test_047_option_3, R.string.firebase_test_047_option_4), 0, R.string.firebase_test_047_explanation, "medium"),
        q("firebase_test_048", "test", R.string.firebase_test_048_question, listOf(R.string.firebase_test_048_option_1, R.string.firebase_test_048_option_2, R.string.firebase_test_048_option_3, R.string.firebase_test_048_option_4), 0, R.string.firebase_test_048_explanation, "medium"),
        q("firebase_test_049", "test", R.string.firebase_test_049_question, listOf(R.string.firebase_test_049_option_1, R.string.firebase_test_049_option_2, R.string.firebase_test_049_option_3, R.string.firebase_test_049_option_4), 0, R.string.firebase_test_049_explanation, "hard"),
        q("firebase_test_050", "test", R.string.firebase_test_050_question, listOf(R.string.firebase_test_050_option_1, R.string.firebase_test_050_option_2, R.string.firebase_test_050_option_3, R.string.firebase_test_050_option_4), 0, R.string.firebase_test_050_explanation)
    )

    private val firebaseTrueFalseQuestions = listOf(
        q("firebase_tf_001", "true_false", R.string.firebase_tf_001_question, trueFalseOptionsRes, 0, R.string.firebase_tf_001_explanation),
        q("firebase_tf_002", "true_false", R.string.firebase_tf_002_question, trueFalseOptionsRes, 0, R.string.firebase_tf_002_explanation),
        q("firebase_tf_003", "true_false", R.string.firebase_tf_003_question, trueFalseOptionsRes, 0, R.string.firebase_tf_003_explanation, "medium"),
        q("firebase_tf_004", "true_false", R.string.firebase_tf_004_question, trueFalseOptionsRes, 0, R.string.firebase_tf_004_explanation),
        q("firebase_tf_005", "true_false", R.string.firebase_tf_005_question, trueFalseOptionsRes, 0, R.string.firebase_tf_005_explanation, "medium"),
        q("firebase_tf_006", "true_false", R.string.firebase_tf_006_question, trueFalseOptionsRes, 1, R.string.firebase_tf_006_explanation),
        q("firebase_tf_007", "true_false", R.string.firebase_tf_007_question, trueFalseOptionsRes, 0, R.string.firebase_tf_007_explanation),
        q("firebase_tf_008", "true_false", R.string.firebase_tf_008_question, trueFalseOptionsRes, 0, R.string.firebase_tf_008_explanation),
        q("firebase_tf_009", "true_false", R.string.firebase_tf_009_question, trueFalseOptionsRes, 0, R.string.firebase_tf_009_explanation),
        q("firebase_tf_010", "true_false", R.string.firebase_tf_010_question, trueFalseOptionsRes, 0, R.string.firebase_tf_010_explanation),

        q("firebase_tf_011", "true_false", R.string.firebase_tf_011_question, trueFalseOptionsRes, 0, R.string.firebase_tf_011_explanation),
        q("firebase_tf_012", "true_false", R.string.firebase_tf_012_question, trueFalseOptionsRes, 1, R.string.firebase_tf_012_explanation),
        q("firebase_tf_013", "true_false", R.string.firebase_tf_013_question, trueFalseOptionsRes, 0, R.string.firebase_tf_013_explanation),
        q("firebase_tf_014", "true_false", R.string.firebase_tf_014_question, trueFalseOptionsRes, 0, R.string.firebase_tf_014_explanation, "medium"),
        q("firebase_tf_015", "true_false", R.string.firebase_tf_015_question, trueFalseOptionsRes, 0, R.string.firebase_tf_015_explanation),
        q("firebase_tf_016", "true_false", R.string.firebase_tf_016_question, trueFalseOptionsRes, 0, R.string.firebase_tf_016_explanation, "medium"),
        q("firebase_tf_017", "true_false", R.string.firebase_tf_017_question, trueFalseOptionsRes, 0, R.string.firebase_tf_017_explanation),
        q("firebase_tf_018", "true_false", R.string.firebase_tf_018_question, trueFalseOptionsRes, 0, R.string.firebase_tf_018_explanation),
        q("firebase_tf_019", "true_false", R.string.firebase_tf_019_question, trueFalseOptionsRes, 0, R.string.firebase_tf_019_explanation, "medium"),
        q("firebase_tf_020", "true_false", R.string.firebase_tf_020_question, trueFalseOptionsRes, 1, R.string.firebase_tf_020_explanation),

        q("firebase_tf_021", "true_false", R.string.firebase_tf_021_question, trueFalseOptionsRes, 1, R.string.firebase_tf_021_explanation, "hard"),
        q("firebase_tf_022", "true_false", R.string.firebase_tf_022_question, trueFalseOptionsRes, 0, R.string.firebase_tf_022_explanation),
        q("firebase_tf_023", "true_false", R.string.firebase_tf_023_question, trueFalseOptionsRes, 0, R.string.firebase_tf_023_explanation),
        q("firebase_tf_024", "true_false", R.string.firebase_tf_024_question, trueFalseOptionsRes, 0, R.string.firebase_tf_024_explanation),
        q("firebase_tf_025", "true_false", R.string.firebase_tf_025_question, trueFalseOptionsRes, 0, R.string.firebase_tf_025_explanation, "medium"),
        q("firebase_tf_026", "true_false", R.string.firebase_tf_026_question, trueFalseOptionsRes, 0, R.string.firebase_tf_026_explanation),
        q("firebase_tf_027", "true_false", R.string.firebase_tf_027_question, trueFalseOptionsRes, 1, R.string.firebase_tf_027_explanation),
        q("firebase_tf_028", "true_false", R.string.firebase_tf_028_question, trueFalseOptionsRes, 0, R.string.firebase_tf_028_explanation),
        q("firebase_tf_029", "true_false", R.string.firebase_tf_029_question, trueFalseOptionsRes, 0, R.string.firebase_tf_029_explanation),
        q("firebase_tf_030", "true_false", R.string.firebase_tf_030_question, trueFalseOptionsRes, 0, R.string.firebase_tf_030_explanation),

        q("firebase_tf_031", "true_false", R.string.firebase_tf_031_question, trueFalseOptionsRes, 0, R.string.firebase_tf_031_explanation),
        q("firebase_tf_032", "true_false", R.string.firebase_tf_032_question, trueFalseOptionsRes, 0, R.string.firebase_tf_032_explanation),
        q("firebase_tf_033", "true_false", R.string.firebase_tf_033_question, trueFalseOptionsRes, 0, R.string.firebase_tf_033_explanation),
        q("firebase_tf_034", "true_false", R.string.firebase_tf_034_question, trueFalseOptionsRes, 0, R.string.firebase_tf_034_explanation),
        q("firebase_tf_035", "true_false", R.string.firebase_tf_035_question, trueFalseOptionsRes, 0, R.string.firebase_tf_035_explanation),
        q("firebase_tf_036", "true_false", R.string.firebase_tf_036_question, trueFalseOptionsRes, 1, R.string.firebase_tf_036_explanation, "hard"),
        q("firebase_tf_037", "true_false", R.string.firebase_tf_037_question, trueFalseOptionsRes, 0, R.string.firebase_tf_037_explanation),
        q("firebase_tf_038", "true_false", R.string.firebase_tf_038_question, trueFalseOptionsRes, 1, R.string.firebase_tf_038_explanation),
        q("firebase_tf_039", "true_false", R.string.firebase_tf_039_question, trueFalseOptionsRes, 0, R.string.firebase_tf_039_explanation, "medium"),
        q("firebase_tf_040", "true_false", R.string.firebase_tf_040_question, trueFalseOptionsRes, 0, R.string.firebase_tf_040_explanation, "medium"),

        q("firebase_tf_041", "true_false", R.string.firebase_tf_041_question, trueFalseOptionsRes, 0, R.string.firebase_tf_041_explanation, "medium"),
        q("firebase_tf_042", "true_false", R.string.firebase_tf_042_question, trueFalseOptionsRes, 0, R.string.firebase_tf_042_explanation, "medium"),
        q("firebase_tf_043", "true_false", R.string.firebase_tf_043_question, trueFalseOptionsRes, 0, R.string.firebase_tf_043_explanation),
        q("firebase_tf_044", "true_false", R.string.firebase_tf_044_question, trueFalseOptionsRes, 0, R.string.firebase_tf_044_explanation),
        q("firebase_tf_045", "true_false", R.string.firebase_tf_045_question, trueFalseOptionsRes, 0, R.string.firebase_tf_045_explanation),
        q("firebase_tf_046", "true_false", R.string.firebase_tf_046_question, trueFalseOptionsRes, 0, R.string.firebase_tf_046_explanation),
        q("firebase_tf_047", "true_false", R.string.firebase_tf_047_question, trueFalseOptionsRes, 0, R.string.firebase_tf_047_explanation),
        q("firebase_tf_048", "true_false", R.string.firebase_tf_048_question, trueFalseOptionsRes, 1, R.string.firebase_tf_048_explanation),
        q("firebase_tf_049", "true_false", R.string.firebase_tf_049_question, trueFalseOptionsRes, 1, R.string.firebase_tf_049_explanation),
        q("firebase_tf_050", "true_false", R.string.firebase_tf_050_question, trueFalseOptionsRes, 0, R.string.firebase_tf_050_explanation)
    )

    private val firebaseCompleteCodeQuestions = listOf(
        q("firebase_code_001", "complete_code", R.string.firebase_code_001_question, listOf(R.string.firebase_code_001_option_1, R.string.firebase_code_001_option_2, R.string.firebase_code_001_option_3, R.string.firebase_code_001_option_4), 0, R.string.firebase_code_001_explanation),
        q("firebase_code_002", "complete_code", R.string.firebase_code_002_question, listOf(R.string.firebase_code_002_option_1, R.string.firebase_code_002_option_2, R.string.firebase_code_002_option_3, R.string.firebase_code_002_option_4), 0, R.string.firebase_code_002_explanation),
        q("firebase_code_003", "complete_code", R.string.firebase_code_003_question, listOf(R.string.firebase_code_003_option_1, R.string.firebase_code_003_option_2, R.string.firebase_code_003_option_3, R.string.firebase_code_003_option_4), 0, R.string.firebase_code_003_explanation, "medium"),
        q("firebase_code_004", "complete_code", R.string.firebase_code_004_question, listOf(R.string.firebase_code_004_option_1, R.string.firebase_code_004_option_2, R.string.firebase_code_004_option_3, R.string.firebase_code_004_option_4), 0, R.string.firebase_code_004_explanation),
        q("firebase_code_005", "complete_code", R.string.firebase_code_005_question, listOf(R.string.firebase_code_005_option_1, R.string.firebase_code_005_option_2, R.string.firebase_code_005_option_3, R.string.firebase_code_005_option_4), 0, R.string.firebase_code_005_explanation),
        q("firebase_code_006", "complete_code", R.string.firebase_code_006_question, listOf(R.string.firebase_code_006_option_1, R.string.firebase_code_006_option_2, R.string.firebase_code_006_option_3, R.string.firebase_code_006_option_4), 0, R.string.firebase_code_006_explanation),
        q("firebase_code_007", "complete_code", R.string.firebase_code_007_question, listOf(R.string.firebase_code_007_option_1, R.string.firebase_code_007_option_2, R.string.firebase_code_007_option_3, R.string.firebase_code_007_option_4), 0, R.string.firebase_code_007_explanation),
        q("firebase_code_008", "complete_code", R.string.firebase_code_008_question, listOf(R.string.firebase_code_008_option_1, R.string.firebase_code_008_option_2, R.string.firebase_code_008_option_3, R.string.firebase_code_008_option_4), 0, R.string.firebase_code_008_explanation),
        q("firebase_code_009", "complete_code", R.string.firebase_code_009_question, listOf(R.string.firebase_code_009_option_1, R.string.firebase_code_009_option_2, R.string.firebase_code_009_option_3, R.string.firebase_code_009_option_4), 0, R.string.firebase_code_009_explanation),
        q("firebase_code_010", "complete_code", R.string.firebase_code_010_question, listOf(R.string.firebase_code_010_option_1, R.string.firebase_code_010_option_2, R.string.firebase_code_010_option_3, R.string.firebase_code_010_option_4), 0, R.string.firebase_code_010_explanation),

        q("firebase_code_011", "complete_code", R.string.firebase_code_011_question, listOf(R.string.firebase_code_011_option_1, R.string.firebase_code_011_option_2, R.string.firebase_code_011_option_3, R.string.firebase_code_011_option_4), 0, R.string.firebase_code_011_explanation, "medium"),
        q("firebase_code_012", "complete_code", R.string.firebase_code_012_question, listOf(R.string.firebase_code_012_option_1, R.string.firebase_code_012_option_2, R.string.firebase_code_012_option_3, R.string.firebase_code_012_option_4), 0, R.string.firebase_code_012_explanation, "medium"),
        q("firebase_code_013", "complete_code", R.string.firebase_code_013_question, listOf(R.string.firebase_code_013_option_1, R.string.firebase_code_013_option_2, R.string.firebase_code_013_option_3, R.string.firebase_code_013_option_4), 0, R.string.firebase_code_013_explanation, "medium"),
        q("firebase_code_014", "complete_code", R.string.firebase_code_014_question, listOf(R.string.firebase_code_014_option_1, R.string.firebase_code_014_option_2, R.string.firebase_code_014_option_3, R.string.firebase_code_014_option_4), 0, R.string.firebase_code_014_explanation),
        q("firebase_code_015", "complete_code", R.string.firebase_code_015_question, listOf(R.string.firebase_code_015_option_1, R.string.firebase_code_015_option_2, R.string.firebase_code_015_option_3, R.string.firebase_code_015_option_4), 0, R.string.firebase_code_015_explanation),
        q("firebase_code_016", "complete_code", R.string.firebase_code_016_question, listOf(R.string.firebase_code_016_option_1, R.string.firebase_code_016_option_2, R.string.firebase_code_016_option_3, R.string.firebase_code_016_option_4), 0, R.string.firebase_code_016_explanation),
        q("firebase_code_017", "complete_code", R.string.firebase_code_017_question, listOf(R.string.firebase_code_017_option_1, R.string.firebase_code_017_option_2, R.string.firebase_code_017_option_3, R.string.firebase_code_017_option_4), 0, R.string.firebase_code_017_explanation, "medium"),
        q("firebase_code_018", "complete_code", R.string.firebase_code_018_question, listOf(R.string.firebase_code_018_option_1, R.string.firebase_code_018_option_2, R.string.firebase_code_018_option_3, R.string.firebase_code_018_option_4), 0, R.string.firebase_code_018_explanation, "medium"),
        q("firebase_code_019", "complete_code", R.string.firebase_code_019_question, listOf(R.string.firebase_code_019_option_1, R.string.firebase_code_019_option_2, R.string.firebase_code_019_option_3, R.string.firebase_code_019_option_4), 0, R.string.firebase_code_019_explanation),
        q("firebase_code_020", "complete_code", R.string.firebase_code_020_question, listOf(R.string.firebase_code_020_option_1, R.string.firebase_code_020_option_2, R.string.firebase_code_020_option_3, R.string.firebase_code_020_option_4), 0, R.string.firebase_code_020_explanation),

        q("firebase_code_021", "complete_code", R.string.firebase_code_021_question, listOf(R.string.firebase_code_021_option_1, R.string.firebase_code_021_option_2, R.string.firebase_code_021_option_3, R.string.firebase_code_021_option_4), 0, R.string.firebase_code_021_explanation),
        q("firebase_code_022", "complete_code", R.string.firebase_code_022_question, listOf(R.string.firebase_code_022_option_1, R.string.firebase_code_022_option_2, R.string.firebase_code_022_option_3, R.string.firebase_code_022_option_4), 0, R.string.firebase_code_022_explanation, "medium"),
        q("firebase_code_023", "complete_code", R.string.firebase_code_023_question, listOf(R.string.firebase_code_023_option_1, R.string.firebase_code_023_option_2, R.string.firebase_code_023_option_3, R.string.firebase_code_023_option_4), 0, R.string.firebase_code_023_explanation),
        q("firebase_code_024", "complete_code", R.string.firebase_code_024_question, listOf(R.string.firebase_code_024_option_1, R.string.firebase_code_024_option_2, R.string.firebase_code_024_option_3, R.string.firebase_code_024_option_4), 0, R.string.firebase_code_024_explanation),
        q("firebase_code_025", "complete_code", R.string.firebase_code_025_question, listOf(R.string.firebase_code_025_option_1, R.string.firebase_code_025_option_2, R.string.firebase_code_025_option_3, R.string.firebase_code_025_option_4), 0, R.string.firebase_code_025_explanation),
        q("firebase_code_026", "complete_code", R.string.firebase_code_026_question, listOf(R.string.firebase_code_026_option_1, R.string.firebase_code_026_option_2, R.string.firebase_code_026_option_3, R.string.firebase_code_026_option_4), 0, R.string.firebase_code_026_explanation),
        q("firebase_code_027", "complete_code", R.string.firebase_code_027_question, listOf(R.string.firebase_code_027_option_1, R.string.firebase_code_027_option_2, R.string.firebase_code_027_option_3, R.string.firebase_code_027_option_4), 0, R.string.firebase_code_027_explanation, "medium"),
        q("firebase_code_028", "complete_code", R.string.firebase_code_028_question, listOf(R.string.firebase_code_028_option_1, R.string.firebase_code_028_option_2, R.string.firebase_code_028_option_3, R.string.firebase_code_028_option_4), 0, R.string.firebase_code_028_explanation),
        q("firebase_code_029", "complete_code", R.string.firebase_code_029_question, listOf(R.string.firebase_code_029_option_1, R.string.firebase_code_029_option_2, R.string.firebase_code_029_option_3, R.string.firebase_code_029_option_4), 0, R.string.firebase_code_029_explanation),
        q("firebase_code_030", "complete_code", R.string.firebase_code_030_question, listOf(R.string.firebase_code_030_option_1, R.string.firebase_code_030_option_2, R.string.firebase_code_030_option_3, R.string.firebase_code_030_option_4), 0, R.string.firebase_code_030_explanation),

        q("firebase_code_031", "complete_code", R.string.firebase_code_031_question, listOf(R.string.firebase_code_031_option_1, R.string.firebase_code_031_option_2, R.string.firebase_code_031_option_3, R.string.firebase_code_031_option_4), 0, R.string.firebase_code_031_explanation),
        q("firebase_code_032", "complete_code", R.string.firebase_code_032_question, listOf(R.string.firebase_code_032_option_1, R.string.firebase_code_032_option_2, R.string.firebase_code_032_option_3, R.string.firebase_code_032_option_4), 0, R.string.firebase_code_032_explanation, "medium"),
        q("firebase_code_033", "complete_code", R.string.firebase_code_033_question, listOf(R.string.firebase_code_033_option_1, R.string.firebase_code_033_option_2, R.string.firebase_code_033_option_3, R.string.firebase_code_033_option_4), 0, R.string.firebase_code_033_explanation, "medium"),
        q("firebase_code_034", "complete_code", R.string.firebase_code_034_question, listOf(R.string.firebase_code_034_option_1, R.string.firebase_code_034_option_2, R.string.firebase_code_034_option_3, R.string.firebase_code_034_option_4), 0, R.string.firebase_code_034_explanation),
        q("firebase_code_035", "complete_code", R.string.firebase_code_035_question, listOf(R.string.firebase_code_035_option_1, R.string.firebase_code_035_option_2, R.string.firebase_code_035_option_3, R.string.firebase_code_035_option_4), 0, R.string.firebase_code_035_explanation),
        q("firebase_code_036", "complete_code", R.string.firebase_code_036_question, listOf(R.string.firebase_code_036_option_1, R.string.firebase_code_036_option_2, R.string.firebase_code_036_option_3, R.string.firebase_code_036_option_4), 0, R.string.firebase_code_036_explanation),
        q("firebase_code_037", "complete_code", R.string.firebase_code_037_question, listOf(R.string.firebase_code_037_option_1, R.string.firebase_code_037_option_2, R.string.firebase_code_037_option_3, R.string.firebase_code_037_option_4), 0, R.string.firebase_code_037_explanation),
        q("firebase_code_038", "complete_code", R.string.firebase_code_038_question, listOf(R.string.firebase_code_038_option_1, R.string.firebase_code_038_option_2, R.string.firebase_code_038_option_3, R.string.firebase_code_038_option_4), 0, R.string.firebase_code_038_explanation),
        q("firebase_code_039", "complete_code", R.string.firebase_code_039_question, listOf(R.string.firebase_code_039_option_1, R.string.firebase_code_039_option_2, R.string.firebase_code_039_option_3, R.string.firebase_code_039_option_4), 0, R.string.firebase_code_039_explanation),
        q("firebase_code_040", "complete_code", R.string.firebase_code_040_question, listOf(R.string.firebase_code_040_option_1, R.string.firebase_code_040_option_2, R.string.firebase_code_040_option_3, R.string.firebase_code_040_option_4), 0, R.string.firebase_code_040_explanation),

        q("firebase_code_041", "complete_code", R.string.firebase_code_041_question, listOf(R.string.firebase_code_041_option_1, R.string.firebase_code_041_option_2, R.string.firebase_code_041_option_3, R.string.firebase_code_041_option_4), 0, R.string.firebase_code_041_explanation),
        q("firebase_code_042", "complete_code", R.string.firebase_code_042_question, listOf(R.string.firebase_code_042_option_1, R.string.firebase_code_042_option_2, R.string.firebase_code_042_option_3, R.string.firebase_code_042_option_4), 0, R.string.firebase_code_042_explanation),
        q("firebase_code_043", "complete_code", R.string.firebase_code_043_question, listOf(R.string.firebase_code_043_option_1, R.string.firebase_code_043_option_2, R.string.firebase_code_043_option_3, R.string.firebase_code_043_option_4), 0, R.string.firebase_code_043_explanation),
        q("firebase_code_044", "complete_code", R.string.firebase_code_044_question, listOf(R.string.firebase_code_044_option_1, R.string.firebase_code_044_option_2, R.string.firebase_code_044_option_3, R.string.firebase_code_044_option_4), 0, R.string.firebase_code_044_explanation, "medium"),
        q("firebase_code_045", "complete_code", R.string.firebase_code_045_question, listOf(R.string.firebase_code_045_option_1, R.string.firebase_code_045_option_2, R.string.firebase_code_045_option_3, R.string.firebase_code_045_option_4), 0, R.string.firebase_code_045_explanation, "medium"),
        q("firebase_code_046", "complete_code", R.string.firebase_code_046_question, listOf(R.string.firebase_code_046_option_1, R.string.firebase_code_046_option_2, R.string.firebase_code_046_option_3, R.string.firebase_code_046_option_4), 0, R.string.firebase_code_046_explanation, "medium"),
        q("firebase_code_047", "complete_code", R.string.firebase_code_047_question, listOf(R.string.firebase_code_047_option_1, R.string.firebase_code_047_option_2, R.string.firebase_code_047_option_3, R.string.firebase_code_047_option_4), 0, R.string.firebase_code_047_explanation, "medium"),
        q("firebase_code_048", "complete_code", R.string.firebase_code_048_question, listOf(R.string.firebase_code_048_option_1, R.string.firebase_code_048_option_2, R.string.firebase_code_048_option_3, R.string.firebase_code_048_option_4), 0, R.string.firebase_code_048_explanation),
        q("firebase_code_049", "complete_code", R.string.firebase_code_049_question, listOf(R.string.firebase_code_049_option_1, R.string.firebase_code_049_option_2, R.string.firebase_code_049_option_3, R.string.firebase_code_049_option_4), 0, R.string.firebase_code_049_explanation, "medium"),
        q("firebase_code_050", "complete_code", R.string.firebase_code_050_question, listOf(R.string.firebase_code_050_option_1, R.string.firebase_code_050_option_2, R.string.firebase_code_050_option_3, R.string.firebase_code_050_option_4), 0, R.string.firebase_code_050_explanation, "medium")
    )

    private val firebaseMatchConceptQuestions = listOf(
        q("firebase_match_001", "match_concept", R.string.firebase_match_001_question, listOf(R.string.firebase_match_001_option_1, R.string.firebase_match_001_option_2, R.string.firebase_match_001_option_3, R.string.firebase_match_001_option_4), 0, R.string.firebase_match_001_explanation),
        q("firebase_match_002", "match_concept", R.string.firebase_match_002_question, listOf(R.string.firebase_match_002_option_1, R.string.firebase_match_002_option_2, R.string.firebase_match_002_option_3, R.string.firebase_match_002_option_4), 0, R.string.firebase_match_002_explanation),
        q("firebase_match_003", "match_concept", R.string.firebase_match_003_question, listOf(R.string.firebase_match_003_option_1, R.string.firebase_match_003_option_2, R.string.firebase_match_003_option_3, R.string.firebase_match_003_option_4), 0, R.string.firebase_match_003_explanation, "medium"),
        q("firebase_match_004", "match_concept", R.string.firebase_match_004_question, listOf(R.string.firebase_match_004_option_1, R.string.firebase_match_004_option_2, R.string.firebase_match_004_option_3, R.string.firebase_match_004_option_4), 0, R.string.firebase_match_004_explanation),
        q("firebase_match_005", "match_concept", R.string.firebase_match_005_question, listOf(R.string.firebase_match_005_option_1, R.string.firebase_match_005_option_2, R.string.firebase_match_005_option_3, R.string.firebase_match_005_option_4), 0, R.string.firebase_match_005_explanation),
        q("firebase_match_006", "match_concept", R.string.firebase_match_006_question, listOf(R.string.firebase_match_006_option_1, R.string.firebase_match_006_option_2, R.string.firebase_match_006_option_3, R.string.firebase_match_006_option_4), 0, R.string.firebase_match_006_explanation),
        q("firebase_match_007", "match_concept", R.string.firebase_match_007_question, listOf(R.string.firebase_match_007_option_1, R.string.firebase_match_007_option_2, R.string.firebase_match_007_option_3, R.string.firebase_match_007_option_4), 0, R.string.firebase_match_007_explanation, "medium"),
        q("firebase_match_008", "match_concept", R.string.firebase_match_008_question, listOf(R.string.firebase_match_008_option_1, R.string.firebase_match_008_option_2, R.string.firebase_match_008_option_3, R.string.firebase_match_008_option_4), 0, R.string.firebase_match_008_explanation, "medium"),
        q("firebase_match_009", "match_concept", R.string.firebase_match_009_question, listOf(R.string.firebase_match_009_option_1, R.string.firebase_match_009_option_2, R.string.firebase_match_009_option_3, R.string.firebase_match_009_option_4), 0, R.string.firebase_match_009_explanation),
        q("firebase_match_010", "match_concept", R.string.firebase_match_010_question, listOf(R.string.firebase_match_010_option_1, R.string.firebase_match_010_option_2, R.string.firebase_match_010_option_3, R.string.firebase_match_010_option_4), 0, R.string.firebase_match_010_explanation)
    )

    private val firebaseDetectErrorQuestions = listOf(
        q("firebase_error_001", "detect_error", R.string.firebase_error_001_question, listOf(R.string.firebase_error_001_option_1, R.string.firebase_error_001_option_2, R.string.firebase_error_001_option_3, R.string.firebase_error_001_option_4), 0, R.string.firebase_error_001_explanation, "medium"),
        q("firebase_error_002", "detect_error", R.string.firebase_error_002_question, listOf(R.string.firebase_error_002_option_1, R.string.firebase_error_002_option_2, R.string.firebase_error_002_option_3, R.string.firebase_error_002_option_4), 0, R.string.firebase_error_002_explanation, "medium"),
        q("firebase_error_003", "detect_error", R.string.firebase_error_003_question, listOf(R.string.firebase_error_003_option_1, R.string.firebase_error_003_option_2, R.string.firebase_error_003_option_3, R.string.firebase_error_003_option_4), 0, R.string.firebase_error_003_explanation),
        q("firebase_error_004", "detect_error", R.string.firebase_error_004_question, listOf(R.string.firebase_error_004_option_1, R.string.firebase_error_004_option_2, R.string.firebase_error_004_option_3, R.string.firebase_error_004_option_4), 0, R.string.firebase_error_004_explanation),
        q("firebase_error_005", "detect_error", R.string.firebase_error_005_question, listOf(R.string.firebase_error_005_option_1, R.string.firebase_error_005_option_2, R.string.firebase_error_005_option_3, R.string.firebase_error_005_option_4), 0, R.string.firebase_error_005_explanation, "hard"),
        q("firebase_error_006", "detect_error", R.string.firebase_error_006_question, listOf(R.string.firebase_error_006_option_1, R.string.firebase_error_006_option_2, R.string.firebase_error_006_option_3, R.string.firebase_error_006_option_4), 0, R.string.firebase_error_006_explanation, "hard"),
        q("firebase_error_007", "detect_error", R.string.firebase_error_007_question, listOf(R.string.firebase_error_007_option_1, R.string.firebase_error_007_option_2, R.string.firebase_error_007_option_3, R.string.firebase_error_007_option_4), 0, R.string.firebase_error_007_explanation),
        q("firebase_error_008", "detect_error", R.string.firebase_error_008_question, listOf(R.string.firebase_error_008_option_1, R.string.firebase_error_008_option_2, R.string.firebase_error_008_option_3, R.string.firebase_error_008_option_4), 0, R.string.firebase_error_008_explanation),
        q("firebase_error_009", "detect_error", R.string.firebase_error_009_question, listOf(R.string.firebase_error_009_option_1, R.string.firebase_error_009_option_2, R.string.firebase_error_009_option_3, R.string.firebase_error_009_option_4), 0, R.string.firebase_error_009_explanation, "hard"),
        q("firebase_error_010", "detect_error", R.string.firebase_error_010_question, listOf(R.string.firebase_error_010_option_1, R.string.firebase_error_010_option_2, R.string.firebase_error_010_option_3, R.string.firebase_error_010_option_4), 0, R.string.firebase_error_010_explanation, "medium")
    )

    private val firebaseConsoleOutputQuestions = listOf(
        q("firebase_output_001", "console_output", R.string.firebase_output_001_question, listOf(R.string.firebase_output_001_option_1, R.string.firebase_output_001_option_2, R.string.firebase_output_001_option_3, R.string.firebase_output_001_option_4), 0, R.string.firebase_output_001_explanation),
        q("firebase_output_002", "console_output", R.string.firebase_output_002_question, listOf(R.string.firebase_output_002_option_1, R.string.firebase_output_002_option_2, R.string.firebase_output_002_option_3, R.string.firebase_output_002_option_4), 0, R.string.firebase_output_002_explanation),
        q("firebase_output_003", "console_output", R.string.firebase_output_003_question, listOf(R.string.firebase_output_003_option_1, R.string.firebase_output_003_option_2, R.string.firebase_output_003_option_3, R.string.firebase_output_003_option_4), 0, R.string.firebase_output_003_explanation),
        q("firebase_output_004", "console_output", R.string.firebase_output_004_question, listOf(R.string.firebase_output_004_option_1, R.string.firebase_output_004_option_2, R.string.firebase_output_004_option_3, R.string.firebase_output_004_option_4), 0, R.string.firebase_output_004_explanation),
        q("firebase_output_005", "console_output", R.string.firebase_output_005_question, listOf(R.string.firebase_output_005_option_1, R.string.firebase_output_005_option_2, R.string.firebase_output_005_option_3, R.string.firebase_output_005_option_4), 0, R.string.firebase_output_005_explanation),
        q("firebase_output_006", "console_output", R.string.firebase_output_006_question, listOf(R.string.firebase_output_006_option_1, R.string.firebase_output_006_option_2, R.string.firebase_output_006_option_3, R.string.firebase_output_006_option_4), 0, R.string.firebase_output_006_explanation),
        q("firebase_output_007", "console_output", R.string.firebase_output_007_question, listOf(R.string.firebase_output_007_option_1, R.string.firebase_output_007_option_2, R.string.firebase_output_007_option_3, R.string.firebase_output_007_option_4), 0, R.string.firebase_output_007_explanation),
        q("firebase_output_008", "console_output", R.string.firebase_output_008_question, listOf(R.string.firebase_output_008_option_1, R.string.firebase_output_008_option_2, R.string.firebase_output_008_option_3, R.string.firebase_output_008_option_4), 0, R.string.firebase_output_008_explanation, "medium"),
        q("firebase_output_009", "console_output", R.string.firebase_output_009_question, listOf(R.string.firebase_output_009_option_1, R.string.firebase_output_009_option_2, R.string.firebase_output_009_option_3, R.string.firebase_output_009_option_4), 0, R.string.firebase_output_009_explanation),
        q("firebase_output_010", "console_output", R.string.firebase_output_010_question, listOf(R.string.firebase_output_010_option_1, R.string.firebase_output_010_option_2, R.string.firebase_output_010_option_3, R.string.firebase_output_010_option_4), 0, R.string.firebase_output_010_explanation)
    )

    private val firebaseOrderCodeQuestions = listOf(
        q("firebase_order_001", "order_code", R.string.firebase_order_001_question, listOf(R.string.firebase_order_001_option_1, R.string.firebase_order_001_option_2, R.string.firebase_order_001_option_3, R.string.firebase_order_001_option_4), 0, R.string.firebase_order_001_explanation),
        q("firebase_order_002", "order_code", R.string.firebase_order_002_question, listOf(R.string.firebase_order_002_option_1, R.string.firebase_order_002_option_2, R.string.firebase_order_002_option_3, R.string.firebase_order_002_option_4), 0, R.string.firebase_order_002_explanation, "medium"),
        q("firebase_order_003", "order_code", R.string.firebase_order_003_question, listOf(R.string.firebase_order_003_option_1, R.string.firebase_order_003_option_2, R.string.firebase_order_003_option_3, R.string.firebase_order_003_option_4), 0, R.string.firebase_order_003_explanation),
        q("firebase_order_004", "order_code", R.string.firebase_order_004_question, listOf(R.string.firebase_order_004_option_1, R.string.firebase_order_004_option_2, R.string.firebase_order_004_option_3, R.string.firebase_order_004_option_4), 0, R.string.firebase_order_004_explanation),
        q("firebase_order_005", "order_code", R.string.firebase_order_005_question, listOf(R.string.firebase_order_005_option_1, R.string.firebase_order_005_option_2, R.string.firebase_order_005_option_3, R.string.firebase_order_005_option_4), 0, R.string.firebase_order_005_explanation, "medium"),
        q("firebase_order_006", "order_code", R.string.firebase_order_006_question, listOf(R.string.firebase_order_006_option_1, R.string.firebase_order_006_option_2, R.string.firebase_order_006_option_3, R.string.firebase_order_006_option_4), 0, R.string.firebase_order_006_explanation),
        q("firebase_order_007", "order_code", R.string.firebase_order_007_question, listOf(R.string.firebase_order_007_option_1, R.string.firebase_order_007_option_2, R.string.firebase_order_007_option_3, R.string.firebase_order_007_option_4), 0, R.string.firebase_order_007_explanation, "medium"),
        q("firebase_order_008", "order_code", R.string.firebase_order_008_question, listOf(R.string.firebase_order_008_option_1, R.string.firebase_order_008_option_2, R.string.firebase_order_008_option_3, R.string.firebase_order_008_option_4), 0, R.string.firebase_order_008_explanation, "medium"),
        q("firebase_order_009", "order_code", R.string.firebase_order_009_question, listOf(R.string.firebase_order_009_option_1, R.string.firebase_order_009_option_2, R.string.firebase_order_009_option_3, R.string.firebase_order_009_option_4), 0, R.string.firebase_order_009_explanation),
        q("firebase_order_010", "order_code", R.string.firebase_order_010_question, listOf(R.string.firebase_order_010_option_1, R.string.firebase_order_010_option_2, R.string.firebase_order_010_option_3, R.string.firebase_order_010_option_4), 0, R.string.firebase_order_010_explanation)
    )

    private val firebaseTechnicalWordleQuestions = listOf(
        q("firebase_wordle_001", "technical_wordle", R.string.firebase_wordle_001_question, listOf(R.string.firebase_wordle_001_option_1, R.string.firebase_wordle_001_option_2, R.string.firebase_wordle_001_option_3, R.string.firebase_wordle_001_option_4), 0, R.string.firebase_wordle_001_explanation),
        q("firebase_wordle_002", "technical_wordle", R.string.firebase_wordle_002_question, listOf(R.string.firebase_wordle_002_option_1, R.string.firebase_wordle_002_option_2, R.string.firebase_wordle_002_option_3, R.string.firebase_wordle_002_option_4), 0, R.string.firebase_wordle_002_explanation),
        q("firebase_wordle_003", "technical_wordle", R.string.firebase_wordle_003_question, listOf(R.string.firebase_wordle_003_option_1, R.string.firebase_wordle_003_option_2, R.string.firebase_wordle_003_option_3, R.string.firebase_wordle_003_option_4), 0, R.string.firebase_wordle_003_explanation),
        q("firebase_wordle_004", "technical_wordle", R.string.firebase_wordle_004_question, listOf(R.string.firebase_wordle_004_option_1, R.string.firebase_wordle_004_option_2, R.string.firebase_wordle_004_option_3, R.string.firebase_wordle_004_option_4), 0, R.string.firebase_wordle_004_explanation),
        q("firebase_wordle_005", "technical_wordle", R.string.firebase_wordle_005_question, listOf(R.string.firebase_wordle_005_option_1, R.string.firebase_wordle_005_option_2, R.string.firebase_wordle_005_option_3, R.string.firebase_wordle_005_option_4), 0, R.string.firebase_wordle_005_explanation),
        q("firebase_wordle_006", "technical_wordle", R.string.firebase_wordle_006_question, listOf(R.string.firebase_wordle_006_option_1, R.string.firebase_wordle_006_option_2, R.string.firebase_wordle_006_option_3, R.string.firebase_wordle_006_option_4), 0, R.string.firebase_wordle_006_explanation, "medium"),
        q("firebase_wordle_007", "technical_wordle", R.string.firebase_wordle_007_question, listOf(R.string.firebase_wordle_007_option_1, R.string.firebase_wordle_007_option_2, R.string.firebase_wordle_007_option_3, R.string.firebase_wordle_007_option_4), 0, R.string.firebase_wordle_007_explanation),
        q("firebase_wordle_008", "technical_wordle", R.string.firebase_wordle_008_question, listOf(R.string.firebase_wordle_008_option_1, R.string.firebase_wordle_008_option_2, R.string.firebase_wordle_008_option_3, R.string.firebase_wordle_008_option_4), 0, R.string.firebase_wordle_008_explanation, "medium"),
        q("firebase_wordle_009", "technical_wordle", R.string.firebase_wordle_009_question, listOf(R.string.firebase_wordle_009_option_1, R.string.firebase_wordle_009_option_2, R.string.firebase_wordle_009_option_3, R.string.firebase_wordle_009_option_4), 0, R.string.firebase_wordle_009_explanation, "medium"),
        q("firebase_wordle_010", "technical_wordle", R.string.firebase_wordle_010_question, listOf(R.string.firebase_wordle_010_option_1, R.string.firebase_wordle_010_option_2, R.string.firebase_wordle_010_option_3, R.string.firebase_wordle_010_option_4), 0, R.string.firebase_wordle_010_explanation)
    )

    private val firebaseCrosswordQuestions = listOf(
        q("firebase_crossword_001", "crossword", R.string.firebase_crossword_001_question, listOf(R.string.firebase_crossword_001_option_1, R.string.firebase_crossword_001_option_2, R.string.firebase_crossword_001_option_3, R.string.firebase_crossword_001_option_4), 0, R.string.firebase_crossword_001_explanation),
        q("firebase_crossword_002", "crossword", R.string.firebase_crossword_002_question, listOf(R.string.firebase_crossword_002_option_1, R.string.firebase_crossword_002_option_2, R.string.firebase_crossword_002_option_3, R.string.firebase_crossword_002_option_4), 0, R.string.firebase_crossword_002_explanation),
        q("firebase_crossword_003", "crossword", R.string.firebase_crossword_003_question, listOf(R.string.firebase_crossword_003_option_1, R.string.firebase_crossword_003_option_2, R.string.firebase_crossword_003_option_3, R.string.firebase_crossword_003_option_4), 0, R.string.firebase_crossword_003_explanation),
        q("firebase_crossword_004", "crossword", R.string.firebase_crossword_004_question, listOf(R.string.firebase_crossword_004_option_1, R.string.firebase_crossword_004_option_2, R.string.firebase_crossword_004_option_3, R.string.firebase_crossword_004_option_4), 0, R.string.firebase_crossword_004_explanation),
        q("firebase_crossword_005", "crossword", R.string.firebase_crossword_005_question, listOf(R.string.firebase_crossword_005_option_1, R.string.firebase_crossword_005_option_2, R.string.firebase_crossword_005_option_3, R.string.firebase_crossword_005_option_4), 0, R.string.firebase_crossword_005_explanation),
        q("firebase_crossword_006", "crossword", R.string.firebase_crossword_006_question, listOf(R.string.firebase_crossword_006_option_1, R.string.firebase_crossword_006_option_2, R.string.firebase_crossword_006_option_3, R.string.firebase_crossword_006_option_4), 0, R.string.firebase_crossword_006_explanation),
        q("firebase_crossword_007", "crossword", R.string.firebase_crossword_007_question, listOf(R.string.firebase_crossword_007_option_1, R.string.firebase_crossword_007_option_2, R.string.firebase_crossword_007_option_3, R.string.firebase_crossword_007_option_4), 0, R.string.firebase_crossword_007_explanation),
        q("firebase_crossword_008", "crossword", R.string.firebase_crossword_008_question, listOf(R.string.firebase_crossword_008_option_1, R.string.firebase_crossword_008_option_2, R.string.firebase_crossword_008_option_3, R.string.firebase_crossword_008_option_4), 0, R.string.firebase_crossword_008_explanation, "medium"),
        q("firebase_crossword_009", "crossword", R.string.firebase_crossword_009_question, listOf(R.string.firebase_crossword_009_option_1, R.string.firebase_crossword_009_option_2, R.string.firebase_crossword_009_option_3, R.string.firebase_crossword_009_option_4), 0, R.string.firebase_crossword_009_explanation),
        q("firebase_crossword_010", "crossword", R.string.firebase_crossword_010_question, listOf(R.string.firebase_crossword_010_option_1, R.string.firebase_crossword_010_option_2, R.string.firebase_crossword_010_option_3, R.string.firebase_crossword_010_option_4), 0, R.string.firebase_crossword_010_explanation)
    )

    val questions: List<Question> =
        firebaseTestQuestions +
                firebaseTrueFalseQuestions +
                firebaseCompleteCodeQuestions +
                firebaseMatchConceptQuestions +
                firebaseDetectErrorQuestions +
                firebaseConsoleOutputQuestions +
                firebaseOrderCodeQuestions +
                firebaseTechnicalWordleQuestions +
                firebaseCrosswordQuestions
}