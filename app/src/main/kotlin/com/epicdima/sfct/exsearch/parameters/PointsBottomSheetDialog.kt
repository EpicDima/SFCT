package com.epicdima.sfct.exsearch.parameters

import android.content.DialogInterface
import android.content.res.ColorStateList
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.InputType
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.epicdima.sfct.R
import com.epicdima.sfct.databinding.BottomSheetInputBinding
import com.epicdima.sfct.exsearch.ParametersViewModel
import com.epicdima.sfct.utils.ExtendedBottomSheetDialog
import com.epicdima.sfct.utils.hide
import com.epicdima.sfct.utils.parentViewModel
import com.epicdima.sfct.utils.show
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.hilt.android.AndroidEntryPoint

/**
 * @author EpicDima
 */
@AndroidEntryPoint
class PointsBottomSheetDialog : ExtendedBottomSheetDialog<BottomSheetInputBinding>() {

    companion object {

        fun newInstance() = PointsBottomSheetDialog()
    }

    private val viewModel: ParametersViewModel by parentViewModel()

    override fun createViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        BottomSheetInputBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.infoErrorTextview.apply {
            setLines(2)
            text = getString(R.string.points_input_info)
        }
        binding.inputEdittext.apply {
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

            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                s?.also { text ->
                    if (text.isNotEmpty()) {
                        binding.footerLayout.resetButton.show()
                        if (text.length > 1) {
                            if (text.all { '0' == it }) {
                                binding.inputEdittext.setText("0")
                                binding.inputEdittext.setSelection(start)
                            } else if (text[0] == '0') {
                                binding.inputEdittext.setText(text.drop(1).toString())
                                binding.inputEdittext.setSelection(start)
                            }
                        }
                        if (validateText(text.toString())) {
                            showInfo()
                        } else {
                            showError()
                        }
                    } else {
                        binding.footerLayout.resetButton.hide()
                    }
                }
            }
        }
    }

    private fun showInfo() {
        binding.apply {
            infoErrorTextview.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.controlNormal
                )
            )
            infoErrorTextview.text = getText(R.string.points_input_info)
            inputEdittext.backgroundTintList =
                ColorStateList.valueOf(
                    ContextCompat.getColor(
                        requireContext(),
                        android.R.color.black
                    )
                )
            footerLayout.confirmButton.show()
        }
    }

    private fun showError() {
        binding.apply {
            infoErrorTextview.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.colorPrimaryDark
                )
            )
            infoErrorTextview.text = getText(R.string.points_input_error)
            inputEdittext.backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.colorPrimaryDark
                )
            )
            footerLayout.confirmButton.hide()
        }
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
        binding.inputEdittext.text?.clear()
        showInfo()
    }

    override fun onConfirmButtonClick() {
        dialog?.cancel()
    }

    override fun onCancel(dialog: DialogInterface) {
        if (validateText(binding.inputEdittext.text.toString())) {
            viewModel.points.currentValue.value = binding.inputEdittext.text.toString()
        }
        super.onCancel(dialog)
    }
}