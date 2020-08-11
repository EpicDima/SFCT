package com.epicdima.sfct.exsearch.parameters

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import com.epicdima.sfct.R
import com.epicdima.sfct.exsearch.ParametersViewModel
import com.epicdima.sfct.utils.EnumRadioAdapter
import com.epicdima.sfct.utils.ExtendedBottomSheetDialog
import com.epicdima.sfct.utils.parentViewModel
import com.epicdima.sfct.utils.setStandardProperties
import kotlinx.android.synthetic.main.list_header_bottom_sheet.*

/**
 * @author EpicDima
 */
abstract class SingleSelectedBottomSheetDialog<E : Enum<E>> : ExtendedBottomSheetDialog() {

    protected val viewModel: ParametersViewModel by parentViewModel()

    protected lateinit var adapter: SingleSelectedAdapter

    override fun getLayoutId(): Int = R.layout.list_header_bottom_sheet

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = createAdapter()
        configureAdapter()
        recycler_view.setStandardProperties(requireContext(), adapter)
    }

    override fun onCloseButtonClick() {
        dismiss()
    }

    protected abstract fun createAdapter(): SingleSelectedAdapter
    protected abstract fun configureAdapter()


    inner class SingleSelectedAdapter(
        context: Context,
        enumClass: Class<E>,
        selectListener: (E) -> Unit
    ) : EnumRadioAdapter<E, EnumRadioAdapter.EnumViewHolder<E>>(
        context, selectListener, enumClass
    ) {
        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): EnumViewHolder<E> {
            return EnumViewHolder(createBinding(parent))
        }
    }
}