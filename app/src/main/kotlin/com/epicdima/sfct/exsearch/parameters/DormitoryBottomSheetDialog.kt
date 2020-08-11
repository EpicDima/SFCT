package com.epicdima.sfct.exsearch.parameters

import com.epicdima.sfct.R
import com.epicdima.sfct.core.usecases.ExsearchParams
import dagger.hilt.android.AndroidEntryPoint

/**
 * @author EpicDima
 */
@AndroidEntryPoint
open class DormitoryBottomSheetDialog :
    SingleSelectedBottomSheetDialog<ExsearchParams.Dormitory>() {
    companion object {
        const val TAG = "DormitoryBottomSheetDialog"

        fun newInstance() = DormitoryBottomSheetDialog()
    }

    override fun getTitleStringId(): Int = R.string.dormitory_title

    override fun createAdapter(): SingleSelectedAdapter {
        return SingleSelectedAdapter(requireContext(), ExsearchParams.Dormitory::class.java) {
            viewModel.dormitory.currentValue.value = it
            dismiss()
        }
    }

    override fun configureAdapter() {
        adapter.select(viewModel.dormitory.currentValue.value!!)
    }
}