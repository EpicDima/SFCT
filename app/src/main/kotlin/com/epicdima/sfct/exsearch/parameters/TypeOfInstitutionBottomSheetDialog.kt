package com.epicdima.sfct.exsearch.parameters

import com.epicdima.sfct.R
import com.epicdima.sfct.core.usecases.ExsearchParams
import dagger.hilt.android.AndroidEntryPoint

/**
 * @author EpicDima
 */
@AndroidEntryPoint
class TypeOfInstitutionBottomSheetDialog :
    SingleSelectedBottomSheetDialog<ExsearchParams.TypeOfInstitution>() {
    companion object {
        const val TAG = "TypeOfInstitutionBottomSheetDialog"

        fun newInstance() = TypeOfInstitutionBottomSheetDialog()
    }

    override fun getTitleStringId(): Int = R.string.type_of_institution_title

    override fun createAdapter(): SingleSelectedAdapter {
        return SingleSelectedAdapter(
            requireContext(),
            ExsearchParams.TypeOfInstitution::class.java
        ) {
            viewModel.typeOfInstitution.currentValue.value = it
            dismiss()
        }
    }

    override fun configureAdapter() {
        adapter.select(viewModel.typeOfInstitution.currentValue.value!!)
    }
}