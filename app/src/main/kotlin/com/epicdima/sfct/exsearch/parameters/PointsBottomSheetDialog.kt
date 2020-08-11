package com.epicdima.sfct.exsearch.parameters

import android.content.DialogInterface
import android.content.res.ColorStateList
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.InputType
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.epicdima.sfct.R
import com.epicdima.sfct.exsearch.ParametersViewModel
import com.epicdima.sfct.utils.ExtendedBottomSheetDialog
import com.epicdima.sfct.utils.hide
import com.epicdima.sfct.utils.parentViewModel
import com.epicdima.sfct.utils.show
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.footer_of_bottom_sheet.*
import kotlinx.android.synthetic.main.input_bottom_sheet.*

/**
 * @author EpicDima
 */
@AndroidEntryPoint
class PointsBottomSheetDialog : ExtendedBottomSheetDialog() {

    companion object {
        const val TAG = "PointsBottomSheetDialog"

        fun newInstance() = PointsBottomSheetDialog()
    }

    private val viewModel: ParametersViewModel by parentViewModel()

    override fun getLayoutId(): Int = R.layout.input_bottom_sheet

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        info_error_textview.apply {
            setLines(2)
            text = getString(R.string.points_input_info)
        }
        input_edittext.apply {
            inputType = InputType.TYPE_CLASS_NUMBER
            addTextChangedListener(keyboardChangeListener())
            filters = arrayOf(InputFilter.LengthFilter(3))
            imeOptions = EditorInfo.IME_ACTION_NONE
        }
    }

    override fun configureBottomSheetBehaviour(behavior: BottomSheetBehavior<FrameLayout>) {
        behavior.apply {
            skipCollapsed = true
            state = BottomSheetBehavior.STATE_HALF_EXPANDED
        }
    }

    private fun keyboardChangeListener(): TextWatcher {
        return object : TextWatcher {

            override fun afterTextChanged(p0: Editable?) {}

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                p0?.let { text ->
                    if (text.isNotEmpty()) {
                        reset_button.show()
                        if (text.length > 1) {
                            if (text.all { '0' == it }) {
                                input_edittext.setText("0")
                                input_edittext.setSelection(p1)
                            } else if (text[0] == '0') {
                                input_edittext.setText(text.drop(1).toString())
                                input_edittext.setSelection(p1)
                            }
                        }
                        if (validateText(text.toString())) {
                            showInfo()
                        } else {
                            showError()
                        }
                    } else {
                        reset_button.hide()
                    }
                }
            }
        }
    }

    private fun showInfo() {
        info_error_textview.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.controlNormal
            )
        )
        info_error_textview.text = getText(R.string.points_input_info)
        input_edittext.backgroundTintList =
            ColorStateList.valueOf(ContextCompat.getColor(requireContext(), android.R.color.black))
        confirm_button.show()
    }

    private fun showError() {
        info_error_textview.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.colorPrimaryDark
            )
        )
        info_error_textview.text = getText(R.string.points_input_error)
        input_edittext.backgroundTintList = ColorStateList.valueOf(
            ContextCompat.getColor(
                requireContext(),
                R.color.colorPrimaryDark
            )
        )
        confirm_button.hide()
    }

    private fun validateText(text: String): Boolean {
        return try {
            val number = text.toInt()
            number in 0..400
        } catch (e: NumberFormatException) {
            false
        }
    }

    override fun getTitleStringId(): Int = R.string.points_title

    override fun onCloseButtonClick() {
        dismiss()
    }

    override fun onResetButtonClick() {
        input_edittext.text?.clear()
        showInfo()
    }

    override fun onConfirmButtonClick() {
        dialog?.cancel()
    }

    override fun onCancel(dialog: DialogInterface) {
        if (validateText(input_edittext.text.toString())) {
            viewModel.points.currentValue.value = input_edittext.text.toString()
        }
        super.onCancel(dialog)
    }

}