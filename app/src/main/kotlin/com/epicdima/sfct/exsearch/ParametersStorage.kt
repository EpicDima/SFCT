package com.epicdima.sfct.exsearch

import androidx.lifecycle.MutableLiveData
import com.epicdima.sfct.core.usecases.ExsearchParams

/**
 * @author EpicDima
 */
class ParametersStorage {
    companion object {
        val DEFAULT_PARAMS = ExsearchParams()
    }

    val exams: Parameter<List<ExsearchParams.Exam>> = Parameter(DEFAULT_PARAMS.exams)
    val region: Parameter<ExsearchParams.Region> = Parameter(DEFAULT_PARAMS.region)
    val teachForm: Parameter<ExsearchParams.TeachForm> = Parameter(DEFAULT_PARAMS.teachForm)
    val typeOfInstitution: Parameter<ExsearchParams.TypeOfInstitution> =
        Parameter(DEFAULT_PARAMS.typeOfInstitution)
    val paymentForm: Parameter<ExsearchParams.PaymentForm> = Parameter(DEFAULT_PARAMS.paymentForm)
    val rangeOfPoints: Parameter<ExsearchParams.RangeOfPoints> =
        Parameter(DEFAULT_PARAMS.rangeOfPoints)
    val points: Parameter<String> = Parameter(DEFAULT_PARAMS.points)
    val dormitory: Parameter<ExsearchParams.Dormitory> = Parameter(DEFAULT_PARAMS.dormitory)

    fun toParams(): ExsearchParams {
        return ExsearchParams(
            exams.actualValue.value!!,
            region.actualValue.value!!,
            teachForm.actualValue.value!!,
            typeOfInstitution.actualValue.value!!,
            paymentForm.actualValue.value!!,
            rangeOfPoints.actualValue.value!!,
            points.actualValue.value!!,
            dormitory.actualValue.value!!
        )
    }

    fun fromParams(params: ExsearchParams) {
        exams.actualValue.value = params.exams
        region.actualValue.value = params.region
        teachForm.actualValue.value = params.teachForm
        typeOfInstitution.actualValue.value = params.typeOfInstitution
        paymentForm.actualValue.value = params.paymentForm
        rangeOfPoints.actualValue.value = params.rangeOfPoints
        points.actualValue.value = params.points
        dormitory.actualValue.value = params.dormitory
    }
}


class Parameter<T>(
    val defaultValue: T
) {
    val actualValue: MutableLiveData<T> = MutableLiveData(defaultValue)

    fun reset() {
        actualValue.value = defaultValue
    }

    fun isDefault() = (actualValue.value == defaultValue)
}