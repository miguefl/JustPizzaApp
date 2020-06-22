package com.migferlab.justpizza.features.home

import com.hoopcarpool.fluxy.Dispatcher
import com.hoopcarpool.fluxy.Result
import com.migferlab.justpizza.domain.flux.UserLogOutAction
import com.migferlab.justpizza.domain.model.User
import com.migferlab.justpizza.features.base.BaseViewModel

class HomeViewModel(private val dispatcher: Dispatcher) :
    BaseViewModel<Result<User>>() {

    fun logOut() {
        dispatcher.dispatch(UserLogOutAction())
        viewData.postValue(Result.Empty())
    }
}