package com.epicdima.sfct.core.utils

/**
 * @author EpicDima
 */
fun String.getGroupString(regex: Regex, index: Int): String {
    return regex.find(this)?.groupValues?.get(index) ?: ""
}

fun String.getGroupInt(regex: Regex, index: Int): Int {
    val str = getGroupString(regex, index)
    return if (str.isEmpty()) -1 else str.toInt()
}