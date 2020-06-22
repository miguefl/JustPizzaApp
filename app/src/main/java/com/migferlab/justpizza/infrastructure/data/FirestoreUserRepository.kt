package com.migferlab.justpizza.infrastructure.data

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.migferlab.justpizza.domain.model.User
import com.migferlab.justpizza.domain.repository.UserRepository

class FirestoreUserRepository(
    private val userDatabase: CollectionReference,
    private val firebaseAuth: FirebaseAuth
) : UserRepository {
    override fun setPizzaFavorite(userId: String, pizzaId: String) {
        val document = userDatabase.document(userId)
        document.get().addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists()) {
                val favs = documentSnapshot.get("favs") as List<String>

                val newFavs = if (favs.contains(pizzaId)) favs.filter { it != pizzaId }.toList()
                else favs + pizzaId

                document.set(hashMapOf("favs" to newFavs))

            } else {
                document.set(hashMapOf("favs" to arrayListOf(pizzaId)))
            }
        }
    }

    override fun singUp(
        email: String,
        password: String,
        onSuccess: (User) -> Any,
        onFailure: (Exception) -> Any
    ) {
        firebaseAuth
            .createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    onSuccess(User(it.result!!.user!!.uid))
                } else {
                    onFailure(it.exception!!)
                }
            }
    }

    override fun logIn(
        email: String,
        password: String,
        onSuccess: (User) -> Any,
        onFailure: (Exception) -> Any
    ) {
        firebaseAuth
            .signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    onSuccess(User(it.result!!.user!!.uid))
                } else {
                    onFailure(it.exception!!)
                }
            }
    }

    override fun singInWithGoogle(
        credential: AuthCredential,
        onSuccess: (User) -> Any,
        onFailure: (Exception) -> Any
    ) {
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful) {
                onSuccess(User(it.result!!.user!!.uid))
            } else {
                onFailure(it.exception!!)
            }
        }
    }

    override fun logOut() {
        firebaseAuth.signOut()
    }

    @Suppress("UNCHECKED_CAST")
    override fun subscribeFavorites(
        userId: String,
        onSuccess: (User) -> Any,
        onFailure: (Exception) -> Any
    ) {
        val document = userDatabase.document(userId)

        document.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val favs = documentSnapshot.get("favs") as List<String>
                    onSuccess(User(userId, favs))
                } else {
                    val favs = arrayListOf<String>()
                    document.set(hashMapOf("favs" to favs))
                }
                document.addSnapshotListener { snapshot, firebaseFirestoreException ->
                    if (firebaseFirestoreException != null) {
                        onFailure(firebaseFirestoreException)
                        return@addSnapshotListener
                    }
                    onSuccess(User(userId, snapshot!!.get("favs") as List<String>))
                }
            }

    }

}