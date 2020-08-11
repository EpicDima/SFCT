package com.epicdima.sfct.exsearch.parameters

import com.epicdima.sfct.R
import com.epicdima.sfct.core.usecases.ExsearchParams
import dagger.hilt.android.AndroidEntryPoint

/**
 * @author EpicDima
 */
@AndroidEntryPoint
class RegionBottomSheetDialog : SingleSelectedBottomSheetDialog<ExsearchParams.Region>() {
    companion object {
        const val TAG = "RegionBottomSheetDialog"

        fun newInstance() = RegionBottomSheetDialog()
    }

    override fun getTitleStringId(): Int = R.string.region_title

    override fun createAdapter(): SingleSelectedAdapter {
        return SingleSelectedAdapter(requireContext(), ExsearchParams.Region::class.java) {
            viewModel.region.currentValue.value = it
            dismiss()
        }
    }

    override fun configureAdapter() {
        adapter.select(viewModel.region.currentValue.value!!)
    }
}