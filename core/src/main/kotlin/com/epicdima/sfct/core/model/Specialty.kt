package com.epicdima.sfct.core.model

/**
 * @author EpicDima
 */
data class Specialty(
    val id: Int = 0,
    val name: String = "",
    val faculty: String = "",
    val qualification: String = "",
    val periodOfStudy: String = "",
    val teachForm: String = "",
    val paymentForm: String = "",
    val exams: String = "",
    val profileExams: Pair<String, String> = Pair("", ""),
    val receptionPlan: String = "",
    val notes: String = "",
    val scores: List<PassingScore> = emptyList()
)

data class PassingScore(
    val year: Int,
    val fullTimeBudget: Int,
    val fullTimePaid: Int,
    val partTimeBudget: Int,
    val partTimePaid: Int
)