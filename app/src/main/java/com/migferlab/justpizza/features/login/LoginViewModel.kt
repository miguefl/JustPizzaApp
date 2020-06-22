package com.migferlab.justpizza.features.login

import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.GoogleAuthProvider
import com.hoopcarpool.fluxy.Dispatcher
import com.hoopcarpool.fluxy.Result
import com.migferlab.justpizza.domain.flux.GoogleSignInAction
import com.migferlab.justpizza.domain.flux.UserLogInAction
import com.migferlab.justpizza.domain.flux.UserSignUpAction
import com.migferlab.justpizza.domain.flux.UserStore
import com.migferlab.justpizza.domain.model.User
import com.migferlab.justpizza.features.base.BaseViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch


class LoginViewModel(private val dispatcher: Dispatcher, private val userStore: UserStore) :
    BaseViewModel<Result<User>>() {

    init {
        viewModelScope.launch {
            userStore.flow()
                .map { it.userResult }
                .collect {
                    viewData.postValue(it)
                }
        }
    }

    fun signUp(email: String, password: String) {
        dispatcher.dispatch(UserSignUpAction(email, password))
    }

    fun logIn(email: String, password: String) {
        dispatcher.dispatch(UserLogInAction(email, password))
    }

    fun googleSignIn(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        dispatcher.dispatch(GoogleSignInAction(credential))
    }

}