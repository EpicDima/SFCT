package com.epicdima.sfct.utils

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.viewbinding.ViewBinding
import com.epicdima.sfct.R
import com.epicdima.sfct.databinding.BottomSheetInputBinding
import com.epicdima.sfct.databinding.BottomSheetListFooterHeaderBinding
import com.epicdima.sfct.databinding.BottomSheetListHeaderBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

/**
 * @author EpicDima
 */
abstract class ExtendedBottomSheetDialog<VB : ViewBinding> : BottomSheetDialogFragment() {

    protected lateinit var binding: VB

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = createViewBinding(inflater, container)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            val tempBinding = binding

            if (tempBinding is BottomSheetListHeaderBinding) {
                val titleId = getTitleStringId()

                tempBinding.headerLayout.titleTextview.text =
                    if (titleId == 0) "" else getString(titleId)
                tempBinding.headerLayout.closeButton.setOnClickListener { onCloseButtonClick() }
            }

            if (tempBinding is BottomSheetListFooterHeaderBinding) {
                val titleId = getTitleStringId()

                tempBinding.headerLayout.titleTextview.text =
                    if (titleId == 0) "" else getString(titleId)
                tempBinding.headerLayout.closeButton.setOnClickListener { onCloseButtonClick() }

                tempBinding.footerLayout.resetButton.setOnClickListener { onResetButtonClick() }
                tempBinding.footerLayout.confirmButton.setOnClickListener { onConfirmButtonClick() }
            }

            if (tempBinding is BottomSheetInputBinding) {
                val titleId = getTitleStringId()

                tempBinding.headerLayout.titleTextview.text =
                    if (titleId == 0) "" else getString(titleId)
                tempBinding.headerLayout.closeButton.setOnClickListener { onCloseButtonClick() }

                tempBinding.footerLayout.resetButton.setOnClickListener { onResetButtonClick() }
                tempBinding.footerLayout.confirmButton.setOnClickListener { onConfirmButtonClick() }
            }
        }
    }

    override fun getTheme(): Int = R.style.BottomSheetDialogTheme

    override fun onCreateDialog(savedInstanceState: Bundle?): BottomSheetDialog {
        return BottomSheetDialog(requireContext(), theme).apply {
            setOnKeyListener { _, keyCode, _ ->
                if (keyCode == android.view.KeyEvent.KEYCODE_BACK) {
                    dismiss()
                    true
                } else {
                    false
                }
            }
            setOnShowListener { dialog ->
                if (dialog is BottomSheetDialog) {
                    val layout = dialog.findViewById<FrameLayout>(
                        com.google.android.material.R.id.design_bottom_sheet
                    )!!
                    configureBottomSheetBehaviour(BottomSheetBehavior.from(layout))
                }
            }
        }
    }

    protected open fun configureBottomSheetBehaviour(behavior: BottomSheetBehavior<FrameLayout>) {
        behavior.apply {
            skipCollapsed = true
            state = BottomSheetBehavior.STATE_EXPANDED
        }
    }

    protected abstract fun createViewBinding(inflater: LayoutInflater, container: ViewGroup?): VB

    protected open fun getTitleStringId(): Int = 0

    protected open fun onCloseButtonClick() {}

    protected open fun onResetButtonClick() {}

    protected open fun onConfirmButtonClick() {}
}