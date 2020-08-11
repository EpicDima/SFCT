package com.epicdima.sfct.exsearch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.epicdima.sfct.R
import com.epicdima.sfct.core.model.Specialty
import com.epicdima.sfct.databinding.ItemSpecialtyBottomSheetBinding
import com.epicdima.sfct.utils.ExtendedBottomSheetDialog
import com.epicdima.sfct.utils.parentViewModel
import com.epicdima.sfct.utils.setStandardProperties
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.header_of_bottom_sheet.*
import kotlinx.android.synthetic.main.header_of_bottom_sheet.view.*
import kotlinx.android.synthetic.main.list_header_bottom_sheet.*

/**
 * @author EpicDima
 */
@AndroidEntryPoint
class InstitutionSpecialtiesBottomSheet :
    ExtendedBottomSheetDialog() {

    companion object {
        const val TAG = "InstitutionSpecialtiesBottomSheet"

        fun newInstance(index: Int): InstitutionSpecialtiesBottomSheet {
            val fragment = InstitutionSpecialtiesBottomSheet()
            fragment.index = index
            return fragment
        }
    }

    private val viewModel: ExsearchViewModel by parentViewModel()

    private var index = -1

    override fun getLayoutId(): Int = R.layout.list_header_bottom_sheet

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (index == -1 || viewModel.resultList.value == null
            || viewModel.resultList.value!!.lastIndex < index
        ) {
            dismiss()
            return
        }
        header_layout.title_textview.text = viewModel.resultList.value!![index].name
        val adapter = SpecialtiesAdapter(viewModel.resultList.value!![index].specialties) { id ->
            Navigation.findNavController(requireParentFragment().requireView())
                .navigate(
                    R.id.action_exsearchFragment_to_specialtyFragment,
                    bundleOf("specialtyId" to id)
                )
        }
        recycler_view.setStandardProperties(requireContext(), adapter)
    }

    override fun onCloseButtonClick() {
        dismiss()
    }


    private class SpecialtiesAdapter(
        private val list: List<Specialty>,
        private val selectListener: (Int) -> Unit
    ) : RecyclerView.Adapter<SpecialtiesAdapter.SpecialtyHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpecialtyHolder {
            val binding =
                ItemSpecialtyBottomSheetBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            return SpecialtyHolder(binding)
        }

        override fun onBindViewHolder(holder: SpecialtyHolder, position: Int) {
            holder.bind(list[position], selectListener)
        }

        override fun getItemCount() = list.size

        class SpecialtyHolder(
            private val binding: ItemSpecialtyBottomSheetBinding
        ) : RecyclerView.ViewHolder(binding.root) {
            fun bind(specialty: Specialty, selectListener: (Int) -> Unit) {
                binding.value = specialty
                binding.root.setOnClickListener { selectListener(specialty.id) }
            }
        }
    }
}