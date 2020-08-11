package com.epicdima.sfct.core.network

/**
 * @author EpicDima
 */
sealed class DataResult<out T : Any> {
    data class Success<out T : Any>(val data: T) : DataResult<T>()
    data class Failure(val throwable: Throwable) : DataResult<Nothing>()
}
