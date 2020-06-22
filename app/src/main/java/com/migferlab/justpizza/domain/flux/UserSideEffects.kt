package com.migferlab.justpizza.domain.flux

import com.google.firebase.auth.AuthCredential
import com.hoopcarpool.fluxy.Dispatcher
import com.hoopcarpool.fluxy.Result
import com.migferlab.justpizza.domain.repository.UserRepository

class UserSideEffects(
    private val dispatcher: Dispatcher,
    private val userRepository: UserRepository
) {
    fun singUp(email: String, password: String) {

        userRepository.singUp(
            email,
            password,
            { dispatcher.dispatch(UserSignUpResolvedAction(Result.Success(it))) },
            { dispatcher.dispatch(UserSignUpResolvedAction(Result.Failure(it))) }
        )
    }

    fun logIn(email: String, password: String) {
        userRepository.logIn(
            email,
            password,
            { dispatcher.dispatch(UserLogInResolvedAction(Result.Success(it))) },
            { dispatcher.dispatch(UserLogInResolvedAction(Result.Failure(it))) }
        )
    }
    fun logOut() {
        userRepository.logOut()
    }

    fun singInGoogle(credential: AuthCredential) {
        userRepository.singInWithGoogle(
            credential,
            { dispatcher.dispatch(UserSignUpResolvedAction(Result.Success(it))) },
            { dispatcher.dispatch(UserSignUpResolvedAction(Result.Failure(it))) }
        )
    }
}
