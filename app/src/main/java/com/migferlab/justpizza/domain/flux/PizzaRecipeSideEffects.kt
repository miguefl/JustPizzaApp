package com.migferlab.justpizza.domain.flux

import com.hoopcarpool.fluxy.Dispatcher
import com.hoopcarpool.fluxy.Result
import com.migferlab.justpizza.domain.model.Pizza
import com.migferlab.justpizza.domain.model.User
import com.migferlab.justpizza.domain.repository.PizzaListenerRepository
import com.migferlab.justpizza.domain.repository.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class PizzaRecipeSideEffects(
    private val dispatcher: Dispatcher,
    private val pizzaListenerRepository: PizzaListenerRepository,
    private val usersRepository: UserRepository
) {

    private val job: Job = SupervisorJob()
    private val supervisorScope = CoroutineScope(job + Dispatchers.IO)

    fun subscribeToRealTimePizzaList() {
        pizzaListenerRepository.subscribeToPizzaCollection(this::onDataChange, this::onError)
    }

    private fun onDataChange(pizzas: List<Pizza>) {
        dispatcher.dispatch(PizzasFetchedAction(Result.Success(pizzas), false))
    }

    private fun onError(error: Throwable) {
        dispatcher.dispatch(PizzasFetchedAction(Result.Failure(error), false))
    }

    fun pizzaFavorite(pizzaId: String, userId: String) {
        supervisorScope.launch {
            usersRepository.setPizzaFavorite(
                userId,
                pizzaId
            )
        }
    }

    fun findFavorites(user: User) {
        usersRepository.subscribeFavorites(user.uid, this::onFavChange, this::onFavError)
    }

    private fun onFavChange(user: User) {
        dispatcher.dispatch(UserFavoriteListFetchedAction(Result.Success(user)))
    }

    private fun onFavError(error: Throwable) {
        dispatcher.dispatch(PizzasFetchedAction(Result.Failure(error), false))
    }
}
