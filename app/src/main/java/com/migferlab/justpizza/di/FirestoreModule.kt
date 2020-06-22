package com.migferlab.justpizza.di

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.singleton

object FirestoreModule {
    fun create() = DI.Module("FirestoreModule", true) {
        bind<CollectionReference>(tag = "PizzaDB") with singleton {
            Firebase.firestore.collection("pizzas")
        }
        bind<CollectionReference>(tag = "UserDB") with singleton {
            Firebase.firestore.collection("users")
        }
    }
}