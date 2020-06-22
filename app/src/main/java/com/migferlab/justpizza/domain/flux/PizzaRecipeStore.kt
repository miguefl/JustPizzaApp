package com.migferlab.justpizza.domain.flux

import com.hoopcarpool.fluxy.FluxyStore
import com.hoopcarpool.fluxy.Result
import com.migferlab.justpizza.domain.model.Pizza
import com.migferlab.justpizza.domain.model.User

data class PizzaState(
    val pizzasResult: Result<List<Pizza>> = Result.Empty(),
    val isCached: Boolean = false,
    val user: Result<User> = Result.Empty()
)

class PizzaRecipeStore(private val pizzaRecipeSideEffects: PizzaRecipeSideEffects) :
    FluxyStore<PizzaState>() {
    override fun init() {

        reduce<FetchPizzasAction> {
            newState = state.copy(pizzasResult = Result.Loading())
            pizzaRecipeSideEffects.subscribeToRealTimePizzaList()
        }

        reduce<PizzasFetchedAction> {
            newState = state.copy(pizzasResult = it.pizzas, isCached = it.isCached)
        }

        reduce<PizzaFavoriteAction> {
            val userId = (state.user as Result.Success).value.uid
            pizzaRecipeSideEffects.pizzaFavorite(it.pizzaId, userId)
        }

        reduce<UserLogInResolvedAction> {
            newState = state.copy(user = it.result)
            if(it.result is Result.Success) {
                pizzaRecipeSideEffects.findFavorites(it.result.value)
            }
        }

        reduce<UserSignUpResolvedAction> {
            newState = state.copy(user = it.result)
            if(it.result is Result.Success) {
                pizzaRecipeSideEffects.findFavorites(it.result.value)
            }
        }

        reduce<UserFavoriteListFetchedAction> {
            newState = state.copy(user = it.result)
        }
    }

}