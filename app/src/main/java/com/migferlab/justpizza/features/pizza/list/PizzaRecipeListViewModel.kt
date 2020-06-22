package com.migferlab.justpizza.features.pizza.list

import androidx.lifecycle.viewModelScope
import com.hoopcarpool.fluxy.Dispatcher
import com.hoopcarpool.fluxy.Result
import com.migferlab.justpizza.domain.flux.FetchPizzasAction
import com.migferlab.justpizza.domain.flux.PizzaFavoriteAction
import com.migferlab.justpizza.domain.flux.PizzaRecipeStore
import com.migferlab.justpizza.features.base.BaseViewModel
import com.migferlab.justpizza.features.pizza.list.PizzaRecipeListViewState.PizzaListItem
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class PizzaRecipeListViewModel(
    private val dispatcher: Dispatcher,
    private val pizzaRecipeStore: PizzaRecipeStore
) : BaseViewModel<Result<PizzaRecipeListViewState>>() {

    fun setUp(fragmentArgs: PizzaRecipeListFragmentArgs) {
        viewModelScope.launch {
            pizzaRecipeStore.flow()
                .collect {

                    val favs = when (it.user) {
                        is Result.Success -> it.user.value.favs
                        is Result.Loading, is Result.Failure, is Result.Empty -> listOf()
                    }

                    val result: Result<PizzaRecipeListViewState> = when (it.pizzasResult) {
                        is Result.Success -> {
                            val list =
                                if (fragmentArgs.favs) it.pizzasResult.value.filter { item ->
                                    favs.contains(item.id)
                                } else it.pizzasResult.value
                            Result.Success(
                                PizzaRecipeListViewState(
                                    pizzas = list.map { pizza ->
                                        PizzaListItem(
                                            id = pizza.id,
                                            name = pizza.name,
                                            imageUrl = pizza.imageUrl,
                                            tags = pizza.tags,
                                            fav = favs.contains(pizza.id)
                                        )
                                    }, cached = pizzaRecipeStore.state.isCached
                                )
                            )
                        }
                        is Result.Loading -> Result.Loading()
                        is Result.Failure -> Result.Failure()
                        is Result.Empty -> Result.Empty()
                    }
                    viewData.postValue(result)
                }
        }
    }

    fun favoritePizza(id: String) {
        dispatcher.dispatch(PizzaFavoriteAction(id))
    }

    fun loadPizzaRecipes() {
        dispatcher.dispatch(FetchPizzasAction(false))
    }

}

data class PizzaRecipeListViewState(val pizzas: List<PizzaListItem>, val cached: Boolean) {
    data class PizzaListItem(
        val id: String,
        val name: String,
        val fav: Boolean,
        val imageUrl: String,
        val tags: List<String>
    )
}
