package com.epicdima.sfct.utils

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel

/**
 * @author EpicDima
 */
interface SharedViewModelFragment {
    fun getSharedViewModel(): ViewModel
}


@Suppress("UNCHECKED_CAST")
fun <VM : ViewModel> Fragment.parentViewModel() = lazy {
    (parentFragment as SharedViewModelFragment).getSharedViewModel() as VM
}