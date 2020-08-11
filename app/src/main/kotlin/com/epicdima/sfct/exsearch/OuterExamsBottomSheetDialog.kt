package com.epicdima.sfct.exsearch

import android.content.DialogInterface
import androidx.fragment.app.viewModels
import com.epicdima.sfct.exsearch.parameters.ExamsBottomSheetDialog

/**
 * @author EpicDima
 */
class OuterExamsBottomSheetDialog : ExamsBottomSheetDialog() {
    companion object {
        const val TAG = "OuterExamsBottomSheetDialog"

        fun newInstance() = OuterExamsBottomSheetDialog()
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        viewModel.save()
    }

    override fun createViewModel(): Lazy<ParametersViewModel> = viewModels()
}