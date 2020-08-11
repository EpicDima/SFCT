package com.epicdima.sfct.exsearch.parameters

import com.epicdima.sfct.R
import com.epicdima.sfct.core.usecases.ExsearchParams
import dagger.hilt.android.AndroidEntryPoint

/**
 * @author EpicDima
 */
@AndroidEntryPoint
class TeachFormBottomSheetDialog : SingleSelectedBottomSheetDialog<ExsearchParams.TeachForm>() {
    companion object {
        const val TAG = "TeachFormBottomSheetDialog"

        fun newInstance() = TeachFormBottomSheetDialog()
    }

    override fun getTitleStringId(): Int = R.string.teach_form_title

    override fun createAdapter(): SingleSelectedAdapter {
        return SingleSelectedAdapter(requireContext(), ExsearchParams.TeachForm::class.java) {
            viewModel.teachForm.currentValue.value = it
            dismiss()
        }
    }

    override fun configureAdapter() {
        adapter.select(viewModel.teachForm.currentValue.value!!)
    }
}