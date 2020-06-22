package com.migferlab.justpizza.features.pizza.detail

import androidx.annotation.StringRes
import com.migferlab.justpizza.R
import com.migferlab.justpizza.core.extension.formatUI
import com.migferlab.justpizza.domain.model.Difficulty
import com.migferlab.justpizza.domain.model.Pizza
import com.migferlab.justpizza.domain.model.Unit

data class PizzaRecipeDetailViewState(
    val imageUrl: String,
    val name: String,
    val fav: Boolean,
    val ingredients: List<IngredientDetailView>,
    val recipeSteps: List<String>,
    val time: Int,
    val difficulty: DifficultyDetailView,
    val people: Int
) {
    data class IngredientDetailView(
        val name: String,
        val quantity: String,
        val unit: UnitDetailView
    )

    enum class UnitDetailView(@StringRes var unit: Int) {
        UNIT(R.string.qtyUnit),
        GRAMS(R.string.gramsUnit),
        CENTILITRES(R.string.clUnit),
        TBSP(R.string.spoonUnit),
        CUSTOM(R.string.customUnit),
        MILLILITRES(R.string.mlUnit)
    }

    enum class DifficultyDetailView(@StringRes var difficulty: Int) {
        EASY(R.string.easyDifficulty),
        MEDIUM(R.string.mediumDifficulty),
        HARD(R.string.hardDifficulty)
    }

}

fun adapt(pizza: Pizza, fav: Boolean): PizzaRecipeDetailViewState {
    return PizzaRecipeDetailViewState(
        imageUrl = pizza.imageUrl,
        name = pizza.name,
        fav = fav,
        time = pizza.pizzaRecipe.time,
        difficulty = when (pizza.pizzaRecipe.difficulty) {
            Difficulty.EASY -> PizzaRecipeDetailViewState.DifficultyDetailView.EASY
            Difficulty.MEDIUM -> PizzaRecipeDetailViewState.DifficultyDetailView.MEDIUM
            Difficulty.HARD -> PizzaRecipeDetailViewState.DifficultyDetailView.HARD
        },
        ingredients = pizza.pizzaRecipe.ingredients.map {

            val unit = when (it.unit) {
                Unit.UNIT -> PizzaRecipeDetailViewState.UnitDetailView.UNIT
                Unit.GRAMS -> PizzaRecipeDetailViewState.UnitDetailView.GRAMS
                Unit.CENTILITRES -> PizzaRecipeDetailViewState.UnitDetailView.CENTILITRES
                Unit.TBSP -> PizzaRecipeDetailViewState.UnitDetailView.TBSP
                Unit.CUSTOM -> PizzaRecipeDetailViewState.UnitDetailView.CUSTOM
                Unit.MILLILITRES -> PizzaRecipeDetailViewState.UnitDetailView.MILLILITRES
            }

            PizzaRecipeDetailViewState.IngredientDetailView(it.name, it.quantity.formatUI(), unit)
        },
        recipeSteps = pizza.pizzaRecipe.steps,
        people = pizza.pizzaRecipe.people
    )
}

