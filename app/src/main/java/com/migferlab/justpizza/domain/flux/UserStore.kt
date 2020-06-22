package com.migferlab.justpizza.domain.flux

import com.hoopcarpool.fluxy.FluxyStore
import com.hoopcarpool.fluxy.Result
import com.migferlab.justpizza.domain.model.User

data class UserState(val userResult: Result<User> = Result.Empty())

class UserStore(private val userSideEffects: UserSideEffects) : FluxyStore<UserState>() {
    override fun init() {
        reduce<UserSignUpAction> {
            newState = state.copy(userResult = Result.Loading())
            userSideEffects.singUp(it.email, it.password)
        }

        reduce<UserSignUpResolvedAction> {
            newState = state.copy(userResult = it.result)
        }

        reduce<UserLogInAction> {
            newState = state.copy(userResult = Result.Loading())
            userSideEffects.logIn(it.email, it.password)
        }

        reduce<UserLogInResolvedAction> {
            newState = state.copy(userResult = it.result)
        }

        reduce<UserLogOutAction> {
            newState = state.copy(userResult = Result.Empty())
            userSideEffects.logOut()
        }

        reduce<GoogleSignInAction> {
            newState = state.copy(userResult = Result.Loading())
            userSideEffects.singInGoogle(it.credential)
        }
    }
}