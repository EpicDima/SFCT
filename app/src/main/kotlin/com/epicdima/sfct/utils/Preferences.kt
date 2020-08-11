package com.epicdima.sfct.utils

import android.content.Context

/**
 * @author EpicDima
 */
const val PREFERENCES_NAME = "sfct_prefs"

fun Context.getSharedPreferences() = getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)