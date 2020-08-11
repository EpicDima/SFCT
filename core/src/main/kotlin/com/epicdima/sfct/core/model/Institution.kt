package com.epicdima.sfct.core.model

/**
 * @author EpicDima
 */
data class Institution(
    val id: Int = 0,
    val name: String = "",
    val specialties: List<Specialty> = emptyList(),
    val address: String = "",
    val phone: String = "",
    val email: String = "",
    val site: String = ""
)