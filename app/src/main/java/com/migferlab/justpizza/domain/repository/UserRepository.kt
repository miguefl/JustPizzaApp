package com.migferlab.justpizza.domain.repository

import com.google.firebase.auth.AuthCredential
import com.migferlab.justpizza.domain.model.User

interface UserRepository {
    fun setPizzaFavorite(userId: String, pizzaId: String)
    fun singUp(
        email: String,
        password: String,
        onSuccess: (User) -> Any,
        onFailure: (Exception) -> Any
    )

    fun logIn(
        email: String,
        password: String,
        onSuccess: (User) -> Any,
        onFailure: (Exception) -> Any
    )

    fun singInWithGoogle(
        credential: AuthCredential,
        onSuccess: (User) -> Any,
        onFailure: (Exception) -> Any
    )

    fun logOut()

    fun subscribeFavorites(userId: String, onSuccess: (User) -> Any, onFailure: (Exception) -> Any)
}