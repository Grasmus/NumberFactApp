package com.numberfacts.common.base.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.numberfacts.common.base.utils.SingleLiveEvent

abstract class BaseViewModel: ViewModel() {

    val isLoading: LiveData<Boolean>
        get() = mutableIsLoading

    val errorMessage: LiveData<String>
        get() = mutableErrorMessage

    private val mutableIsLoading = SingleLiveEvent<Boolean>()
    protected val mutableErrorMessage = SingleLiveEvent<String>()

    private var callFlow: MutableSet<() -> Unit> = mutableSetOf()

    fun tryAgain() {
        executeFlow()
    }

    protected fun formCallFlow(vararg call: () -> Unit) {
        callFlow = call.toMutableSet()
    }

    protected fun onLoading(isLoading: Boolean) = mutableIsLoading.postValue(isLoading)

    private fun executeFlow() {
        for (call in callFlow) call()
    }
}
