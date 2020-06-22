package com.migferlab.justpizza.domain.flux

import com.google.firebase.auth.AuthCredential
import com.hoopcarpool.fluxy.BaseAction
import com.hoopcarpool.fluxy.Result
import com.migferlab.justpizza.domain.model.User

data class UserSignUpAction(val email: String, val password: String) : BaseAction
data class GoogleSignInAction(val credential: AuthCredential) : BaseAction
data class UserSignUpResolvedAction(val result: Result<User>) : BaseAction
data class UserLogInAction(val email: String, val password: String) : BaseAction
data class UserLogInResolvedAction(val result: Result<User>) : BaseAction
class UserLogOutAction(): BaseAction
data class UserFavoriteListFetchedAction(val result: Result<User>): BaseAction