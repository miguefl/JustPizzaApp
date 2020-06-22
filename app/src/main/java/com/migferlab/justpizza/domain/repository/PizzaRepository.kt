package com.migferlab.justpizza.domain.repository

import com.migferlab.justpizza.domain.model.Pizza

interface PizzaRepository {

    fun getCachedPizzas(): RepositoryResult<List<Pizza>>
    suspend fun getPizzas(): RepositoryResult<List<Pizza>>
}
