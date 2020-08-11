package com.epicdima.sfct.utils

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.epicdima.sfct.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.footer_of_bottom_sheet.*
import kotlinx.android.synthetic.main.footer_of_bottom_sheet.view.*
import kotlinx.android.synthetic.main.header_of_bottom_sheet.*
import kotlinx.android.synthetic.main.header_of_bottom_sheet.view.*

/**
 * @author EpicDima
 */
abstract class ExtendedBottomSheetDialog : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(getLayoutId(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (header_layout != null) {
            val titleId = getTitleStringId()
            header_layout.title_textview.text = if (titleId == 0) "" else getString(titleId)
            header_layout.close_button.setOnClickListener { onCloseButtonClick() }
        }
        if (footer_layout != null) {
            footer_layout.reset_button.setOnClickListener { onResetButtonClick() }
            footer_layout.confirm_button.setOnClickListener { onConfirmButtonClick() }
        }
    }

    override fun getTheme(): Int = R.style.BottomSheetDialogTheme

    override fun onCreateDialog(savedInstanceState: Bundle?): BottomSheetDialog {
        val bottomSheetDialog = BottomSheetDialog(requireContext(), theme)
        bottomSheetDialog.setOnKeyListener { _, keyCode, _ ->
            if (keyCode == android.view.KeyEvent.KEYCODE_BACK) {
                dismiss()
                true
            } else {
                false
            }
        }
        bottomSheetDialog.setOnShowListener { dialog ->
            if (dialog is BottomSheetDialog) {
                val layout = dialog.findViewById<FrameLayout>(
                    com.google.android.material.R.id.design_bottom_sheet
                )!!
                configureBottomSheetBehaviour(BottomSheetBehavior.from(layout))
            }
        }
        return bottomSheetDialog
    }

    protected open fun configureBottomSheetBehaviour(behavior: BottomSheetBehavior<FrameLayout>) {
        behavior.apply {
            skipCollapsed = true
            state = BottomSheetBehavior.STATE_EXPANDED
        }
    }

    protected open fun getLayoutId(): Int = 0
    protected open fun getTitleStringId(): Int = 0
    protected open fun onCloseButtonClick() {}
    protected open fun onResetButtonClick() {}
    protected open fun onConfirmButtonClick() {}
}