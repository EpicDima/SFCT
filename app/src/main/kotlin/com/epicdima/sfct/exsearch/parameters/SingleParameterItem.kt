package com.epicdima.sfct.exsearch.parameters

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.epicdima.sfct.R
import com.epicdima.sfct.core.usecases.ExsearchParams
import com.epicdima.sfct.exsearch.Parameter
import com.epicdima.sfct.exsearch.ParametersFragment
import com.epicdima.sfct.exsearch.ParametersStorage
import com.epicdima.sfct.utils.getEnumString
import com.epicdima.sfct.utils.getEnumStrings

/**
 * @author EpicDima
 */
class SingleParameterItem<T>(
    private val parameter: Parameter<T>,
    private val converter: (value: T, context: Context) -> String
) {

    val currentValue: MutableLiveData<T> = MutableLiveData(parameter.actualValue.value!!)

    fun isDefault() = (currentValue.value == parameter.defaultValue)

    fun reset() {
        currentValue.value = parameter.defaultValue
    }

    fun resetActual() {
        reset()
        parameter.reset()
    }

    fun setActualToCurrent() {
        if (currentValue.value != parameter.actualValue.value) {
            currentValue.value = parameter.actualValue.value
        }
    }

    fun setCurrentToActual() {
        if (parameter.actualValue.value != currentValue.value) {
            parameter.actualValue.value = currentValue.value
        }
    }

    fun convertToStringOfSelected(context: Context) = converter(currentValue.value!!, context)
}


fun ParametersStorage.createExamsParameter() = SingleParameterItem(exams) { value, context ->
    if (value.isEmpty()) {
        context.getString(R.string.exam_empty)
    } else {
        context.getEnumStrings(ExsearchParams.Exam::class.java)
            .filter { value.contains(it.key) }
            .values.joinToString(", ")
    }
}

fun ParametersStorage.createRegionParameter() = SingleParameterItem(region) { value, context ->
    context.getEnumString(value)
}

fun ParametersStorage.createTeachFormParameter() =
    SingleParameterItem(teachForm) { value, context ->
        context.getEnumString(value)
    }

fun ParametersStorage.createTypeOfInstitutionParameter() =
    SingleParameterItem(typeOfInstitution) { value, context ->
        context.getEnumString(value)
    }

fun ParametersStorage.createPaymentFormParameter() =
    SingleParameterItem(paymentForm) { value, context ->
        context.getEnumString(value)
    }

fun ParametersStorage.createRangeOfPointsParameter() =
    SingleParameterItem(rangeOfPoints) { value, context ->
        context.getEnumString(value)
    }

fun ParametersStorage.createPointsParameter() = SingleParameterItem(points) { value, context ->
    if (value.isEmpty()) context.getString(R.string.points_unspecified) else value
}

fun ParametersStorage.createDormitoryParameter() =
    SingleParameterItem(dormitory) { value, context ->
        context.getEnumString(value)
    }


fun <T> LifecycleOwner.observeParameter(
    context: Context,
    singleParameterItem: SingleParameterItem<T>,
    parameterViewItem: ParametersFragment.ParameterViewItem,
    callback: (() -> Unit)? = null
) {
    singleParameterItem.currentValue.observe(this) {
        if (callback != null) {
            callback()
        }
        parameterViewItem.apply {
            singleParameterItem.apply {
                selected.set(convertToStringOfSelected(context))
                defaultValue.set(isDefault())
            }
        }
    }
}