package com.epicdima.sfct.core.usecases

import com.epicdima.sfct.core.network.ApiService
import com.epicdima.sfct.core.network.DataResult
import com.epicdima.sfct.core.parsers.DormitoryParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * @author EpicDima
 */
class GetDormitoryByIdUseCase @Inject constructor(
    private val service: ApiService
) {
    private val parser = DormitoryParser()

    suspend fun execute(id: Int): DataResult<String> = withContext(Dispatchers.IO) {
        try {
            DataResult.Success(
                parser.parse(
                    service.getDormitoryById(id)
                )
            )
        } catch (e: Exception) {
            DataResult.Failure(e)
        }
    }
}