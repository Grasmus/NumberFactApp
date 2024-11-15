package com.numberfacts.feature.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.numberfacts.common.base.viewmodel.BaseViewModel
import com.numberfacts.common.di.ApplicationScope
import javax.inject.Inject

@ApplicationScope
class DetailsViewModel @Inject constructor(): BaseViewModel() {

    val number: LiveData<Int>
        get() = mutableNumber

    val fact: LiveData<String>
        get() = mutableFact

    private val mutableNumber = MutableLiveData<Int>()
    private val mutableFact = MutableLiveData<String>()

    fun setNumber(number: Int) {
        mutableNumber.value = number
    }

    fun setFact(fact: String) {
        mutableFact.value = fact
    }
}
