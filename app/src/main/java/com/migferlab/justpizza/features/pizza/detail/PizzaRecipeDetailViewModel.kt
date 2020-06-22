package com.migferlab.justpizza.features.pizza.detail

import androidx.lifecycle.viewModelScope
import com.hoopcarpool.fluxy.Dispatcher
import com.hoopcarpool.fluxy.Result
import com.hoopcarpool.fluxy.Result.Empty
import com.hoopcarpool.fluxy.Result.Failure
import com.hoopcarpool.fluxy.Result.Loading
import com.hoopcarpool.fluxy.Result.Success
import com.migferlab.justpizza.domain.flux.FetchPizzasAction
import com.migferlab.justpizza.domain.flux.PizzaFavoriteAction
import com.migferlab.justpizza.domain.flux.PizzaRecipeStore
import com.migferlab.justpizza.features.base.BaseViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.kodein.di.android.di
import kotlin.random.Random

class PizzaRecipeDetailViewModel(
    private val dispatcher: Dispatcher,
    private val pizzaRecipeStore: PizzaRecipeStore
) : BaseViewModel<Result<PizzaRecipeDetailViewState>>() {

    lateinit var pizzaId: String

    fun setUp(fragmentArgs: PizzaRecipeDetailFragmentArgs) {

        //HACK
        val randomIndex = Random.nextInt(0, 14 )

        if (fragmentArgs.random) {
            loadPizzaRecipes()
        }

        viewModelScope.launch {
            pizzaRecipeStore.flow()
                .collect {

                    val favs = when (it.user) {
                        is Success -> it.user.value.favs
                        is Loading, is Failure, is Empty -> listOf()
                    }

                    when (val result = it.pizzasResult) {
                        is Success -> {
                            val pizza = if (fragmentArgs.random) {
                                result.value[randomIndex]
                            } else {
                                result.value.find { item -> item.id == fragmentArgs.pizzaRecipeId }!!
                            }

                            pizzaId = pizza.id

                            viewData.postValue(
                                Success(
                                    adapt(
                                        pizza,
                                        favs.contains(pizza.id)
                                    )
                                )
                            )
                        }
                        is Loading, is Failure, is Empty -> {
                        }
                    }
                }
        }
    }

    fun favoritePizza() {
        dispatcher.dispatch(PizzaFavoriteAction(pizzaId))
    }

    fun loadPizzaRecipes() {
        dispatcher.dispatch(FetchPizzasAction(false))
    }

}