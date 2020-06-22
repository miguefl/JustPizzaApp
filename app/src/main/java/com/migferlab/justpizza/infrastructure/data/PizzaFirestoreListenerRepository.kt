package com.migferlab.justpizza.infrastructure.data

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.ktx.toObject
import com.migferlab.justpizza.domain.model.Pizza
import com.migferlab.justpizza.domain.repository.PizzaListenerRepository

class PizzaFirestoreListenerRepository(private val pizzaDatabase: CollectionReference) :
    PizzaListenerRepository {

    override fun subscribeToPizzaCollection(
        onDataChange: (List<Pizza>) -> Any,
        onDataError: (Throwable) -> Any
    ) {
        pizzaDatabase.addSnapshotListener { querySnapshot, firebaseFirestoreException ->

            if (firebaseFirestoreException != null) {
                onDataError(firebaseFirestoreException)
                return@addSnapshotListener
            }

            onDataChange(querySnapshot!!.map { adaptFirebasePizza(it) }.toList())
        }
    }

    private fun adaptFirebasePizza(queryDocumentSnapshot: QueryDocumentSnapshot): Pizza {
        return adapt(
            queryDocumentSnapshot.toObject<FirebasePizza>().pizza!!,
            queryDocumentSnapshot.id
        )
    }
}