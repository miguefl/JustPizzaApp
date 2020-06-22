package com.migferlab.justpizza.di

import com.migferlab.justpizza.domain.flux.PizzaRecipeSideEffects
import com.migferlab.justpizza.domain.flux.PizzaRecipeStore
import com.migferlab.justpizza.domain.flux.UserSideEffects
import com.migferlab.justpizza.domain.flux.UserStore
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton

object StoreModule {
    fun create() = DI.Module("StoreModule", true) {
        bindStore { PizzaRecipeStore(instance()) }
        bind<PizzaRecipeSideEffects>() with singleton {
            PizzaRecipeSideEffects(instance(), instance(), instance())
        }
        bindStore { UserStore(instance()) }
        bind<UserSideEffects>() with singleton {
            UserSideEffects(instance(), instance())
        }
    }
}