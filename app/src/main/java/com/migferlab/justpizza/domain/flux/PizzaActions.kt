package com.migferlab.justpizza.domain.flux

import com.hoopcarpool.fluxy.BaseAction
import com.hoopcarpool.fluxy.Result
import com.migferlab.justpizza.domain.model.Pizza

data class PizzaFavoriteAction(val pizzaId: String) : BaseAction
data class PizzaFavoriteCompletedAction(val pizzas: Result<List<Pizza>>) : BaseAction
class FetchPizzasAction(val onlyCached: Boolean) : BaseAction
data class PizzasFetchedAction(val pizzas: Result<List<Pizza>>, val isCached: Boolean) : BaseAction