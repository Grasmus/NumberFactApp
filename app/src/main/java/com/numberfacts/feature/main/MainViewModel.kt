package com.numberfacts.feature.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.numberfacts.common.base.utils.SingleLiveEvent
import com.numberfacts.common.base.viewmodel.BaseViewModel
import com.numberfacts.common.di.ApplicationScope
import com.numberfacts.common.models.Filter
import com.numberfacts.domain.constants.OrderBy
import com.numberfacts.domain.constants.OrderDirection
import com.numberfacts.domain.entities.exceptions.ApiErrorException
import com.numberfacts.domain.entities.numberfact.NumberFact
import com.numberfacts.domain.usecases.ResultCallbacks
import com.numberfacts.domain.usecases.numberfact.DeleteNumberFactByIdUseCase
import com.numberfacts.domain.usecases.numberfact.GetNumberFactUseCase
import com.numberfacts.domain.usecases.numberfact.GetRandomNumberFactUseCase
import com.numberfacts.domain.usecases.numberfact.GetSavedNumberFactsUseCase
import com.numberfacts.domain.usecases.numberfact.SaveNumberFactUseCase
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@ApplicationScope
class MainViewModel @Inject constructor(
    private val getNumberFactUseCase: GetNumberFactUseCase,
    private val getRandomNumberFactUseCase: GetRandomNumberFactUseCase,
    private val getSavedNumberFactsUseCase: GetSavedNumberFactsUseCase,
    private val saveNumberFactUseCase: SaveNumberFactUseCase,
    private val deleteNumberFactByIdUseCase: DeleteNumberFactByIdUseCase
): BaseViewModel() {

    val uiEvent: LiveData<UiEvent>
        get() = mutableUiEvent

    val numberFacts: LiveData<List<NumberFact>>
        get() = mutableNumberFacts

    val filter: LiveData<Filter>
        get() = mutableFilter

    private val mutableUiEvent = SingleLiveEvent<UiEvent>()
    private val mutableNumberFacts = MutableLiveData<List<NumberFact>>()
    private val mutableFilter = MutableLiveData<Filter>()

    private var numberFactIdToDelete: Int? = null
    private var number: Int? = null

    private var orderBy = OrderBy.Date
    private var orderDirection = OrderDirection.Descending

    init {
        getSavedNumberFacts()
    }

    fun deleteNumberFact(numberFactId: Int) {
        numberFactIdToDelete = numberFactId

        deleteNumberFact()
    }

    fun getNumberFact(number: Int) {
        this.number = number

        getNumberFact()
    }

    fun getRandomNumberFact() {
        getRandomNumberFactUseCase.invoke(
            scope = viewModelScope,
            dispatcher = Dispatchers.IO,
            result = ResultCallbacks(
                onSuccess = ::onGetNumberFactSuccess,
                onError = ::onGetNumberFactError,
                onConnectionError = ::onGetRandomNumberFactConnectionError,
                onLoading = ::onLoading,
                onUnexpectedError = ::onUnexpectedError
            )
        )
    }

    fun getNumberFactsByOrder(orderBy: OrderBy) {
        this.orderBy = orderBy

        getSavedNumberFacts()
    }

    fun getNumberFactsByDirection(orderDirection: OrderDirection) {
        this.orderDirection = orderDirection

        getSavedNumberFacts()
    }

    fun getNumberFactsWithFilter(from: Int, till: Int) {
        mutableFilter.value = Filter(from, till)

        getSavedNumberFacts()
    }

    fun getFilter() = filter.value

    fun removeFilters() {
        mutableFilter.value = Filter(null, null)

        getSavedNumberFacts()
    }

    private fun getSavedNumberFacts() {
        getSavedNumberFactsUseCase.invoke(
            scope = viewModelScope,
            dispatcher = Dispatchers.IO,
            params = GetSavedNumberFactsUseCase.Params(
                orderBy = orderBy,
                orderDirection = orderDirection,
                from = filter.value?.from,
                till = filter.value?.till
            ),
            result = ResultCallbacks(
                onSuccess = ::onGetSavedNumberFactsSuccess,
                onLoading = ::onLoading,
                onUnexpectedError = ::onUnexpectedError
            )
        )
    }

    private fun saveNumberFact(numberFact: NumberFact?) {
        saveNumberFactUseCase.invoke(
            scope = viewModelScope,
            dispatcher = Dispatchers.IO,
            params = numberFact?.let { SaveNumberFactUseCase.Params(it) },
            result = ResultCallbacks(
                onSuccess = ::onSaveNumberFactSuccess,
                onLoading = ::onLoading,
                onUnexpectedError = ::onUnexpectedError
            )
        )
    }

    private fun deleteNumberFact() {
        deleteNumberFactByIdUseCase.invoke(
            scope = viewModelScope,
            dispatcher = Dispatchers.IO,
            params = numberFactIdToDelete?.let { DeleteNumberFactByIdUseCase.Params(it) },
            result = ResultCallbacks(
                onSuccess = ::onDeleteNumberFactSuccess,
                onLoading = ::onLoading,
                onUnexpectedError = ::onUnexpectedError
            )
        )
    }

    private fun getNumberFact() {
        getNumberFactUseCase.invoke(
            scope = viewModelScope,
            dispatcher = Dispatchers.IO,
            params = number?.let { GetNumberFactUseCase.Prams(it) },
            result = ResultCallbacks(
                onSuccess = ::onGetNumberFactSuccess,
                onError = ::onGetNumberFactError,
                onConnectionError = ::onGetNumberFactConnectionError,
                onLoading = ::onLoading,
                onUnexpectedError = ::onUnexpectedError
            )
        )
    }

    private fun onGetSavedNumberFactsSuccess(numberFacts: List<NumberFact>) {
        mutableNumberFacts.postValue(numberFacts)
    }

    private fun onSaveNumberFactSuccess(result: Unit) {
        getSavedNumberFacts()
    }

    private fun onDeleteNumberFactSuccess(result: Unit) {
        getSavedNumberFacts()
    }

    private fun onGetNumberFactSuccess(numberFact: NumberFact) {
        saveNumberFact(numberFact)
    }

    private fun onGetNumberFactError(apiErrorException: ApiErrorException) {
        Log.wtf("MainViewModel::onGetNumberFactError",
            "Api returned ${apiErrorException.code}")

        mutableUiEvent.postValue(UiEvent.OnUnexpectedError)
    }

    private fun onGetRandomNumberFactConnectionError(throwable: Throwable) {
        Log.wtf("MainViewModel::onGetRandomNumberFactConnectionError",
            throwable.message)

        formCallFlow(::getRandomNumberFact)

        mutableUiEvent.postValue(UiEvent.OnConnectionError)
    }

    private fun onGetNumberFactConnectionError(throwable: Throwable) {
        Log.wtf("MainViewModel::onGetNumberFactConnectionError",
            throwable.message)

        formCallFlow(::getNumberFact)

        mutableUiEvent.postValue(UiEvent.OnConnectionError)
    }

    private fun onUnexpectedError(throwable: Throwable) {
        mutableUiEvent.postValue(UiEvent.OnUnexpectedError)

        Log.wtf(
            "MainViewModel::onUnexpectedError",
            throwable.message
        )
    }

    sealed class UiEvent {
        data object OnConnectionError: UiEvent()

        data object OnUnexpectedError: UiEvent()
    }
}
