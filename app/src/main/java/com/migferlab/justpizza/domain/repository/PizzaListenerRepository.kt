package com.migferlab.justpizza.domain.repository

import com.migferlab.justpizza.domain.model.Pizza

interface PizzaListenerRepository {
    fun subscribeToPizzaCollection(
        onDataChange: (List<Pizza>) -> Any,
        onDataError: (Throwable) -> Any
    )
}