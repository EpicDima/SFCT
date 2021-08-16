package com.epicdima.sfct.exsearch.parameters

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.epicdima.sfct.R
import com.epicdima.sfct.core.usecases.ExsearchParams
import com.epicdima.sfct.databinding.ListFooterHeaderBottomSheetBinding
import com.epicdima.sfct.exsearch.ParametersViewModel
import com.epicdima.sfct.utils.*
import dagger.hilt.android.AndroidEntryPoint

/**
 * @author EpicDima
 */
@AndroidEntryPoint
open class ExamsBottomSheetDialog :
    ExtendedBottomSheetDialog<ListFooterHeaderBottomSheetBinding>() {

    companion object {

        fun newInstance() = ExamsBottomSheetDialog()
    }

    protected val viewModel: ParametersViewModel by createViewModel()

    private lateinit var adapter: ExamsCheckedAdapter

    override fun createViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        ListFooterHeaderBottomSheetBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.headerLayout.titleTextview.text = getText(R.string.exam_title)
        binding.examsList.setStandardProperties(requireContext(), createAdapter())
    }

    private fun createAdapter(): ExamsCheckedAdapter {
        adapter = ExamsCheckedAdapter(requireContext()) {
            if (adapter.map.values.any { it.selected.get() }) {
                binding.footerLayout.resetButton.show()
            } else {
                binding.footerLayout.resetButton.hide()
            }
        }
        viewModel.exams.currentValue.value!!.also {
            if (it.isNotEmpty()) {
                binding.footerLayout.resetButton.show()
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