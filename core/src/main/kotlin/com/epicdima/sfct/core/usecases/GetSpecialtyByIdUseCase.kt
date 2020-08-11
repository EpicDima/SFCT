package com.epicdima.sfct.core.usecases

import com.epicdima.sfct.core.model.Institution
import com.epicdima.sfct.core.network.ApiService
import com.epicdima.sfct.core.network.DataResult
import com.epicdima.sfct.core.parsers.SpecialtyParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * @author EpicDima
 */
class GetSpecialtyByIdUseCase @Inject constructor(
    private val service: ApiService
) {
    private val parser = SpecialtyParser()

    suspend fun execute(id: Int): DataResult<Institution> = withContext(Dispatchers.IO) {
        try {
            DataResult.Success(
                parser.parse(
                    service.getSpecialtyById(id)
                )
            )
        } catch (e: Exception) {
            DataResult.Failure(e)
        }
    }
}