package com.epicdima.sfct.exsearch.parameters

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import com.epicdima.sfct.R
import com.epicdima.sfct.core.usecases.ExsearchParams
import com.epicdima.sfct.exsearch.ParametersViewModel
import com.epicdima.sfct.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.footer_of_bottom_sheet.*
import kotlinx.android.synthetic.main.header_of_bottom_sheet.*
import kotlinx.android.synthetic.main.list_footer_header_bottom_sheet.*

/**
 * @author EpicDima
 */
@AndroidEntryPoint
open class ExamsBottomSheetDialog : ExtendedBottomSheetDialog() {
    companion object {
        const val TAG = "ExamsBottomSheetDialog"

        fun newInstance() = ExamsBottomSheetDialog()
    }

    protected val viewModel: ParametersViewModel by createViewModel()

    private lateinit var adapter: ExamsCheckedAdapter

    override fun getLayoutId(): Int = R.layout.list_footer_header_bottom_sheet

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        title_textview.text = getText(R.string.exam_title)
        exams_list.setStandardProperties(requireContext(), createAdapter())
    }

    private fun createAdapter(): ExamsCheckedAdapter {
        adapter = ExamsCheckedAdapter(requireContext()) {
            if (adapter.map.values.any { it.selected.get() }) {
                reset_button.show()
            } else {
                reset_button.hide()
            }
        }
        viewModel.exams.currentValue.value!!.let {
            if (it.isNotEmpty()) {
                reset_button.show()
                adapter.select(it)
            }
        }
        return adapter
    }

    override fun onCloseButtonClick() {
        dismiss()
    }

    override fun onResetButtonClick() {
        adapter.clear()
    }

    override fun onConfirmButtonClick() {
        dialog?.cancel()
    }

    override fun onCancel(dialog: DialogInterface) {
        viewModel.exams.currentValue.value = adapter.getSelected()
        super.onCancel(dialog)
    }

    protected open fun createViewModel() = parentViewModel<ParametersViewModel>()


    class ExamsCheckedAdapter(
        context: Context,
        changeListener: () -> Unit
    ) : EnumCheckedAdapter<ExsearchParams.Exam, EnumCheckedAdapter.EnumViewHolder<ExsearchParams.Exam>>(
        context, changeListener, ExsearchParams.Exam::class.java
    ) {

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): EnumViewHolder<ExsearchParams.Exam> {
            return EnumViewHolder(createBinding(parent))
        }
    }
}