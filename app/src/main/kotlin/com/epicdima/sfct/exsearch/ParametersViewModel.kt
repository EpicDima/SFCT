package com.epicdima.sfct.exsearch

import androidx.lifecycle.ViewModel
import com.epicdima.sfct.exsearch.parameters.*
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * @author EpicDima
 */
@HiltViewModel
class ParametersViewModel @Inject constructor(
    parametersStorage: ParametersStorage
) : ViewModel() {

    val exams = parametersStorage.createExamsParameter()
    val region = parametersStorage.createRegionParameter()
    val teachForm = parametersStorage.createTeachFormParameter()
    val typeOfInstitution = parametersStorage.createTypeOfInstitutionParameter()
    val paymentForm = parametersStorage.createPaymentFormParameter()
    val rangeOfPoints = parametersStorage.createRangeOfPointsParameter()
    val points = parametersStorage.createPointsParameter()
    val dormitory = parametersStorage.createDormitoryParameter()

    private val parameters = listOf(
        exams,
        region,
        teachForm,
        typeOfInstitution,
        paymentForm,
        rangeOfPoints,
        points,
        dormitory
    )

    fun save() {
        exams.setCurrentToActual()
        region.setCurrentToActual()
        teachForm.setCurrentToActual()
        typeOfInstitution.setCurrentToActual()
        paymentForm.setCurrentToActual()
        rangeOfPoints.setCurrentToActual()
        points.setCurrentToActual()
        dormitory.setCurrentToActual()
    }

    fun resetAll() {
        parameters.forEach(SingleParameterItem<out Any>::reset)
    }

    fun isAllDefault(): Boolean = parameters.all(SingleParameterItem<out Any>::isDefault)
}