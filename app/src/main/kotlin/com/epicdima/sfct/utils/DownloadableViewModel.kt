package com.epicdima.sfct.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.io.IOException

/**
 * @author EpicDima
 */
abstract class DownloadableViewModel : ViewModel() {

    private val _status = MutableLiveData(Status())
    val status: LiveData<Status> = _status
    val rawStatus: Status
        get() = _status.value!!

    protected fun startLoading() {
        _status.value = Status(loading = true)
    }

    protected fun endLoading() {
        _status.value = _status.value!!.copy(loading = false)
    }

    protected fun resetError() {
        _status.value = _status.value!!.copy(error = false, networkError = false)
    }

    protected fun setError(error: Throwable) {
        val isNetworkError = error is IOException
        _status.value = _status.value!!.copy(error = !isNetworkError, networkError = isNetworkError)
    }
}


data class Status(
    val loading: Boolean = false,
    val error: Boolean = false,
    val networkError: Boolean = false
) {

    fun isAllTrue() = (loading && error && networkError)

    fun isAnyTrue() = (loading || error || networkError)

    fun isAllFalse() = (!loading && !error && !networkError)

    fun isAnyFalse() = (!loading || !error || !networkError)

    fun isErrored() = (error || networkError)
}