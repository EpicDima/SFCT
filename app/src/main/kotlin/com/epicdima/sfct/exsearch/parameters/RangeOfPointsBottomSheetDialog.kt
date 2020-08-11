package com.epicdima.sfct.exsearch.parameters

import com.epicdima.sfct.R
import com.epicdima.sfct.core.usecases.ExsearchParams
import dagger.hilt.android.AndroidEntryPoint

/**
 * @author EpicDima
 */
@AndroidEntryPoint
class RangeOfPointsBottomSheetDialog :
    SingleSelectedBottomSheetDialog<ExsearchParams.RangeOfPoints>() {
    companion object {
        const val TAG = "RangeOfPointsBottomSheetDialog"

        fun newInstance() = RangeOfPointsBottomSheetDialog()
    }

    override fun getTitleStringId(): Int = R.string.range_of_points_title

    override fun createAdapter(): SingleSelectedAdapter {
        return SingleSelectedAdapter(requireContext(), ExsearchParams.RangeOfPoints::class.java) {
            viewModel.rangeOfPoints.currentValue.value = it
            dismiss()
        }
    }

    override fun configureAdapter() {
        adapter.select(viewModel.rangeOfPoints.currentValue.value!!)
    }
}