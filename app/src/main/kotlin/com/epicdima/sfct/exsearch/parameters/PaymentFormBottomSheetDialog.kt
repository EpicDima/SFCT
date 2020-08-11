package com.epicdima.sfct.exsearch.parameters

import com.epicdima.sfct.R
import com.epicdima.sfct.core.usecases.ExsearchParams
import dagger.hilt.android.AndroidEntryPoint

/**
 * @author EpicDima
 */
@AndroidEntryPoint
class PaymentFormBottomSheetDialog : SingleSelectedBottomSheetDialog<ExsearchParams.PaymentForm>() {
    companion object {
        const val TAG = "PaymentFormBottomSheetDialog"

        fun newInstance() = PaymentFormBottomSheetDialog()
    }

    override fun getTitleStringId(): Int = R.string.payment_form_title

    override fun createAdapter(): SingleSelectedAdapter {
        return SingleSelectedAdapter(requireContext(), ExsearchParams.PaymentForm::class.java) {
            viewModel.paymentForm.currentValue.value = it
            dismiss()
        }
    }

    override fun configureAdapter() {
        adapter.select(viewModel.paymentForm.currentValue.value!!)
    }
}