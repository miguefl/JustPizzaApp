package com.migferlab.justpizza.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.singleton

object FirebaseModule {
    fun create() = DI.Module("FirebaseModule", true) {
        bind<DatabaseReference>(tag = "PizzaRealTimeDB") with singleton {
            FirebaseDatabase.getInstance().getReference("/pizzas/")
        }
        bind<FirebaseAuth>() with singleton { FirebaseAuth.getInstance() }
    }
}