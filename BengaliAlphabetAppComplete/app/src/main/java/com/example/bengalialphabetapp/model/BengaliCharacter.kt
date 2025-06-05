package com.example.bengalialphabetapp.model

/**
 * Data class representing a Bengali alphabet character with all its properties
 * needed for display, tracing, and guidance.
 */
data class BengaliCharacter(
    val id: Int,
    val unicode: String,
    val romanizedName: String,
    val bengaliName: String,
    val type: CharacterType,
    val audioResId: Int,
    val strokePaths: List<StrokePath>,
    val difficulty: Int = 1
) {
    /**
     * Enum representing the type of Bengali character
     */
    enum class CharacterType {
        VOWEL,
        CONSONANT,
        VOWEL_SIGN,
        CONJUNCT,
        NUMBER
    }
}

/**
 * Data class representing a single stroke path for a Bengali character
 */
data class StrokePath(
    val id: Int,
    val order: Int,
    val pathData: String,
    val startPoint: Point,
    val endPoint: Point,
    val guidancePoints: List<Point>
)

/**
 * Data class representing a point in 2D space
 */
data class Point(val x: Float, val y: Float)
