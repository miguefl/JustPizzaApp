package com.migferlab.justpizza.di

import com.migferlab.justpizza.domain.repository.PizzaListenerRepository
import com.migferlab.justpizza.domain.repository.UserRepository
import com.migferlab.justpizza.infrastructure.data.FirestoreUserRepository
import com.migferlab.justpizza.infrastructure.data.PizzaFirestoreListenerRepository
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton

object RepositoryModule {
    fun create() = DI.Module("Infrastructure", true) {
        bind<PizzaListenerRepository>() with singleton { PizzaFirestoreListenerRepository(instance("PizzaDB")) }
        bind<UserRepository>() with singleton {
            FirestoreUserRepository(
                instance("UserDB"),
                instance()
            )
        }
    }
}