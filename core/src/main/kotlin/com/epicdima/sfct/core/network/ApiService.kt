package com.epicdima.sfct.core.network

/**
 * @author EpicDima
 */
interface ApiService {
    suspend fun postExsearch(
        exams: Array<String>,
        region: String,
        teachForm: String,
        typeOfInstitution: String,
        paymentForm: String,
        rangeOfPoints: String,
        points: String,
        dormitory: String
    ): String

    suspend fun getSpecialtyById(id: Int): String

    suspend fun getInstitutionById(id: Int): String

    suspend fun getDormitoryById(id: Int): String
}