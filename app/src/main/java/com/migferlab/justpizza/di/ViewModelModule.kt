package com.migferlab.justpizza.di

import com.migferlab.justpizza.features.home.HomeViewModel
import com.migferlab.justpizza.features.login.LoginViewModel
import com.migferlab.justpizza.features.pizza.detail.PizzaRecipeDetailViewModel
import com.migferlab.justpizza.features.pizza.list.PizzaRecipeListViewModel
import org.kodein.di.DI
import org.kodein.di.instance

object ViewModelModule {
    fun create() = DI.Module("ViewModels", true) {
        bindViewModel { PizzaRecipeListViewModel(instance(), instance()) }
        bindViewModel { PizzaRecipeDetailViewModel(instance(), instance()) }
        bindViewModel { LoginViewModel(instance(), instance()) }
        bindViewModel { HomeViewModel(instance()) }
    }
}