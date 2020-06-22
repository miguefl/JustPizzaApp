package com.migferlab.justpizza.infrastructure.data

import androidx.annotation.Keep
import com.google.firebase.Timestamp
import com.google.firebase.firestore.IgnoreExtraProperties
import com.migferlab.justpizza.domain.model.Difficulty
import com.migferlab.justpizza.domain.model.Ingredient
import com.migferlab.justpizza.domain.model.Pizza
import com.migferlab.justpizza.domain.model.Recipe
import com.migferlab.justpizza.domain.model.Unit


@Keep
@IgnoreExtraProperties
data class FirebasePizza(
    var pizza: PizzaDB? = null,
    var author: String? = null,
    var created: Timestamp? = null,
    var updated: Timestamp? = null
)

@Keep
@IgnoreExtraProperties
data class PizzaDB(
    var name: String? = null,
    var imageUrl: String? = null,
    var tags: List<String>? = null,
    var recipe: RecipeDB? = null,
    var fav: Boolean? = null
)

@Keep
@IgnoreExtraProperties
data class RecipeDB(
    var description: String? = null,
    var people: Int? = null,
    var ingredients: List<IngredientDB>? = null,
    var steps: List<String>? = null,
    var time: Int? = null,
    var difficulty: DifficultyDB? = null
)

@Keep
@IgnoreExtraProperties
data class IngredientDB(
    var name: String? = null,
    var quantity: Float? = null,
    var unit: UnitDB? = null
)


enum class UnitDB {
    UNIT, GRAMS, CENTILITRES, TBSP, CUSTOM, MILLILITRES
}

enum class DifficultyDB {
    EASY, MEDIUM, HARD
}


fun adapt(pizza: PizzaDB, id: String, fav: Boolean = false): Pizza =
    Pizza(
        id = id,
        imageUrl = pizza.imageUrl!!,
        tags = pizza.tags!!,
        pizzaRecipe = Recipe(
            description = pizza.recipe!!.description!!,
            ingredients = pizza.recipe!!.ingredients!!.map {
                Ingredient(
                    name = it.name!!,
                    unit = when (it.unit!!) {
                        UnitDB.CENTILITRES -> Unit.CENTILITRES
                        UnitDB.UNIT -> Unit.UNIT
                        UnitDB.GRAMS -> Unit.GRAMS
                        UnitDB.TBSP -> Unit.TBSP
                        UnitDB.CUSTOM -> Unit.CUSTOM
                        UnitDB.MILLILITRES -> Unit.MILLILITRES
                    },
                    quantity = it.quantity!!
                )
            },
            people = pizza.recipe!!.people!!,
            difficulty = when (pizza.recipe!!.difficulty!!) {
                DifficultyDB.EASY -> Difficulty.EASY
                DifficultyDB.MEDIUM -> Difficulty.MEDIUM
                DifficultyDB.HARD -> Difficulty.HARD
            },
            time = pizza.recipe!!.time!!,
            steps = pizza.recipe!!.steps!!
        ),
        name = pizza.name!!,
        fav = fav
    )