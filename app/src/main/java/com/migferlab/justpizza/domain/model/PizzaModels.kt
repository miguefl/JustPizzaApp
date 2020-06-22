package com.migferlab.justpizza.domain.model

data class Pizza(
    val id: String,
    val name: String,
    val imageUrl: String,
    val tags: List<String>,
    val pizzaRecipe: Recipe,
    val fav: Boolean = false
)

data class Recipe(
    val description: String,
    val people: Int,
    val ingredients: List<Ingredient>,
    val steps: List<String>,
    val time: Int,
    val difficulty: Difficulty
)

data class Ingredient(val name: String, val quantity: Float, val unit: Unit)

enum class Unit {
    UNIT, GRAMS, CENTILITRES, TBSP, CUSTOM, MILLILITRES
}

enum class Difficulty {
    EASY, MEDIUM, HARD
}