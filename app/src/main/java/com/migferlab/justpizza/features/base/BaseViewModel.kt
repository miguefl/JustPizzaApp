package com.migferlab.justpizza.features.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class BaseViewModel<T> : ViewModel() {

    protected val viewData = MutableLiveData<T>()
    fun getLiveData(): LiveData<T> = viewData
}