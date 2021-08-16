package com.epicdima.sfct.exsearch

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.epicdima.sfct.core.model.Institution
import com.epicdima.sfct.core.network.DataResult
import com.epicdima.sfct.core.usecases.ExsearchParams
import com.epicdima.sfct.core.usecases.GetExsearchUseCase
import com.epicdima.sfct.exsearch.parameters.SingleParameterItem
import com.epicdima.sfct.exsearch.parameters.createExamsParameter
import com.epicdima.sfct.utils.DownloadableViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author EpicDima
 */
@HiltViewModel
class ExsearchViewModel @Inject constructor(
    private val parametersStorage: ParametersStorage,
    private val exsearchUseCase: GetExsearchUseCase
) : DownloadableViewModel() {

    private val parametersWithoutExams = listOf(
        parametersStorage.region,
        parametersStorage.teachForm,
        parametersStorage.typeOfInstitution,
        parametersStorage.paymentForm,
        parametersStorage.rangeOfPoints,
        parametersStorage.points,
        parametersStorage.dormitory
    )

    val exams: SingleParameterItem<List<ExsearchParams.Exam>> =
        parametersStorage.createExamsParameter()

    private val _resultList = MutableLiveData<List<Institution>>(emptyList())
    val resultList: LiveData<List<Institution>> = _resultList

    private val _anyChangeObservable = MediatorLiveData<Unit>().apply {
        addSource(parametersStorage.exams.actualValue) { value = Unit }
        addSource(parametersStorage.region.actualValue) { value = Unit }
        addSource(parametersStorage.teachForm.actualValue) { value = Unit }
        addSource(parametersStorage.typeOfInstitution.actualValue) { value = Unit }
        addSource(parametersStorage.paymentForm.actualValue) { value = Unit }
        addSource(parametersStorage.rangeOfPoints.actualValue) { value = Unit }
        addSource(parametersStorage.points.actualValue) { value = Unit }
        addSource(parametersStorage.dormitory.actualValue) { value = Unit }
    }
    val anyChangeObservable: LiveData<Unit> = _anyChangeObservable

    private var savedParams = parametersStorage.toParams()

    fun isExamsDefault() = parametersStorage.exams.isDefault()

    fun setEmptyList() {
        _resultList.value = emptyList()
    }

    private fun updateParams() {
        savedParams = parametersStorage.toParams()
    }

    fun isSavedParams(): Boolean {
        return parametersStorage.toParams() == savedParams
    }

    fun search() = viewModelScope.launch {
        if (!rawStatus.loading) {
            startLoading()
            updateParams()
            if (savedParams != ParametersStorage.DEFAULT_PARAMS) {
                _resultList.value = when (val result = exsearchUseCase.execute(savedParams)) {
                    is DataResult.Success -> {
                        resetError()
                        result.data
                    }
                    is DataResult.Failure -> {
                        setError(result.throwable)
                        emptyList()
                    }
                }
            } else {
                _resultList.value = emptyList()
            }
            endLoading()
        }
    }

    fun resetParamsWithoutExams() {
        parametersWithoutExams.forEach(Parameter<out Any>::reset)
    }

    fun notDefaultParamsCountWithoutExams(): Int {
        return parametersWithoutExams.count { !it.isDefault() }
    }

    fun resetExams() {
        _resultList.value = emptyList()
        exams.resetActual()
        updateParams()
    }
}